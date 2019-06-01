package org.eventify.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "organizer_login")
@XmlRootElement
public class OrganizerLogin implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(length = 32, name = "id", updatable = false, nullable = false)
	private String id;

	@Column
	private String email;

	@Version
	@Column(name = "version")
	private int version;

	@Column(length = 50)
	private String hashPassword;

	@Column(length = 32)
	private String token;

	@Column
	private Date sessionStartTime;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id", insertable = false, updatable = false)
	private OrganizerInfo organizerInfo;

	public OrganizerInfo getOrganizerInfo() {
		return organizerInfo;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public void setOrganizerInfo(OrganizerInfo organizerInfo) {
		this.organizerInfo = organizerInfo;
	}

	public String getHashPassword() {
		return hashPassword;
	}

	public void setHashPassword(String hashPassword) {
		this.hashPassword = hashPassword;
	}

	public Date getSessionStartTime() {
		return sessionStartTime;
	}

	public void setSessionStartTime(Date sessionStartTime) {
		this.sessionStartTime = sessionStartTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return hashPassword;
	}

	public void setPassword(String password) {
		this.hashPassword = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof OrganizerLogin)) {
			return false;
		}
		OrganizerLogin other = (OrganizerLogin) obj;
		if (id != null) {
			if (!id.equals(other.id)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

}