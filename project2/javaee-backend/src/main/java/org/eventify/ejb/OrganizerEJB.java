package org.eventify.ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.eventify.entity.OrganizerInfo;
import org.eventify.entity.OrganizerLogin;
import org.eventify.util.Utility;

@Stateless
public class OrganizerEJB {
	@PersistenceContext(unitName = "eventify-persistence-unit")
	private EntityManager em;

	public void create(OrganizerLogin entity) throws Exception {
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
				em.remove(em.find(OrganizerLogin.class, id));
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

	public OrganizerLogin findOrganizerById(String id) throws Exception {
		TypedQuery<OrganizerLogin> findByIdQuery = em.createQuery(
				"SELECT DISTINCT e FROM OrganizerLogin e WHERE e.id = :entityId ORDER BY e.id", OrganizerLogin.class);
		findByIdQuery.setParameter("entityId", id);
		OrganizerLogin entity;

		try {
			entity = findByIdQuery.getSingleResult();
		} catch (NoResultException nre) {
			entity = null;
		}
		return entity;
	}

	public OrganizerLogin findOrganizerByEmail(String email) throws Exception {
		TypedQuery<OrganizerLogin> findByIdQuery = em.createQuery(
				"SELECT DISTINCT e FROM OrganizerLogin e WHERE e.email = :entityemail ORDER BY e.id",
				OrganizerLogin.class);
		findByIdQuery.setParameter("entityemail", email);
		OrganizerLogin entity;
		try {
			entity = findByIdQuery.getSingleResult();
		} catch (NoResultException nre) {
			entity = null;
		}
		return entity;
	}

	public void updateOrganizerLogin(OrganizerLogin entity) throws Exception {
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

	public void updateOrganizerInfo(OrganizerInfo entity) throws Exception {
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
