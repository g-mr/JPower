package com.wlcb.jpower.operate;

import com.wlcb.jpower.dbs.entity.TbCoreFile;
import org.springframework.web.multipart.MultipartFile;

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
	TbCoreFile upload(MultipartFile file) throws Exception;

	/**
	 * 下载文件
	 * @Author mr.g
	 * @param coreFile
	 * @return java.lang.Boolean
	 **/
	Boolean download(TbCoreFile coreFile) throws IOException;

	/**
	 * 删除文件
	 * @Author mr.g
	 * @param tbCoreFile
	 * @return java.lang.Boolean
	 **/
	Boolean deleteFile(TbCoreFile tbCoreFile);
}
