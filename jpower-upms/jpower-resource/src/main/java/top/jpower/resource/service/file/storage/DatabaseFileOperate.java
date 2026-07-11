package top.jpower.resource.service.file.storage;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.IoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import top.jpower.common.constants.DefaultValConstants;
import top.jpower.common.enums.OssCategoryEnum;
import top.jpower.core.util.utils.*;
import top.jpower.resource.dbs.dao.ResourceFileDao;
import top.jpower.resource.dbs.entity.ResourceFile;
import top.jpower.resource.service.file.FileOperate;

import java.io.IOException;

import static top.jpower.resource.service.file.storage.DatabaseFileOperate.STORAGE_TYPE;

/**
 * 数据库文件操作实现
 * <p>
 * 将文件内容直接存储到数据库中的实现类
 * </p>
 *
 * @author mr.g
 * @since 2020-07-28
 */
@Component(STORAGE_TYPE)
@RequiredArgsConstructor
public class DatabaseFileOperate implements FileOperate {

	public static final String STORAGE_TYPE = "DATABASE";
	private final ResourceFileDao coreFileDao;

	@Override
	public ResourceFile upload(byte[] bytes, String name, Long size, Long groupId) {

		ResourceFile coreFile = new ResourceFile();
		coreFile.setFileType(FileTypeUtil.getType(IoUtil.toStream(bytes),name));
		coreFile.setFileSize(size);
		coreFile.setId(Fc.randomSnowFlakeId());
		coreFile.setMark(DesUtil.encrypt(Fc.toStr(coreFile.getId()), DefaultValConstants.FILE_DES_KEY));
		coreFile.setStorageType(OssCategoryEnum.DATABASE.name());
		coreFile.setContent(bytes);
		coreFile.setName(name);
		coreFile.setGroupId(groupId);

        coreFileDao.save(coreFile);

		return coreFile;
	}

	@Override
	public Boolean download(ResourceFile coreFile) throws IOException {
		return FileUtil.download(coreFile.getContent(), WebUtil.getResponse(), coreFile.getName());
	}

	@Override
	public byte[] getByte(ResourceFile coreFile) {
		return coreFileDao.getContentById(coreFile.getId());
	}

	@Override
	public Boolean deleteFile(ResourceFile tbCoreFile) {
		return true;
	}

	/**
	 * 获取文件外链
	 *
	 * @param coreFile 文件实体
	 * @return String 文件外链地址
	 * @author mr.g
	 * @since 2020-07-28
	 */
	@Override
	public String getUrl(ResourceFile coreFile) {
		String url = WebUtil.getRequest().getRequestURL().toString();
		return StringUtil.replace(url, "/url/", "/download/");
	}
}
