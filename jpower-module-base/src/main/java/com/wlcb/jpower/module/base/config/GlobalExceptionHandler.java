package com.wlcb.jpower.module.base.config;

import com.wlcb.jpower.module.base.exception.BusinessException;
import com.wlcb.jpower.module.base.exception.JpowerException;
import com.wlcb.jpower.module.base.properties.SysProperties;
import com.wlcb.jpower.module.base.vo.ErrorReturnJson;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

/**
 * @ClassName GlobalExceptionHandler
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-01-27 17:24
 * @Version 1.0
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String isSend = SysProperties.getInstance().getProperties("isSend");
    private static final String host = SysProperties.getInstance().getProperties("email.host");
    private static final String protocol = SysProperties.getInstance().getProperties("email.protocol");

    private static final String emailUser = SysProperties.getInstance().getProperties("email.username");
    private static final String emailPassword = SysProperties.getInstance().getProperties("email.password");

    private static final String toEmail = SysProperties.getInstance().getProperties("email.toEmail");

    /**
     * 系统异常处理，比如：404,500
     * @param request
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ErrorReturnJson defaultErrorHandler(HttpServletRequest request, Exception e) throws Exception {
        String currentPath = request.getServletPath();

        ErrorReturnJson r = new ErrorReturnJson();
        r.setMessage(e.getMessage());
//        r.setMessage("系统异常，请联系管理员");
        if (e instanceof org.springframework.web.servlet.NoHandlerFoundException) {

            if("/".equals(currentPath)){
                r.setCode(0);
                r.setStatus(true);
                r.setMessage("ok");
                return r;
            }

            r.setCode(404);
        }else if (e instanceof BusinessException) {
            r.setCode(501);
//            logger.error("{}", e);
        }else if (e instanceof JpowerException) {
            r.setCode(((JpowerException) e).getCode());
//            logger.error("{}", e);
        } else {
            r.setCode(500);
//            logger.error("message={},！！！{}", e.getMessage() , e);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String msg=sw.toString();

            if (StringUtils.isNotBlank(isSend) && Integer.parseInt(isSend) == 1){
                //异常警告
                sendEmail(request,msg);
            }
        }
//        r.setData(null);
        r.setStatus(false);
        return r;
    }

    private void sendEmail(HttpServletRequest request,String content){
        try {
            String currentPath = request.getServletPath();

            Map<String,String[]> map = request.getParameterMap();
            StringBuffer paramBuffer = new StringBuffer();
            for(Map.Entry<String, String[]> entry : map.entrySet()){
                String mapKey = entry.getKey();
                String[] mapValue = entry.getValue();
                paramBuffer.append(mapKey).append("=").append(StringUtils.join(mapValue,",")).append("&");
            }

            StringBuffer headerBuffer = new StringBuffer();
            headerBuffer.append("接口地址：").append(currentPath).append(";\n\n");
            headerBuffer.append("接口参数：").append(paramBuffer.toString()).append(";\n\n");
            headerBuffer.append("报错内容：").append(content);

            Properties prop=new Properties();
            prop.put("mail.host",host );
            prop.put("mail.transport.protocol", protocol);
            prop.put("mail.smtp.auth", "true");
            Session session=Session.getInstance(prop);
            session.setDebug(true);
            Transport ts=session.getTransport();
            ts.connect(emailUser, emailPassword);
            Message message=new MimeMessage(session);
            message.setFrom(new InternetAddress(emailUser));
            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("系统异常告警");
            message.setContent(headerBuffer.toString(), "text/plain;charset=utf-8");
            ts.sendMessage(message, message.getAllRecipients());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("邮件发送失败{}",e.getMessage());
        }
    }
}
