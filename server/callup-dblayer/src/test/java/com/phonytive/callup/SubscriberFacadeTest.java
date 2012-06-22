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
package com.phonytive.callup;

import com.phonytive.callup.db.entities.Subscriber;
import com.phonytive.callup.db.entities.Tag;
import com.phonytive.callup.db.entities.User;
import com.phonytive.callup.db.facades.SubscriberFacade;
import com.phonytive.callup.db.facades.UserFacade;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import junit.framework.TestCase;

/**
 *
 * @since 1.0.0
 */
public class SubscriberFacadeTest extends TestCase {

    public SubscriberFacadeTest(String testName) {
        super(testName);
    }

    public void testUserFacade() throws UnknownHostException {
        SubscriberFacade sf = new SubscriberFacade();
        UserFacade uf = new UserFacade();        

        // Ensure its empty
        assertTrue(sf.findAll().isEmpty());

        // Create new user
        User u = new User();
        u.setName("Jhonny");
        uf.create(u);
        
        // Create tag
        Tag t = new Tag();
        t.setName("my-tag");
        
        // Create subscriber
        List<Tag> tagList = new ArrayList();
        tagList.add(t);        
        
        Subscriber s = new Subscriber();
        s.setTags(tagList);
        s.setUser(u);
        sf.create(s);

        // Should be one entry        
        assertEquals(sf.findAll().size(), 1);

        // Removing users
        List<Subscriber> subs = sf.findAll();
        for (Subscriber curSub : subs) {
            sf.remove(curSub);
        }

        // Should be empty again
        assertTrue(sf.findAll().isEmpty());
    }
}
