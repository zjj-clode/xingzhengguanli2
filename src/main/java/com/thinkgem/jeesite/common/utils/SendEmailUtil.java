package com.thinkgem.jeesite.common.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.sun.mail.util.MailSSLSocketFactory;

/**
 * 腾讯企业邮箱、javax.mail
 *
 * @author lsp
 *
 */
public class SendEmailUtil {

	private static String name; //发件人姓名
	private static String account; //登录用户名
	private static String pass; //登录密码
	private static String host; //服务器地址（邮件服务器）
	private static String port; //端口
	private static String protocol; //协议
	private static String ssl; //是否安全协议

	static {
		Properties prop = new Properties();
		try {
			prop = PropertiesLoaderUtils.loadAllProperties("email.properties");//生产环境
		} catch (IOException e) {
			System.out.println("加载属性文件失败");
		}
		name = prop.getProperty("email.fromName");
		account = prop.getProperty("email.username");
		pass = prop.getProperty("email.password");

		host = prop.getProperty("email.host");
		port = prop.getProperty("email.port");
		protocol = prop.getProperty("email.protocol");
		ssl = prop.getProperty("email.ssl");
	}

	static class MyAuthenricator extends Authenticator {
		String u = null;
		String p = null;

		public MyAuthenricator(String u, String p) {
			this.u = u;
			this.p = p;
		}

		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(u, p);
		}
	}

	public static boolean sendCommonMail(String to, String subject, String content) {
		return send(to, subject, content, null);
	}

	public static boolean send(String to, String subject, String content, String fileStr) {
		Properties prop = new Properties();
		//协议
		prop.setProperty("mail.transport.protocol", protocol);
		//服务器
		prop.setProperty("mail.smtp.host", host);
		//端口
		prop.setProperty("mail.smtp.port", port);
		//使用smtp身份验证
		prop.setProperty("mail.smtp.auth", "true");

		//使用SSL，企业邮箱必需！
		if ("true".equals(ssl) || "1".equals(ssl) || "on".equals(ssl)) {
			MailSSLSocketFactory sf = null;
			try {
				sf = new MailSSLSocketFactory();
				sf.setTrustAllHosts(true);
			} catch (GeneralSecurityException e1) {
				e1.printStackTrace();
			}
			prop.put("mail.smtp.ssl.enable", "true");
			prop.put("mail.smtp.ssl.socketFactory", sf);
		}

		Session session = Session.getDefaultInstance(prop, new MyAuthenricator(account, pass));
		session.setDebug(true);
		MimeMessage mimeMessage = new MimeMessage(session);
		try {
			//发件人
			mimeMessage.setFrom(new InternetAddress(account, name)); //可以设置发件人的别名
			//收件人
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			//主题
			mimeMessage.setSubject(subject);
			//时间
			mimeMessage.setSentDate(new Date());
			//容器类，可以包含多个MimeBodyPart对象
			Multipart mp = new MimeMultipart();

			//MimeBodyPart可以包装文本，图片，附件
			MimeBodyPart body = new MimeBodyPart();
			//HTML正文
			body.setContent(content, "text/html; charset=UTF-8");
			mp.addBodyPart(body);

			//添加图片&附件
			if (fileStr != null && fileStr.trim().length() > 0) {
				body = new MimeBodyPart();
				body.attachFile(fileStr);
				mp.addBodyPart(body);
			}

			//设置邮件内容
			mimeMessage.setContent(mp);
			mimeMessage.saveChanges();
			Transport.send(mimeMessage);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void sendCommonMultMail( List<String> sendstoList, String subject, String content) {
		sendMult(sendstoList, subject, content, null);
	}

	public static void sendMult( List<String> sendstoList, String subject, String content, List<String> fileList) {
		Properties prop = new Properties();
		//协议
		prop.setProperty("mail.transport.protocol", protocol);
		//服务器
		prop.setProperty("mail.smtp.host", host);
		//端口
		prop.setProperty("mail.smtp.port", port);
		//使用smtp身份验证
		prop.setProperty("mail.smtp.auth", "true");

		//使用SSL，企业邮箱必需！
		if ("true".equals(ssl) || "1".equals(ssl) || "on".equals(ssl)) {
			MailSSLSocketFactory sf = null;
			try {
				sf = new MailSSLSocketFactory();
				sf.setTrustAllHosts(true);
			} catch (GeneralSecurityException e1) {
				e1.printStackTrace();
			}
			prop.put("mail.smtp.ssl.enable", "true");
			prop.put("mail.smtp.ssl.socketFactory", sf);
		}

		Session session = Session.getDefaultInstance(prop, new MyAuthenricator(account, pass));
		session.setDebug(true);
		MimeMessage mimeMessage = new MimeMessage(session);
		try {
			//发件人
			mimeMessage.setFrom(new InternetAddress(account, name)); //可以设置发件人的别名
			//收件人
			//mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			Address to[] = new InternetAddress[sendstoList.size()];
            for(int i=0;i<sendstoList.size();i++){
                to[i] = new InternetAddress(sendstoList.get(i));
            }
            // 多个收件人地址
            mimeMessage.setRecipients(Message.RecipientType.TO, to);
			//主题
			mimeMessage.setSubject(subject);
			//时间
			mimeMessage.setSentDate(new Date());
			//容器类，可以包含多个MimeBodyPart对象
			Multipart mp = new MimeMultipart();

			//MimeBodyPart可以包装文本，图片，附件
			MimeBodyPart body = new MimeBodyPart();
			//HTML正文
			body.setContent(content, "text/html; charset=UTF-8");
			mp.addBodyPart(body);

			//添加图片&附件
			if (fileList != null && fileList.size() > 0) {
				/*body = new MimeBodyPart();
				body.attachFile(fileStr);
				mp.addBodyPart(body);
				*/
				for (String fileStr : fileList) {
					File file = new File(fileStr);
					
					MimeBodyPart attachmentPart = new MimeBodyPart();

	                FileDataSource source = new FileDataSource(file);
	                attachmentPart.setDataHandler(new DataHandler(source));
	                attachmentPart.setFileName(MimeUtility.encodeWord(file.getName(), "gb2312", null));
	                mp.addBodyPart(attachmentPart);
				}
			}

			//设置邮件内容
			mimeMessage.setContent(mp);
			mimeMessage.saveChanges();
			Transport.send(mimeMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SendEmailUtil.sendCommonMail("1491759834@qq.com", "测试发送",
				"测试内容");

	}
}
