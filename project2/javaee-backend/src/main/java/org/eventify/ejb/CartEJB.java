package org.eventify.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.eventify.entity.Cart;
import org.eventify.util.Utility;

@Stateless
public class CartEJB {
	@PersistenceContext(unitName = "eventify-persistence-unit")
	private EntityManager em;

	public void create(Cart entity) throws Exception {
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

	public void deleteByUserEventId(String user_id, String event_id) throws Exception {
		int maxRetry = Utility.MAX_RETRY;
		while (maxRetry > 0) {
			try {
				Query query = em.createNativeQuery(
						"DELETE FROM carts WHERE user_id = '" + user_id + "' AND event_id = '" + event_id + "'");
				query.executeUpdate();
				break;
			} catch (OptimisticLockException e) {
				maxRetry--;
				Utility.delay();
			}
		}
		if (maxRetry <= 0)
			throw new Exception();
	}

	public void deleteByUserId(String user_id) throws Exception {
		int maxRetry = Utility.MAX_RETRY;
		while (maxRetry > 0) {
			try {
				Query query = em.createNativeQuery("DELETE FROM carts WHERE user_id = '" + user_id + "'");
				query.executeUpdate();
				break;
			} catch (OptimisticLockException e) {
				maxRetry--;
				Utility.delay();
			}
		}
		if (maxRetry <= 0)
			throw new Exception();
	}

	public Cart findById(String id) throws Exception {
		TypedQuery<Cart> findByIdQuery = em
				.createQuery("SELECT DISTINCT e FROM Cart e WHERE e.id = :entityId ORDER BY e.id", Cart.class);
		findByIdQuery.setParameter("entityId", id);
		Cart entity;

		try {
			entity = findByIdQuery.getSingleResult();
		} catch (NoResultException nre) {
			entity = null;
		}
		return entity;
	}

	public Cart findByUserIdEventId(String userId, String eventId) throws Exception {
		TypedQuery<Cart> findByIdQuery = em.createQuery(
				"SELECT DISTINCT e FROM Cart e WHERE e.user_id = :userId AND e.event_id = :eventId ORDER BY e.id",
				Cart.class);
		findByIdQuery.setParameter("userId", userId);
		findByIdQuery.setParameter("eventId", eventId);
		Cart entity;
		try {
			entity = findByIdQuery.getSingleResult();
		} catch (NoResultException nre) {
			entity = null;
		}
		return entity;
	}

	public void deleteById(String id) throws Exception {
		int maxRetry = Utility.MAX_RETRY;
		while (maxRetry > 0) {
			try {
				em.remove(em.find(Cart.class, id));
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

	public List<Cart> getCartByUserId(String id) throws Exception {
		TypedQuery<Cart> findByIdQuery = em
				.createQuery("SELECT DISTINCT e FROM Cart e WHERE e.user_id = :entityId ORDER BY e.id", Cart.class);
		findByIdQuery.setParameter("entityId", id);
		List<Cart> entity;

		try {
			entity = findByIdQuery.getResultList();
		} catch (NoResultException nre) {
			entity = null;
		}
		return entity;
	}

	public void update(Cart entity) throws Exception {
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
