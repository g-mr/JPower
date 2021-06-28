package com.wlcb.jpower.module.common.controller;

import com.wlcb.jpower.module.base.exception.BusinessException;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.DateUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.FileUtil;
import com.wlcb.jpower.module.common.utils.WebUtil;
import com.wlcb.jpower.module.common.utils.constants.ImportExportConstants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.beans.PropertyEditorSupport;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * web层通用数据处理
 * @author mr.gmac
 */
@Slf4j
public class BaseController
{
    protected final Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Value("${server.port}")
    private Integer port;

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport()
        {
            @Override
            public void setAsText(String text){
                if (Fc.isBlank(text)) {
                    setValue(null);
                }else {
                    setValue(DateUtil.parseDate(text));
                }
            }
        });
    }

    /**
     * 获取request
     */
    protected HttpServletRequest getRequest(){
        return WebUtil.getRequest();
    }

    /**
     * 获取response
     */
    protected HttpServletResponse getResponse()
    {
        return WebUtil.getResponse();
    }

    /**
     * 获取session
     */
    protected HttpSession getSession()
    {
        return getRequest().getSession();
    }

    protected void download(ResponseData responseData,String fileName)
    {
        File file = new File(ImportExportConstants.EXPORT_PATH + responseData.getData());
        if (file.exists()) {
            try {
                FileUtil.download(file, getResponse(), fileName);
            } catch (IOException e) {
                logger.error("下载文件出错。file={},error={}", file.getAbsolutePath(), e.getMessage());
                throw new BusinessException("下载文件出错，请联系网站管理员");
            }

            FileUtil.deleteFile(file);
        } else {
            throw new BusinessException(responseData.getData() + "文件生成失败，无法下载");
        }
    }
}
