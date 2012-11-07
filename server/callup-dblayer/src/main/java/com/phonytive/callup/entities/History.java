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
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Pedro Sanders <psanders@kaffeineminds.com>
 * @since 0.1
 * @version $Id$
 */
@Entity
@Table(name = "History", catalog = "callup", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "History.findAll", query = "SELECT h FROM History h"),
    @NamedQuery(name = "History.findByHistoryId", query = "SELECT h FROM History h WHERE h.historyPK.historyId = :historyId"),
    @NamedQuery(name = "History.findByDescription", query = "SELECT h FROM History h WHERE h.description = :description"),
    @NamedQuery(name = "History.findByUseruserId", query = "SELECT h FROM History h WHERE h.historyPK.useruserId = :useruserId")})
public class History implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected HistoryPK historyPK;
    @Column(name = "description", length = 45)
    private String description;
    @JoinColumn(name = "HistoryType", referencedColumnName = "historyTypeId", nullable = false)
    @ManyToOne(optional = false)
    private HistoryType historyType;
    @JoinColumn(name = "User_userId", referencedColumnName = "userId", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user;

    public History() {
    }

    public History(HistoryPK historyPK) {
        this.historyPK = historyPK;
    }

    public History(int historyId, int useruserId) {
        this.historyPK = new HistoryPK(historyId, useruserId);
    }

    public HistoryPK getHistoryPK() {
        return historyPK;
    }

    public void setHistoryPK(HistoryPK historyPK) {
        this.historyPK = historyPK;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HistoryType getHistoryType() {
        return historyType;
    }

    public void setHistoryType(HistoryType historyType) {
        this.historyType = historyType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (historyPK != null ? historyPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof History)) {
            return false;
        }
        History other = (History) object;
        if ((this.historyPK == null && other.historyPK != null) || (this.historyPK != null && !this.historyPK.equals(other.historyPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.phonytive.callup.entities.History[ historyPK=" + historyPK + " ]";
    }

}
