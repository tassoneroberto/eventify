package org.eventify.ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.eventify.entity.UserInfo;
import org.eventify.entity.UserLogin;
import org.eventify.util.Utility;

@Stateless
public class UserEJB {
	@PersistenceContext(unitName = "eventify-persistence-unit")
	private EntityManager em;

	public void create(UserLogin entity) throws Exception {
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
				em.remove(em.find(UserLogin.class, id));
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

	public UserLogin findUserById(String id) throws Exception {
		TypedQuery<UserLogin> query = em.createQuery(
				"SELECT DISTINCT e FROM UserLogin e WHERE e.id = :entityId ORDER BY e.id", UserLogin.class);
		query.setParameter("entityId", id);
		UserLogin entity;
		try {
			entity = query.getSingleResult();
		} catch (NoResultException nre) {
			entity = null;
		}
		return entity;
	}

	public UserLogin findUserByEmail(String email) throws Exception {
		TypedQuery<UserLogin> query = em.createQuery(
				"SELECT DISTINCT e FROM UserLogin e WHERE e.email = :entityEmail ORDER BY e.id", UserLogin.class);
		query.setParameter("entityEmail", email);
		UserLogin entity;
		try {
			entity = query.getSingleResult();
		} catch (NoResultException nre) {
			entity = null;
		}
		return entity;
	}

	public void updateUserLogin(UserLogin entity) throws Exception {
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

	public void updateUserInfo(String id, UserInfo entity) throws Exception {
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
