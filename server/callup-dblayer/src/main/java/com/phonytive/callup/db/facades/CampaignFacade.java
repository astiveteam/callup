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
package com.phonytive.callup.db.facades;

import com.phonytive.callup.db.EntityManager;
import com.phonytive.callup.db.PersistenceContext;
import com.phonytive.callup.db.entities.Campaign;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @since 1.0.0
 */
public class CampaignFacade {

    @PersistenceContext
    private EntityManager em = new EntityManager();

    public void create(Campaign campaign) {
        em.persist(campaign);
    }

    public void edit(Campaign campaign) {
        em.merge(campaign);
    }

    public void remove(Campaign campaign) {
        em.remove(campaign);
    }

    public Campaign find(ObjectId id) {
        return (Campaign) em.findOne(Campaign.class, id);
    }

    public List<Campaign> findAll() {
        return em.find(Campaign.class);
    }
}