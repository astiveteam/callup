package com.phonytive.callup.api.test;

import com.phonytive.callup.controllers.UserJpaController;
import com.phonytive.callup.entities.User;
import com.phonytive.callup.exceptions.PreexistingEntityException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author psanders
 */
public class AudioFilesAPITest extends TestCase {
    
    public AudioFilesAPITest(String testName) {
        super(testName);
    }

    public void testHello() throws PreexistingEntityException, Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CallupPU");
        UserJpaController ujc = new UserJpaController(emf);
        User user = new User();
        user.setName("Jhon Doe");
        ujc.create(user);
        assert(1 == 1);
    }
}
