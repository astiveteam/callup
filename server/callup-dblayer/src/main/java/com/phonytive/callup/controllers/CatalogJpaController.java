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
import com.phonytive.callup.entities.User;
import com.phonytive.callup.entities.Subscriber;
import java.util.ArrayList;
import java.util.Collection;
import com.phonytive.callup.entities.Campaign;
import com.phonytive.callup.entities.Catalog;
import com.phonytive.callup.exceptions.IllegalOrphanException;
import com.phonytive.callup.exceptions.NonexistentEntityException;
import com.phonytive.callup.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class CatalogJpaController implements Serializable {

    public CatalogJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Catalog catalog) throws IllegalOrphanException, PreexistingEntityException, Exception {
        if (catalog.getSubscriberCollection() == null) {
            catalog.setSubscriberCollection(new ArrayList<Subscriber>());
        }
        if (catalog.getCampaignCollection() == null) {
            catalog.setCampaignCollection(new ArrayList<Campaign>());
        }
        List<String> illegalOrphanMessages = null;
        User userOrphanCheck = catalog.getUser();
        if (userOrphanCheck != null) {
            Catalog oldCatalogOfUser = userOrphanCheck.getCatalog();
            if (oldCatalogOfUser != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The User " + userOrphanCheck + " already has an item of type Catalog whose user column cannot be null. Please make another selection for the user field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User user = catalog.getUser();
            if (user != null) {
                user = em.getReference(user.getClass(), user.getUserId());
                catalog.setUser(user);
            }
            Collection<Subscriber> attachedSubscriberCollection = new ArrayList<Subscriber>();
            for (Subscriber subscriberCollectionSubscriberToAttach : catalog.getSubscriberCollection()) {
                subscriberCollectionSubscriberToAttach = em.getReference(subscriberCollectionSubscriberToAttach.getClass(), subscriberCollectionSubscriberToAttach.getSubscriberId());
                attachedSubscriberCollection.add(subscriberCollectionSubscriberToAttach);
            }
            catalog.setSubscriberCollection(attachedSubscriberCollection);
            Collection<Campaign> attachedCampaignCollection = new ArrayList<Campaign>();
            for (Campaign campaignCollectionCampaignToAttach : catalog.getCampaignCollection()) {
                campaignCollectionCampaignToAttach = em.getReference(campaignCollectionCampaignToAttach.getClass(), campaignCollectionCampaignToAttach.getCampaignId());
                attachedCampaignCollection.add(campaignCollectionCampaignToAttach);
            }
            catalog.setCampaignCollection(attachedCampaignCollection);
            em.persist(catalog);
            if (user != null) {
                user.setCatalog(catalog);
                user = em.merge(user);
            }
            for (Subscriber subscriberCollectionSubscriber : catalog.getSubscriberCollection()) {
                subscriberCollectionSubscriber.getCatalogCollection().add(catalog);
                subscriberCollectionSubscriber = em.merge(subscriberCollectionSubscriber);
            }
            for (Campaign campaignCollectionCampaign : catalog.getCampaignCollection()) {
                Catalog oldCatalogOfCampaignCollectionCampaign = campaignCollectionCampaign.getCatalog();
                campaignCollectionCampaign.setCatalog(catalog);
                campaignCollectionCampaign = em.merge(campaignCollectionCampaign);
                if (oldCatalogOfCampaignCollectionCampaign != null) {
                    oldCatalogOfCampaignCollectionCampaign.getCampaignCollection().remove(campaignCollectionCampaign);
                    oldCatalogOfCampaignCollectionCampaign = em.merge(oldCatalogOfCampaignCollectionCampaign);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCatalog(catalog.getListId()) != null) {
                throw new PreexistingEntityException("Catalog " + catalog + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Catalog catalog) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Catalog persistentCatalog = em.find(Catalog.class, catalog.getListId());
            User userOld = persistentCatalog.getUser();
            User userNew = catalog.getUser();
            Collection<Subscriber> subscriberCollectionOld = persistentCatalog.getSubscriberCollection();
            Collection<Subscriber> subscriberCollectionNew = catalog.getSubscriberCollection();
            Collection<Campaign> campaignCollectionOld = persistentCatalog.getCampaignCollection();
            Collection<Campaign> campaignCollectionNew = catalog.getCampaignCollection();
            List<String> illegalOrphanMessages = null;
            if (userNew != null && !userNew.equals(userOld)) {
                Catalog oldCatalogOfUser = userNew.getCatalog();
                if (oldCatalogOfUser != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The User " + userNew + " already has an item of type Catalog whose user column cannot be null. Please make another selection for the user field.");
                }
            }
            for (Campaign campaignCollectionOldCampaign : campaignCollectionOld) {
                if (!campaignCollectionNew.contains(campaignCollectionOldCampaign)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Campaign " + campaignCollectionOldCampaign + " since its catalog field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (userNew != null) {
                userNew = em.getReference(userNew.getClass(), userNew.getUserId());
                catalog.setUser(userNew);
            }
            Collection<Subscriber> attachedSubscriberCollectionNew = new ArrayList<Subscriber>();
            for (Subscriber subscriberCollectionNewSubscriberToAttach : subscriberCollectionNew) {
                subscriberCollectionNewSubscriberToAttach = em.getReference(subscriberCollectionNewSubscriberToAttach.getClass(), subscriberCollectionNewSubscriberToAttach.getSubscriberId());
                attachedSubscriberCollectionNew.add(subscriberCollectionNewSubscriberToAttach);
            }
            subscriberCollectionNew = attachedSubscriberCollectionNew;
            catalog.setSubscriberCollection(subscriberCollectionNew);
            Collection<Campaign> attachedCampaignCollectionNew = new ArrayList<Campaign>();
            for (Campaign campaignCollectionNewCampaignToAttach : campaignCollectionNew) {
                campaignCollectionNewCampaignToAttach = em.getReference(campaignCollectionNewCampaignToAttach.getClass(), campaignCollectionNewCampaignToAttach.getCampaignId());
                attachedCampaignCollectionNew.add(campaignCollectionNewCampaignToAttach);
            }
            campaignCollectionNew = attachedCampaignCollectionNew;
            catalog.setCampaignCollection(campaignCollectionNew);
            catalog = em.merge(catalog);
            if (userOld != null && !userOld.equals(userNew)) {
                userOld.setCatalog(null);
                userOld = em.merge(userOld);
            }
            if (userNew != null && !userNew.equals(userOld)) {
                userNew.setCatalog(catalog);
                userNew = em.merge(userNew);
            }
            for (Subscriber subscriberCollectionOldSubscriber : subscriberCollectionOld) {
                if (!subscriberCollectionNew.contains(subscriberCollectionOldSubscriber)) {
                    subscriberCollectionOldSubscriber.getCatalogCollection().remove(catalog);
                    subscriberCollectionOldSubscriber = em.merge(subscriberCollectionOldSubscriber);
                }
            }
            for (Subscriber subscriberCollectionNewSubscriber : subscriberCollectionNew) {
                if (!subscriberCollectionOld.contains(subscriberCollectionNewSubscriber)) {
                    subscriberCollectionNewSubscriber.getCatalogCollection().add(catalog);
                    subscriberCollectionNewSubscriber = em.merge(subscriberCollectionNewSubscriber);
                }
            }
            for (Campaign campaignCollectionNewCampaign : campaignCollectionNew) {
                if (!campaignCollectionOld.contains(campaignCollectionNewCampaign)) {
                    Catalog oldCatalogOfCampaignCollectionNewCampaign = campaignCollectionNewCampaign.getCatalog();
                    campaignCollectionNewCampaign.setCatalog(catalog);
                    campaignCollectionNewCampaign = em.merge(campaignCollectionNewCampaign);
                    if (oldCatalogOfCampaignCollectionNewCampaign != null && !oldCatalogOfCampaignCollectionNewCampaign.equals(catalog)) {
                        oldCatalogOfCampaignCollectionNewCampaign.getCampaignCollection().remove(campaignCollectionNewCampaign);
                        oldCatalogOfCampaignCollectionNewCampaign = em.merge(oldCatalogOfCampaignCollectionNewCampaign);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = catalog.getListId();
                if (findCatalog(id) == null) {
                    throw new NonexistentEntityException("The catalog with id " + id + " no longer exists.");
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
            Catalog catalog;
            try {
                catalog = em.getReference(Catalog.class, id);
                catalog.getListId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The catalog with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Campaign> campaignCollectionOrphanCheck = catalog.getCampaignCollection();
            for (Campaign campaignCollectionOrphanCheckCampaign : campaignCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Catalog (" + catalog + ") cannot be destroyed since the Campaign " + campaignCollectionOrphanCheckCampaign + " in its campaignCollection field has a non-nullable catalog field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            User user = catalog.getUser();
            if (user != null) {
                user.setCatalog(null);
                user = em.merge(user);
            }
            Collection<Subscriber> subscriberCollection = catalog.getSubscriberCollection();
            for (Subscriber subscriberCollectionSubscriber : subscriberCollection) {
                subscriberCollectionSubscriber.getCatalogCollection().remove(catalog);
                subscriberCollectionSubscriber = em.merge(subscriberCollectionSubscriber);
            }
            em.remove(catalog);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Catalog> findCatalogEntities() {
        return findCatalogEntities(true, -1, -1);
    }

    public List<Catalog> findCatalogEntities(int maxResults, int firstResult) {
        return findCatalogEntities(false, maxResults, firstResult);
    }

    private List<Catalog> findCatalogEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Catalog.class));
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

    public Catalog findCatalog(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Catalog.class, id);
        } finally {
            em.close();
        }
    }

    public int getCatalogCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Catalog> rt = cq.from(Catalog.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
