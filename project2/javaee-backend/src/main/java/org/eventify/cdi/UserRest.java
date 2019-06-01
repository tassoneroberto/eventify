package org.eventify.cdi;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.eventify.ejb.CartEJB;
import org.eventify.ejb.EventEJB;
import org.eventify.ejb.TicketEJB;
import org.eventify.ejb.UserEJB;
import org.eventify.entity.Cart;
import org.eventify.entity.Event;
import org.eventify.entity.Ticket;
import org.eventify.entity.UserInfo;
import org.eventify.entity.UserLogin;
import org.eventify.restModel.receive.EventIdToken;
import org.eventify.restModel.receive.Login;
import org.eventify.restModel.receive.TicketData;
import org.eventify.restModel.receive.TicketsListToken;
import org.eventify.restModel.receive.Token;
import org.eventify.restModel.receive.UserData;
import org.eventify.restModel.receive.UserPrefToken;
import org.eventify.restModel.send.CartEventDI;
import org.eventify.restModel.send.EventDI;
import org.eventify.restModel.send.ResponseMessage;
import org.eventify.restModel.send.TicketEventDI;
import org.eventify.restModel.send.UserDI;
import org.eventify.util.Utility;

@Path("/user")
public class UserRest {

	@EJB
	private UserEJB userEJB;

	@EJB
	private CartEJB cartEJB;

	@EJB
	private TicketEJB ticketEJB;

	@EJB
	private EventEJB eventEJB;

	private boolean isTokenValid(String loginId, String token) {
		UserLogin ul;
		try {
			ul = userEJB.findUserById(loginId);
			if (ul == null)
				return false;
		} catch (Exception e) {
			return false;
		}
		if (!token.equals(ul.getToken())) {
			return false;
		} else {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(ul.getSessionStartTime());
			cal.add(Calendar.DATE, 1);
			if (GregorianCalendar.getInstance().before(cal)) {
				ul.setSessionStartTime(Utility.getCurrentTimestamp());
				try {
					userEJB.updateUserLogin(ul);
				} catch (Exception e) {
					return true;
				}
				return true;
			} else {
				return false;
			}
		}
	}

	@POST
	@Path("/register")
	@Produces("application/json")
	@Consumes("application/json")
	public Response register(UserData entity) {
		try {
			if (userEJB.findUserByEmail(entity.getEmail()) != null)
				return Utility.RESP_EMAIL_UNAVAILABLE;
			UserLogin ul = new UserLogin();
			ul.setEmail(entity.getEmail());
			ul.setId(Utility.generateUUID());
			ul.setHashPassword(Utility.hash(entity.getPassword()));
			ul.setToken(Utility.generateUUID());
			ul.setSessionStartTime(Utility.getCurrentTimestamp());
			ul.setCartTimestamp(Utility.getCurrentTimestamp());
			UserInfo ui = new UserInfo();
			ui.setId(ul.getId());
			ui.setName(entity.getName());
			ui.setSurname(entity.getSurname());
			ul.setUserInfo(ui);
			userEJB.create(ul);
			UserDI resp = new UserDI(ul.getToken(), ul.getId());
			resp.setEmail(ul.getEmail());
			resp.setName(ul.getUserInfo().getName());
			resp.setSurname(ul.getUserInfo().getSurname());
			return Response.ok(resp).build();
		} catch (Exception e) {
			return Utility.RESP_ERROR;
		}
	}

	@POST
	@Path("/login")
	@Produces("application/json")
	@Consumes("application/json")
	public Response login(Login entity) {
		try {
			UserLogin ul = userEJB.findUserByEmail(entity.getEmail());
			if (ul == null)
				return Utility.RESP_WRONG_LOGIN;
			if (Utility.isPasswordValid(entity.getPassword(), ul.getHashPassword())) {
				ul.setToken(Utility.generateUUID());
				ul.setSessionStartTime(Utility.getCurrentTimestamp());
				userEJB.updateUserLogin(ul);
				UserDI resp = new UserDI(ul.getToken(), ul.getId());
				resp.setEmail(ul.getEmail());
				resp.setName(ul.getUserInfo().getName());
				resp.setSurname(ul.getUserInfo().getSurname());
				return Response.ok(resp).build();
			} else {
				return Utility.RESP_WRONG_LOGIN;
			}
		} catch (Exception e) {
			return Utility.RESP_ERROR;
		}
	}

