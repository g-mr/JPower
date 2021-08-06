package com.wlcb.jpower.operate.storage;

import com.wlcb.jpower.dbs.entity.TbCoreFile;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.BusinessException;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.common.utils.*;
import com.wlcb.jpower.module.common.utils.constants.ConstantsUtils;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import com.wlcb.jpower.operate.FileOperate;
import com.wlcb.jpower.service.CoreFileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static com.wlcb.jpower.module.common.utils.constants.ConstantsEnum.FILE_STORAGE_TYPE.SERVER;
import static com.wlcb.jpower.operate.storage.ServerFileOperate.STORAGE_TYPE;

/**
 * 上传文件保存到服务器
 * @Author mr.g
 **/
@Component(STORAGE_TYPE)
public class ServerFileOperate implements FileOperate {

	public static final String STORAGE_TYPE = "SERVER";
	@Value("${jpower.fileParentPath:}")
	private String fileParentPath;
	@Autowired
	private CoreFileService coreFileService;

	private final String pathPrefix = "file";
	@Override
	public TbCoreFile upload(MultipartFile file) throws IOException {
		JpowerAssert.notEmpty(fileParentPath, JpowerError.Unknown,"未配置文件保存路径");

		String path = MultipartFileUtil.saveFile(file,fileParentPath + File.separator + pathPrefix);
		File saveFile = new File(fileParentPath + File.separator +pathPrefix+File.separator+path);

		TbCoreFile coreFile = new TbCoreFile();
		coreFile.setPath(pathPrefix + File.separator +path);
		coreFile.setName(saveFile.getName());
		coreFile.setStorageType(SERVER.getValue());
		coreFile.setFileType(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(StringPool.DOT) + 1));
		coreFile.setFileSize(file.getSize());
		coreFile.setId(UUIDUtil.getUUID());
		coreFile.setMark(DESUtil.encrypt(coreFile.getId(), ConstantsUtils.FILE_DES_KEY));

		try {
			if (!coreFileService.add(coreFile)){
				FileUtil.deleteFile(saveFile);
				return null;
			}
		}catch (Exception e){
			FileUtil.deleteFile(saveFile);
			return null;
		}


		return coreFile;
	}

	@Override
	public Boolean download(TbCoreFile coreFile) throws IOException {
		String path = coreFile.getPath();
		if(StringUtils.isBlank(path)){
			throw new BusinessException("文件不存在，无法下载");
		}

		File file = new File(fileParentPath+File.separator+path);
		if (!file.exists()){
			throw new BusinessException(file.getName()+"文件不存在，无法下载");
		}
		Integer is = FileUtil.download(file, WebUtil.getResponse(),file.getName());

		return is == 0;
	}

	@Override
	public byte[] getByte(TbCoreFile coreFile){
		String path = coreFile.getPath();
		JpowerAssert.notEmpty(path,JpowerError.Parser,"文件路径不存在");

		File file = new File(fileParentPath+File.separator+path);
		if (!file.exists()){
			throw new BusinessException(file.getName()+"文件不存在，无法下载");
		}

		return cn.hutool.core.io.FileUtil.readBytes(file);
	}

	@Override
	public Boolean deleteFile(TbCoreFile tbCoreFile) {
		File file = new File(fileParentPath+File.separator+tbCoreFile.getPath());
		FileUtil.deleteFile(file);
		return true;
	}
}
