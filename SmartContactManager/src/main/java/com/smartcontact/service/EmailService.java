package com.smartcontact.service;

import java.util.Properties;
import jakarta.mail.*;
import org.springframework.stereotype.Service;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	
	 public boolean sendEmail(String subject,String message,String to) {
		 
		         boolean flag = false;
		         
		         String from="siddhanttapre@gmail.com";
		 
		         Properties properties=new Properties();
		         properties.put("mail.smtp.auth","true");
		         properties.put("mail.smtp.starttls.enable","true");
		         properties.put("mail.smtp.host","smtp.gmail.com");
		         properties.put("mail.smtp.port","587");
		 
		 
		         String username="siddhanttapre";
		         String password="zmrh wmca djgp fkhi";
		 
		 
		         Session session=Session.getInstance(properties, new Authenticator() {
		             @Override
		             protected PasswordAuthentication getPasswordAuthentication() {
		                 return new PasswordAuthentication(username,password);
		             }
		         });
		         
		         session.setDebug(true);
		         
		         MimeMessage msg = new MimeMessage(session);
		 
		         try {
		 
		             
		             msg.setRecipients(MimeMessage.RecipientType.TO,InternetAddress.parse(to));
		             msg.setFrom(from);
		             msg.setSubject(subject);
//		             msg.setText(message);
		             
		             msg.setContent(message,"text/html");
		 
		             Transport.send(msg);
		 
		             flag=true;
		         }catch (Exception e)
		         {
		             e.printStackTrace();
		         }
		 
		         return flag;
		     }
	
	

}
