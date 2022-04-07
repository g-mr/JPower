package com.wlcb.jpower.operate.storage;

import cn.hutool.core.io.FileTypeUtil;
import com.wlcb.jpower.dbs.entity.TbCoreFile;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.common.utils.DESUtil;
import com.wlcb.jpower.module.common.utils.FileUtil;
import com.wlcb.jpower.module.common.utils.UUIDUtil;
import com.wlcb.jpower.module.common.utils.WebUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsUtils;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import com.wlcb.jpower.operate.FileOperate;
import com.wlcb.jpower.service.CoreFileService;
import com.wlcb.jpower.utils.FileDfsUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.wlcb.jpower.module.common.utils.constants.ConstantsEnum.FILE_STORAGE_TYPE.FASTDFS;
import static com.wlcb.jpower.operate.storage.FastDfsFileOperate.STORAGE_TYPE;

/**
 * 上传文件保存到FastDfs
 * @Author mr.g
 **/
@Component(STORAGE_TYPE)
@AllArgsConstructor
public class FastDfsFileOperate implements FileOperate {

	public static final String STORAGE_TYPE = "FASTDFS";
	private CoreFileService coreFileService;


	@Override
	public TbCoreFile upload(MultipartFile file) throws IOException {

		String originalFileName = file.getOriginalFilename();
		String dfsPath = FileDfsUtil.upload(file.getBytes(),file.getSize(),originalFileName.substring(originalFileName.lastIndexOf(StringPool.DOT)));

		TbCoreFile coreFile = new TbCoreFile();
		coreFile.setFileType(FileTypeUtil.getType(file.getInputStream(),originalFileName));
		coreFile.setFileSize(file.getSize());
		coreFile.setId(UUIDUtil.getUUID());
		coreFile.setMark(DESUtil.encrypt(coreFile.getId(), ConstantsUtils.FILE_DES_KEY));
		coreFile.setStorageType(FASTDFS.getValue());
		coreFile.setPath(dfsPath);
		coreFile.setName(originalFileName);

		try {
			if (!coreFileService.add(coreFile)){
				FileDfsUtil.deleteFile(dfsPath);
				return null;
			}
		}catch (Exception e){
			FileDfsUtil.deleteFile(dfsPath);
			return null;
		}

		return coreFile;
	}

	@Override
	public Boolean download(TbCoreFile coreFile) throws IOException {
		JpowerAssert.notEmpty(coreFile.getPath(), JpowerError.Parser, "文件不存在");
		byte[] bytes = FileDfsUtil.downloadFile(coreFile.getPath());
		return FileUtil.download(bytes, WebUtil.getResponse(), coreFile.getName());
	}

	@Override
	public byte[] getByte(TbCoreFile coreFile) {
		JpowerAssert.notEmpty(coreFile.getPath(), JpowerError.Parser, "文件不存在");
		return FileDfsUtil.downloadFile(coreFile.getPath());
	}

	@Override
	public Boolean deleteFile(TbCoreFile coreFile) {
		JpowerAssert.notEmpty(coreFile.getPath(), JpowerError.Parser, "文件不存在");
		return FileDfsUtil.deleteFile(coreFile.getPath());
	}
}
