package org.eventify.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "organizer_info")
@XmlRootElement
public class OrganizerInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(length = 32, name = "id", updatable = false, nullable = false)
	private String id;

	@Column
	private String username;

	@Version
	@Column(name = "version")
	private int version;

	@Column
	private String phone;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id", insertable = false, updatable = false)
	private OrganizerLogin organizerLogin;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "organizer", cascade = CascadeType.ALL)
	private Collection<Event> events = new LinkedHashSet<Event>();

	public OrganizerLogin getOrganizerLogin() {
		return organizerLogin;
	}

	public void setOrganizerLogin(OrganizerLogin organizerLogin) {
		this.organizerLogin = organizerLogin;
	}

	public Collection<Event> getEvents() {
		return events;
	}

	public void setEvents(Collection<Event> events) {
		this.events = events;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof OrganizerInfo)) {
			return false;
		}
		OrganizerInfo other = (OrganizerInfo) obj;
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