	@POST
	@Path("/logout")
	@Produces("application/json")
	@Consumes("application/json")
	public Response logout(Token entity) {
		if (isTokenValid(entity.getLoginId(), entity.getToken())) {
			try {
				UserLogin ul = userEJB.findUserById(entity.getLoginId());
				ul.setToken(Utility.generateUUID());
				ul.setSessionStartTime(new Date(0));
				userEJB.updateUserLogin(ul);
				return Utility.RESP_SUCCESS;
			} catch (Exception e) {
				return Utility.RESP_ERROR;
			}
		}
		return Utility.RESP_INVALID_TOKEN;
	}

	@POST
	@Path("/add")
	@Produces("application/json")
	@Consumes("application/json")
	public Response insertEventOnCart(EventIdToken entity) {
		if (isTokenValid(entity.getLoginId(), entity.getToken())) {
			try {
				UserLogin ul = userEJB.findUserById(entity.getLoginId());
				if (entity.getTimestamp().getTime() < ul.getCartTimestamp().getTime()) {
					return Utility.RESP_ERROR;
				}
				Event e = eventEJB.findEventById(entity.getEventId());
				if (e == null)
					return Utility.RESP_ERROR;
				Cart cart = cartEJB.findByUserIdEventId(entity.getLoginId(), entity.getEventId());
				if (cart != null) {
					if (e.getRemaining_posts() < cart.getAmount() + 1) {
						return Utility.RESP_TICKET_UNAVAILABLE;
					}
					cart.setAmount(cart.getAmount() + 1);
					cartEJB.update(cart);
				} else {
					Cart t = new Cart();
					t.setAmount(1);
					t.setEvent_id(entity.getEventId());
					t.setId(Utility.generateUUID());
					t.setUser_id(entity.getLoginId());
					cartEJB.create(t);
				}
				return getCartEvents(new Token(entity.getToken(), entity.getLoginId()));
			} catch (Exception e) {
				return Utility.RESP_ERROR;
			}
		}
		return Utility.RESP_INVALID_TOKEN;
	}

	@POST
	@Path("/remove")
	@Produces("application/json")
	@Consumes("application/json")
	public Response removeEventFromCart(EventIdToken entity) {
		if (isTokenValid(entity.getLoginId(), entity.getToken())) {
			try {
				UserLogin ul = userEJB.findUserById(entity.getLoginId());
				if (entity.getTimestamp().getTime() < ul.getCartTimestamp().getTime()) {
					return Utility.RESP_ERROR;
				}
				Cart c = cartEJB.findByUserIdEventId(entity.getLoginId(), entity.getEventId());
				if (c == null)
					return Utility.RESP_ERROR;
				if (c.getAmount() == 1) {
					cartEJB.deleteByUserEventId(entity.getLoginId(), entity.getEventId());
				} else {
					c.setAmount(c.getAmount() - 1);
					cartEJB.update(c);
				}
				return getCartEvents(new Token(entity.getToken(), entity.getLoginId()));
			} catch (Exception e) {
				return Utility.RESP_ERROR;
			}
		}
		return Utility.RESP_INVALID_TOKEN;
	}

	@POST
	@Path("/getCart")
	@Produces("application/json")
	@Consumes("application/json")
	public Response getCartEvents(Token entity) {
		if (isTokenValid(entity.getLoginId(), entity.getToken())) {
			try {
				List<Cart> eventsOnCart = cartEJB.getCartByUserId(entity.getLoginId());
				List<CartEventDI> resultsDI = new LinkedList<CartEventDI>();
				for (Cart c : eventsOnCart) {
					if (!c.getEvent().getDeleted())
						resultsDI.add(new CartEventDI(c));
				}
				return Response.ok(resultsDI).build();
			} catch (Exception e) {
				return Utility.RESP_ERROR;
			}
		}
		return Utility.RESP_INVALID_TOKEN;
	}

