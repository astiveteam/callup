package com.phonytive.callup;

import com.phonytive.callup.db.CallupAPIImpl;
import com.phonytive.callup.db.entities.Campaign;
import java.util.Date;
import junit.framework.TestCase;
import org.bson.types.ObjectId;

/**
 * 
 * @since 1.0.0
 */
public class CallupAPITest extends TestCase {

    public CallupAPITest(String testName) {
        super(testName);
    }    
    
    public void testAddUser() {
        
        /*ObjectId u = new ObjectId();
        u.setId(new ObjectId("4fa6a07444aef1d0302b5ed3"));
        u.setName("Te amo Raysa");
        
        CallupAPIImpl cai = new CallupAPIImpl();
        cai.addUser(u);
        
        Campaign c = new Campaign();
        c.setCallerId("Campaign Name");
        c.setCreated(new Date());
        c.setMaxRetries(3);
        c.setRetryTime(300);
        c.setUser(u);
        cai.addCampaign(c);
        
        for(Campaign cc: cai.getCampaings(u)) {
            System.out.println(cc.getCallerId());
        }*/
    }
}
