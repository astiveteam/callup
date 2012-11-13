/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phonytive.callup.api.test;

import com.phonytive.callup.PUSelector;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author psanders
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({UsersAPITest.class})
public class AllTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        PUSelector.setNamePU("CallupH2TestPU");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
}
