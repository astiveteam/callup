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
import com.phonytive.callup.entities.Catalog;
import java.util.ArrayList;
import java.util.Collection;
import com.phonytive.callup.entities.CallDetailRecord;
import com.phonytive.callup.entities.Subscriber;
import com.phonytive.callup.exceptions.IllegalOrphanException;
import com.phonytive.callup.exceptions.NonexistentEntityException;
import com.phonytive.callup.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class SubscriberJpaController implements Serializable {

    public SubscriberJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Subscriber subscriber) throws PreexistingEntityException, Exception {
        if (subscriber.getCatalogCollection() == null) {
            subscriber.setCatalogCollection(new ArrayList<Catalog>());
        }
        if (subscriber.getCallDetailRecordCollection() == null) {
            subscriber.setCallDetailRecordCollection(new ArrayList<CallDetailRecord>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Catalog> attachedCatalogCollection = new ArrayList<Catalog>();
            for (Catalog catalogCollectionCatalogToAttach : subscriber.getCatalogCollection()) {
                catalogCollectionCatalogToAttach = em.getReference(catalogCollectionCatalogToAttach.getClass(), catalogCollectionCatalogToAttach.getListId());
                attachedCatalogCollection.add(catalogCollectionCatalogToAttach);
            }
            subscriber.setCatalogCollection(attachedCatalogCollection);
            Collection<CallDetailRecord> attachedCallDetailRecordCollection = new ArrayList<CallDetailRecord>();
            for (CallDetailRecord callDetailRecordCollectionCallDetailRecordToAttach : subscriber.getCallDetailRecordCollection()) {
                callDetailRecordCollectionCallDetailRecordToAttach = em.getReference(callDetailRecordCollectionCallDetailRecordToAttach.getClass(), callDetailRecordCollectionCallDetailRecordToAttach.getCallDetailRecordPK());
                attachedCallDetailRecordCollection.add(callDetailRecordCollectionCallDetailRecordToAttach);
            }
            subscriber.setCallDetailRecordCollection(attachedCallDetailRecordCollection);
            em.persist(subscriber);
            for (Catalog catalogCollectionCatalog : subscriber.getCatalogCollection()) {
                catalogCollectionCatalog.getSubscriberCollection().add(subscriber);
                catalogCollectionCatalog = em.merge(catalogCollectionCatalog);
            }
            for (CallDetailRecord callDetailRecordCollectionCallDetailRecord : subscriber.getCallDetailRecordCollection()) {
                Subscriber oldSubscriber1OfCallDetailRecordCollectionCallDetailRecord = callDetailRecordCollectionCallDetailRecord.getSubscriber1();
                callDetailRecordCollectionCallDetailRecord.setSubscriber1(subscriber);
                callDetailRecordCollectionCallDetailRecord = em.merge(callDetailRecordCollectionCallDetailRecord);
                if (oldSubscriber1OfCallDetailRecordCollectionCallDetailRecord != null) {
                    oldSubscriber1OfCallDetailRecordCollectionCallDetailRecord.getCallDetailRecordCollection().remove(callDetailRecordCollectionCallDetailRecord);
                    oldSubscriber1OfCallDetailRecordCollectionCallDetailRecord = em.merge(oldSubscriber1OfCallDetailRecordCollectionCallDetailRecord);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSubscriber(subscriber.getSubscriberId()) != null) {
                throw new PreexistingEntityException("Subscriber " + subscriber + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Subscriber subscriber) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Subscriber persistentSubscriber = em.find(Subscriber.class, subscriber.getSubscriberId());
            Collection<Catalog> catalogCollectionOld = persistentSubscriber.getCatalogCollection();
            Collection<Catalog> catalogCollectionNew = subscriber.getCatalogCollection();
            Collection<CallDetailRecord> callDetailRecordCollectionOld = persistentSubscriber.getCallDetailRecordCollection();
            Collection<CallDetailRecord> callDetailRecordCollectionNew = subscriber.getCallDetailRecordCollection();
            List<String> illegalOrphanMessages = null;
            for (CallDetailRecord callDetailRecordCollectionOldCallDetailRecord : callDetailRecordCollectionOld) {
                if (!callDetailRecordCollectionNew.contains(callDetailRecordCollectionOldCallDetailRecord)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CallDetailRecord " + callDetailRecordCollectionOldCallDetailRecord + " since its subscriber1 field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Catalog> attachedCatalogCollectionNew = new ArrayList<Catalog>();
            for (Catalog catalogCollectionNewCatalogToAttach : catalogCollectionNew) {
                catalogCollectionNewCatalogToAttach = em.getReference(catalogCollectionNewCatalogToAttach.getClass(), catalogCollectionNewCatalogToAttach.getListId());
                attachedCatalogCollectionNew.add(catalogCollectionNewCatalogToAttach);
            }
            catalogCollectionNew = attachedCatalogCollectionNew;
            subscriber.setCatalogCollection(catalogCollectionNew);
            Collection<CallDetailRecord> attachedCallDetailRecordCollectionNew = new ArrayList<CallDetailRecord>();
            for (CallDetailRecord callDetailRecordCollectionNewCallDetailRecordToAttach : callDetailRecordCollectionNew) {
                callDetailRecordCollectionNewCallDetailRecordToAttach = em.getReference(callDetailRecordCollectionNewCallDetailRecordToAttach.getClass(), callDetailRecordCollectionNewCallDetailRecordToAttach.getCallDetailRecordPK());
                attachedCallDetailRecordCollectionNew.add(callDetailRecordCollectionNewCallDetailRecordToAttach);
            }
            callDetailRecordCollectionNew = attachedCallDetailRecordCollectionNew;
            subscriber.setCallDetailRecordCollection(callDetailRecordCollectionNew);
            subscriber = em.merge(subscriber);
            for (Catalog catalogCollectionOldCatalog : catalogCollectionOld) {
                if (!catalogCollectionNew.contains(catalogCollectionOldCatalog)) {
                    catalogCollectionOldCatalog.getSubscriberCollection().remove(subscriber);
                    catalogCollectionOldCatalog = em.merge(catalogCollectionOldCatalog);
                }
            }
            for (Catalog catalogCollectionNewCatalog : catalogCollectionNew) {
                if (!catalogCollectionOld.contains(catalogCollectionNewCatalog)) {
                    catalogCollectionNewCatalog.getSubscriberCollection().add(subscriber);
                    catalogCollectionNewCatalog = em.merge(catalogCollectionNewCatalog);
                }
            }
            for (CallDetailRecord callDetailRecordCollectionNewCallDetailRecord : callDetailRecordCollectionNew) {
                if (!callDetailRecordCollectionOld.contains(callDetailRecordCollectionNewCallDetailRecord)) {
                    Subscriber oldSubscriber1OfCallDetailRecordCollectionNewCallDetailRecord = callDetailRecordCollectionNewCallDetailRecord.getSubscriber1();
                    callDetailRecordCollectionNewCallDetailRecord.setSubscriber1(subscriber);
                    callDetailRecordCollectionNewCallDetailRecord = em.merge(callDetailRecordCollectionNewCallDetailRecord);
                    if (oldSubscriber1OfCallDetailRecordCollectionNewCallDetailRecord != null && !oldSubscriber1OfCallDetailRecordCollectionNewCallDetailRecord.equals(subscriber)) {
                        oldSubscriber1OfCallDetailRecordCollectionNewCallDetailRecord.getCallDetailRecordCollection().remove(callDetailRecordCollectionNewCallDetailRecord);
                        oldSubscriber1OfCallDetailRecordCollectionNewCallDetailRecord = em.merge(oldSubscriber1OfCallDetailRecordCollectionNewCallDetailRecord);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = subscriber.getSubscriberId();
                if (findSubscriber(id) == null) {
                    throw new NonexistentEntityException("The subscriber with id " + id + " no longer exists.");
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
            Subscriber subscriber;
            try {
                subscriber = em.getReference(Subscriber.class, id);
                subscriber.getSubscriberId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The subscriber with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<CallDetailRecord> callDetailRecordCollectionOrphanCheck = subscriber.getCallDetailRecordCollection();
            for (CallDetailRecord callDetailRecordCollectionOrphanCheckCallDetailRecord : callDetailRecordCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Subscriber (" + subscriber + ") cannot be destroyed since the CallDetailRecord " + callDetailRecordCollectionOrphanCheckCallDetailRecord + " in its callDetailRecordCollection field has a non-nullable subscriber1 field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Catalog> catalogCollection = subscriber.getCatalogCollection();
            for (Catalog catalogCollectionCatalog : catalogCollection) {
                catalogCollectionCatalog.getSubscriberCollection().remove(subscriber);
                catalogCollectionCatalog = em.merge(catalogCollectionCatalog);
            }
            em.remove(subscriber);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Subscriber> findSubscriberEntities() {
        return findSubscriberEntities(true, -1, -1);
    }

    public List<Subscriber> findSubscriberEntities(int maxResults, int firstResult) {
        return findSubscriberEntities(false, maxResults, firstResult);
    }

    private List<Subscriber> findSubscriberEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Subscriber.class));
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

    public Subscriber findSubscriber(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Subscriber.class, id);
        } finally {
            em.close();
        }
    }

    public int getSubscriberCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Subscriber> rt = cq.from(Subscriber.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
