package com.wlcb.jpower.web.sync.controller.file;

import com.wlcb.jpower.module.base.exception.BusinessException;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @ClassName TiwenController
 * @Description TODO 体温调用入口Controller
 * @Author 郭丁志
 * @Date 2020-02-13 14:10
 * @Version 1.0
 */
@RestController
@RequestMapping("file")
public class FileController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @GetMapping("/export/download")
    public void syncStart(String fileName) throws Exception {

        if (!FileUtils.isValidFilename(fileName))
        {
            throw new Exception("文件名称("+fileName+")非法，不允许下载。 ");
        }

        File file = new File(downloadPath+File.separator+fileName);
        if (file.exists()){
            HttpServletResponse response = getResponse();
            try {
                FileUtils.download(file, response,"导出数据.xlsx");
            } catch (IOException e) {
                logger.error("下载文件出错。file={},error={}",file.getAbsolutePath(),e.getMessage());
                throw new BusinessException("下载文件出错，请联系网站管理员");
            }

            FileUtils.deleteFile(file);
        }else {
            throw new BusinessException(fileName+"文件不存在，无法下载");
        }
    }

}
