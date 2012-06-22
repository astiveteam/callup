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

import com.phonytive.callup.db.entities.Campaign;
import com.phonytive.callup.db.entities.Subscriber;
import com.phonytive.callup.db.entities.Tag;
import com.phonytive.callup.db.entities.User;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @since 1.0.0
 */
public interface CallupAPI {

    public void addUser(User user);

    public void addTag(User user, String tag);

    public void addTagToSubscriber(Subscriber subscriber, Tag tag);

    public void addCampaign(Campaign campaign);

    public void editUser(User user);

    public void editTag(Tag tag);
    
    public void editCampaign(Campaign campaign);

    public User getUser(ObjectId id);
    
    public List<User> getUsers();
        
    public Campaign getCampaing(ObjectId id);

    public List<Campaign> getCampaings(User user);
    
    public Subscriber getSubscriber(ObjectId id);

    public List<Subscriber> getSubscribers(User user);

    public void deleteUser(User user);
    
    public void deleteTag(User user, String tag);
    
    public void deleteSubscriberTag(Subscriber subscriber, String tag);

    public void deleteSubscriber(Subscriber subscriber);
    
    public void deleteCampaign(Campaign campaign);
    
    public void deleteCampaigns(User user);
}
