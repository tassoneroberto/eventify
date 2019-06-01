package org.eventify.util;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.core.Response;

import org.eventify.restModel.send.ResponseMessage;

public class Utility {

	private static PBKDF2Hasher hasher = new PBKDF2Hasher(15);

	public static final int RETRY_DELAY = 3000;
	public static final int EMAIL_DELAY = 60000;
	public static final int MAX_RETRY = 3;

	public static final int DEFAULT_RANGE_TIME = 365;
	public static final int DEFAULT_RANGE_DISTANCE = 1000;
	public static final double DEFAULT_LATITUDE = 40.7591704;
	public static final double DEFAULT_LONGITUDE = -74.039271;

	public static final String MESSAGE_SUCCESS = "Success";
	public static final String MESSAGE_ERROR = "Error";
	public static final String MESSAGE_EMAIL_UNAVAILABLE = "Error: Email Unavailable";
	public static final String MESSAGE_WRONG_LOGIN = "Error: Wrong Login Data";
	public static final String MESSAGE_INVALID_TOKEN = "Error: Invalid Token";
	public static final String MESSAGE_INVALID_EVENT = "Error: Invalid Event";
	public static final String MESSAGE_BUY_ERROR = "Error: Cannot complete the purchase";
	public static final String MESSAGE_PRICE_CHANGED = "Error: Ticket price changed";
	public static final String MESSAGE_TICKET_UNAVAILABLE = "Error: Ticket unavailable";
	public static final String MESSAGE_DELETED_EVENT = "Error: E";

	public static final Response RESP_EMAIL_UNAVAILABLE = Response.ok()
			.entity(new ResponseMessage(MESSAGE_EMAIL_UNAVAILABLE)).build();
	public static final Response RESP_SUCCESS = Response.ok().entity(new ResponseMessage(MESSAGE_SUCCESS)).build();
	public static final Response RESP_ERROR = Response.ok().entity(new ResponseMessage(MESSAGE_ERROR)).build();
	public static final Response RESP_WRONG_LOGIN = Response.ok().entity(new ResponseMessage(MESSAGE_WRONG_LOGIN))
			.build();
	public static final Response RESP_INVALID_TOKEN = Response.ok().entity(new ResponseMessage(MESSAGE_INVALID_TOKEN))
			.build();
	public static final Response RESP_INVALID_EVENT = Response.ok().entity(new ResponseMessage(MESSAGE_INVALID_EVENT))
			.build();
	public static final Response RESP_BUY_ERROR = Response.ok().entity(new ResponseMessage(MESSAGE_BUY_ERROR)).build();
	public static final Response RESP_TICKET_UNAVAILABLE = Response.ok()
			.entity(new ResponseMessage(MESSAGE_TICKET_UNAVAILABLE)).build();

	public static final String EVENTIFY_EMAIL = "eventifyserver@gmail.com";
	public static final String EVENTIFY_PASSWORD = "goodEventify";

	public static String generateUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public static String hash(String password) {
		return hasher.hash(password.toCharArray());
	}

	public static boolean isPasswordValid(String password, String hash) {
		return hasher.checkPassword(password.toCharArray(), hash);
	}

	public static Date getCurrentTimestamp() {
		return new Timestamp(new Date().getTime());
	}

	public static void delay() {
		try {
			Thread.sleep((long) Math.random() * RETRY_DELAY);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void emailDelay() {
		try {
			Thread.sleep(EMAIL_DELAY);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static int getRealRangeTime(Integer rangeTime) {
		switch (rangeTime) {
		case 0:
			return 1;
		case 1:
			return 3;
		case 2:
			return 7;
		case 3:
			return 14;
		case 4:
			return 30;
		case 5:
			return 60;
		default:
			return DEFAULT_RANGE_TIME;
		}
	}

	public static int getRealRangeDistance(Integer rangeDistance) {
		switch (rangeDistance) {
		case 0:
			return 5;
		case 1:
			return 20;
		case 2:
			return 50;
		case 3:
			return 200;
		case 4:
			return 500;
		case 5:
			return 1000;
		default:
			return DEFAULT_RANGE_TIME;
		}
	}

	// TODO: replace thread with bean
	public static void sendEmail(String to, String subject, String msg) {
		Thread thread = new Thread() {
			public void run() {
				try {
					Properties props = new Properties();
					props.setProperty("mail.transport.protocol", "smtp");
					props.setProperty("mail.host", "smtp.gmail.com");
					props.put("mail.smtp.auth", "true");
					props.put("mail.smtp.port", "465");
					props.put("mail.debug", "true");
					props.put("mail.smtp.socketFactory.port", "465");
					props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
					props.put("mail.smtp.socketFactory.fallback", "false");
					Session session = Session.getInstance(props, new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(Utility.EVENTIFY_EMAIL, Utility.EVENTIFY_PASSWORD);
						}
					});
					Transport transport = session.getTransport();
					InternetAddress addressFrom = new InternetAddress(Utility.EVENTIFY_EMAIL);
					MimeMessage message = new MimeMessage(session);
					message.setSender(addressFrom);
					message.setSubject(subject);
					message.setContent(msg, "text/plain");
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
					transport.connect();
					Transport.send(message);
					transport.close();
				} catch (Exception e) {

				}
			}
		};
		thread.start();
	}

}
