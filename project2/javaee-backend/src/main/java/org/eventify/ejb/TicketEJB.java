package org.eventify.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.eventify.entity.Ticket;
import org.eventify.util.Utility;

@Stateless
public class TicketEJB {
	@PersistenceContext(unitName = "eventify-persistence-unit")
	private EntityManager em;

	public void create(Ticket entity) throws Exception {
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
				em.remove(em.find(Ticket.class, id));
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

	public Ticket findTicketById(String id) throws Exception {
		TypedQuery<Ticket> findByIdQuery = em
				.createQuery("SELECT DISTINCT e FROM Ticket e WHERE e.id = :entityId ORDER BY e.id", Ticket.class);
		findByIdQuery.setParameter("entityId", id);
		Ticket entity;
		try {
			entity = findByIdQuery.getSingleResult();
		} catch (NoResultException nre) {
			entity = null;
		}
		return entity;
	}

	public List<Ticket> findTicketByUserId(String id) throws Exception {
		TypedQuery<Ticket> findByIdQuery = em
				.createQuery("SELECT DISTINCT e FROM Ticket e WHERE e.user_id = :entityId ORDER BY e.id", Ticket.class);
		findByIdQuery.setParameter("entityId", id);
		List<Ticket> entity;
		try {
			entity = findByIdQuery.getResultList();
		} catch (NoResultException nre) {
			entity = null;
		}
		return entity;
	}

	public List<Ticket> findTicketByEventId(String id) throws Exception {
		TypedQuery<Ticket> findByIdQuery = em.createQuery(
				"SELECT DISTINCT e FROM Ticket e WHERE e.event_id = :entityId ORDER BY e.id", Ticket.class);
		findByIdQuery.setParameter("entityId", id);
		List<Ticket> entity;
		try {
			entity = findByIdQuery.getResultList();
		} catch (NoResultException nre) {
			entity = null;
		}
		return entity;
	}

	public void update(Ticket entity) throws Exception {
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
}
