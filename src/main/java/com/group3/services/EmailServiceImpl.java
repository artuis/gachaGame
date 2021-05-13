package com.group3.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender jms;
    @Autowired
    private Environment env;
	
	@Override
	public void sendEmail(String to, String subject,  String text) {
		String emailuser = env.getProperty("EMAIL_USER");
		if(emailuser == null) {
			return;
		}

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setFrom(emailuser);
		message.setSubject(subject);
		message.setText(text);
		jms.send(message);
	}
}
