package top.jpower.jpower.operate.storage;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.IoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import top.jpower.common.constants.DefaultValConstants;
import top.jpower.common.enums.FileStorageTypeEnum;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.*;
import top.jpower.jpower.dbs.entity.TbResourceFile;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.jpower.operate.FileOperate;
import top.jpower.jpower.operate.properties.FileProperties;
import top.jpower.jpower.service.ResourceFileService;
import top.jpower.jpower.utils.FileDfsUtil;

import java.io.IOException;

import static top.jpower.jpower.operate.storage.FastDfsFileOperate.STORAGE_TYPE;

/**
 * 上传文件保存到FastDfs
 * @Author mr.g
 **/
@Component(STORAGE_TYPE)
@RequiredArgsConstructor
public class FastDfsFileOperate implements FileOperate {

	public static final String STORAGE_TYPE = "FASTDFS";
	private final FileProperties fileProperties;
	private final ResourceFileService coreFileService;


	@Override
	public TbResourceFile upload(byte[] bytes, String name, Long size) {

		String type = FileTypeUtil.getType(IoUtil.toStream(bytes), name);

		String dfsPath = FileDfsUtil.upload(bytes, size, type);

		TbResourceFile file = new TbResourceFile();
		file.setFileType(type);
		file.setFileSize(size);
		file.setId(Fc.randomSnowFlakeId());
		file.setMark(DesUtil.encrypt(Fc.toStr(file.getId()), DefaultValConstants.FILE_DES_KEY));
		file.setStorageType(FileStorageTypeEnum.FASTDFS.getValue());
		file.setPath(dfsPath);
		file.setName(name);

		try {
			if (!coreFileService.add(file)){
				FileDfsUtil.deleteFile(dfsPath);
				return null;
			}
		}catch (Exception e){
			FileDfsUtil.deleteFile(dfsPath);
			return null;
		}

		return file;
	}

	@Override
	public Boolean download(TbResourceFile coreFile) throws IOException {
		JpowerAssert.notEmpty(coreFile.getPath(), JpowerError.Parser, "文件不存在");
		byte[] bytes = FileDfsUtil.downloadFile(coreFile.getPath());
		return FileUtil.download(bytes, WebUtil.getResponse(), coreFile.getName());
	}

	@Override
	public byte[] getByte(TbResourceFile coreFile) {
		JpowerAssert.notEmpty(coreFile.getPath(), JpowerError.Parser, "文件不存在");
		return FileDfsUtil.downloadFile(coreFile.getPath());
	}

	@Override
	public Boolean deleteFile(TbResourceFile coreFile) {
		JpowerAssert.notEmpty(coreFile.getPath(), JpowerError.Parser, "文件不存在");
		return FileDfsUtil.deleteFile(coreFile.getPath());
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
		String domain = StringUtil.removeAllSuffix(fileProperties.getFastDfs().getDomain(), StringPool.SLASH);
		return domain+coreFile.getPath();
	}
}
