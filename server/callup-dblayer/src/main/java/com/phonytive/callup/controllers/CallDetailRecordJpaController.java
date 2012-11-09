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

import com.phonytive.callup.entities.CallDetailRecord;
import com.phonytive.callup.entities.CallDetailRecordPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.phonytive.callup.entities.Campaign;
import com.phonytive.callup.entities.Subscriber;
import com.phonytive.callup.exceptions.NonexistentEntityException;
import com.phonytive.callup.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class CallDetailRecordJpaController implements Serializable {

    public CallDetailRecordJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CallDetailRecord callDetailRecord) throws PreexistingEntityException, Exception {
        if (callDetailRecord.getCallDetailRecordPK() == null) {
            callDetailRecord.setCallDetailRecordPK(new CallDetailRecordPK());
        }
        callDetailRecord.getCallDetailRecordPK().setCampaign(callDetailRecord.getCampaign1().getCampaignId());
        callDetailRecord.getCallDetailRecordPK().setSubscriber(callDetailRecord.getSubscriber1().getSubscriberId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Campaign campaign1 = callDetailRecord.getCampaign1();
            if (campaign1 != null) {
                campaign1 = em.getReference(campaign1.getClass(), campaign1.getCampaignId());
                callDetailRecord.setCampaign1(campaign1);
            }
            Subscriber subscriber1 = callDetailRecord.getSubscriber1();
            if (subscriber1 != null) {
                subscriber1 = em.getReference(subscriber1.getClass(), subscriber1.getSubscriberId());
                callDetailRecord.setSubscriber1(subscriber1);
            }
            em.persist(callDetailRecord);
            if (campaign1 != null) {
                campaign1.getCallDetailRecordCollection().add(callDetailRecord);
                campaign1 = em.merge(campaign1);
            }
            if (subscriber1 != null) {
                subscriber1.getCallDetailRecordCollection().add(callDetailRecord);
                subscriber1 = em.merge(subscriber1);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCallDetailRecord(callDetailRecord.getCallDetailRecordPK()) != null) {
                throw new PreexistingEntityException("CallDetailRecord " + callDetailRecord + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CallDetailRecord callDetailRecord) throws NonexistentEntityException, Exception {
        callDetailRecord.getCallDetailRecordPK().setCampaign(callDetailRecord.getCampaign1().getCampaignId());
        callDetailRecord.getCallDetailRecordPK().setSubscriber(callDetailRecord.getSubscriber1().getSubscriberId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CallDetailRecord persistentCallDetailRecord = em.find(CallDetailRecord.class, callDetailRecord.getCallDetailRecordPK());
            Campaign campaign1Old = persistentCallDetailRecord.getCampaign1();
            Campaign campaign1New = callDetailRecord.getCampaign1();
            Subscriber subscriber1Old = persistentCallDetailRecord.getSubscriber1();
            Subscriber subscriber1New = callDetailRecord.getSubscriber1();
            if (campaign1New != null) {
                campaign1New = em.getReference(campaign1New.getClass(), campaign1New.getCampaignId());
                callDetailRecord.setCampaign1(campaign1New);
            }
            if (subscriber1New != null) {
                subscriber1New = em.getReference(subscriber1New.getClass(), subscriber1New.getSubscriberId());
                callDetailRecord.setSubscriber1(subscriber1New);
            }
            callDetailRecord = em.merge(callDetailRecord);
            if (campaign1Old != null && !campaign1Old.equals(campaign1New)) {
                campaign1Old.getCallDetailRecordCollection().remove(callDetailRecord);
                campaign1Old = em.merge(campaign1Old);
            }
            if (campaign1New != null && !campaign1New.equals(campaign1Old)) {
                campaign1New.getCallDetailRecordCollection().add(callDetailRecord);
                campaign1New = em.merge(campaign1New);
            }
            if (subscriber1Old != null && !subscriber1Old.equals(subscriber1New)) {
                subscriber1Old.getCallDetailRecordCollection().remove(callDetailRecord);
                subscriber1Old = em.merge(subscriber1Old);
            }
            if (subscriber1New != null && !subscriber1New.equals(subscriber1Old)) {
                subscriber1New.getCallDetailRecordCollection().add(callDetailRecord);
                subscriber1New = em.merge(subscriber1New);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                CallDetailRecordPK id = callDetailRecord.getCallDetailRecordPK();
                if (findCallDetailRecord(id) == null) {
                    throw new NonexistentEntityException("The callDetailRecord with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(CallDetailRecordPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CallDetailRecord callDetailRecord;
            try {
                callDetailRecord = em.getReference(CallDetailRecord.class, id);
                callDetailRecord.getCallDetailRecordPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The callDetailRecord with id " + id + " no longer exists.", enfe);
            }
            Campaign campaign1 = callDetailRecord.getCampaign1();
            if (campaign1 != null) {
                campaign1.getCallDetailRecordCollection().remove(callDetailRecord);
                campaign1 = em.merge(campaign1);
            }
            Subscriber subscriber1 = callDetailRecord.getSubscriber1();
            if (subscriber1 != null) {
                subscriber1.getCallDetailRecordCollection().remove(callDetailRecord);
                subscriber1 = em.merge(subscriber1);
            }
            em.remove(callDetailRecord);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CallDetailRecord> findCallDetailRecordEntities() {
        return findCallDetailRecordEntities(true, -1, -1);
    }

    public List<CallDetailRecord> findCallDetailRecordEntities(int maxResults, int firstResult) {
        return findCallDetailRecordEntities(false, maxResults, firstResult);
    }

    private List<CallDetailRecord> findCallDetailRecordEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CallDetailRecord.class));
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

    public CallDetailRecord findCallDetailRecord(CallDetailRecordPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CallDetailRecord.class, id);
        } finally {
            em.close();
        }
    }

    public int getCallDetailRecordCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CallDetailRecord> rt = cq.from(CallDetailRecord.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
