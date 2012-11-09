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

import com.phonytive.callup.entities.*;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import com.phonytive.callup.exceptions.IllegalOrphanException;
import com.phonytive.callup.exceptions.NonexistentEntityException;
import com.phonytive.callup.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Pedro Sanders <psanders@kaffeineminds.com>
 * @since 0.1
 * @version $Id$
 */
public class CampaignJpaController implements Serializable {

    public CampaignJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Campaign campaign) throws PreexistingEntityException, Exception {
        if (campaign.getAudioFileCollection() == null) {
            campaign.setAudioFileCollection(new ArrayList<AudioFile>());
        }
        if (campaign.getCallDetailRecordCollection() == null) {
            campaign.setCallDetailRecordCollection(new ArrayList<CallDetailRecord>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Catalog catalog = campaign.getCatalog();
            if (catalog != null) {
                catalog = em.getReference(catalog.getClass(), catalog.getListId());
                campaign.setCatalog(catalog);
            }
            User user = campaign.getUser();
            if (user != null) {
                user = em.getReference(user.getClass(), user.getUserId());
                campaign.setUser(user);
            }
            Collection<AudioFile> attachedAudioFileCollection = new ArrayList<AudioFile>();
            for (AudioFile audioFileCollectionAudioFileToAttach : campaign.getAudioFileCollection()) {
                audioFileCollectionAudioFileToAttach = em.getReference(audioFileCollectionAudioFileToAttach.getClass(), audioFileCollectionAudioFileToAttach.getAudioFileId());
                attachedAudioFileCollection.add(audioFileCollectionAudioFileToAttach);
            }
            campaign.setAudioFileCollection(attachedAudioFileCollection);
            Collection<CallDetailRecord> attachedCallDetailRecordCollection = new ArrayList<CallDetailRecord>();
            for (CallDetailRecord callDetailRecordCollectionCallDetailRecordToAttach : campaign.getCallDetailRecordCollection()) {
                callDetailRecordCollectionCallDetailRecordToAttach = em.getReference(callDetailRecordCollectionCallDetailRecordToAttach.getClass(), callDetailRecordCollectionCallDetailRecordToAttach.getCallDetailRecordPK());
                attachedCallDetailRecordCollection.add(callDetailRecordCollectionCallDetailRecordToAttach);
            }
            campaign.setCallDetailRecordCollection(attachedCallDetailRecordCollection);
            em.persist(campaign);
            if (catalog != null) {
                catalog.getCampaignCollection().add(campaign);
                catalog = em.merge(catalog);
            }
            if (user != null) {
                user.getCampaignCollection().add(campaign);
                user = em.merge(user);
            }
            for (AudioFile audioFileCollectionAudioFile : campaign.getAudioFileCollection()) {
                audioFileCollectionAudioFile.getCampaignCollection().add(campaign);
                audioFileCollectionAudioFile = em.merge(audioFileCollectionAudioFile);
            }
            for (CallDetailRecord callDetailRecordCollectionCallDetailRecord : campaign.getCallDetailRecordCollection()) {
                Campaign oldCampaign1OfCallDetailRecordCollectionCallDetailRecord = callDetailRecordCollectionCallDetailRecord.getCampaign1();
                callDetailRecordCollectionCallDetailRecord.setCampaign1(campaign);
                callDetailRecordCollectionCallDetailRecord = em.merge(callDetailRecordCollectionCallDetailRecord);
                if (oldCampaign1OfCallDetailRecordCollectionCallDetailRecord != null) {
                    oldCampaign1OfCallDetailRecordCollectionCallDetailRecord.getCallDetailRecordCollection().remove(callDetailRecordCollectionCallDetailRecord);
                    oldCampaign1OfCallDetailRecordCollectionCallDetailRecord = em.merge(oldCampaign1OfCallDetailRecordCollectionCallDetailRecord);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCampaign(campaign.getCampaignId()) != null) {
                throw new PreexistingEntityException("Campaign " + campaign + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Campaign campaign) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Campaign persistentCampaign = em.find(Campaign.class, campaign.getCampaignId());
            Catalog catalogOld = persistentCampaign.getCatalog();
            Catalog catalogNew = campaign.getCatalog();
            User userOld = persistentCampaign.getUser();
            User userNew = campaign.getUser();
            Collection<AudioFile> audioFileCollectionOld = persistentCampaign.getAudioFileCollection();
            Collection<AudioFile> audioFileCollectionNew = campaign.getAudioFileCollection();
            Collection<CallDetailRecord> callDetailRecordCollectionOld = persistentCampaign.getCallDetailRecordCollection();
            Collection<CallDetailRecord> callDetailRecordCollectionNew = campaign.getCallDetailRecordCollection();
            List<String> illegalOrphanMessages = null;
            for (CallDetailRecord callDetailRecordCollectionOldCallDetailRecord : callDetailRecordCollectionOld) {
                if (!callDetailRecordCollectionNew.contains(callDetailRecordCollectionOldCallDetailRecord)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CallDetailRecord " + callDetailRecordCollectionOldCallDetailRecord + " since its campaign1 field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (catalogNew != null) {
                catalogNew = em.getReference(catalogNew.getClass(), catalogNew.getListId());
                campaign.setCatalog(catalogNew);
            }
            if (userNew != null) {
                userNew = em.getReference(userNew.getClass(), userNew.getUserId());
                campaign.setUser(userNew);
            }
            Collection<AudioFile> attachedAudioFileCollectionNew = new ArrayList<AudioFile>();
            for (AudioFile audioFileCollectionNewAudioFileToAttach : audioFileCollectionNew) {
                audioFileCollectionNewAudioFileToAttach = em.getReference(audioFileCollectionNewAudioFileToAttach.getClass(), audioFileCollectionNewAudioFileToAttach.getAudioFileId());
                attachedAudioFileCollectionNew.add(audioFileCollectionNewAudioFileToAttach);
            }
            audioFileCollectionNew = attachedAudioFileCollectionNew;
            campaign.setAudioFileCollection(audioFileCollectionNew);
            Collection<CallDetailRecord> attachedCallDetailRecordCollectionNew = new ArrayList<CallDetailRecord>();
            for (CallDetailRecord callDetailRecordCollectionNewCallDetailRecordToAttach : callDetailRecordCollectionNew) {
                callDetailRecordCollectionNewCallDetailRecordToAttach = em.getReference(callDetailRecordCollectionNewCallDetailRecordToAttach.getClass(), callDetailRecordCollectionNewCallDetailRecordToAttach.getCallDetailRecordPK());
                attachedCallDetailRecordCollectionNew.add(callDetailRecordCollectionNewCallDetailRecordToAttach);
            }
            callDetailRecordCollectionNew = attachedCallDetailRecordCollectionNew;
            campaign.setCallDetailRecordCollection(callDetailRecordCollectionNew);
            campaign = em.merge(campaign);
            if (catalogOld != null && !catalogOld.equals(catalogNew)) {
                catalogOld.getCampaignCollection().remove(campaign);
                catalogOld = em.merge(catalogOld);
            }
            if (catalogNew != null && !catalogNew.equals(catalogOld)) {
                catalogNew.getCampaignCollection().add(campaign);
                catalogNew = em.merge(catalogNew);
            }
            if (userOld != null && !userOld.equals(userNew)) {
                userOld.getCampaignCollection().remove(campaign);
                userOld = em.merge(userOld);
            }
            if (userNew != null && !userNew.equals(userOld)) {
                userNew.getCampaignCollection().add(campaign);
                userNew = em.merge(userNew);
            }
            for (AudioFile audioFileCollectionOldAudioFile : audioFileCollectionOld) {
                if (!audioFileCollectionNew.contains(audioFileCollectionOldAudioFile)) {
                    audioFileCollectionOldAudioFile.getCampaignCollection().remove(campaign);
                    audioFileCollectionOldAudioFile = em.merge(audioFileCollectionOldAudioFile);
                }
            }
            for (AudioFile audioFileCollectionNewAudioFile : audioFileCollectionNew) {
                if (!audioFileCollectionOld.contains(audioFileCollectionNewAudioFile)) {
                    audioFileCollectionNewAudioFile.getCampaignCollection().add(campaign);
                    audioFileCollectionNewAudioFile = em.merge(audioFileCollectionNewAudioFile);
                }
            }
            for (CallDetailRecord callDetailRecordCollectionNewCallDetailRecord : callDetailRecordCollectionNew) {
                if (!callDetailRecordCollectionOld.contains(callDetailRecordCollectionNewCallDetailRecord)) {
                    Campaign oldCampaign1OfCallDetailRecordCollectionNewCallDetailRecord = callDetailRecordCollectionNewCallDetailRecord.getCampaign1();
                    callDetailRecordCollectionNewCallDetailRecord.setCampaign1(campaign);
                    callDetailRecordCollectionNewCallDetailRecord = em.merge(callDetailRecordCollectionNewCallDetailRecord);
                    if (oldCampaign1OfCallDetailRecordCollectionNewCallDetailRecord != null && !oldCampaign1OfCallDetailRecordCollectionNewCallDetailRecord.equals(campaign)) {
                        oldCampaign1OfCallDetailRecordCollectionNewCallDetailRecord.getCallDetailRecordCollection().remove(callDetailRecordCollectionNewCallDetailRecord);
                        oldCampaign1OfCallDetailRecordCollectionNewCallDetailRecord = em.merge(oldCampaign1OfCallDetailRecordCollectionNewCallDetailRecord);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = campaign.getCampaignId();
                if (findCampaign(id) == null) {
                    throw new NonexistentEntityException("The campaign with id " + id + " no longer exists.");
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
            Campaign campaign;
            try {
                campaign = em.getReference(Campaign.class, id);
                campaign.getCampaignId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The campaign with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<CallDetailRecord> callDetailRecordCollectionOrphanCheck = campaign.getCallDetailRecordCollection();
            for (CallDetailRecord callDetailRecordCollectionOrphanCheckCallDetailRecord : callDetailRecordCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Campaign (" + campaign + ") cannot be destroyed since the CallDetailRecord " + callDetailRecordCollectionOrphanCheckCallDetailRecord + " in its callDetailRecordCollection field has a non-nullable campaign1 field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Catalog catalog = campaign.getCatalog();
            if (catalog != null) {
                catalog.getCampaignCollection().remove(campaign);
                catalog = em.merge(catalog);
            }
            User user = campaign.getUser();
            if (user != null) {
                user.getCampaignCollection().remove(campaign);
                user = em.merge(user);
            }
            Collection<AudioFile> audioFileCollection = campaign.getAudioFileCollection();
            for (AudioFile audioFileCollectionAudioFile : audioFileCollection) {
                audioFileCollectionAudioFile.getCampaignCollection().remove(campaign);
                audioFileCollectionAudioFile = em.merge(audioFileCollectionAudioFile);
            }
            em.remove(campaign);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Campaign> findCampaignEntities() {
        return findCampaignEntities(true, -1, -1);
    }

    public List<Campaign> findCampaignEntities(int maxResults, int firstResult) {
        return findCampaignEntities(false, maxResults, firstResult);
    }

    private List<Campaign> findCampaignEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Campaign.class));
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

    public Campaign findCampaign(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Campaign.class, id);
        } finally {
            em.close();
        }
    }

    public int getCampaignCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Campaign> rt = cq.from(Campaign.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
