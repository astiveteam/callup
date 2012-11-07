package com.phonytive.callup.api;

import com.phonytive.callup.AccountStatusEnum;
import com.phonytive.callup.CallupException;
import com.phonytive.callup.entities.billing.Account;

public interface AccountAPI {

    Account getAccount(Integer accountId);

    /**
     * 
     * @param account
     * @throws CallupException if accountId is duplicated
     */
    void createAccount(Account account) throws CallupException;

    void changeAccountStatus(Account account, AccountStatusEnum status) throws CallupException;    
}
