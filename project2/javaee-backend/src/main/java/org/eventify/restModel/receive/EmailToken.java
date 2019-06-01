package org.eventify.restModel.receive;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EmailToken extends EventIdToken implements Serializable {

	public EmailToken(String token, String loginId) {
		super(token, loginId);
	}

	public EmailToken() {

	}

	private static final long serialVersionUID = 1L;

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}