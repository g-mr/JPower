package com.wlcb.ylth.module.common.utils;

import com.wlcb.ylth.module.base.properties.SysProperties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * @ClassName SendmailUtil
 * @Description TODO 邮件工具类
 * @Author 郭丁志
 * @Date 2020-04-24 11:51
 * @Version 1.0
 */
public class SendmailUtil {

    private static Properties prop = new Properties();

    private static final String host = SysProperties.getInstance().getProperties("email.host","smtp.mxhichina.com");
    private static final String protocol = SysProperties.getInstance().getProperties("email.protocol","smtp");
    private static final String emailDir = SysProperties.getInstance().getProperties("email.dir","/");

    private static final String emailUser = SysProperties.getInstance().getProperties("email.username","gaojing@nmgylth.com");
    private static final String emailPassword = SysProperties.getInstance().getProperties("email.password","Ll123456");

    static {
        prop.setProperty("mail.host", host);
        prop.setProperty("mail.transport.protocol", protocol);
        prop.setProperty("mail.smtp.auth", "true");
    }

    public static void main(String[] args) throws Exception {
        sendEmail("guodingzhi@nmgylth.com","12","213321");
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 发送文本邮件
     * @Date 12:52 2020-04-24
     * @Param [username, title, content]
     * @return void
     **/
    public static void sendEmail(String username,String title,String content) throws Exception {
        createEmail(username,title,content,null);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 发送带附件的邮件
     * @Date 12:53 2020-04-24
     * @Param [username, title, content, file]
     * @return void
     **/
    public static void sendEmail(String username,String title,String content,File file) throws Exception {
        createEmail(username,title,content,file);
    }

    private static void createEmail(String username,String title,String content,File file) throws Exception {
        //使用JavaMail发送邮件的5个步骤
        //1、创建session
        Session session = Session.getInstance(prop);
        //开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
        session.setDebug(true);
        //2、通过session得到transport对象
        Transport ts = session.getTransport();
        //3、使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器，用户名和密码都通过验证之后才能够正常发送邮件给收件人。
        ts.connect(host, emailUser, emailPassword);
        //4、创建邮件
        Message message;
        if (file==null){
            message = createSimpleMail(session,username,title,content);
        }else {
            message = createAttachMail(session,username,title,content,file);
        }
        //5、发送邮件
        ts.sendMessage(message, message.getAllRecipients());
        ts.close();
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 只包含文本的邮件
     * @Date 12:33 2020-04-24
     * @Param [session, username, title, content]
     * @return javax.mail.internet.MimeMessage
     **/
    private static MimeMessage createSimpleMail(Session session,String username,String title,String content)
            throws Exception {
        //创建邮件对象
        MimeMessage message = new MimeMessage(session);
        //指明邮件的发件人
        message.setFrom(new InternetAddress(emailUser));

        //指明邮件的收件人
        message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(username));
        //邮件的标题
        message.setSubject(title);
        //邮件的文本内容
        message.setContent(content, "text/html;charset=UTF-8");
        //返回创建好的邮件对象
        return message;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 带附件的邮件
     * @Date 12:36 2020-04-24
     * @Param [session]
     * @return javax.mail.internet.MimeMessage
     **/
    private static MimeMessage createAttachMail(Session session,String username,String title,String content,File file) throws Exception{
        MimeMessage message = new MimeMessage(session);

        //设置邮件的基本信息
        //发件人
        message.setFrom(new InternetAddress(emailUser));
        //收件人
        message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(username));
        //邮件标题
        message.setSubject(title);

        //创建邮件正文，为了避免邮件正文中文乱码问题，需要使用charset=UTF-8指明字符编码
        MimeBodyPart text = new MimeBodyPart();
        text.setContent(content, "text/html;charset=UTF-8");

        //创建邮件附件
        MimeBodyPart attach = new MimeBodyPart();
        DataHandler dh = new DataHandler(new FileDataSource(file));
        attach.setDataHandler(dh);
        attach.setFileName(dh.getName());

        //创建容器描述数据关系
        MimeMultipart mp = new MimeMultipart();
        mp.addBodyPart(text);
        mp.addBodyPart(attach);
        mp.setSubType("mixed");

        message.setContent(mp);
        message.saveChanges();
        //将创建的Email写入到E盘存储
        message.writeTo(new FileOutputStream(emailDir+ File.separator +"attachMail.eml"));
        //返回生成的邮件
        return message;
    }

}
