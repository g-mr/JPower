package top.jpower.jpower.operate;

import top.jpower.jpower.dbs.entity.TbResourceFile;

import java.io.IOException;

/**
 * 上传文件定义
 * @Author mr.g
 **/
public interface FileOperate {

	/**
	 * 上传文件
	 *
	 * @param file 上传文件
	 * @return TbCoreFile
	 */
	TbResourceFile upload(byte[] bytes, String name, Long size);

	/**
	 * 下载文件
	 * @Author mr.g
	 * @param coreFile
	 * @return java.lang.Boolean
	 **/
	Boolean download(TbResourceFile coreFile) throws IOException;

	/**
	 * 获取文件字节
	 * @Author mr.g
	 * @param coreFile
	 * @return byte[]
	 **/
	byte[] getByte(TbResourceFile coreFile);

	/**
	 * 删除文件
	 * @Author mr.g
	 * @param tbCoreFile
	 * @return java.lang.Boolean
	 **/
	Boolean deleteFile(TbResourceFile tbCoreFile);

	/**
	 * 获取文件外链
	 * @author mr.g
	 * @param coreFile 文件
	 * @return 外链
	 **/
	String getUrl(TbResourceFile coreFile);
}
