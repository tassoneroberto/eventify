package org.eventify.restModel.send;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.eventify.entity.Ticket;

@XmlRootElement
public class TicketEventDI extends EventDI implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer amount;

	private String ticketId;

	public TicketEventDI(Ticket entity) {
		super(entity.getEvent());
		super.setPrice(entity.getPrice());
		this.amount = entity.getAmount();
		this.ticketId = entity.getId();

	}

	public TicketEventDI() {

	}

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof TicketEventDI)) {
			return false;
		}
		TicketEventDI other = (TicketEventDI) obj;
		if (getId() != null) {
			if (!getId().equals(other.getId())) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

}