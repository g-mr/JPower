package com.qidiangk.smart.resource.service.file.storage;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import com.qidiangk.smart.common.constants.DefaultValConstants;
import com.qidiangk.smart.common.enums.FileStorageTypeEnum;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.*;
import com.qidiangk.smart.resource.dbs.entity.ResourceFile;
import com.qidiangk.smart.resource.service.ResourceFileService;
import com.qidiangk.smart.resource.service.file.FileOperate;
import com.qidiangk.smart.resource.service.file.properties.FileProperties;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static com.qidiangk.smart.common.constants.ServiceCodeConstants.FILE_NOT_EXIST;
import static com.qidiangk.smart.common.constants.ServiceCodeConstants.FILE_PATH_NOT_EXIST;
import static com.qidiangk.smart.common.constants.ServiceCodeConstants.FILE_SAVE_PATH_NOT_CONFIG;
import static com.qidiangk.smart.resource.service.file.storage.ServerFileOperate.STORAGE_TYPE;

/**
 * 服务器文件操作实现
 * <p>
 * 将文件保存到本地服务器的文件操作实现类
 * </p>
 *
 * @author mr.g
 * @since 2020-07-28
 */
@Component(STORAGE_TYPE)
@RequiredArgsConstructor
public class ServerFileOperate implements FileOperate {

	public static final String STORAGE_TYPE = "SERVER";
	private final FileProperties fileProperties;
	private final ResourceFileService coreFileService;

	@Override
	public ResourceFile upload(byte[] bytes, String name, Long size, Long groupId) {
		JpowerAssert.notEmpty(fileProperties.getServer().getPath(), JpowerError.Unknown,FILE_SAVE_PATH_NOT_CONFIG);

		File saveFile = FileUtil.saveFile(bytes, IdUtil.objectId(), fileProperties.getServer().getPath());

		ResourceFile coreFile = new ResourceFile();
		coreFile.setPath(saveFile.getAbsolutePath());
		coreFile.setName(name);
		coreFile.setStorageType(FileStorageTypeEnum.SERVER.getValue());
		coreFile.setFileType(FileTypeUtil.getType(saveFile));
		coreFile.setFileSize(size);
		coreFile.setId(Fc.randomSnowFlakeId());
		coreFile.setMark(DesUtil.encrypt(Fc.toStr(coreFile.getId()), DefaultValConstants.FILE_DES_KEY));
		coreFile.setGroupId(groupId);

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
		String domain = StringUtil.removeAllSuffix(fileProperties.getServer().getDomain(), StringPool.SLASH);
		File file = new File(coreFile.getPath());
		return StringUtil.concat(domain, StringPool.SLASH, Optional.ofNullable(file.getParentFile()).map(File::getName).orElse(StringPool.EMPTY), StringPool.SLASH, file.getName());
	}
}