	@POST
	@Path("/getTickets")
	@Produces("application/json")
	@Consumes("application/json")
	public Response getTickets(Token entity) {
		if (isTokenValid(entity.getLoginId(), entity.getToken())) {
			try {
				List<Ticket> tickets = ticketEJB.findTicketByUserId(entity.getLoginId());
				List<TicketEventDI> resultsDI = new LinkedList<TicketEventDI>();
				for (Ticket t : tickets) {
					resultsDI.add(new TicketEventDI(t));
				}
				return Response.ok(resultsDI).build();
			} catch (Exception e) {
				return Utility.RESP_ERROR;
			}
		}
		return Utility.RESP_INVALID_TOKEN;
	}

	@POST
	@Path("/buy")
	@Produces("application/json")
	@Consumes("application/json")
	public Response buyTickets(TicketsListToken entity) {
		if (isTokenValid(entity.getLoginId(), entity.getToken())) {
			try {
				String buyMessage = eventEJB.buy(entity.getTickets(), entity.getLoginId(), entity.getTimestamp());
				StringBuilder sb = new StringBuilder();
				double totalPrice = 0;
				for (TicketData t : entity.getTickets()) {
					Event event = eventEJB.findEventById(t.getEventId());
					if (event.getDeleted())
						continue;
					totalPrice += t.getAmount() * t.getPrice();
					sb.append(t.getAmount() + " x \"" + event.getTitle() + "\" (" + t.getAmount() + " x "
							+ event.getPrice() + " €)\n");
				}
				sb.append("\nTotal: " + totalPrice + " €");
				UserLogin ul = userEJB.findUserById(entity.getLoginId());
				Utility.sendEmail(ul.getEmail(), "Tickets Purchase Report", sb.toString());
				return Response.ok(new ResponseMessage(buyMessage)).build();
			} catch (Exception e) {
				return Utility.RESP_ERROR;
			}
		}
		return Utility.RESP_INVALID_TOKEN;
	}

	@POST
	@Path("/getEvents")
	@Produces("application/json")
	@Consumes("application/json")
	public Response getEvents(Token entity) {
		if (isTokenValid(entity.getLoginId(), entity.getToken())) {
			try {
				List<Event> resultsEvents = eventEJB.listAll();
				List<EventDI> resultsEventsDI = new LinkedList<EventDI>();
				for (Event event : resultsEvents) {
					if (!event.getDeleted())
						resultsEventsDI.add(new EventDI(event));
				}
				return Response.ok(resultsEventsDI).build();
			} catch (Exception e) {
				return Utility.RESP_ERROR;
			}
		}
		return Utility.RESP_INVALID_TOKEN;
	}

	@POST
	@Path("/getCustomEvents")
	@Produces("application/json")
	@Consumes("application/json")
	public Response getCustomEvents(UserPrefToken entity) {
		if (isTokenValid(entity.getLoginId(), entity.getToken())) {
			try {
				int rangeTime = (entity.getRangeTime() == null) ? Utility.DEFAULT_RANGE_TIME : entity.getRangeTime();
				int rangeDistance = (entity.getRangeDistance() == null) ? Utility.DEFAULT_RANGE_DISTANCE
						: entity.getRangeDistance();
				double latitude = (entity.getLatitude() == null) ? Utility.DEFAULT_LATITUDE : entity.getLatitude();
				double longitude = (entity.getLongitude() == null) ? Utility.DEFAULT_LONGITUDE : entity.getLongitude();
				List<Event> resultsEvents = eventEJB.getEventsByUserPref(latitude, longitude, rangeTime, rangeDistance);
				List<EventDI> resultsEventsDI = new LinkedList<EventDI>();
				for (Event event : resultsEvents) {
					if (!event.getDeleted())
						resultsEventsDI.add(new EventDI(event));
				}
				return Response.ok(resultsEventsDI).build();
			} catch (Exception e) {
				return Utility.RESP_ERROR;
			}
		}
		return Utility.RESP_INVALID_TOKEN;
	}

}
