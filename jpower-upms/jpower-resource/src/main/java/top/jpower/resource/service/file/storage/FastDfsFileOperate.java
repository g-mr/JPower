package top.jpower.resource.service.file.storage;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.IoUtil;
import lombok.RequiredArgsConstructor;
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
import top.jpower.resource.utils.FileDfsUtil;

import java.io.IOException;

import static top.jpower.common.constants.ServiceCodeConstants.FILE_NOT_EXIST;

/**
 * FastDFS文件操作实现
 * <p>
 * 使用FastDFS分布式文件系统进行文件存储的实现类
 * </p>
 *
 * @author mr.g
 * @since 2020-07-28
 */
@RequiredArgsConstructor
public class FastDfsFileOperate implements FileOperate {

	private final ResourceOss resourceOss;
	private final ResourceFileDao resourceFileDao;


	@Override
	public ResourceFile upload(byte[] bytes, String name, Long size, Long groupId) {
		String type = FileTypeUtil.getType(IoUtil.toStream(bytes), name);

		String dfsPath = FileDfsUtil.upload(bytes, size, type);
		ResourceFile file = new ResourceFile();
		file.setFileType(type);
		file.setFileSize(size);
		file.setId(Fc.randomSnowFlakeId());
		file.setMark(DesUtil.encrypt(Fc.toStr(file.getId()), DefaultValConstants.FILE_DES_KEY));
		file.setStorageType(OssCategoryEnum.FASTDFS.name());
		file.setPath(dfsPath);
		file.setName(name);
		file.setGroupId(groupId);

		try {
			if (!resourceFileDao.save(file)){
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
	public Boolean download(ResourceFile coreFile) throws IOException {
		JpowerAssert.notEmpty(coreFile.getPath(), JpowerError.NotFind, FILE_NOT_EXIST);
		byte[] bytes = FileDfsUtil.downloadFile(coreFile.getPath());
		return FileUtil.download(bytes, WebUtil.getResponse(), coreFile.getName());
	}

	@Override
	public byte[] getByte(ResourceFile coreFile) {
		JpowerAssert.notEmpty(coreFile.getPath(), JpowerError.NotFind, FILE_NOT_EXIST);
		return FileDfsUtil.downloadFile(coreFile.getPath());
	}

	@Override
	public Boolean deleteFile(ResourceFile coreFile) {
		JpowerAssert.notEmpty(coreFile.getPath(), JpowerError.NotFind, FILE_NOT_EXIST);
		return FileDfsUtil.deleteFile(coreFile.getPath());
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
		return domain+coreFile.getPath();
	}
}
