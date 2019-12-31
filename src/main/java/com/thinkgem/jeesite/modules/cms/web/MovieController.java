/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.codec.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ckfinder.connector.utils.FileUtils;
import com.cloudinte.common.utils.FfmpegTool;
import com.cloudinte.modules.upload.web.ChunkUploadService;
import com.cloudinte.modules.upload.web.ChunkUploadService.UploadStatus;
import com.cloudinte.modules.upload.web.ResultVo;
import com.cloudinte.modules.upload.web.UploadChunk;
import com.google.common.collect.Sets;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.ResponseObject;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.cms.entity.Category;
import com.thinkgem.jeesite.modules.cms.entity.Movie;
import com.thinkgem.jeesite.modules.cms.entity.Site;
import com.thinkgem.jeesite.modules.cms.service.CategoryService;
import com.thinkgem.jeesite.modules.cms.service.MovieService;
import com.thinkgem.jeesite.modules.cms.utils.CmsUtils;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.SettingsUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 视频新闻Controller
 * 
 * @author hhw
 * @version 2017-05-01
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/movie")
public class MovieController extends BaseController {

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private MovieService movieService;

	@ModelAttribute
	public Movie get(@RequestParam(required = false) String id) {
		Movie entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = movieService.get(id);
		}
		if (entity == null) {
			entity = new Movie();
		}
		return entity;
	}

	@RequiresPermissions("cms:movie:view")
	@RequestMapping(value = { "list", "" })
	public String list(Movie movie, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Movie> page = movieService.findPage(new Page<Movie>(request, response), movie);
		model.addAttribute("page", page);
		model.addAttribute("ename", "movie");
		model.addAttribute("categoryId", movie.getCategory() != null ? movie.getCategory().getId() : "");
		return "modules/cms/movieList";
	}

	@RequiresPermissions("cms:movie:view")
	@RequestMapping(value = "form")
	public String form(Movie movie, Model model) {

		/////////////
		Category category = movie.getCategory();
		// 如果当前传参有子节点，则选择取消传参选择。即：只有叶子节点栏目下才能有文章
		if (category != null && StringUtils.isNotBlank(category.getId())) {
			List<Category> list = categoryService.findByParentId(category.getId(), Site.getCurrentSiteId());
			if (list.size() > 0) { // 非叶子节点栏目
				movie.setCategory(new Category()); // 将栏目置空（无id无name）
			} else {
				category = categoryService.get(category); // 根据id获取更详细的栏目信息
				if (category == null) {// 此栏目不存在 
					category = new Category();
				}
				movie.setCategory(category);
			}
		}
		/////////////

		model.addAttribute("movie", movie);
		model.addAttribute("ename", "movie");
		return "modules/cms/movieForm";
	}

	@RequiresPermissions("cms:movie:edit")
	@RequestMapping(value = "save")
	public String save(Movie movie, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, movie)) {
			return form(movie, model);
		}
		CmsUtils.removeCache("topmovieList");
		movieService.save(movie);
		addMessage(redirectAttributes, "保存视频新闻成功");
		String categoryId = movie.getCategory() != null ? movie.getCategory().getId() : null;
		return "redirect:" + Global.getAdminPath() + "/cms/?repage&module=movie&category.id=" + (categoryId != null ? categoryId : "");
	}

	@RequiresPermissions("cms:movie:edit")
	@RequestMapping(value = "delete")
	public String delete(Movie movie, RedirectAttributes redirectAttributes) {
		movieService.delete(movie);
		addMessage(redirectAttributes, "删除视频新闻成功");
		String categoryId = movie.getCategory() != null ? movie.getCategory().getId() : null;
		return "redirect:" + Global.getAdminPath() + "/cms/movie?repage&category.id=" + (categoryId != null ? categoryId : "");
	}

	////////////////////////////// 断点续传视频 ////////////////////////////

	private static final String SETTINGS_KEY_CMS_MOVIE_FILESIZE = "cms.movie.fileSize";
	private static final String SETTINGS_KEY_CMS_MOVIE_FILETYPE = "cms.movie.fileType";
	private static final String SETTINGS_KEY_CMS_MOVIE_VIDEOURL = "cms.movie.videoUrl";
	private static final String SETTINGS_KEY_CMS_MOVIE_POSTERURL = "cms.movie.posterUrl";

	@Autowired
	private ChunkUploadService chunkUploadService;

	/**
	 * 秒传判断，断点判断。<br/>
	 * 根据md5值，查询文件是否（上传完了、上传一部分、没上传），执行相应的操作（秒传即不传、续传、上传）<br/>
	 */
	@RequiresPermissions("cms:movie:edit")
	@RequestMapping(value = "chunkupload/checkFile", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject checkFile(UploadChunk uploadChunk) {
		String md5 = uploadChunk.getMd5();
		logger.debug("checkFile(), uploadChunk ---->{} , md5----->{}", uploadChunk, md5);
		try {
			UploadStatus uploadStatus = chunkUploadService.getUploadStatus(md5);
			logger.debug("uploadStatus--------->{}", uploadStatus);
			ResultVo resultVo = new ResultVo(uploadStatus);
			// 上传一部分，将未传完的分片索引返回给客户端。
			if (UploadStatus.HALF == uploadStatus) {
				resultVo.setMissChunks(chunkUploadService.getMissChunks(md5));
			}
			return ResponseObject.success().data(resultVo);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseObject.failure();
		}
	}

	/**
	 * 上传分片文件
	 * 
	 * @param uploadChunk 分片
	 */
	@RequiresPermissions("cms:movie:edit")
	@RequestMapping(value = "chunkupload/fileUpload", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject fileUpload(UploadChunk uploadChunk, HttpServletRequest request) {

		//logger.debug("fileUpload(), uploadChunk ----> {}", uploadChunk);

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {

			logger.info("upload chunk start ...");

			// 文件大小
			Long size = uploadChunk.getSize();
			// 文件名称
			String name = uploadChunk.getName();
			logger.debug("file size = {}, file name = {}", size, name);

			// 检查文件大小和名称
			if (uploadChunk.getSize() == null || StringUtils.isBlank(uploadChunk.getName())) {
				logger.error("文件上传失败。{}", uploadChunk.toString());
				return ResponseObject.failure();
			}
			long limitSize = SettingsUtils.getSysConfig(SETTINGS_KEY_CMS_MOVIE_FILESIZE, 500) * 1024 * 1024; // 默认500M字节
			if (size <= 0 || size > limitSize) {
				logger.error("文件上传失败。{}", "文件大小不合法");
				return ResponseObject.failure().msg("文件大小不合法");
			}
			boolean contains = true;
			// 检查文件类型
			/*String extension = FileUtils.getFileExtension(name);
			
			String fileType = SettingsUtils.getSysConfig(SETTINGS_KEY_CMS_MOVIE_FILETYPE, "mp4");
			Set<String> extensionSet = Sets.newHashSet(fileType.split(","));
			for (String ex : extensionSet) {
				if (ex.trim().equalsIgnoreCase(extension)) {
					contains = true;
					break;
				}
			}*/
			if (!contains) {
				logger.error("文件上传失败。{}", "文件类型不合法");
				return ResponseObject.failure().msg("文件类型不合法");
			}

			// 上传路径  /upload/cms/video/
			// /userfiles/1/files/cms/video/file/2018/08/
			String uploadPath = SettingsUtils.getSysConfig(SETTINGS_KEY_CMS_MOVIE_VIDEOURL, "/cms/video/file/");
			User user = UserUtils.getUser();
			uploadPath = "/userfiles/" + user.getId() + "/files" + StringUtils.appendIfMissing(StringUtils.prependIfMissing(uploadPath, "/"), "/") + DateUtils.formatDate(new Date(), "yyyy/MM/");

			String realUploadPath = Servlets.getRealPath(request, uploadPath);

			try {

				// 上传文件分片
				String fileName = chunkUploadService.uploadFile(uploadChunk, realUploadPath);
				logger.info("fileName : {}", fileName);

				// fileName不为空时表示全部上传完了
				if (StringUtils.isNotBlank(fileName)) {

					String oldFileName = StringUtils.appendIfMissing(realUploadPath, File.separator) + fileName;
					logger.debug("oldFileName ------> {}", oldFileName);

					/*
					// 自定义其他参数uid
					String uid = uploadChunk.getUid();
					logger.debug("uid -------> {}", uid);
					if (user != null && StringUtils.isNotBlank(user.getId())) {
						if (!user.getId().equals(uid)) { // 是否是当前用户上传
							new File(oldFileName).delete();
							return ResponseObject.failure().msg("非法权限").data(false);
						}
					}
					*/

					// 待将上传的文件重命名： aaaa.mp4 ---> 096acc9395f344d2b2f372d0dbdd6220.mp4
					String newFileName = IdGen.uuid() + fileName.substring(fileName.lastIndexOf(".")); // 注意：uuid 每次生成都不一样
					String videoUrl = uploadPath + newFileName;

					// 上传完后，重命名
					chunkUploadService.renameFile(new File(oldFileName), newFileName, uploadChunk.getMd5());

					//
					File videoFile = new File(realUploadPath + File.separator + newFileName);
					logger.debug("file ---------> {}", videoFile);
					// 检查文件是否存在
					if (!checkIfFileUploadSuccess(videoFile)) {
						videoFile.delete();
						return ResponseObject.failure().msg("文件不存在或大小为0").data(false);
					}

					// 检查文件md5值
					// 注意： 在FfmpegTool.transcoding或者qt-faststart处理后，视频文件md5跟上传之前的md5可能就一样了。需要在它们之前检查md5值。
					String md5 = uploadChunk.getMd5();
					String fileMd5 = checkMessageDigest(videoFile, "MD5");
					logger.debug("md5 ---> {}, fileMd5 ---> {}", md5, fileMd5);
					/*if (!StringUtils.equalsIgnoreCase(md5, fileMd5)) {
						videoFile.delete();
						return ResponseObject.failure().msg("文件完整性检查失败！").data(false);
					}*/
/*
					// 转码处理和视频截图
					String posterUrl = null;
					try {
						posterUrl = dealVideoFile(request, realUploadPath, newFileName);
					} catch (Exception e) {
						e.printStackTrace();
					}
*/
					// 返回客户端数据：视频预览路径和视频截图路径
					Map<String, Object> map = new HashMap<>();
					map.put("videoUrl", videoUrl);
					//map.put("posterUrl", posterUrl);
					logger.debug("success , data ------> {}", map);
					return ResponseObject.success().data(map);
				}

				return ResponseObject.success().data(true);

			} catch (IOException e) {
				e.printStackTrace();
				logger.error("文件上传失败。{}", uploadChunk.toString());
			}
			logger.info("upload chunk end");
		}

		return ResponseObject.failure().data(false);
	}

	/**
	 * <p>
	 * 1、视频转码 2、视频截图 3、处理边下边播 <br/>
	 * <b>暂时只截图</b>
	 * </p>
	 */
	private String dealVideoFile(HttpServletRequest request, String realUploadDirPath, String newFileName) throws IOException {

		// 视频截图保存目录 /upload/cms/poster/
		String posterPath = SettingsUtils.getSysConfig(SETTINGS_KEY_CMS_MOVIE_POSTERURL, "/cms/video/poster/");
		User user = UserUtils.getUser();
		posterPath = "/userfiles/" + user.getId() + "/images" + StringUtils.appendIfMissing(StringUtils.prependIfMissing(posterPath, "/"), "/") + DateUtils.formatDate(new Date(), "yyyy/MM/");
		// /userfiles/1/images/cms/video/poster/2018/08/

		// 096acc9395f344d2b2f372d0dbdd6220.mp4.jpg
		String posterFileName = newFileName + ".jpg";
		// 视频截图文件 /upload/cms/poster/096acc9395f344d2b2f372d0dbdd6220.mp4.jpg
		String posterUrl = posterPath + posterFileName;

		// 视频调整、截取首图
		String ffmpegPath = SettingsUtils.getSysConfig("web.av.ffmpeg.path");//ffmpeg执行文件全路径
		logger.debug("ffmpegPath ------> {}", ffmpegPath);

		//如果不是MP4文件，执行转码。有bug，有的文件（是本来能在线播放的？）被处理后不能在线播放了
		/*
		if (!newFileName.toLowerCase().endsWith(".mp4")) {
			String newFilename = newFileName.substring(0, newFileName.lastIndexOf(".")) + ".mp4";//newFilename=aasdasd.mp4
			FfmpegTool.transcoding(ffmpegPath, descFileName, descDirNames + newFilename);
			//new File(descFileName).delete();//删除原文件
		}
		*/

		// 上传视频的截图
		boolean ffmpegSuccess = false;
		// 创建视频截图目录
		String realVideoImgDir = Servlets.getRealPath(request, posterPath);
		com.thinkgem.jeesite.common.utils.FileUtils.createDirectory(realVideoImgDir);

		String realVideoFile = StringUtils.appendIfMissing(realUploadDirPath, File.separator) + newFileName;
		String realPosterFile = StringUtils.appendIfMissing(realVideoImgDir, File.separator) + posterFileName;
		if (StringUtils.isNotBlank(ffmpegPath)) {
			File ffmpegFile = new File(ffmpegPath);
			logger.debug("ffmpegFile.exists() = {} , ffmpegFile.canExecute() = {}", ffmpegFile.exists(), ffmpegFile.canExecute());
			if (ffmpegFile.exists() && ffmpegFile.canExecute()) {
				try {
					logger.debug("FfmpegTool.getThumbStart()...... realVideoFile={}, realPosterFile={}", realVideoFile, realPosterFile);
					FfmpegTool.getThumbStart(ffmpegPath, realVideoFile, realPosterFile, -1, -1);

					ffmpegSuccess = checkIfFileCreate(new File(realPosterFile), 10, 500);

					logger.debug("new File(realVideoImgFile).exists() ------> {}", new File(realPosterFile).exists());

					logger.debug("ffmpegSuccess ------> {}", ffmpegSuccess);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		if (!ffmpegSuccess) {
			boolean flag = false;
			try {
				Class.forName("org.bytedeco.javacv.FFmpegFrameGrabber");
				flag = true;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			logger.debug("org.bytedeco.javacv.FFmpegFrameGrabber exists ? ----> {}", flag);
			if (flag) {
				try {
					logger.debug("JavacvTool.fetchFrame()......{}", realPosterFile);
					FfmpegTool.fetchFrame(realVideoFile, realPosterFile);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}

		////
		// 解决视频不能边下载边播放问题。
		/* if (StringUtils.endsWithIgnoreCase(newFileName, ".mp4")) {
			String qt_faststartPath = SettingsUtils.getSysConfig("web.av.qt-faststart.path");//qt-faststart安装路径
			if (StringUtils.isNotBlank(qt_faststartPath)) {
				try {
					logger.debug("FfmpegTool.qtFastStart()......{}", realVideoFile);
					FfmpegTool.qtFastStart(qt_faststartPath, realVideoFile, realVideoFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}*/

		return posterUrl;
	}

	/**
	 * 检查文件是否上传成功。文件存在硬盘上并且大小大于0
	 */
	private boolean checkIfFileUploadSuccess(File file) {
		if (file == null) {
			return false;
		}
		if (!file.exists()) {
			logger.warn("file not exists");
			return false;
		}
		long size = com.thinkgem.jeesite.common.utils.FileUtils.sizeOf(file);
		if (size == 0) {
			logger.warn("file size = 0");
			return false;
		}
		return true;
	}

	/**
	 * 检查文件是否生成
	 * 
	 * @param file   文件
	 * @param times  检查次数
	 * @param millis 间隔时间（毫秒）
	 * @return
	 */
	private boolean checkIfFileCreate(File file, int times, long millis) {
		while (times-- > 0) {
			logger.debug("times -----> {}", times);
			try {
				if (file.exists()) {
					return true;
				}
				Thread.sleep(millis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 对文件进行md
	 */
	private String checkMessageDigest(File file, String algorithm) {
		if (file == null || StringUtils.isBlank(algorithm)) {
			return null;
		}
		MessageDigest messagedigest;
		try {
			messagedigest = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
			return null;
		}
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = in.read(buffer)) != -1) {
				messagedigest.update(buffer, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
		byte[] digest = messagedigest.digest();
		return Hex.encodeToString(digest).toUpperCase();
	}

}