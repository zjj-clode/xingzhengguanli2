package com.cloudinte.modules.upload.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloudinte.modules.upload.web.ChunkUploadService.UploadStatus;
import com.thinkgem.jeesite.common.mapper.ResponseObject;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.sys.utils.SettingsUtils;

/**
 * 断点续传。例子Controller
 * 
 * @author lsp
 *
 */
//@Controller
//@RequestMapping(value = "/index")
public class ChunkUploadController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(ChunkUploadController.class);

	@Autowired
	private ChunkUploadService chunkUploadService;

	/**
	 * 秒传判断，断点判断。<br/>
	 * 根据md5值，查询文件是否（上传完了、上传一部分、没上传），执行相应的操作（秒传即不传、续传、上传）<br/>
	 */
	@RequestMapping(value = "checkFileMd5", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject checkFileMd5(UploadChunk uploadChunk) throws IOException {
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
	 * 上传文件
	 */
	@RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject fileUpload(UploadChunk uploadChunk, HttpServletRequest request) {

		logger.debug("uploadChunk ----> {}", uploadChunk);

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
			logger.info("上传文件start。");
			try {
				String uploadDirPath = SettingsUtils.getSysConfig("uploadDirPath", Servlets.getRealPath(request, "/uploadDirPath"));
				String fileName = chunkUploadService.uploadFile(uploadChunk, uploadDirPath);
				if (StringUtils.isNotBlank(fileName)) {
					onUploadAll();
				}
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("文件上传失败。{}", uploadChunk.toString());
			}
			logger.info("上传文件end。");
			return ResponseObject.success().data(true);
		}
		return ResponseObject.success().data(false);
	}

	private void onUploadAll() {
		logger.debug("onUploadAll ......");
	}
}
