package top.jpower.core.boot.controller;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import top.jpower.core.util.utils.DateUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.WebUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.beans.PropertyEditorSupport;
import java.util.Date;

/**
 * web层通用数据处理
 * @author mr.g
 */
public class BaseController
{

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
                    setValue(DateUtil.parse(text));
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

}
