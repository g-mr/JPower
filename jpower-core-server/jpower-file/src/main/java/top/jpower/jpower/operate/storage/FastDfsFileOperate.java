package top.jpower.jpower.operate.storage;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import top.jpower.jpower.dbs.entity.TbCoreFile;
import top.jpower.jpower.module.base.enums.JpowerError;
import top.jpower.jpower.module.base.exception.JpowerAssert;
import top.jpower.jpower.module.common.utils.DesUtil;
import top.jpower.jpower.module.common.utils.Fc;
import top.jpower.jpower.module.common.utils.FileUtil;
import top.jpower.jpower.module.common.utils.WebUtil;
import top.jpower.jpower.module.common.utils.constants.ConstantsUtils;
import top.jpower.jpower.operate.FileOperate;
import top.jpower.jpower.service.CoreFileService;
import top.jpower.jpower.utils.FileDfsUtil;

import java.io.IOException;

import static top.jpower.jpower.module.common.utils.constants.ConstantsEnum.FILE_STORAGE_TYPE.FASTDFS;
import static top.jpower.jpower.operate.storage.FastDfsFileOperate.STORAGE_TYPE;

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
	public TbCoreFile upload(byte[] bytes, String name, Long size) {

		String dfsPath = FileDfsUtil.upload(bytes, size, FileNameUtil.getPrefix(name));

		TbCoreFile coreFile = new TbCoreFile();
		coreFile.setFileType(FileTypeUtil.getType(IoUtil.toStream(bytes), name));
		coreFile.setFileSize(size);
		coreFile.setId(Fc.randomSnowFlakeId());
		coreFile.setMark(DesUtil.encrypt(Fc.toStr(coreFile.getId()), ConstantsUtils.FILE_DES_KEY));
		coreFile.setStorageType(FASTDFS.getValue());
		coreFile.setPath(dfsPath);
		coreFile.setName(name);

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
