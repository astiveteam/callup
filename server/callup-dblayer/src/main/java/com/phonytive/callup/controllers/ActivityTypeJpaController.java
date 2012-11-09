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

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.phonytive.callup.entities.Activity;
import com.phonytive.callup.entities.ActivityType;
import com.phonytive.callup.exceptions.IllegalOrphanException;
import com.phonytive.callup.exceptions.NonexistentEntityException;
import com.phonytive.callup.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class ActivityTypeJpaController implements Serializable {

    public ActivityTypeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ActivityType activityType) throws PreexistingEntityException, Exception {
        if (activityType.getActivityCollection() == null) {
            activityType.setActivityCollection(new ArrayList<Activity>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Activity> attachedActivityCollection = new ArrayList<Activity>();
            for (Activity activityCollectionActivityToAttach : activityType.getActivityCollection()) {
                activityCollectionActivityToAttach = em.getReference(activityCollectionActivityToAttach.getClass(), activityCollectionActivityToAttach.getActivityPK());
                attachedActivityCollection.add(activityCollectionActivityToAttach);
            }
            activityType.setActivityCollection(attachedActivityCollection);
            em.persist(activityType);
            for (Activity activityCollectionActivity : activityType.getActivityCollection()) {
                ActivityType oldActivityTypeOfActivityCollectionActivity = activityCollectionActivity.getActivityType();
                activityCollectionActivity.setActivityType(activityType);
                activityCollectionActivity = em.merge(activityCollectionActivity);
                if (oldActivityTypeOfActivityCollectionActivity != null) {
                    oldActivityTypeOfActivityCollectionActivity.getActivityCollection().remove(activityCollectionActivity);
                    oldActivityTypeOfActivityCollectionActivity = em.merge(oldActivityTypeOfActivityCollectionActivity);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findActivityType(activityType.getActivityTypeId()) != null) {
                throw new PreexistingEntityException("ActivityType " + activityType + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ActivityType activityType) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ActivityType persistentActivityType = em.find(ActivityType.class, activityType.getActivityTypeId());
            Collection<Activity> activityCollectionOld = persistentActivityType.getActivityCollection();
            Collection<Activity> activityCollectionNew = activityType.getActivityCollection();
            List<String> illegalOrphanMessages = null;
            for (Activity activityCollectionOldActivity : activityCollectionOld) {
                if (!activityCollectionNew.contains(activityCollectionOldActivity)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Activity " + activityCollectionOldActivity + " since its activityType field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Activity> attachedActivityCollectionNew = new ArrayList<Activity>();
            for (Activity activityCollectionNewActivityToAttach : activityCollectionNew) {
                activityCollectionNewActivityToAttach = em.getReference(activityCollectionNewActivityToAttach.getClass(), activityCollectionNewActivityToAttach.getActivityPK());
                attachedActivityCollectionNew.add(activityCollectionNewActivityToAttach);
            }
            activityCollectionNew = attachedActivityCollectionNew;
            activityType.setActivityCollection(activityCollectionNew);
            activityType = em.merge(activityType);
            for (Activity activityCollectionNewActivity : activityCollectionNew) {
                if (!activityCollectionOld.contains(activityCollectionNewActivity)) {
                    ActivityType oldActivityTypeOfActivityCollectionNewActivity = activityCollectionNewActivity.getActivityType();
                    activityCollectionNewActivity.setActivityType(activityType);
                    activityCollectionNewActivity = em.merge(activityCollectionNewActivity);
                    if (oldActivityTypeOfActivityCollectionNewActivity != null && !oldActivityTypeOfActivityCollectionNewActivity.equals(activityType)) {
                        oldActivityTypeOfActivityCollectionNewActivity.getActivityCollection().remove(activityCollectionNewActivity);
                        oldActivityTypeOfActivityCollectionNewActivity = em.merge(oldActivityTypeOfActivityCollectionNewActivity);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = activityType.getActivityTypeId();
                if (findActivityType(id) == null) {
                    throw new NonexistentEntityException("The activityType with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ActivityType activityType;
            try {
                activityType = em.getReference(ActivityType.class, id);
                activityType.getActivityTypeId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The activityType with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Activity> activityCollectionOrphanCheck = activityType.getActivityCollection();
            for (Activity activityCollectionOrphanCheckActivity : activityCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ActivityType (" + activityType + ") cannot be destroyed since the Activity " + activityCollectionOrphanCheckActivity + " in its activityCollection field has a non-nullable activityType field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(activityType);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ActivityType> findActivityTypeEntities() {
        return findActivityTypeEntities(true, -1, -1);
    }

    public List<ActivityType> findActivityTypeEntities(int maxResults, int firstResult) {
        return findActivityTypeEntities(false, maxResults, firstResult);
    }

    private List<ActivityType> findActivityTypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ActivityType.class));
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

    public ActivityType findActivityType(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ActivityType.class, id);
        } finally {
            em.close();
        }
    }

    public int getActivityTypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ActivityType> rt = cq.from(ActivityType.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
