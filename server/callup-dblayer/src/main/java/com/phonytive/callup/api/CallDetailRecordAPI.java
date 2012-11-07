package com.phonytive.callup.api;

import com.phonytive.callup.entities.CallDetailRecord;
import com.phonytive.callup.entities.Subscriber;
import com.phonytive.callup.entities.User;
import java.util.Date;
import java.util.List;


public interface CallDetailRecordAPI {

    List<CallDetailRecord> getCallDetailRecord(Subscriber subscriber, Date from, Date to);
    
    List<CallDetailRecord> getCallDetailRecord(User user, Date from, Date to);
    
}
