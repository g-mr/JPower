package top.jpower.jpower.operate.storage;

import cn.hutool.core.io.FileTypeUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import top.jpower.jpower.dbs.entity.TbCoreFile;
import top.jpower.jpower.module.common.utils.DesUtil;
import top.jpower.jpower.module.common.utils.Fc;
import top.jpower.jpower.module.common.utils.FileUtil;
import top.jpower.jpower.module.common.utils.WebUtil;
import top.jpower.jpower.module.common.utils.constants.ConstantsUtils;
import top.jpower.jpower.module.mp.support.Condition;
import top.jpower.jpower.operate.FileOperate;
import top.jpower.jpower.service.CoreFileService;

import java.io.IOException;

import static top.jpower.jpower.module.common.utils.constants.ConstantsEnum.FILE_STORAGE_TYPE.DATABASE;
import static top.jpower.jpower.operate.storage.DatabaseFileOperate.STORAGE_TYPE;

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
		coreFile.setFileType(FileTypeUtil.getType(file.getInputStream(),originalFileName));
		coreFile.setFileSize(file.getSize());
		coreFile.setId(Fc.randomSnowFlakeId());
		coreFile.setMark(DesUtil.encrypt(Fc.toStr(coreFile.getId()), ConstantsUtils.FILE_DES_KEY));
		coreFile.setStorageType(DATABASE.getValue());
		coreFile.setContent(file.getBytes());
		coreFile.setName(originalFileName);

		coreFileService.add(coreFile);

		return coreFile;
	}

	@Override
	public Boolean download(TbCoreFile coreFile) throws IOException {
		return FileUtil.download(coreFile.getContent(), WebUtil.getResponse(), coreFile.getName());
	}

	@Override
	public byte[] getByte(TbCoreFile coreFile) {
		return coreFileService.getOne(Condition.<TbCoreFile>getQueryWrapper()
				.lambda()
				.select(TbCoreFile::getContent)
				.eq(TbCoreFile::getId,coreFile.getId())).getContent();
	}

	@Override
	public Boolean deleteFile(TbCoreFile tbCoreFile) {
		return true;
	}
}
