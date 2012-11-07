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
public class HistoryPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "historyId", nullable = false)
    private int historyId;
    @Basic(optional = false)
    @Column(name = "User_userId", nullable = false)
    private int useruserId;

    public HistoryPK() {
    }

    public HistoryPK(int historyId, int useruserId) {
        this.historyId = historyId;
        this.useruserId = useruserId;
    }

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    public int getUseruserId() {
        return useruserId;
    }

    public void setUseruserId(int useruserId) {
        this.useruserId = useruserId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) historyId;
        hash += (int) useruserId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HistoryPK)) {
            return false;
        }
        HistoryPK other = (HistoryPK) object;
        if (this.historyId != other.historyId) {
            return false;
        }
        if (this.useruserId != other.useruserId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.phonytive.callup.entities.HistoryPK[ historyId=" + historyId + ", useruserId=" + useruserId + " ]";
    }

}
