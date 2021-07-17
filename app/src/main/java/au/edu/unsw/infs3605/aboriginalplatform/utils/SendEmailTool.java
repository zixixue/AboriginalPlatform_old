package au.edu.unsw.infs3605.aboriginalplatform.utils;

import android.text.TextUtils;

import java.util.Date;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public final class SendEmailTool {

    private SendEmailTool() {
    }

    private static class Holder {
        private static final SendEmailTool SINGLETON = new SendEmailTool();
    }

    public static SendEmailTool getInstance() {
        return SendEmailTool.Holder.SINGLETON;
    }

    /**
     * 网易163邮箱的 SMTP 服务器地址为: smtp.163.com  腾讯企业邮箱是 smtp.exmail.qq.com
     */
    private final String MY_EMAIL_SMTP_HOST = "smtp.qq.com";

    public void sendEmail(String receiverEmail, String receiverName, String subject, String content) {
        String sendEmail = "1169674382@qq.com";
        String sendPass = "tozplgvewpypfejg";
        String senderName = "大大";
        send(sendEmail, sendPass, senderName, receiverEmail, receiverName, "", "", subject, content);
    }

    /**
     * @param sendEmail     发送者的邮箱
     * @param sendPass      发送者的邮箱密码
     * @param senderName    发送者的昵称
     * @param receiverEmail 接收者的邮箱
     * @param receiverName  接受者的昵称
     * @param ccEmail       抄送的邮箱
     * @param ccName        抄送的昵称
     * @param subject       邮件的主题
     * @param content       邮件的内容
     */
    public void send(String sendEmail, String sendPass, String senderName, String receiverEmail,
                     String receiverName, String ccEmail, String ccName, String subject, String content) {
        // 1. 创建参数配置, 用于连接邮件服务器的参数配置
        // 参数配置
        Properties props = new Properties();
        // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.transport.protocol", "smtp");
        // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.host", MY_EMAIL_SMTP_HOST);
        // 需要请求认证
        props.setProperty("mail.smtp.auth", "true");

        // PS: 某些邮箱服务器要求 SMTP 连接需要使用 SSL 安全认证 (为了提高安全性, 邮箱支持SSL连接, 也可以自己开启),
        //     打开 SSL 安全连接
        final String smtpPort = "465";
        props.setProperty("mail.smtp.port", smtpPort);
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.port", smtpPort);
        // 2. 根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getInstance(props);
        // 设置为debug模式, 可以查看详细的发送 log
        session.setDebug(true);
        MimeMessage message = null;
        try {
            message = createMimeMessage(session, sendEmail, senderName, receiverEmail, receiverName, ccEmail, ccName, subject, content);
            // 4. 根据 Session 获取邮件传输对象
            Transport transport = null;
            transport = session.getTransport();
            // 5. 使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错
            //
            //    PS_01: 成败的判断关键在此一句, 如果连接服务器失败, 都会在控制台输出相应失败原因的 log,
            //           仔细查看失败原因, 有些邮箱服务器会返回错误码或查看错误类型的链接, 根据给出的错误
            //           类型到对应邮件服务器的帮助网站上查看具体失败原因。
            //
            //    PS_02: 连接失败的原因通常为以下几点, 仔细检查代码:
            //           (1) 邮箱没有开启 SMTP 服务;
            //           (2) 邮箱密码错误, 例如某些邮箱开启了独立密码;
            //           (3) 邮箱服务器要求必须要使用 SSL 安全连接;
            //           (4) 请求过于频繁或其他原因, 被邮件服务器拒绝服务;
            //           (5) 如果以上几点都确定无误, 到邮件服务器网站查找帮助。
            //
            //    PS_03: 仔细看log, 认真看log, 看懂log, 错误原因都在log已说明。
            transport.connect(sendEmail, sendPass);

            // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
            transport.sendMessage(message, message.getAllRecipients());
            // 7. 关闭连接
            transport.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 创建一封只包含文本的简单邮件
     *
     * @param session      和服务器交互的会话
     * @param sendMail     发件人邮箱
     * @param shortName    发件人的昵称
     * @param receiveMail  收件人邮箱
     * @param receiverName 收件人的昵称
     * @param ccEmail      抄送邮箱
     * @param ccName       抄送人昵称
     * @param subject      邮件的主题
     * @param content      邮件的内容
     * @return
     * @throws Exception
     */
    private MimeMessage createMimeMessage(Session session, String sendMail, String shortName, String receiveMail, String receiverName,
                                          String ccEmail, String ccName, String subject, String content) throws Exception {
        // 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);
        // 2. From: 发件人（昵称有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改昵称）
        message.setFrom(new InternetAddress(sendMail, shortName, "UTF-8"));
        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, receiverName, "UTF-8"));
        if (!TextUtils.isEmpty(ccEmail)) {
            //添加抄送者
            message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress(ccEmail, ccName, "UTF-8"));
        }
        // 4. Subject: 邮件主题（标题有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改标题）
        message.setSubject(subject, "UTF-8");
        message.setContent(content, "text/html;charset=UTF-8");
        // 6. 设置发件时间
        message.setSentDate(new Date());
        // 7. 保存设置
        message.saveChanges();
        return message;
    }
}
