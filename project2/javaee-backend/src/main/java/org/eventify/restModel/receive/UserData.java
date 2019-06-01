package org.eventify.restModel.receive;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserData extends Login implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String surname;

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

	public UserData(String email, String password, String name, String surname) {
		super(email, password);
		this.name = name;
		this.surname = surname;
	}

	public UserData() {
		super();
	}

	public UserData(String email, String password) {
		super(email, password);
	}

}