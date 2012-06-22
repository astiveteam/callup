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
import com.phonytive.callup.db.entities.User;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @since 1.0.0
 */
public class UserFacade {

    @PersistenceContext
    private EntityManager em = new EntityManager();

    public void create(User user) {
        em.persist(user);
    }

    public void edit(User user) {
        em.merge(user);
    }

    public void remove(User user) {
        em.remove(user);
    }

    public User find(ObjectId id) {
        return (User) em.findOne(User.class, id);
    }

    public List<User> findAll() {
        return em.find(User.class);
    }
}
