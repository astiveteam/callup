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
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Pedro Sanders <psanders@kaffeineminds.com>
 * @since 0.1
 * @version $Id$
 */
@Entity
@Table(name = "HistoryType", catalog = "callup", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HistoryType.findAll", query = "SELECT h FROM HistoryType h"),
    @NamedQuery(name = "HistoryType.findByHistoryTypeId", query = "SELECT h FROM HistoryType h WHERE h.historyTypeId = :historyTypeId"),
    @NamedQuery(name = "HistoryType.findByName", query = "SELECT h FROM HistoryType h WHERE h.name = :name"),
    @NamedQuery(name = "HistoryType.findByDescription", query = "SELECT h FROM HistoryType h WHERE h.description = :description")})
public class HistoryType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "historyTypeId", nullable = false)
    private Integer historyTypeId;
    @Column(name = "name", length = 45)
    private String name;
    @Column(name = "description", length = 45)
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "historyType")
    private List<History> historyList;

    public HistoryType() {
    }

    public HistoryType(Integer historyTypeId) {
        this.historyTypeId = historyTypeId;
    }

    public Integer getHistoryTypeId() {
        return historyTypeId;
    }

    public void setHistoryTypeId(Integer historyTypeId) {
        this.historyTypeId = historyTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public List<History> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<History> historyList) {
        this.historyList = historyList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (historyTypeId != null ? historyTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HistoryType)) {
            return false;
        }
        HistoryType other = (HistoryType) object;
        if ((this.historyTypeId == null && other.historyTypeId != null) || (this.historyTypeId != null && !this.historyTypeId.equals(other.historyTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.phonytive.callup.entities.HistoryType[ historyTypeId=" + historyTypeId + " ]";
    }

}
