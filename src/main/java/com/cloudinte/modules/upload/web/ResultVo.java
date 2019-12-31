package com.cloudinte.modules.upload.web;

import java.util.List;

import com.cloudinte.modules.upload.web.ChunkUploadService.UploadStatus;
import com.google.common.collect.Lists;

public class ResultVo {

	/** 文件上传状态 */
	private UploadStatus uploadStatus;

	/** 未上传的片段 */
	private List<Integer> missChunks = Lists.newArrayList();

	public ResultVo(UploadStatus uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

	public ResultVo(UploadStatus uploadStatus, List<Integer> missChunks) {
		this(uploadStatus);
		this.missChunks = missChunks;
	}

	public List<Integer> getMissChunks() {
		return missChunks;
	}

	public void setMissChunks(List<Integer> missChunks) {
		this.missChunks = missChunks;
	}

	public UploadStatus getUploadStatus() {
		return uploadStatus;
	}

	public void setUploadStatus(UploadStatus uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

}
