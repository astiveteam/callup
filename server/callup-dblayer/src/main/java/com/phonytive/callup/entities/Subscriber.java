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

@Entity
@Table(name = "Subscriber", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"externalId"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Subscriber.findAll", query = "SELECT s FROM Subscriber s"),
    @NamedQuery(name = "Subscriber.findBySubscriberId", query = "SELECT s FROM Subscriber s WHERE s.subscriberId = :subscriberId"),
    @NamedQuery(name = "Subscriber.findByExternalId", query = "SELECT s FROM Subscriber s WHERE s.externalId = :externalId"),
    @NamedQuery(name = "Subscriber.findByNumber", query = "SELECT s FROM Subscriber s WHERE s.number = :number"),
    @NamedQuery(name = "Subscriber.findByAddedOn", query = "SELECT s FROM Subscriber s WHERE s.addedOn = :addedOn"),
    @NamedQuery(name = "Subscriber.findByFirstName", query = "SELECT s FROM Subscriber s WHERE s.firstName = :firstName"),
    @NamedQuery(name = "Subscriber.findByMidName", query = "SELECT s FROM Subscriber s WHERE s.midName = :midName"),
    @NamedQuery(name = "Subscriber.findByLastName", query = "SELECT s FROM Subscriber s WHERE s.lastName = :lastName"),
    @NamedQuery(name = "Subscriber.findByDescription", query = "SELECT s FROM Subscriber s WHERE s.description = :description")})
public class Subscriber implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "subscriberId", nullable = false)
    private Integer subscriberId;
    @Basic(optional = false)
    @Column(name = "externalId", nullable = false, length = 45)
    private String externalId;
    @Column(name = "number", length = 45)
    private String number;
    @Column(name = "addedOn")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addedOn;
    @Column(name = "firstName", length = 45)
    private String firstName;
    @Column(name = "midName", length = 45)
    private String midName;
    @Column(name = "lastName", length = 45)
    private String lastName;
    @Column(name = "description", length = 45)
    private String description;
    @JoinTable(name = "SubscriberCatalog", joinColumns = {
        @JoinColumn(name = "subscriber", referencedColumnName = "subscriberId", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "list", referencedColumnName = "listId", nullable = false)})
    @ManyToMany
    private Collection<Catalog> catalogCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subscriber1")
    private Collection<CallDetailRecord> callDetailRecordCollection;

    public Subscriber() {
    }

    public Subscriber(Integer subscriberId) {
        this.subscriberId = subscriberId;
    }

    public Subscriber(Integer subscriberId, String externalId) {
        this.subscriberId = subscriberId;
        this.externalId = externalId;
    }

    public Integer getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(Integer subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(Date addedOn) {
        this.addedOn = addedOn;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMidName() {
        return midName;
    }

    public void setMidName(String midName) {
        this.midName = midName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public Collection<Catalog> getCatalogCollection() {
        return catalogCollection;
    }

    public void setCatalogCollection(Collection<Catalog> catalogCollection) {
        this.catalogCollection = catalogCollection;
    }

    @XmlTransient
    public Collection<CallDetailRecord> getCallDetailRecordCollection() {
        return callDetailRecordCollection;
    }

    public void setCallDetailRecordCollection(Collection<CallDetailRecord> callDetailRecordCollection) {
        this.callDetailRecordCollection = callDetailRecordCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (subscriberId != null ? subscriberId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Subscriber)) {
            return false;
        }
        Subscriber other = (Subscriber) object;
        if ((this.subscriberId == null && other.subscriberId != null) || (this.subscriberId != null && !this.subscriberId.equals(other.subscriberId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.phonytive.callup.entities.Subscriber[ subscriberId=" + subscriberId + " ]";
    }

}
