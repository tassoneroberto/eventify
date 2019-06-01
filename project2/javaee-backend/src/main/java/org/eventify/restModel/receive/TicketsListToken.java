package org.eventify.restModel.receive;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TicketsListToken extends Token implements Serializable {

	public TicketsListToken(String token, String loginId) {
		super(token, loginId);
	}

	public TicketsListToken() {
		super();
	}

	private static final long serialVersionUID = 1L;

	private LinkedList<TicketData> tickets;

	private Date timestamp;

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public LinkedList<TicketData> getTickets() {
		return tickets;
	}

	public void setTickets(LinkedList<TicketData> tickets) {
		this.tickets = tickets;
	}

}