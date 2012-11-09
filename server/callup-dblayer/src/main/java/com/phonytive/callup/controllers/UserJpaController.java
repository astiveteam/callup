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

public class UserJpaController implements Serializable {

    public UserJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(User user) throws PreexistingEntityException, Exception {
        if (user.getAudioFileCollection() == null) {
            user.setAudioFileCollection(new ArrayList<AudioFile>());
        }
        if (user.getCampaignCollection() == null) {
            user.setCampaignCollection(new ArrayList<Campaign>());
        }
        if (user.getActivityCollection() == null) {
            user.setActivityCollection(new ArrayList<Activity>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Catalog catalog = user.getCatalog();
            if (catalog != null) {
                catalog = em.getReference(catalog.getClass(), catalog.getListId());
                user.setCatalog(catalog);
            }
            Collection<AudioFile> attachedAudioFileCollection = new ArrayList<AudioFile>();
            for (AudioFile audioFileCollectionAudioFileToAttach : user.getAudioFileCollection()) {
                audioFileCollectionAudioFileToAttach = em.getReference(audioFileCollectionAudioFileToAttach.getClass(), audioFileCollectionAudioFileToAttach.getAudioFileId());
                attachedAudioFileCollection.add(audioFileCollectionAudioFileToAttach);
            }
            user.setAudioFileCollection(attachedAudioFileCollection);
            Collection<Campaign> attachedCampaignCollection = new ArrayList<Campaign>();
            for (Campaign campaignCollectionCampaignToAttach : user.getCampaignCollection()) {
                campaignCollectionCampaignToAttach = em.getReference(campaignCollectionCampaignToAttach.getClass(), campaignCollectionCampaignToAttach.getCampaignId());
                attachedCampaignCollection.add(campaignCollectionCampaignToAttach);
            }
            user.setCampaignCollection(attachedCampaignCollection);
            Collection<Activity> attachedActivityCollection = new ArrayList<Activity>();
            for (Activity activityCollectionActivityToAttach : user.getActivityCollection()) {
                activityCollectionActivityToAttach = em.getReference(activityCollectionActivityToAttach.getClass(), activityCollectionActivityToAttach.getActivityPK());
                attachedActivityCollection.add(activityCollectionActivityToAttach);
            }
            user.setActivityCollection(attachedActivityCollection);
            em.persist(user);
            if (catalog != null) {
                User oldUserOfCatalog = catalog.getUser();
                if (oldUserOfCatalog != null) {
                    oldUserOfCatalog.setCatalog(null);
                    oldUserOfCatalog = em.merge(oldUserOfCatalog);
                }
                catalog.setUser(user);
                catalog = em.merge(catalog);
            }
            for (AudioFile audioFileCollectionAudioFile : user.getAudioFileCollection()) {
                audioFileCollectionAudioFile.getUserCollection().add(user);
                audioFileCollectionAudioFile = em.merge(audioFileCollectionAudioFile);
            }
            for (Campaign campaignCollectionCampaign : user.getCampaignCollection()) {
                User oldUserOfCampaignCollectionCampaign = campaignCollectionCampaign.getUser();
                campaignCollectionCampaign.setUser(user);
                campaignCollectionCampaign = em.merge(campaignCollectionCampaign);
                if (oldUserOfCampaignCollectionCampaign != null) {
                    oldUserOfCampaignCollectionCampaign.getCampaignCollection().remove(campaignCollectionCampaign);
                    oldUserOfCampaignCollectionCampaign = em.merge(oldUserOfCampaignCollectionCampaign);
                }
            }
            for (Activity activityCollectionActivity : user.getActivityCollection()) {
                User oldUserOfActivityCollectionActivity = activityCollectionActivity.getUser();
                activityCollectionActivity.setUser(user);
                activityCollectionActivity = em.merge(activityCollectionActivity);
                if (oldUserOfActivityCollectionActivity != null) {
                    oldUserOfActivityCollectionActivity.getActivityCollection().remove(activityCollectionActivity);
                    oldUserOfActivityCollectionActivity = em.merge(oldUserOfActivityCollectionActivity);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUser(user.getUserId()) != null) {
                throw new PreexistingEntityException("User " + user + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(User user) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User persistentUser = em.find(User.class, user.getUserId());
            Catalog catalogOld = persistentUser.getCatalog();
            Catalog catalogNew = user.getCatalog();
            Collection<AudioFile> audioFileCollectionOld = persistentUser.getAudioFileCollection();
            Collection<AudioFile> audioFileCollectionNew = user.getAudioFileCollection();
            Collection<Campaign> campaignCollectionOld = persistentUser.getCampaignCollection();
            Collection<Campaign> campaignCollectionNew = user.getCampaignCollection();
            Collection<Activity> activityCollectionOld = persistentUser.getActivityCollection();
            Collection<Activity> activityCollectionNew = user.getActivityCollection();
            List<String> illegalOrphanMessages = null;
            if (catalogOld != null && !catalogOld.equals(catalogNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Catalog " + catalogOld + " since its user field is not nullable.");
            }
            for (Campaign campaignCollectionOldCampaign : campaignCollectionOld) {
                if (!campaignCollectionNew.contains(campaignCollectionOldCampaign)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Campaign " + campaignCollectionOldCampaign + " since its user field is not nullable.");
                }
            }
            for (Activity activityCollectionOldActivity : activityCollectionOld) {
                if (!activityCollectionNew.contains(activityCollectionOldActivity)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Activity " + activityCollectionOldActivity + " since its user field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (catalogNew != null) {
                catalogNew = em.getReference(catalogNew.getClass(), catalogNew.getListId());
                user.setCatalog(catalogNew);
            }
            Collection<AudioFile> attachedAudioFileCollectionNew = new ArrayList<AudioFile>();
            for (AudioFile audioFileCollectionNewAudioFileToAttach : audioFileCollectionNew) {
                audioFileCollectionNewAudioFileToAttach = em.getReference(audioFileCollectionNewAudioFileToAttach.getClass(), audioFileCollectionNewAudioFileToAttach.getAudioFileId());
                attachedAudioFileCollectionNew.add(audioFileCollectionNewAudioFileToAttach);
            }
            audioFileCollectionNew = attachedAudioFileCollectionNew;
            user.setAudioFileCollection(audioFileCollectionNew);
            Collection<Campaign> attachedCampaignCollectionNew = new ArrayList<Campaign>();
            for (Campaign campaignCollectionNewCampaignToAttach : campaignCollectionNew) {
                campaignCollectionNewCampaignToAttach = em.getReference(campaignCollectionNewCampaignToAttach.getClass(), campaignCollectionNewCampaignToAttach.getCampaignId());
                attachedCampaignCollectionNew.add(campaignCollectionNewCampaignToAttach);
            }
            campaignCollectionNew = attachedCampaignCollectionNew;
            user.setCampaignCollection(campaignCollectionNew);
            Collection<Activity> attachedActivityCollectionNew = new ArrayList<Activity>();
            for (Activity activityCollectionNewActivityToAttach : activityCollectionNew) {
                activityCollectionNewActivityToAttach = em.getReference(activityCollectionNewActivityToAttach.getClass(), activityCollectionNewActivityToAttach.getActivityPK());
                attachedActivityCollectionNew.add(activityCollectionNewActivityToAttach);
            }
            activityCollectionNew = attachedActivityCollectionNew;
            user.setActivityCollection(activityCollectionNew);
            user = em.merge(user);
            if (catalogNew != null && !catalogNew.equals(catalogOld)) {
                User oldUserOfCatalog = catalogNew.getUser();
                if (oldUserOfCatalog != null) {
                    oldUserOfCatalog.setCatalog(null);
                    oldUserOfCatalog = em.merge(oldUserOfCatalog);
                }
                catalogNew.setUser(user);
                catalogNew = em.merge(catalogNew);
            }
            for (AudioFile audioFileCollectionOldAudioFile : audioFileCollectionOld) {
                if (!audioFileCollectionNew.contains(audioFileCollectionOldAudioFile)) {
                    audioFileCollectionOldAudioFile.getUserCollection().remove(user);
                    audioFileCollectionOldAudioFile = em.merge(audioFileCollectionOldAudioFile);
                }
            }
            for (AudioFile audioFileCollectionNewAudioFile : audioFileCollectionNew) {
                if (!audioFileCollectionOld.contains(audioFileCollectionNewAudioFile)) {
                    audioFileCollectionNewAudioFile.getUserCollection().add(user);
                    audioFileCollectionNewAudioFile = em.merge(audioFileCollectionNewAudioFile);
                }
            }
            for (Campaign campaignCollectionNewCampaign : campaignCollectionNew) {
                if (!campaignCollectionOld.contains(campaignCollectionNewCampaign)) {
                    User oldUserOfCampaignCollectionNewCampaign = campaignCollectionNewCampaign.getUser();
                    campaignCollectionNewCampaign.setUser(user);
                    campaignCollectionNewCampaign = em.merge(campaignCollectionNewCampaign);
                    if (oldUserOfCampaignCollectionNewCampaign != null && !oldUserOfCampaignCollectionNewCampaign.equals(user)) {
                        oldUserOfCampaignCollectionNewCampaign.getCampaignCollection().remove(campaignCollectionNewCampaign);
                        oldUserOfCampaignCollectionNewCampaign = em.merge(oldUserOfCampaignCollectionNewCampaign);
                    }
                }
            }
            for (Activity activityCollectionNewActivity : activityCollectionNew) {
                if (!activityCollectionOld.contains(activityCollectionNewActivity)) {
                    User oldUserOfActivityCollectionNewActivity = activityCollectionNewActivity.getUser();
                    activityCollectionNewActivity.setUser(user);
                    activityCollectionNewActivity = em.merge(activityCollectionNewActivity);
                    if (oldUserOfActivityCollectionNewActivity != null && !oldUserOfActivityCollectionNewActivity.equals(user)) {
                        oldUserOfActivityCollectionNewActivity.getActivityCollection().remove(activityCollectionNewActivity);
                        oldUserOfActivityCollectionNewActivity = em.merge(oldUserOfActivityCollectionNewActivity);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = user.getUserId();
                if (findUser(id) == null) {
                    throw new NonexistentEntityException("The user with id " + id + " no longer exists.");
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
            User user;
            try {
                user = em.getReference(User.class, id);
                user.getUserId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The user with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Catalog catalogOrphanCheck = user.getCatalog();
            if (catalogOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Catalog " + catalogOrphanCheck + " in its catalog field has a non-nullable user field.");
            }
            Collection<Campaign> campaignCollectionOrphanCheck = user.getCampaignCollection();
            for (Campaign campaignCollectionOrphanCheckCampaign : campaignCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Campaign " + campaignCollectionOrphanCheckCampaign + " in its campaignCollection field has a non-nullable user field.");
            }
            Collection<Activity> activityCollectionOrphanCheck = user.getActivityCollection();
            for (Activity activityCollectionOrphanCheckActivity : activityCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Activity " + activityCollectionOrphanCheckActivity + " in its activityCollection field has a non-nullable user field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<AudioFile> audioFileCollection = user.getAudioFileCollection();
            for (AudioFile audioFileCollectionAudioFile : audioFileCollection) {
                audioFileCollectionAudioFile.getUserCollection().remove(user);
                audioFileCollectionAudioFile = em.merge(audioFileCollectionAudioFile);
            }
            em.remove(user);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<User> findUserEntities() {
        return findUserEntities(true, -1, -1);
    }

    public List<User> findUserEntities(int maxResults, int firstResult) {
        return findUserEntities(false, maxResults, firstResult);
    }

    private List<User> findUserEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(User.class));
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

    public User findUser(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<User> rt = cq.from(User.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
