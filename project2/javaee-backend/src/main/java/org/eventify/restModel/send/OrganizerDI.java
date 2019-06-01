package org.eventify.restModel.send;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.eventify.restModel.receive.Token;

@XmlRootElement
public class OrganizerDI extends Token implements Serializable {

	public OrganizerDI(String token, String loginId) {
		super(token, loginId);
	}

	private static final long serialVersionUID = 1L;

	private String email;
	private String phone;
	private String username;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}