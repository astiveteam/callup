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
import java.util.Date;
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
@Table(name = "List", catalog = "callup", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "List.findAll", query = "SELECT l FROM List l"),
    @NamedQuery(name = "List.findByListId", query = "SELECT l FROM List l WHERE l.listId = :listId"),
    @NamedQuery(name = "List.findByName", query = "SELECT l FROM List l WHERE l.name = :name"),
    @NamedQuery(name = "List.findByCreated", query = "SELECT l FROM List l WHERE l.created = :created"),
    @NamedQuery(name = "List.findByUpdated", query = "SELECT l FROM List l WHERE l.updated = :updated")})
public class List implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "listId", nullable = false)
    private Integer listId;
    @Column(name = "name", length = 45)
    private String name;
    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Column(name = "updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    @ManyToMany(mappedBy = "listList")
    private java.util.List<Subscriber> subscriberList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "list")
    private java.util.List<Campaign> campaignList;
    @JoinColumn(name = "listId", referencedColumnName = "userId", nullable = false, insertable = false, updatable = false)
    @OneToOne(optional = false)
    private User user;

    public List() {
    }

    public List(Integer listId) {
        this.listId = listId;
    }

    public Integer getListId() {
        return listId;
    }

    public void setListId(Integer listId) {
        this.listId = listId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @XmlTransient
    public java.util.List<Subscriber> getSubscriberList() {
        return subscriberList;
    }

    public void setSubscriberList(java.util.List<Subscriber> subscriberList) {
        this.subscriberList = subscriberList;
    }

    @XmlTransient
    public java.util.List<Campaign> getCampaignList() {
        return campaignList;
    }

    public void setCampaignList(java.util.List<Campaign> campaignList) {
        this.campaignList = campaignList;
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
        hash += (listId != null ? listId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof List)) {
            return false;
        }
        List other = (List) object;
        if ((this.listId == null && other.listId != null) || (this.listId != null && !this.listId.equals(other.listId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.phonytive.callup.entities.List[ listId=" + listId + " ]";
    }

}
