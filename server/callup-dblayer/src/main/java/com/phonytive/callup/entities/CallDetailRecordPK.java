// Astive, is the core library of Astive Toolkit, the framework for
// developers wishing to create concise and easy to maintain applications
// for Asterisk® PBX, even for complex navigation.
//
// Copyright (C) 2010-2011 PhonyTive, S.L.
// http://www.phonytive.com/astive
//
// This file is part of Astive
//
// Astive is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// Astive is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with Astive.  If not, see <http://www.gnu.org/licenses/>.
package com.phonytive.callup.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Pedro Sanders <psanders@kaffeineminds.com>
 * @since 0.1
 * @version $Id$
 */
@Embeddable
public class CallDetailRecordPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "callDetailRecordId", nullable = false)
    private int callDetailRecordId;
    @Basic(optional = false)
    @Column(name = "subscriber", nullable = false)
    private int subscriber;
    @Basic(optional = false)
    @Column(name = "campaign", nullable = false)
    private int campaign;

    public CallDetailRecordPK() {
    }

    public CallDetailRecordPK(int callDetailRecordId, int subscriber, int campaign) {
        this.callDetailRecordId = callDetailRecordId;
        this.subscriber = subscriber;
        this.campaign = campaign;
    }

    public int getCallDetailRecordId() {
        return callDetailRecordId;
    }

    public void setCallDetailRecordId(int callDetailRecordId) {
        this.callDetailRecordId = callDetailRecordId;
    }

    public int getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(int subscriber) {
        this.subscriber = subscriber;
    }

    public int getCampaign() {
        return campaign;
    }

    public void setCampaign(int campaign) {
        this.campaign = campaign;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) callDetailRecordId;
        hash += (int) subscriber;
        hash += (int) campaign;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CallDetailRecordPK)) {
            return false;
        }
        CallDetailRecordPK other = (CallDetailRecordPK) object;
        if (this.callDetailRecordId != other.callDetailRecordId) {
            return false;
        }
        if (this.subscriber != other.subscriber) {
            return false;
        }
        if (this.campaign != other.campaign) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.phonytive.callup.entities.CallDetailRecordPK[ callDetailRecordId=" + callDetailRecordId + ", subscriber=" + subscriber + ", campaign=" + campaign + " ]";
    }

}
