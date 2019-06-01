package org.eventify.restModel.receive;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TicketData implements Serializable {

	public TicketData() {

	}

	private static final long serialVersionUID = 1L;

	private String eventId;

	private Double price;

	private Integer amount;

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String id) {
		this.eventId = id;
	}

}