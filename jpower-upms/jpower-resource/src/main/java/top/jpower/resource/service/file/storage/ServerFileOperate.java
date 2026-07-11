package top.jpower.resource.service.file.storage;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import top.jpower.common.constants.DefaultValConstants;
import top.jpower.common.enums.OssCategoryEnum;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.*;
import top.jpower.resource.dbs.dao.ResourceFileDao;
import top.jpower.resource.dbs.entity.ResourceFile;
import top.jpower.resource.dbs.entity.ResourceOss;
import top.jpower.resource.service.file.FileOperate;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static top.jpower.common.constants.ServiceCodeConstants.*;

/**
 * 服务器文件操作实现
 * <p>
 * 将文件保存到本地服务器的文件操作实现类
 * </p>
 *
 * @author mr.g
 * @since 2020-07-28
 */
@RequiredArgsConstructor
public class ServerFileOperate implements FileOperate {

	private final ResourceOss resourceOss;
	private final ResourceFileDao resourceFileDao;

	@Override
	public ResourceFile upload(byte[] bytes, String name, Long size, Long groupId) {
		JpowerAssert.notEmpty(resourceOss.getBucketName(), JpowerError.Unknown,FILE_SAVE_PATH_NOT_CONFIG);

		File saveFile = FileUtil.saveFile(bytes, IdUtil.objectId(), resourceOss.getBucketName());

		ResourceFile coreFile = new ResourceFile();
		coreFile.setPath(saveFile.getAbsolutePath());
		coreFile.setName(name);
		coreFile.setStorageType(OssCategoryEnum.SERVER.name());
		coreFile.setFileType(FileTypeUtil.getType(saveFile));
		coreFile.setFileSize(size);
		coreFile.setId(Fc.randomSnowFlakeId());
		coreFile.setMark(DesUtil.encrypt(Fc.toStr(coreFile.getId()), DefaultValConstants.FILE_DES_KEY));
		coreFile.setGroupId(groupId);

		try {
			if (!resourceFileDao.save(coreFile)){
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
	public Boolean download(ResourceFile coreFile) throws IOException {
		String path = coreFile.getPath();
		if(StringUtils.isBlank(path)) {
			WebUtil.getResponse().setHeader("iserror", "true");
			JpowerAssert.createException(JpowerError.NotFind, FILE_PATH_NOT_EXIST);
		}

		File file = new File(path);
		if (!file.exists()) {
			WebUtil.getResponse().setHeader("iserror", "true");
			JpowerAssert.createException(JpowerError.NotFind, FILE_NOT_EXIST);
		}
		return FileUtil.download(file, WebUtil.getResponse(),coreFile.getName());
	}

	@Override
	public byte[] getByte(ResourceFile coreFile) {
		String path = coreFile.getPath();
		JpowerAssert.notEmpty(path,JpowerError.Parser,FILE_PATH_NOT_EXIST);

		File file = new File(path);
		JpowerAssert.isTrue(file.exists(),JpowerError.NotFind,FILE_NOT_EXIST);

		return cn.hutool.core.io.FileUtil.readBytes(file);
	}

	@Override
	public Boolean deleteFile(ResourceFile tbCoreFile) {
		File file = new File(tbCoreFile.getPath());
		FileUtil.deleteFile(file);
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
		String domain = StringUtil.removeAllSuffix(resourceOss.getExternalAddress(), StringPool.SLASH);
		File file = new File(coreFile.getPath());
		return StringUtil.concat(domain, StringPool.SLASH, Optional.ofNullable(file.getParentFile()).map(File::getName).orElse(StringPool.EMPTY), StringPool.SLASH, file.getName());
	}
}
