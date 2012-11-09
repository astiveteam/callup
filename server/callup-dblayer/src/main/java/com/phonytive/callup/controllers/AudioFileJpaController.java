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

import com.phonytive.callup.entities.AudioFile;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.phonytive.callup.entities.User;
import java.util.ArrayList;
import java.util.Collection;
import com.phonytive.callup.entities.Campaign;
import com.phonytive.callup.exceptions.NonexistentEntityException;
import com.phonytive.callup.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class AudioFileJpaController implements Serializable {

    public AudioFileJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AudioFile audioFile) throws PreexistingEntityException, Exception {
        if (audioFile.getUserCollection() == null) {
            audioFile.setUserCollection(new ArrayList<User>());
        }
        if (audioFile.getCampaignCollection() == null) {
            audioFile.setCampaignCollection(new ArrayList<Campaign>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<User> attachedUserCollection = new ArrayList<User>();
            for (User userCollectionUserToAttach : audioFile.getUserCollection()) {
                userCollectionUserToAttach = em.getReference(userCollectionUserToAttach.getClass(), userCollectionUserToAttach.getUserId());
                attachedUserCollection.add(userCollectionUserToAttach);
            }
            audioFile.setUserCollection(attachedUserCollection);
            Collection<Campaign> attachedCampaignCollection = new ArrayList<Campaign>();
            for (Campaign campaignCollectionCampaignToAttach : audioFile.getCampaignCollection()) {
                campaignCollectionCampaignToAttach = em.getReference(campaignCollectionCampaignToAttach.getClass(), campaignCollectionCampaignToAttach.getCampaignId());
                attachedCampaignCollection.add(campaignCollectionCampaignToAttach);
            }
            audioFile.setCampaignCollection(attachedCampaignCollection);
            em.persist(audioFile);
            for (User userCollectionUser : audioFile.getUserCollection()) {
                userCollectionUser.getAudioFileCollection().add(audioFile);
                userCollectionUser = em.merge(userCollectionUser);
            }
            for (Campaign campaignCollectionCampaign : audioFile.getCampaignCollection()) {
                campaignCollectionCampaign.getAudioFileCollection().add(audioFile);
                campaignCollectionCampaign = em.merge(campaignCollectionCampaign);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAudioFile(audioFile.getAudioFileId()) != null) {
                throw new PreexistingEntityException("AudioFile " + audioFile + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AudioFile audioFile) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AudioFile persistentAudioFile = em.find(AudioFile.class, audioFile.getAudioFileId());
            Collection<User> userCollectionOld = persistentAudioFile.getUserCollection();
            Collection<User> userCollectionNew = audioFile.getUserCollection();
            Collection<Campaign> campaignCollectionOld = persistentAudioFile.getCampaignCollection();
            Collection<Campaign> campaignCollectionNew = audioFile.getCampaignCollection();
            Collection<User> attachedUserCollectionNew = new ArrayList<User>();
            for (User userCollectionNewUserToAttach : userCollectionNew) {
                userCollectionNewUserToAttach = em.getReference(userCollectionNewUserToAttach.getClass(), userCollectionNewUserToAttach.getUserId());
                attachedUserCollectionNew.add(userCollectionNewUserToAttach);
            }
            userCollectionNew = attachedUserCollectionNew;
            audioFile.setUserCollection(userCollectionNew);
            Collection<Campaign> attachedCampaignCollectionNew = new ArrayList<Campaign>();
            for (Campaign campaignCollectionNewCampaignToAttach : campaignCollectionNew) {
                campaignCollectionNewCampaignToAttach = em.getReference(campaignCollectionNewCampaignToAttach.getClass(), campaignCollectionNewCampaignToAttach.getCampaignId());
                attachedCampaignCollectionNew.add(campaignCollectionNewCampaignToAttach);
            }
            campaignCollectionNew = attachedCampaignCollectionNew;
            audioFile.setCampaignCollection(campaignCollectionNew);
            audioFile = em.merge(audioFile);
            for (User userCollectionOldUser : userCollectionOld) {
                if (!userCollectionNew.contains(userCollectionOldUser)) {
                    userCollectionOldUser.getAudioFileCollection().remove(audioFile);
                    userCollectionOldUser = em.merge(userCollectionOldUser);
                }
            }
            for (User userCollectionNewUser : userCollectionNew) {
                if (!userCollectionOld.contains(userCollectionNewUser)) {
                    userCollectionNewUser.getAudioFileCollection().add(audioFile);
                    userCollectionNewUser = em.merge(userCollectionNewUser);
                }
            }
            for (Campaign campaignCollectionOldCampaign : campaignCollectionOld) {
                if (!campaignCollectionNew.contains(campaignCollectionOldCampaign)) {
                    campaignCollectionOldCampaign.getAudioFileCollection().remove(audioFile);
                    campaignCollectionOldCampaign = em.merge(campaignCollectionOldCampaign);
                }
            }
            for (Campaign campaignCollectionNewCampaign : campaignCollectionNew) {
                if (!campaignCollectionOld.contains(campaignCollectionNewCampaign)) {
                    campaignCollectionNewCampaign.getAudioFileCollection().add(audioFile);
                    campaignCollectionNewCampaign = em.merge(campaignCollectionNewCampaign);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = audioFile.getAudioFileId();
                if (findAudioFile(id) == null) {
                    throw new NonexistentEntityException("The audioFile with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AudioFile audioFile;
            try {
                audioFile = em.getReference(AudioFile.class, id);
                audioFile.getAudioFileId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The audioFile with id " + id + " no longer exists.", enfe);
            }
            Collection<User> userCollection = audioFile.getUserCollection();
            for (User userCollectionUser : userCollection) {
                userCollectionUser.getAudioFileCollection().remove(audioFile);
                userCollectionUser = em.merge(userCollectionUser);
            }
            Collection<Campaign> campaignCollection = audioFile.getCampaignCollection();
            for (Campaign campaignCollectionCampaign : campaignCollection) {
                campaignCollectionCampaign.getAudioFileCollection().remove(audioFile);
                campaignCollectionCampaign = em.merge(campaignCollectionCampaign);
            }
            em.remove(audioFile);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AudioFile> findAudioFileEntities() {
        return findAudioFileEntities(true, -1, -1);
    }

    public List<AudioFile> findAudioFileEntities(int maxResults, int firstResult) {
        return findAudioFileEntities(false, maxResults, firstResult);
    }

    private List<AudioFile> findAudioFileEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AudioFile.class));
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

    public AudioFile findAudioFile(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AudioFile.class, id);
        } finally {
            em.close();
        }
    }

    public int getAudioFileCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AudioFile> rt = cq.from(AudioFile.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
