package org.eventify.ejb;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.eventify.entity.Event;
import org.eventify.entity.Ticket;
import org.eventify.entity.UserLogin;
import org.eventify.restModel.receive.TicketData;
import org.eventify.util.Utility;

@Stateless
public class EventEJB {
	@PersistenceContext(unitName = "eventify-persistence-unit")
	private EntityManager em;

	@EJB
	private CartEJB cartEJB;

	@EJB
	private UserEJB userEJB;

	@EJB
	private TicketEJB ticketEJB;

	public void create(Event entity) throws Exception {
		int maxRetry = Utility.MAX_RETRY;
		while (maxRetry > 0) {
			try {
				em.persist(entity);
				em.flush();
				break;
			} catch (OptimisticLockException e) {
				maxRetry--;
				Utility.delay();
			}
		}
		if (maxRetry <= 0)
			throw new Exception();
	}

	public void deleteById(String id) throws Exception {
		int maxRetry = Utility.MAX_RETRY;
		while (maxRetry > 0) {
			try {
				em.remove(em.find(Event.class, id));
				em.flush();
				break;
			} catch (OptimisticLockException e) {
				maxRetry--;
				Utility.delay();
			}
		}
		if (maxRetry <= 0)
			throw new Exception();
	}

	public Event findEventById(String id) throws Exception {
		TypedQuery<Event> query = em.createQuery("SELECT DISTINCT e FROM Event e WHERE e.id = :entityId ORDER BY e.id",
				Event.class);
		query.setParameter("entityId", id);
		Event entity;
		try {
			entity = query.getSingleResult();
		} catch (NoResultException nre) {
			entity = null;
		}
		return entity;
	}

	public List<Event> getEventsByUserPref(Double latitude, Double longitude, Integer rangeTime, Integer rangeDistance)
			throws Exception {
		int kmRange = Utility.getRealRangeDistance(rangeDistance);
		double lonLength = 111.325 * Math.cos(latitude * 0.0174532925199433);
		double rangeDegreeLon = kmRange / lonLength;
		double rangeDegreeLat = kmRange / 111.132;
		int daysRange = Utility.getRealRangeTime(rangeTime);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String today = format.format(new Date());

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, daysRange);
		String dateLimit = format.format(c.getTime());

		TypedQuery<Event> query = em.createQuery("SELECT DISTINCT e FROM Event e WHERE latitude<'"
				+ (latitude + rangeDegreeLat) + "' AND latitude>'" + (latitude - rangeDegreeLat) + "' AND longitude<'"
				+ (longitude + rangeDegreeLon) + "' AND longitude>'" + (longitude - rangeDegreeLon)
				+ "' AND ((opening<='" + today + "' AND ending>='" + today + "') OR (ending>='" + today
				+ "' AND ending<='" + dateLimit + "')) ORDER BY e.id", Event.class);

		List<Event> entity;
		try {
			entity = query.getResultList();
		} catch (NoResultException nre) {
			entity = null;
		}
		return entity;
	}

	public List<Event> findEventsByOwnerId(String id) throws Exception {
		TypedQuery<Event> query = em
				.createQuery("SELECT DISTINCT e FROM Event e WHERE e.owner_id = :entityId ORDER BY e.id", Event.class);
		query.setParameter("entityId", id);
		List<Event> entity;

		try {
			entity = query.getResultList();
		} catch (NoResultException nre) {
			entity = null;
		}
		return entity;
	}

	public void update(Event entity) throws Exception {
		int maxRetry = Utility.MAX_RETRY;
		while (maxRetry > 0) {
			try {
				entity = em.merge(entity);
				em.flush();
				break;
			} catch (OptimisticLockException e) {
				maxRetry--;
				Utility.delay();
			}
		}
		if (maxRetry <= 0)
			throw new Exception();
	}

	public List<Event> listAll() throws Exception {
		TypedQuery<Event> query = em.createQuery("SELECT DISTINCT e FROM Event e ", Event.class);
		List<Event> entity;
		try {
			entity = query.getResultList();
		} catch (NoResultException nre) {
			entity = null;
		}
		return entity;
	}

	public String buy(LinkedList<TicketData> tickets, String userId, Date timestamp) throws Exception {
		for (TicketData t : tickets) {
			if (!availableTicket(t)) {
				return Utility.MESSAGE_TICKET_UNAVAILABLE;
			}
			if (priceChanged(t)) {
				return Utility.MESSAGE_PRICE_CHANGED;
			}
			if (deletedEvent(t)) {
				return Utility.MESSAGE_DELETED_EVENT;
			}
		}
		for (TicketData t : tickets) {
			buyTicket(t, userId);
		}
		UserLogin ul = userEJB.findUserById(userId);
		ul.setCartTimestamp(timestamp);
		userEJB.updateUserLogin(ul);
		return Utility.MESSAGE_SUCCESS;
	}

	private boolean availableTicket(TicketData t) {
		try {
			Event e = findEventById(t.getEventId());
			if (e == null || e.getRemaining_posts() < t.getAmount()) {
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean priceChanged(TicketData t) {
		try {
			Event e = findEventById(t.getEventId());
			return !e.getPrice().equals(t.getPrice());
		} catch (Exception e) {
			return true;
		}
	}

	private boolean deletedEvent(TicketData t) {
		try {
			Event e = findEventById(t.getEventId());
			return e.getDeleted();
		} catch (Exception e) {
			return true;
		}
	}

	private void buyTicket(TicketData entity, String userId) throws Exception {
		try {
			Event event = findEventById(entity.getEventId());
			event.setRemaining_posts(event.getRemaining_posts() - entity.getAmount());
			update(event);
			cartEJB.deleteByUserEventId(userId, entity.getEventId());
			Ticket t = new Ticket();
			t.setAmount(entity.getAmount());
			t.setEvent_id(entity.getEventId());
			t.setId(Utility.generateUUID());
			t.setPrice(entity.getPrice());
			t.setUser_id(userId);
			ticketEJB.create(t);
		} catch (Exception e) {
			throw new Exception();
		}
	}

}
