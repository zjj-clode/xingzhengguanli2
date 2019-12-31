package com.cloudinte.modules.upload.web;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.utils.SettingsUtils;

@Service
public class ChunkUploadServiceImpl implements ChunkUploadService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	/** 缓存名称 */
	private static final String CACHE_NAME_CHUNK_UPLOAD = "cache_key_chunk_upload";
	/** 上传进度记录key */
	private static final String CACHE_KEY_PROCESS_ = "cache_key_process_";
	/** 上传完成状态key */
	private static final String CACHE_KEY_FINISHED_ = "cache_key_finished_";
	/** 文件存放路径key */
	private static final String CACHE_KEY_FILE_PATH_ = "cache_key_file_path_";

	private static final String SETTINGS_KEY_CHUNKUPLOAD_CHUNK_SIZE = "chunkUpload.chunk.size";

	/** 默认的分片大小：5M */
	public static final long CHUNK_SIZE = 5242880;

	@Override
	public String uploadFile(UploadChunk uploadChunk, String uploadDirPath) throws IOException {
		//logger.debug("uploadChunk-------->{}", uploadChunk);

		// 上传小文件时，前端不分片，参数chunks=null, chunk=null
		// 为了兼容代码将chunk设置为0，第1个分片
		if (uploadChunk.getChunk() == null) {
			uploadChunk.setChunk(0);
		}
		// 为了兼容代码将chunks设置为1，共1个分片
		if (uploadChunk.getChunks() == null) {
			uploadChunk.setChunks(1);
		}

		// 上传目录
		//logger.debug("uploadDirPath-------->{}", uploadDirPath);
		File dir = new File(uploadDirPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		// 根据key找到上传记录
		String key = uploadChunk.getMd5();

		// 文件名称
		String fileName = uploadChunk.getName();

		// 临时文件跟key值挂钩
		String tempFileName = fileName + "." + key;
		File tmpFile = new File(uploadDirPath, tempFileName);
		logger.debug("tmpFile-------->{}", tmpFile);

		// 随机访问
		RandomAccessFile accessTmpFile = new RandomAccessFile(tmpFile, "rw");
		// 偏移量 = 分片大小 * 当前分片数
		long chunkSize = SettingsUtils.getSysConfig(SETTINGS_KEY_CHUNKUPLOAD_CHUNK_SIZE, CHUNK_SIZE);

		long offset = chunkSize * uploadChunk.getChunk();
		//logger.debug("offset------->{}", offset);
		// 待写入该分片数据
		byte[] fileData = uploadChunk.getFile().getBytes();

		///////////////////////////////////////
		// 定位到该分片的偏移量并写入数据
		//mapAndPut(accessTmpFile, offset, fileData);
		seekAndWrite(accessTmpFile, offset, fileData);
		///////////////////////////////////////

		// 释放
		accessTmpFile.close();

		String absolutePath = tmpFile.getAbsolutePath();
		String filePath = getFilePathFromCache(key);
		if (filePath == null) {
			setFilePathToCache(key, absolutePath);
		}

		// 是否上传完了
		boolean isFinished = checkAndSetUploadProgress(uploadChunk, uploadDirPath);
		// 上传完后，将临时文件重命名为真正的文件名称。
		if (isFinished) {
			boolean flag = renameFile(tmpFile, fileName, key);
			if (flag) {
				return fileName;
			} else {
				return tempFileName;
			}
		}
		return null;
	}

	/**
	 * 基于RandomAccessFile随机写实现
	 */
	private void seekAndWrite(RandomAccessFile accessTmpFile, long offset, byte[] fileData) throws IOException {
		accessTmpFile.seek(offset);
		accessTmpFile.write(fileData);
	}

	/**
	 * 基于MappedByteBuffer来实现文件的保存。<br/>
	 * bug：1、上传后的文件的key会发生变化。2、文件超过300M会报异常
	 */
	private void mapAndPut(RandomAccessFile accessTmpFile, long offset, byte[] fileData) throws IOException {
		FileChannel fileChannel = accessTmpFile.getChannel();
		MappedByteBuffer mappedByteBuffer = null;
		try {
			// TODO 报异常：请求的操作无法在使用用户映射区域打开的文件上执行。
			mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, offset, fileData.length);
			mappedByteBuffer.put(fileData);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 释放
			FileMD5Util.freedMappedByteBuffer(mappedByteBuffer);
			fileChannel.close();
		}
	}

	/**
	 * 检查并修改文件上传进度。进度保存在一个int数组中，数组长度为总分片数，对应位置的值为0表示该分片未完成上传，为1表示该分片已完成上传。
	 */
	private boolean checkAndSetUploadProgress(UploadChunk uploadChunk, String uploadDirPath) throws IOException {

		String key = uploadChunk.getMd5();

		// 读取进度
		int[] process = getProcessFromCache(key);
		// 不存在，初始化进度：0为未完成
		if (process == null) {
			process = new int[uploadChunk.getChunks()];
			for (int i = 0; i < process.length; i++) {
				process[i] = 0;
			}
		}
		// 设置当前chunk位置已完成：1为已完成
		process[uploadChunk.getChunk()] = 1;
		// 检查是否都完成了
		boolean finished = true;
		for (int i = 0; i < process.length; i++) {
			if (process[i] != 1) {
				finished = false;
				break;
			}
		}
		//////////////////////////////
		String confStr = "[";
		for (int i : process) {
			confStr += i == 1 ? "*" : " ";
		}
		confStr += "]";
		logger.debug("上传进度条 ---------> {}", confStr);
		//////////////////////////////
		if (finished) {
			setUploadStatusToCache(key, UploadStatus.ALL);
			deleteProcessFromCache(key);
			return true;
		} else {
			setUploadStatusToCache(key, UploadStatus.HALF);
			setProcessToCache(key, process);
			return false;
		}
	}

	@Override
	public boolean renameFile(File oldFile, String newFileName, String key) {
		if (!oldFile.exists() || oldFile.isDirectory()) {
			logger.info("File does not exist: " + oldFile.getName());
			return false;
		}
		String parent = oldFile.getParent();
		File newFile = new File(parent + File.separatorChar + newFileName);
		logger.debug("newFile ----> {}", newFile.getAbsolutePath());
		if (newFile.exists()) {
			boolean delete = newFile.delete();
			logger.debug("delete newFile : {} ----> {}", newFile.getAbsolutePath(), delete);
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		boolean succeeded = oldFile.renameTo(newFile);
		if (succeeded) {
			// 重命名成功，更新缓存中的文件路径
			setFilePathToCache(key, newFile.getAbsolutePath());
		}
		return succeeded;
	}

	@Override
	public UploadStatus getUploadStatus(String key) {
		UploadStatus uploadStatus = getUploadStatusFromCache(key);
		if (uploadStatus == null) {
			deleteFilePathFromCache(key);
			deleteProcessFromCache(key);
			deleteUploadStatusFromCache(key);
			return UploadStatus.NONE;
		}
		// 文件不存在，返回“未上传过”
		String filepath = getFilePathFromCache(key);
		if (StringUtils.isBlank(filepath)) {
			deleteFilePathFromCache(key);
			deleteProcessFromCache(key);
			deleteUploadStatusFromCache(key);
			return UploadStatus.NONE;
		}
		// 文件不存在，返回“未上传过”
		File file = new File(filepath);
		if (!file.exists()) {
			deleteFilePathFromCache(key);
			deleteProcessFromCache(key);
			deleteUploadStatusFromCache(key);
			return UploadStatus.NONE;
		}
		return uploadStatus;
	}

	@Override
	public List<Integer> getMissChunks(String key) {
		List<Integer> missChunkList = new LinkedList<>();
		int[] conf = getProcessFromCache(key);
		for (int i = 0; i < conf.length; i++) {
			if (conf[i] != 1) {
				logger.debug("分片{}未上传", i);
				missChunkList.add(i);
			}
		}
		return missChunkList;
	}

	/**
	 * 上传进度
	 */
	private int[] getProcessFromCache(String key) {
		return (int[]) CacheUtils.get(CACHE_NAME_CHUNK_UPLOAD, CACHE_KEY_PROCESS_ + key);
	}

	/**
	 * 上传进度
	 */
	private void setProcessToCache(String key, int[] process) {
		CacheUtils.put(CACHE_NAME_CHUNK_UPLOAD, CACHE_KEY_PROCESS_ + key, process);
	}

	/**
	 * 上传进度
	 */
	private void deleteProcessFromCache(String key) {
		CacheUtils.remove(CACHE_NAME_CHUNK_UPLOAD, CACHE_KEY_PROCESS_ + key);
	}

	/**
	 * 上传状态
	 */
	private UploadStatus getUploadStatusFromCache(String key) {
		return (UploadStatus) CacheUtils.get(CACHE_NAME_CHUNK_UPLOAD, CACHE_KEY_FINISHED_ + key);
	}

	/**
	 * 上传状态
	 */
	private void setUploadStatusToCache(String key, UploadStatus uploadStatus) {
		logger.debug("cache put upload status ----> {}", uploadStatus);
		CacheUtils.put(CACHE_NAME_CHUNK_UPLOAD, CACHE_KEY_FINISHED_ + key, uploadStatus);
	}

	/**
	 * 上传状态
	 */
	private void deleteUploadStatusFromCache(String key) {
		CacheUtils.remove(CACHE_NAME_CHUNK_UPLOAD, CACHE_KEY_FINISHED_ + key);
	}

	/**
	 * 上传路径
	 */
	private String getFilePathFromCache(String key) {
		return (String) CacheUtils.get(CACHE_NAME_CHUNK_UPLOAD, CACHE_KEY_FILE_PATH_ + key);
	}

	/**
	 * 上传路径
	 */
	private void setFilePathToCache(String key, String filePath) {
		logger.debug("cache put upload file path ----> {}", filePath);
		CacheUtils.put(CACHE_NAME_CHUNK_UPLOAD, CACHE_KEY_FILE_PATH_ + key, filePath);
	}

	/**
	 * 上传路径
	 */
	private void deleteFilePathFromCache(String key) {
		CacheUtils.remove(CACHE_NAME_CHUNK_UPLOAD, CACHE_KEY_FILE_PATH_ + key);
	}

	@Override
	public void destroy() {
		logger.info("destroy......");
		CacheUtils.getCacheManager().removeCache(CACHE_NAME_CHUNK_UPLOAD);
	}

	@Override
	public void init() {
		logger.info("init......");
	}
}
