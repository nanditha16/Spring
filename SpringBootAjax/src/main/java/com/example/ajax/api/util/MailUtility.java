package com.example.ajax.api.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:admin/admin.properties")
public class MailUtility {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private ConfigUtility configUtil;

	public String sendEmail(String to, String textBody) {
		String msg = "";

		SimpleMailMessage message;
		String from = configUtil.getStringProperty("library.admin.from");

		try {
			message = new SimpleMailMessage();
			message.setText(textBody);
			message.setTo(to);
			message.setSubject("Return Books Alert");

			message.setFrom(from);
			javaMailSender.send(message);
			msg = "Mail Sent Successfully. ";
		} catch (Exception e) {
			msg = e.getMessage();
		}
		return msg;
	}
}
