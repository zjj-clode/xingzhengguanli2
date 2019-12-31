package com.thinkgem.jeesite.common.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.UriUtils;

import com.ckfinder.connector.utils.ImageUtils;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.Encodes;
import com.thinkgem.jeesite.common.utils.StringUtils;

/**
 * 查看CK上传的图片
 *
 * @author ThinkGem
 * @version 2014-06-25
 */
public class UserfilesDownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(getClass());

	public void fileOutputStream(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String filepath = HtmlUtils.htmlEscape(req.getRequestURI());
		logger.debug("filepath-------> {}", filepath);
		int index = filepath.indexOf(Global.USERFILES_BASE_URL);
		if (index >= 0) {
			filepath = filepath.substring(index + Global.USERFILES_BASE_URL.length());
			logger.debug("substring filepath-------> {}", filepath);
		}
		try {
			filepath = UriUtils.decode(filepath, "UTF-8");
			logger.debug("url decoded filepath-------> {}", filepath);
		} catch (UnsupportedEncodingException e1) {
			logger.error(String.format("解释文件路径失败，URL地址为%s", filepath), e1);
		}
		File file = new File(Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL + filepath);

		long length = file.length();
		logger.debug("file length ---> {}", length);
		try {

			if (ImageUtils.isImage(file)) {

				// 图片，浏览器显示
				showImg(req, resp, file);
				return;

			}

			// 如果是视频，且不是要下载文件（请求参数play=false时为下载，其他为播放）， 在线播放
			if (isVideo(file) && !"false".equalsIgnoreCase(req.getParameter("play"))) {

				showVideo(req, resp, file);
				return;

			}

			// 其他情况，文件下载
			downloadFile(req, resp, file);
			return;

		} catch (FileNotFoundException e) {
			req.setAttribute("exception", new FileNotFoundException("请求的文件不存在"));
			req.getRequestDispatcher("/WEB-INF/views/error/404.jsp").forward(req, resp);
		}
	}

	/**
	 * 显示图片
	 */
	private void showImg(HttpServletRequest request, HttpServletResponse response, File file) throws IOException {
		FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
	}

	/**
	 * 在线播放视频，支持拖动
	 */
	private void showVideo(HttpServletRequest request, HttpServletResponse response, File file) throws IOException {
		RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
		long fileLength = randomAccessFile.length();
		String range = request.getHeader("Range");
		logger.debug("range ------> {}", range);
		int start = 0, end = 0;
		if (range != null && range.startsWith("bytes=")) {
			String[] values = range.split("=")[1].split("-");
			start = Integer.parseInt(values[0]);
			if (values.length > 1) {
				end = Integer.parseInt(values[1]);
			}
		}
		int requestSize = 0;
		if (end != 0 && end > start) {
			requestSize = end - start + 1;
		} else {
			requestSize = Integer.MAX_VALUE;
		}
		response.setContentType("video/mp4;charset=UTF-8");
		response.setHeader("Accept-Ranges", "bytes");
		response.setHeader("ETag", "W/\"" + Encodes.encodeBase64(file.getName()) + "\"");
		response.setHeader("Last-Modified", new Date().toString());
		if (range == null) {
			response.setHeader("Content-length", String.valueOf(fileLength));
		} else {
			response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
			long requestStart = 0, requestEnd = 0;
			String[] ranges = range.split("=");
			if (ranges.length > 1) {
				String[] rangeDatas = ranges[1].split("-");
				requestStart = Integer.parseInt(rangeDatas[0]);
				if (rangeDatas.length > 1) {
					requestEnd = Integer.parseInt(rangeDatas[1]);
				}
			}
			long length = 0;
			if (requestEnd > 0) {
				length = requestEnd - requestStart + 1;
				response.setHeader("Content-length", String.valueOf(length));
				response.setHeader("Content-Range", "bytes " + requestStart + "-" + requestEnd + "/" + fileLength);
			} else {
				length = fileLength - requestStart;
				response.setHeader("Content-length", String.valueOf(length));
				response.setHeader("Content-Range", "bytes " + requestStart + "-" + (fileLength - 1) + "/" + fileLength);
			}
		}
		ServletOutputStream servletOutputStream = response.getOutputStream();
		int needSize = requestSize;
		randomAccessFile.seek(start);
		byte[] buffer = new byte[4096];
		while (needSize > 0) {
			int len = randomAccessFile.read(buffer);
			if (needSize < buffer.length) {
				servletOutputStream.write(buffer, 0, needSize);
			} else {
				servletOutputStream.write(buffer, 0, len);
				if (len < buffer.length) {
					break;
				}
			}
			needSize -= buffer.length;
		}
		randomAccessFile.close();
		servletOutputStream.close();
	}

	/**
	 * 当前只限mp4
	 */
	private boolean isVideo(File file) {
		return StringUtils.endsWithAny(file.getName().toLowerCase(), ".mp4");
	}

	/**
	 * 下载文件
	 */
	private void downloadFile(HttpServletRequest request, HttpServletResponse response, File file) throws IOException {
		String fileName = UriUtils.encode(file.getName(), "UTF-8"); // 空格会编码成%20
		logger.debug("url encoded fileName -------> {}", fileName);
		response.setHeader("Content-Type", "application/octet-stream");
		response.setHeader("Content-Length", String.valueOf(file.length()));
		response.setHeader("content-disposition", "attachment; filename=" + fileName + "; filename*=UTF-8''" + fileName);
		FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		fileOutputStream(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		fileOutputStream(req, resp);
	}
}
