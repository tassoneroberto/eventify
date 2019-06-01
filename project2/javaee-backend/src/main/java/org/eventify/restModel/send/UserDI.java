package org.eventify.restModel.send;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.eventify.restModel.receive.Token;

@XmlRootElement
public class UserDI extends Token implements Serializable {

	public UserDI(String token, String loginId) {
		super(token, loginId);
	}

	private static final long serialVersionUID = 1L;

	private String email;
	private String name;
	private String surname;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

}