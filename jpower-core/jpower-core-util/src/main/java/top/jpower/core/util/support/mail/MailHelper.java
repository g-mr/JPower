package top.jpower.core.util.support.mail;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import top.jpower.core.util.utils.Fc;

import java.util.Collection;
import java.util.Collections;

/**
 * 邮件发送助手，桥接 application.yml 配置与 Hutool MailUtil。
 * <p>
 * 使用方式与 {@link MailUtil} 一致，但无需手动传入 {@link MailAccount}，
 * 配置统一从 application.yml 中的 jpower.mail 前缀读取。
 *
 * @author mr.g
 */
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(MailProperties.class)
public class MailHelper {

    private final MailProperties properties;

    /**
     * 发送纯文本邮件
     *
     * @param to      收件人邮箱
     * @param subject 邮件主题
     * @param content 纯文本内容
     * @return 消息ID
     */
    public String sendText(String to, String subject, String content) {
        return send(Collections.singletonList(to), subject, content, false);
    }

    /**
     * 发送HTML邮件
     *
     * @param to      收件人邮箱
     * @param subject 邮件主题
     * @param content HTML内容
     * @return 消息ID
     */
    public String sendHtml(String to, String subject, String content) {
        return send(Collections.singletonList(to), subject, content, true);
    }

    /**
     * 发送邮件
     *
     * @param tos     收件人列表
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param isHtml  是否为HTML内容
     * @return 消息ID
     */
    public String send(Collection<String> tos, String subject, String content, boolean isHtml) {
        return MailUtil.send(buildAccount(), tos, subject, content, isHtml);
    }

    private MailAccount buildAccount() {
        if (Fc.isAnyBlank(properties.getHost(), properties.getFrom(), properties.getUser(), properties.getPass())) {
            throw new IllegalStateException("邮件配置不完整");
        }
        MailAccount account = new MailAccount();
        account.setHost(properties.getHost());
        account.setPort(properties.getPort());
        account.setFrom(properties.getFrom());
        account.setUser(properties.getUser());
        account.setPass(properties.getPass());
        account.setSslEnable(properties.getSslEnable());
        return account;
    }

}
