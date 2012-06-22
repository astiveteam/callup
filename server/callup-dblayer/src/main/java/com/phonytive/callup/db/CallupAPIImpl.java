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
package com.phonytive.callup.db;

import com.phonytive.callup.db.entities.*;
import com.phonytive.callup.db.facades.CampaignFacade;
import com.phonytive.callup.db.facades.SubscriberFacade;
import com.phonytive.callup.db.facades.UserFacade;
import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @since 1.0.0
 */
public class CallupAPIImpl implements CallupAPI {

    @PersistenceContext
    private EntityManager em;    
    private UserFacade uf;
    private CampaignFacade cf;
    private SubscriberFacade sf;    
    
    public CallupAPIImpl() {
        uf = new UserFacade();
        cf = new CampaignFacade();
        sf = new SubscriberFacade();
        em = new EntityManager();
    }
    
    @Override
    public void addUser(User user) {
        uf.create(user);
    }

    @Override
    public void addTag(User user, String tagName) {
        Tag tag = new Tag();
        tag.setName(tagName);
        tag.setCreated(new Date());
        List<Tag> tags = user.getTags();
        tags.add(tag);
        
        user.setTags(tags);
        uf.create(user);
    }

    @Override
    public void addTagToSubscriber(Subscriber subscriber, Tag tag) {
        List<Tag> tags = subscriber.getTags();
        tags.add(tag);
        subscriber.setTags(tags);
    }

    @Override
    public void addCampaign(Campaign campaign) {
        cf.create(campaign);
    }

    @Override
    public void editUser(User user) {
        uf.edit(user);
    }

    @Override
    public void editTag(Tag tag) {
        em.merge(tag);
    }

    @Override
    public void editCampaign(Campaign campaign) {
        cf.edit(campaign);
    }

    @Override
    public User getUser(ObjectId id) {
        return uf.find(id);
    }

    @Override
    public List<User> getUsers() {
        return uf.findAll();
    }

    @Override
    public Campaign getCampaing(ObjectId id) {
        return cf.find(id);
    }

    @Override
    public List<Campaign> getCampaings(User user) {
        return em.find(Campaign.class, "{user._id: 'Te amo Raysa'}");
    }

    @Override
    public Subscriber getSubscriber(ObjectId id) {
        return sf.find(id);
    }

    @Override
    public List<Subscriber> getSubscribers(User user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteUser(User user) {
        user.setStatus(UserStatus.REMOVED);
        uf.edit(user);
    }

    @Override
    public void deleteTag(User user, String tag) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteSubscriberTag(Subscriber subscriber, String tagName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteSubscriber(Subscriber subscriber) {
        subscriber.setStatus(SubscriberStatus.REMOVED);
        sf.edit(subscriber);
    }

    @Override
    public void deleteCampaign(Campaign campaign) {
        campaign.setStatus(CampaignStatus.REMOVED);
        cf.edit(campaign);
    }

    @Override
    public void deleteCampaigns(User user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
