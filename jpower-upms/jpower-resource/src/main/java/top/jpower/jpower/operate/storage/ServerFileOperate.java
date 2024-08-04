package top.jpower.jpower.operate.storage;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.jpower.common.constants.DefaultValConstants;
import top.jpower.common.enums.FileStorageTypeEnum;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.*;
import top.jpower.jpower.dbs.entity.TbResourceFile;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.handler.BusinessException;
import top.jpower.core.exception.handler.JpowerAssert;
import top.jpower.jpower.operate.FileOperate;
import top.jpower.jpower.operate.properties.FileProperties;
import top.jpower.jpower.service.ResourceFileService;

import java.io.File;
import java.io.IOException;

import static top.jpower.jpower.operate.storage.ServerFileOperate.STORAGE_TYPE;

/**
 * 上传文件保存到服务器
 * @Author mr.g
 **/
@Component(STORAGE_TYPE)
@RequiredArgsConstructor
public class ServerFileOperate implements FileOperate {

	public static final String STORAGE_TYPE = "SERVER";
	@Autowired
	private final FileProperties fileProperties;
	@Autowired
	private final ResourceFileService coreFileService;

	@Override
	public TbResourceFile upload(byte[] bytes, String name, Long size) {
		JpowerAssert.notEmpty(fileProperties.getServer().getPath(), JpowerError.Unknown,"未配置文件保存路径");

		File saveFile = FileUtil.saveFile(bytes, IdUtil.objectId(), fileProperties.getServer().getPath());

		TbResourceFile coreFile = new TbResourceFile();
		coreFile.setPath(saveFile.getAbsolutePath());
		coreFile.setName(name);
		coreFile.setStorageType(FileStorageTypeEnum.SERVER.getValue());
		coreFile.setFileType(FileTypeUtil.getType(saveFile));
		coreFile.setFileSize(size);
		coreFile.setId(Fc.randomSnowFlakeId());
		coreFile.setMark(DesUtil.encrypt(Fc.toStr(coreFile.getId()), DefaultValConstants.FILE_DES_KEY));

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
	public Boolean download(TbResourceFile coreFile) throws IOException {
		String path = coreFile.getPath();
		if(StringUtils.isBlank(path)){
			WebUtil.getResponse().setHeader("iserror", "true");
			throw new BusinessException("文件不存在，无法下载");
		}

		File file = new File(path);
		if (!file.exists()){
			WebUtil.getResponse().setHeader("iserror", "true");
			throw new BusinessException(file.getName()+"文件不存在，无法下载");
		}
		return FileUtil.download(file, WebUtil.getResponse(),coreFile.getName());
	}

	@Override
	public byte[] getByte(TbResourceFile coreFile){
		String path = coreFile.getPath();
		JpowerAssert.notEmpty(path,JpowerError.Parser,"文件路径不存在");

		File file = new File(path);
		if (!file.exists()){
			throw new BusinessException(file.getName()+"文件不存在，无法下载");
		}

		return cn.hutool.core.io.FileUtil.readBytes(file);
	}

	@Override
	public Boolean deleteFile(TbResourceFile tbCoreFile) {
		File file = new File(tbCoreFile.getPath());
		FileUtil.deleteFile(file);
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
		String domain = StringUtil.removeAllSuffix(fileProperties.getServer().getDomain(), StringPool.SLASH);
		File file = new File(coreFile.getPath());
		return StringUtil.concat(domain, StringPool.SLASH, file.getParentFile().getName(), StringPool.SLASH, file.getName());
	}
}
