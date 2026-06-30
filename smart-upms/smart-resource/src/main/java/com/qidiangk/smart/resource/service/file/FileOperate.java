package com.qidiangk.smart.resource.service.file;

import com.qidiangk.smart.resource.dbs.entity.ResourceFile;

import java.io.IOException;

/**
 * 文件操作接口
 * <p>
 * 定义文件上传、下载、删除等基本操作
 * </p>
 *
 * @author mr.g
 */
public interface FileOperate {

	/**
	 * 上传文件
	 *
	 * @param bytes 文件字节数组
	 * @param name  文件名
	 * @param size  文件大小
	 * @return 文件信息
	 */
	ResourceFile upload(byte[] bytes, String name, Long size, Long groupId);

	/**
	 * 下载文件
	 *
	 * @author mr.g
	 * @param coreFile 文件实体
	 * @return Boolean 下载结果
	 * @throws IOException IO异常
	 */
	Boolean download(ResourceFile coreFile) throws IOException;

	/**
	 * 获取文件字节
	 *
	 * @author mr.g
	 * @param coreFile 文件实体
	 * @return byte[] 文件字节数组
	 */
	byte[] getByte(ResourceFile coreFile);

	/**
	 * 删除文件
	 *
	 * @author mr.g
	 * @param tbCoreFile
	 * @return java.lang.Boolean
	 */
	Boolean deleteFile(ResourceFile tbCoreFile);

	/**
	 * 获取文件外链
	 *
	 * @author mr.g
	 * @param coreFile 文件
	 * @return 外链
	 */
	String getUrl(ResourceFile coreFile);
}
