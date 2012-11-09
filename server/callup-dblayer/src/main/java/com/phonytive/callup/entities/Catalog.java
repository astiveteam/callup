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
package com.phonytive.callup.entities;

import java.io.Serializable;
import java.util.Collection;
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
@Table(name = "Catalog")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Catalog.findAll", query = "SELECT c FROM Catalog c"),
    @NamedQuery(name = "Catalog.findByListId", query = "SELECT c FROM Catalog c WHERE c.listId = :listId"),
    @NamedQuery(name = "Catalog.findByName", query = "SELECT c FROM Catalog c WHERE c.name = :name"),
    @NamedQuery(name = "Catalog.findByCreated", query = "SELECT c FROM Catalog c WHERE c.created = :created"),
    @NamedQuery(name = "Catalog.findByUpdated", query = "SELECT c FROM Catalog c WHERE c.updated = :updated")})
public class Catalog implements Serializable {
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
    @ManyToMany(mappedBy = "catalogCollection")
    private Collection<Subscriber> subscriberCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "catalog")
    private Collection<Campaign> campaignCollection;
    @JoinColumn(name = "listId", referencedColumnName = "userId", nullable = false, insertable = false, updatable = false)
    @OneToOne(optional = false)
    private User user;

    public Catalog() {
    }

    public Catalog(Integer listId) {
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
    public Collection<Subscriber> getSubscriberCollection() {
        return subscriberCollection;
    }

    public void setSubscriberCollection(Collection<Subscriber> subscriberCollection) {
        this.subscriberCollection = subscriberCollection;
    }

    @XmlTransient
    public Collection<Campaign> getCampaignCollection() {
        return campaignCollection;
    }

    public void setCampaignCollection(Collection<Campaign> campaignCollection) {
        this.campaignCollection = campaignCollection;
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
        if (!(object instanceof Catalog)) {
            return false;
        }
        Catalog other = (Catalog) object;
        if ((this.listId == null && other.listId != null) || (this.listId != null && !this.listId.equals(other.listId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.phonytive.callup.entities.Catalog[ listId=" + listId + " ]";
    }

}
