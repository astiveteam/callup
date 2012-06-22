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

import com.phonytive.callup.db.entities.*;
import com.phonytive.callup.db.facades.CampaignFacade;
import com.phonytive.callup.db.facades.UserFacade;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import junit.framework.TestCase;

/**
 *
 * @since 1.0.0
 */
public class CampaignFacadeTest extends TestCase {

    public CampaignFacadeTest(String testName) {
        super(testName);
    }

    public void testCampaignFacade() throws UnknownHostException {
        
        //User u = new User();        
        //UserFacade uf = new UserFacade();
        //uf.create(u);
        
        CampaignFacade cf = new CampaignFacade();
        
        Question question = new Question();
        question.setAudio("question1");
                
        List<Question> questions = new ArrayList();
        questions.add(question);
        
        Message message = new Message();
        message.setAudio("intro");
        message.setQuestions(questions);
        
        Campaign c = new Campaign();
        c.setName("Campaign #1");
        c.setCallerId("Campaign #1");
        c.setFrom(new Date());
        c.setTo(new Date());
        c.setMaxRetries(3);
        c.setRetryTime(3);
        c.setMessage(message);
        c.setStatus(CampaignStatus.ACTIVE);
                
        cf.create(c);
    }
}
