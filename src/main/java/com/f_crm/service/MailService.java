package com.f_crm.service;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MailService {

	@Autowired 
	private JavaMailSender sender;
	
	private final Queue<MimeMessage> queue = new LinkedList<>();
	
	@Scheduled(fixedRate = 5000)
	public void run() {
		MimeMessage message = queue.poll();
		if(message != null) {
			sender.send(message);
		}
	}
	 public void add(Consumer<MimeMessage> messageConsumer) {
		 MimeMessage mimeMessage = sender.createMimeMessage();
		 messageConsumer.accept(mimeMessage);
		 queue.add(mimeMessage);
	 }
}
