package top.jpower.jpower.operate.storage;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.util.IdUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import top.jpower.jpower.dbs.entity.TbCoreFile;
import top.jpower.jpower.module.base.enums.JpowerError;
import top.jpower.jpower.module.base.exception.BusinessException;
import top.jpower.jpower.module.base.exception.JpowerAssert;
import top.jpower.jpower.module.common.utils.*;
import top.jpower.jpower.module.common.utils.constants.ConstantsUtils;
import top.jpower.jpower.module.common.utils.constants.StringPool;
import top.jpower.jpower.operate.FileOperate;
import top.jpower.jpower.operate.properties.FileProperties;
import top.jpower.jpower.service.CoreFileService;

import java.io.File;
import java.io.IOException;

import static top.jpower.jpower.module.common.utils.constants.ConstantsEnum.FILE_STORAGE_TYPE.SERVER;
import static top.jpower.jpower.operate.storage.ServerFileOperate.STORAGE_TYPE;

/**
 * 上传文件保存到服务器
 * @Author mr.g
 **/
@Component(STORAGE_TYPE)
@RefreshScope
public class ServerFileOperate implements FileOperate {

	public static final String STORAGE_TYPE = "SERVER";
	@Autowired
	private FileProperties fileProperties;
	@Autowired
	private CoreFileService coreFileService;

	@Override
	public TbCoreFile upload(byte[] bytes, String name, Long size) {
		JpowerAssert.notEmpty(fileProperties.getServer().getPath(), JpowerError.Unknown,"未配置文件保存路径");

		File saveFile = FileUtil.saveFile(bytes, IdUtil.objectId(), fileProperties.getServer().getPath());

		TbCoreFile coreFile = new TbCoreFile();
		coreFile.setPath(saveFile.getAbsolutePath());
		coreFile.setName(name);
		coreFile.setStorageType(SERVER.getValue());
		coreFile.setFileType(FileTypeUtil.getType(saveFile));
		coreFile.setFileSize(size);
		coreFile.setId(Fc.randomSnowFlakeId());
		coreFile.setMark(DesUtil.encrypt(Fc.toStr(coreFile.getId()), ConstantsUtils.FILE_DES_KEY));

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
	public byte[] getByte(TbCoreFile coreFile){
		String path = coreFile.getPath();
		JpowerAssert.notEmpty(path,JpowerError.Parser,"文件路径不存在");

		File file = new File(path);
		if (!file.exists()){
			throw new BusinessException(file.getName()+"文件不存在，无法下载");
		}

		return cn.hutool.core.io.FileUtil.readBytes(file);
	}

	@Override
	public Boolean deleteFile(TbCoreFile tbCoreFile) {
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
	public String getUrl(TbCoreFile coreFile) {
		String domain = StringUtil.removeAllSuffix(fileProperties.getServer().getDomain(), StringPool.SLASH);
		File file = new File(coreFile.getPath());
		return StringUtil.concat(domain, StringPool.SLASH, file.getParentFile().getName(), StringPool.SLASH, file.getName());
	}
}
