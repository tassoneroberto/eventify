package org.eventify.restModel.send;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.eventify.entity.Cart;

@XmlRootElement
public class CartEventDI extends EventDI implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer amount;

	private String cartId;

	public CartEventDI(Cart entity) {
		super(entity.getEvent());
		this.amount = entity.getAmount();
		this.cartId = entity.getId();

	}

	public CartEventDI() {

	}

	public String getCartId() {
		return cartId;
	}

	public void setCartId(String cartId) {
		this.cartId = cartId;
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
		if (!(obj instanceof CartEventDI)) {
			return false;
		}
		CartEventDI other = (CartEventDI) obj;
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