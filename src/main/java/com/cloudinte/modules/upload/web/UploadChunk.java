package com.cloudinte.modules.upload.web;

import org.springframework.web.multipart.MultipartFile;

/**
 * 接受webuploader上传时的请求参数
 */
public class UploadChunk {

	/** 可以指定为登录用户的id */
	private String uid;

	/** 文件id，如：WU_FILE_0 */
	private String id;

	/** 上传文件的MD5值 */
	private String md5;

	/** 每个分块大小，单位：字节 */
	private Integer chunkSize;

	/** 总分片数量 */
	private Integer chunks;

	/** 当前为第几块分片，从0开始算 */
	private Integer chunk;

	/** 上传的文件分片 */
	private MultipartFile file;

	/** 文件名，如： jdk-9.0.4_windows-x64_bin.exe */
	private String name;

	/** 文件类型 application/x-msdownload */
	private String type;

	/** 文件最后修改时间 */
	private String lastModifiedDate;

	/** 文件大小，单位：字节 */
	private Long size;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getChunks() {
		return chunks;
	}

	public void setChunks(Integer chunks) {
		this.chunks = chunks;
	}

	public Integer getChunk() {
		return chunk;
	}

	public void setChunk(Integer chunk) {
		this.chunk = chunk;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public Integer getChunkSize() {
		return chunkSize;
	}

	public void setChunkSize(Integer chunkSize) {
		this.chunkSize = chunkSize;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Override
	public String toString() {
		return "MultipartFileParam [uid=" + uid + ", md5=" + md5 + ", chunkSize=" + chunkSize + ", id=" + id + ", name=" + name + ", type=" + type
				+ ", lastModifiedDate=" + lastModifiedDate + ", size=" + size + ", chunks=" + chunks + ", chunk=" + chunk + ", file=" + file + "]";
	}

}
