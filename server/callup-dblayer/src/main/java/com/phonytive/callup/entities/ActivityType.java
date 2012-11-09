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
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "ActivityType")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ActivityType.findAll", query = "SELECT a FROM ActivityType a"),
    @NamedQuery(name = "ActivityType.findByActivityTypeId", query = "SELECT a FROM ActivityType a WHERE a.activityTypeId = :activityTypeId"),
    @NamedQuery(name = "ActivityType.findByName", query = "SELECT a FROM ActivityType a WHERE a.name = :name"),
    @NamedQuery(name = "ActivityType.findByDescription", query = "SELECT a FROM ActivityType a WHERE a.description = :description")})
public class ActivityType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "activityTypeId", nullable = false)
    private Integer activityTypeId;
    @Column(name = "name", length = 45)
    private String name;
    @Column(name = "description", length = 45)
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "activityType")
    private Collection<Activity> activityCollection;

    public ActivityType() {
    }

    public ActivityType(Integer activityTypeId) {
        this.activityTypeId = activityTypeId;
    }

    public Integer getActivityTypeId() {
        return activityTypeId;
    }

    public void setActivityTypeId(Integer activityTypeId) {
        this.activityTypeId = activityTypeId;
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
    public Collection<Activity> getActivityCollection() {
        return activityCollection;
    }

    public void setActivityCollection(Collection<Activity> activityCollection) {
        this.activityCollection = activityCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (activityTypeId != null ? activityTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActivityType)) {
            return false;
        }
        ActivityType other = (ActivityType) object;
        if ((this.activityTypeId == null && other.activityTypeId != null) || (this.activityTypeId != null && !this.activityTypeId.equals(other.activityTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.phonytive.callup.entities.ActivityType[ activityTypeId=" + activityTypeId + " ]";
    }

}
