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
package com.phonytive.callup.api;

import com.phonytive.callup.controllers.CallDetailRecordJpaController;
import com.phonytive.callup.controllers.CampaignJpaController;
import com.phonytive.callup.controllers.SubscriberJpaController;
import com.phonytive.callup.controllers.UserJpaController;
import com.phonytive.callup.entities.CallDetailRecord;
import com.phonytive.callup.entities.Campaign;
import com.phonytive.callup.entities.Subscriber;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.EntityManagerFactory;

public class CallDetailRecordsAPI {

    private EntityManagerFactory emf = null;
    CallDetailRecordJpaController cdrjc;
    CampaignJpaController cjc;
    UserJpaController ujc;   
    SubscriberJpaController sjc;
            
    public CallDetailRecordsAPI() {        
        cdrjc = new CallDetailRecordJpaController(emf);
        cjc = new CampaignJpaController(emf);
        ujc = new UserJpaController(emf);
        sjc = new SubscriberJpaController(emf);
    }    
    
    public Collection<CallDetailRecord> getUserCDRs(Integer userId) {
        Collection<Campaign> campaigns = cjc.findCampaignEntities();
        Collection<CallDetailRecord> cdrs = new ArrayList();

        for(Campaign campaign: campaigns) {
            Collection<CallDetailRecord> cdrsColletion = 
                    campaign.getCallDetailRecordCollection();
            for(CallDetailRecord crd: cdrsColletion) {
                cdrs.add(crd);
            }
        }
        
        return cdrs;
    }
    
    // WARN: Not yet implemented
    public Collection<CallDetailRecord> getUserCDRs(Integer userId, Date from, Date to) {
        Campaign campaign = cjc.findCampaign(userId);
        return campaign.getCallDetailRecordCollection();
    }    

    public Collection<CallDetailRecord> getCampaignCDRs(Integer campaignId) {
        Campaign campaign = cjc.findCampaign(campaignId);
        return campaign.getCallDetailRecordCollection();
    }    
    
    // WARN: Not yet implemented
    public Collection<CallDetailRecord> getCampaignCDRs(Integer campaignId, Date from, Date to) {
        Campaign campaign = cjc.findCampaign(campaignId);
        return campaign.getCallDetailRecordCollection();
    }    
    
    public Collection<CallDetailRecord> getSubscriberCDRs(Integer subscriberId) {
        Subscriber subscriber = sjc.findSubscriber(subscriberId);
        return subscriber.getCallDetailRecordCollection();
    }    
    
    // WARN: Not yet implemented
    public Collection<CallDetailRecord> getSubscriberCDRs(Integer subscriberId, Date from, Date to) {
        Subscriber subscriber = sjc.findSubscriber(subscriberId);
        return subscriber.getCallDetailRecordCollection();
    }
    
}
