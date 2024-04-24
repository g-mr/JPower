package top.jpower.jpower.operate.storage;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.IoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import top.jpower.jpower.dbs.entity.TbResourceFile;
import top.jpower.jpower.module.common.utils.*;
import top.jpower.jpower.module.common.utils.constants.ConstantsUtils;
import top.jpower.jpower.module.mp.support.Condition;
import top.jpower.jpower.operate.FileOperate;
import top.jpower.jpower.service.ResourceFileService;

import java.io.IOException;

import static top.jpower.jpower.module.common.utils.constants.ConstantsEnum.FILE_STORAGE_TYPE.DATABASE;
import static top.jpower.jpower.operate.storage.DatabaseFileOperate.STORAGE_TYPE;

/**
 * 上传文件保存到FastDfs
 * @Author mr.g
 **/
@Component(STORAGE_TYPE)
@RequiredArgsConstructor
public class DatabaseFileOperate implements FileOperate {

	public static final String STORAGE_TYPE = "DATABASE";
	private final ResourceFileService coreFileService;

	@Override
	public TbResourceFile upload(byte[] bytes, String name, Long size) {

		TbResourceFile coreFile = new TbResourceFile();
		coreFile.setFileType(FileTypeUtil.getType(IoUtil.toStream(bytes),name));
		coreFile.setFileSize(size);
		coreFile.setId(Fc.randomSnowFlakeId());
		coreFile.setMark(DesUtil.encrypt(Fc.toStr(coreFile.getId()), ConstantsUtils.FILE_DES_KEY));
		coreFile.setStorageType(DATABASE.getValue());
		coreFile.setContent(bytes);
		coreFile.setName(name);

		coreFileService.add(coreFile);

		return coreFile;
	}

	@Override
	public Boolean download(TbResourceFile coreFile) throws IOException {
		return FileUtil.download(coreFile.getContent(), WebUtil.getResponse(), coreFile.getName());
	}

	@Override
	public byte[] getByte(TbResourceFile coreFile) {
		return coreFileService.getOne(Condition.<TbResourceFile>getQueryWrapper()
				.lambda()
				.select(TbResourceFile::getContent)
				.eq(TbResourceFile::getId,coreFile.getId())).getContent();
	}

	@Override
	public Boolean deleteFile(TbResourceFile tbCoreFile) {
		return true;
	}

	/**
	 * 获取文件外链
	 *
	 * @param coreFile 文件
	 * @return 外链
	 * @author mr.g
	 **/
	@Override
	public String getUrl(TbResourceFile coreFile) {
		String url = WebUtil.getRequest().getRequestURL().toString();
		return StringUtil.replace(url, "/url/", "/download/");
	}
}
