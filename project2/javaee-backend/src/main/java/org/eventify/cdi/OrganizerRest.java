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

import org.eventify.ejb.EventEJB;
import org.eventify.ejb.OrganizerEJB;
import org.eventify.ejb.TicketEJB;
import org.eventify.ejb.UserEJB;
import org.eventify.entity.Event;
import org.eventify.entity.OrganizerInfo;
import org.eventify.entity.OrganizerLogin;
import org.eventify.entity.Ticket;
import org.eventify.entity.UserLogin;
import org.eventify.restModel.receive.EmailToken;
import org.eventify.restModel.receive.EventIdToken;
import org.eventify.restModel.receive.EventToken;
import org.eventify.restModel.receive.Login;
import org.eventify.restModel.receive.OrganizerData;
import org.eventify.restModel.receive.Token;
import org.eventify.restModel.send.EventDI;
import org.eventify.restModel.send.OrganizerDI;
import org.eventify.util.Utility;

@Path("/organizer")
public class OrganizerRest {

	@EJB
	private OrganizerEJB organizerEJB;

	@EJB
	private UserEJB userEJB;

	@EJB
	private EventEJB eventEJB;

	@EJB
	private TicketEJB ticketEJB;

	private boolean isTokenValid(String loginId, String token) {
		OrganizerLogin ol;
		try {
			ol = organizerEJB.findOrganizerById(loginId);
			if (ol == null)
				return false;
		} catch (Exception e) {
			return false;
		}
		if (!token.equals(ol.getToken())) {
			return false;
		} else {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(ol.getSessionStartTime());
			cal.add(Calendar.DATE, 1);
			if (GregorianCalendar.getInstance().before(cal)) {
				ol.setSessionStartTime(Utility.getCurrentTimestamp());
				try {
					organizerEJB.updateOrganizerLogin(ol);
				} catch (Exception e) {
					return false;
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
	public Response register(OrganizerData entity) {
		try {
			if (organizerEJB.findOrganizerByEmail(entity.getEmail()) != null)
				return Utility.RESP_EMAIL_UNAVAILABLE;
			OrganizerLogin ol = new OrganizerLogin();
			ol.setEmail(entity.getEmail());
			ol.setId(Utility.generateUUID());
			ol.setHashPassword(Utility.hash(entity.getPassword()));
			ol.setToken(Utility.generateUUID());
			ol.setSessionStartTime(Utility.getCurrentTimestamp());
			OrganizerInfo oi = new OrganizerInfo();
			oi.setId(ol.getId());
			oi.setUsername(entity.getUsername());
			oi.setPhone(entity.getPhone());
			ol.setOrganizerInfo(oi);
			organizerEJB.create(ol);
			OrganizerDI resp = new OrganizerDI(ol.getToken(), ol.getId());
			resp.setEmail(ol.getEmail());
			resp.setPhone(oi.getPhone());
			resp.setUsername(oi.getUsername());
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
			OrganizerLogin ol = organizerEJB.findOrganizerByEmail(entity.getEmail());
			if (ol == null)
				return Utility.RESP_WRONG_LOGIN;
			if (Utility.isPasswordValid(entity.getPassword(), ol.getHashPassword())) {
				ol.setToken(Utility.generateUUID());
				ol.setSessionStartTime(Utility.getCurrentTimestamp());
				organizerEJB.updateOrganizerLogin(ol);
				OrganizerDI response = new OrganizerDI(ol.getToken(), ol.getId());
				response.setEmail(ol.getEmail());
				response.setUsername(ol.getOrganizerInfo().getUsername());
				response.setPhone(ol.getOrganizerInfo().getPhone());
				return Response.ok(response).build();
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
				OrganizerLogin ol = organizerEJB.findOrganizerById(entity.getLoginId());
				ol.setToken(Utility.generateUUID());
				ol.setSessionStartTime(new Date(0));
				organizerEJB.updateOrganizerLogin(ol);
				return Response.ok().build();
			} catch (Exception e) {
				return Utility.RESP_ERROR;
			}
		}
		return Utility.RESP_INVALID_TOKEN;
	}

	@POST
	@Path("/getOwnedEvents")
	@Produces("application/json")
	@Consumes("application/json")
	public Response getOwnedEvents(Token entity) {
		if (isTokenValid(entity.getLoginId(), entity.getToken())) {
			List<Event> resultsEvents = null;
			try {
				resultsEvents = eventEJB.findEventsByOwnerId(entity.getLoginId());
				if (resultsEvents == null)
					return Utility.RESP_ERROR;
			} catch (Exception e1) {
				return Utility.RESP_ERROR;
			}
			List<EventDI> resultsEventsDI = new LinkedList<EventDI>();
			for (Event e : resultsEvents) {
				if (!e.getDeleted())
					resultsEventsDI.add(new EventDI(e));
			}
			return Response.ok(resultsEventsDI).build();
		}
		return Utility.RESP_INVALID_TOKEN;
	}

	@POST
	@Path("/insert")
	@Produces("application/json")
	@Consumes("application/json")
	public Response insertEvent(EventToken entity) {
		if (isTokenValid(entity.getLoginId(), entity.getToken())) {
			try {
				if (!validEvent(entity.getEvent()))
					return Utility.RESP_INVALID_EVENT;
				Event event = new Event();
				event.setCategory(entity.getEvent().getCategory());
				event.setDescription(entity.getEvent().getDescription());
				event.setEnding(entity.getEvent().getEnding());
				event.setId(Utility.generateUUID());
				event.setLatitude(entity.getEvent().getLatitude());
				event.setLocation(entity.getEvent().getLocation());
				event.setLongitude(entity.getEvent().getLongitude());
				event.setOpening(entity.getEvent().getOpening());
				event.setPrice(entity.getEvent().getPrice());
				event.setRemaining_posts(entity.getEvent().getTotal_posts());
				event.setTotal_posts(entity.getEvent().getTotal_posts());
				event.setTitle(entity.getEvent().getTitle());
				event.setOwner_id(entity.getLoginId());
				event.setDeleted(false);
				eventEJB.create(event);
				return getOwnedEvents(new Token(entity.getToken(), entity.getLoginId()));
			} catch (Exception e) {
				return Utility.RESP_ERROR;
			}
		}
		return Utility.RESP_INVALID_TOKEN;
	}

	private boolean validEvent(EventDI event) {
		if (event.getCategory() == null || event.getEnding() == null || event.getLatitude() == null
				|| event.getLocation() == null || event.getLongitude() == null || event.getOpening() == null
				|| event.getPrice() == null || event.getTotal_posts() == null || event.getTitle() == null)
			return false;
		if(event.getOpening().after(event.getEnding()) || event.getEnding().before(new Date()))
			return false;
		return true;
	}

	@POST
	@Path("/sendEmail")
	@Produces("application/json")
	@Consumes("application/json")
	public Response sendEmail(EmailToken entity) {
		if (isTokenValid(entity.getLoginId(), entity.getToken())) {
			try {
				Event e = eventEJB.findEventById(entity.getEventId());
				if (e == null)
					return Utility.RESP_INVALID_EVENT;
				OrganizerLogin ol = organizerEJB.findOrganizerById(entity.getLoginId());
				List<Ticket> tickets = ticketEJB.findTicketByEventId(entity.getEventId());
				for (Ticket t : tickets) {
					UserLogin ul = userEJB.findUserById(t.getUser_id());
					if (ul == null)
						continue;
					Utility.sendEmail(ul.getEmail(), "Communication about the event '" + e.getTitle()
							+ "' organized by " + ol.getOrganizerInfo().getUsername(), entity.getMessage());
					Utility.emailDelay();
				}
				return Utility.RESP_SUCCESS;
			} catch (Exception e) {
				return Utility.RESP_ERROR;
			}
		}
		return Utility.RESP_INVALID_TOKEN;
	}

	@POST
	@Path("/modifyEvent")
	@Produces("application/json")
	@Consumes("application/json")
	public Response modifyEvent(EventToken entity) {
		if (isTokenValid(entity.getLoginId(), entity.getToken())) {
			try {
				if (!validEvent(entity.getEvent()))
					return Utility.RESP_INVALID_EVENT;
				Event event = eventEJB.findEventById(entity.getEvent().getId());
				event.setCategory(entity.getEvent().getCategory());
				event.setDescription(entity.getEvent().getDescription());
				event.setEnding(entity.getEvent().getEnding());
				event.setLatitude(entity.getEvent().getLatitude());
				event.setLocation(entity.getEvent().getLocation());
				event.setLongitude(entity.getEvent().getLongitude());
				event.setOpening(entity.getEvent().getOpening());
				event.setPrice(entity.getEvent().getPrice());
				event.setRemaining_posts(entity.getEvent().getTotal_posts());
				event.setTotal_posts(entity.getEvent().getTotal_posts());
				event.setTitle(entity.getEvent().getTitle());
				eventEJB.update(event);
				return getOwnedEvents(new Token(entity.getToken(), entity.getLoginId()));
			} catch (Exception e) {
				return Utility.RESP_ERROR;
			}
		}
		return Utility.RESP_INVALID_TOKEN;
	}

	@POST
	@Path("/delete")
	@Produces("application/json")
	@Consumes("application/json")
	public Response deleteEvent(EventIdToken entity) {
		if (isTokenValid(entity.getLoginId(), entity.getToken())) {
			try {
				Event event = eventEJB.findEventById(entity.getEventId());
				event.setDeleted(true);
				eventEJB.update(event);
				return Utility.RESP_SUCCESS;
			} catch (Exception e) {
				return Utility.RESP_ERROR;
			}
		}
		return Utility.RESP_INVALID_TOKEN;
	}

}
