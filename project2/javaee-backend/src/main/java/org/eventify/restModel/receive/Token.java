package org.eventify.restModel.receive;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Token implements Serializable {

	private static final long serialVersionUID = 1L;

	private String token;

	private String loginId;

	public Token() {

	}

	public Token(String token, String loginId) {
		super();
		this.token = token;
		this.loginId = loginId;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}