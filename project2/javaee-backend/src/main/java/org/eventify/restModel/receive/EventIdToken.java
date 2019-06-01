package org.eventify.restModel.receive;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EventIdToken extends Token implements Serializable {

	public EventIdToken(String token, String loginId) {
		super(token, loginId);
	}

	public EventIdToken() {

	}

	private static final long serialVersionUID = 1L;

	private String eventId;

	private Date timestamp;

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String id) {
		this.eventId = id;
	}

}