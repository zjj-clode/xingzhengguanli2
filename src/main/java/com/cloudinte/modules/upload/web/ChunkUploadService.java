package com.cloudinte.modules.upload.web;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 断点续传、秒传服务类<br/>
 * 续传需要记录上传一部分的文件的唯一键（md5），以及哪些片段没有上传完，客户端上传的时候一比对md5，判断是同一个文件，那就告诉客户端传那些没有传过的片段就行！<br/>
 * 秒传需要记录已经上传完的文件的唯一键（md5），再次上传的时候一比对md5，判断是同一个文件，那就不用传了，直接拿去用就行！<br/>
 * 续传的用处可能大一点<br/>
 * 记录可以使用内存、缓存、数据库等等实现。<br/>
 */
public interface ChunkUploadService {

	/**
	 * 上传文件分片
	 * 
	 * @param uploadDirPath
	 *            上传到哪个目录
	 * @param uploadChunk
	 *            文件分片
	 * @return 上传完后的最终文件名称，如果还没上传完返回null
	 */
	String uploadFile(UploadChunk uploadChunk, String uploadDirPath) throws IOException;

	/**
	 * 检查文件上传的完成状态。
	 * 
	 * @param md5
	 *            上传文件的md5值，作为文件唯一标识
	 * @return UploadStatus 从未上传过；上传了一部分；已完成上传
	 */
	UploadStatus getUploadStatus(String key);

	/**
	 * 未完成的分片
	 * 
	 * @param md5
	 *            上传文件的md5值，作为文件唯一标识
	 * @return
	 */
	List<Integer> getMissChunks(String key);

	/**
	 * 重命名上传文件。会更新缓存中的上传记录。
	 * 
	 * @param oldFile
	 * @param newFileName
	 * @param key
	 * @return
	 */
	boolean renameFile(File oldFile, String newFileName, String key);

	void init();

	void destroy();

	public static enum UploadStatus {
		/**
		 * 已完成上传
		 */
		ALL,
		/**
		 * 从未上传过
		 */
		NONE,
		/**
		 * 上传了一部分
		 */
		HALF;
	}
}
