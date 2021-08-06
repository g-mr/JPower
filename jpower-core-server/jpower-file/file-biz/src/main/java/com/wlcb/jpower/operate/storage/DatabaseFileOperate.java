package com.wlcb.jpower.operate.storage;

import com.wlcb.jpower.dbs.entity.TbCoreFile;
import com.wlcb.jpower.module.common.utils.DESUtil;
import com.wlcb.jpower.module.common.utils.FileUtil;
import com.wlcb.jpower.module.common.utils.UUIDUtil;
import com.wlcb.jpower.module.common.utils.WebUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsUtils;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import com.wlcb.jpower.operate.FileOperate;
import com.wlcb.jpower.service.CoreFileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.wlcb.jpower.module.common.utils.constants.ConstantsEnum.FILE_STORAGE_TYPE.DATABASE;
import static com.wlcb.jpower.operate.storage.DatabaseFileOperate.STORAGE_TYPE;

/**
 * 上传文件保存到FastDfs
 * @Author mr.g
 **/
@Component(STORAGE_TYPE)
@AllArgsConstructor
public class DatabaseFileOperate implements FileOperate {

	public static final String STORAGE_TYPE = "DATABASE";
	private CoreFileService coreFileService;


	@Override
	public TbCoreFile upload(MultipartFile file) throws IOException {

		String originalFileName = file.getOriginalFilename();

		TbCoreFile coreFile = new TbCoreFile();
		coreFile.setFileType(originalFileName.substring(originalFileName.lastIndexOf(StringPool.DOT) + 1));
		coreFile.setFileSize(file.getSize());
		coreFile.setId(UUIDUtil.getUUID());
		coreFile.setMark(DESUtil.encrypt(coreFile.getId(), ConstantsUtils.FILE_DES_KEY));
		coreFile.setStorageType(DATABASE.getValue());
		coreFile.setContent(file.getBytes());
		coreFile.setName(UUIDUtil.getUUID()+StringPool.DOT+coreFile.getFileType());

		coreFileService.add(coreFile);

		return coreFile;
	}

	@Override
	public Boolean download(TbCoreFile coreFile) throws IOException {
		return FileUtil.download(coreFile.getContent(), WebUtil.getResponse(), coreFile.getName());
	}

	@Override
	public byte[] getByte(TbCoreFile coreFile) {
		return coreFile.getContent();
	}

	@Override
	public Boolean deleteFile(TbCoreFile tbCoreFile) {
		return true;
	}
}
