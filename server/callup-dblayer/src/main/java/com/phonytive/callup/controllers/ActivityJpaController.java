/* 
 * Copyright (C) 2012 PhonyTive LLC
 * http://callup.phonytive.com
 *
 * This file is part of Callup
 *
 * Callup is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Callup is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Callup.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.phonytive.callup.controllers;

import com.phonytive.callup.entities.Activity;
import com.phonytive.callup.entities.ActivityPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.phonytive.callup.entities.ActivityType;
import com.phonytive.callup.entities.User;
import com.phonytive.callup.exceptions.NonexistentEntityException;
import com.phonytive.callup.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class ActivityJpaController implements Serializable {

    public ActivityJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Activity activity) throws PreexistingEntityException, Exception {
        if (activity.getActivityPK() == null) {
            activity.setActivityPK(new ActivityPK());
        }
        activity.getActivityPK().setUseruserId(activity.getUser().getUserId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ActivityType activityType = activity.getActivityType();
            if (activityType != null) {
                activityType = em.getReference(activityType.getClass(), activityType.getActivityTypeId());
                activity.setActivityType(activityType);
            }
            User user = activity.getUser();
            if (user != null) {
                user = em.getReference(user.getClass(), user.getUserId());
                activity.setUser(user);
            }
            em.persist(activity);
            if (activityType != null) {
                activityType.getActivityCollection().add(activity);
                activityType = em.merge(activityType);
            }
            if (user != null) {
                user.getActivityCollection().add(activity);
                user = em.merge(user);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findActivity(activity.getActivityPK()) != null) {
                throw new PreexistingEntityException("Activity " + activity + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Activity activity) throws NonexistentEntityException, Exception {
        activity.getActivityPK().setUseruserId(activity.getUser().getUserId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Activity persistentActivity = em.find(Activity.class, activity.getActivityPK());
            ActivityType activityTypeOld = persistentActivity.getActivityType();
            ActivityType activityTypeNew = activity.getActivityType();
            User userOld = persistentActivity.getUser();
            User userNew = activity.getUser();
            if (activityTypeNew != null) {
                activityTypeNew = em.getReference(activityTypeNew.getClass(), activityTypeNew.getActivityTypeId());
                activity.setActivityType(activityTypeNew);
            }
            if (userNew != null) {
                userNew = em.getReference(userNew.getClass(), userNew.getUserId());
                activity.setUser(userNew);
            }
            activity = em.merge(activity);
            if (activityTypeOld != null && !activityTypeOld.equals(activityTypeNew)) {
                activityTypeOld.getActivityCollection().remove(activity);
                activityTypeOld = em.merge(activityTypeOld);
            }
            if (activityTypeNew != null && !activityTypeNew.equals(activityTypeOld)) {
                activityTypeNew.getActivityCollection().add(activity);
                activityTypeNew = em.merge(activityTypeNew);
            }
            if (userOld != null && !userOld.equals(userNew)) {
                userOld.getActivityCollection().remove(activity);
                userOld = em.merge(userOld);
            }
            if (userNew != null && !userNew.equals(userOld)) {
                userNew.getActivityCollection().add(activity);
                userNew = em.merge(userNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ActivityPK id = activity.getActivityPK();
                if (findActivity(id) == null) {
                    throw new NonexistentEntityException("The activity with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ActivityPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Activity activity;
            try {
                activity = em.getReference(Activity.class, id);
                activity.getActivityPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The activity with id " + id + " no longer exists.", enfe);
            }
            ActivityType activityType = activity.getActivityType();
            if (activityType != null) {
                activityType.getActivityCollection().remove(activity);
                activityType = em.merge(activityType);
            }
            User user = activity.getUser();
            if (user != null) {
                user.getActivityCollection().remove(activity);
                user = em.merge(user);
            }
            em.remove(activity);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Activity> findActivityEntities() {
        return findActivityEntities(true, -1, -1);
    }

    public List<Activity> findActivityEntities(int maxResults, int firstResult) {
        return findActivityEntities(false, maxResults, firstResult);
    }

    private List<Activity> findActivityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Activity.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Activity findActivity(ActivityPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Activity.class, id);
        } finally {
            em.close();
        }
    }

    public int getActivityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Activity> rt = cq.from(Activity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
