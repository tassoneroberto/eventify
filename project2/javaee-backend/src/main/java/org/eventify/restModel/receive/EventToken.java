package org.eventify.restModel.receive;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.eventify.restModel.send.EventDI;

@XmlRootElement
public class EventToken extends Token implements Serializable {

	public EventToken(String token, String loginId) {
		super(token, loginId);
	}

	public EventToken() {

	}

	private static final long serialVersionUID = 1L;

	private EventDI event;

	public EventDI getEvent() {
		return event;
	}

	public void setEvent(EventDI event) {
		this.event = event;
	}

}