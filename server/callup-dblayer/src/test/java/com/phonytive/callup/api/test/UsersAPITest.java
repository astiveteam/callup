package com.phonytive.callup.api.test;

import com.phonytive.callup.PUSelector;
import com.phonytive.callup.controllers.UserJpaController;
import com.phonytive.callup.entities.User;
import com.phonytive.callup.exceptions.PreexistingEntityException;
import javax.persistence.EntityManagerFactory;
import junit.framework.TestCase;

/**
 *
 * @since 1.0.0
 */
public class UsersAPITest extends TestCase {

    public UsersAPITest(String testName) {
        super(testName);
    }

    public void testUserAPI() throws PreexistingEntityException, Exception {
        EntityManagerFactory emf = PUSelector.getEntityManager();
        UserJpaController ujc = new UserJpaController(emf);
        User user = new User();
        user.setUserId(new Integer("2"));
        user.setName("Jhon Doe");
        ujc.create(user);
        assert (1 == 1);
        assert (ujc.findUserEntities().size() == 1);

        user = ujc.findUser(new Integer("2"));
        assert (user.getName().equals("Jhon Doe"));
    }
}
