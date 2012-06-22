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

import com.phonytive.callup.db.entities.User;
import com.phonytive.callup.db.facades.UserFacade;
import java.net.UnknownHostException;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import junit.framework.TestCase;

/**
 *
 * @since 1.0.0
 */
public class UserFacadeTest extends TestCase {

    public UserFacadeTest(String testName) {
        super(testName);
    }

    public void testUserFacade() throws UnknownHostException {
        UserFacade uf = new UserFacade();

        // Ensure its empty
        assertTrue(uf.findAll().isEmpty());

        // Create new user
        User u = new User();
        u.setName("Jhonny");
        uf.create(u);

        // Should be one entry        
        assertEquals(uf.findAll().size(), 1);

        // Removing users
        List<User> users = uf.findAll();
        for (User curUser : users) {
            uf.remove(curUser);
        }

        // Should be empty again
        assertTrue(uf.findAll().isEmpty());
    }
}
