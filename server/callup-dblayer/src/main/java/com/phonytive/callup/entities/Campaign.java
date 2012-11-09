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
@Table(name = "Campaign")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Campaign.findAll", query = "SELECT c FROM Campaign c"),
    @NamedQuery(name = "Campaign.findByCampaignId", query = "SELECT c FROM Campaign c WHERE c.campaignId = :campaignId"),
    @NamedQuery(name = "Campaign.findByCallerId", query = "SELECT c FROM Campaign c WHERE c.callerId = :callerId"),
    @NamedQuery(name = "Campaign.findByName", query = "SELECT c FROM Campaign c WHERE c.name = :name"),
    @NamedQuery(name = "Campaign.findByMaxRetriesPerDay", query = "SELECT c FROM Campaign c WHERE c.maxRetriesPerDay = :maxRetriesPerDay"),
    @NamedQuery(name = "Campaign.findByMaxRetriesPerCampaign", query = "SELECT c FROM Campaign c WHERE c.maxRetriesPerCampaign = :maxRetriesPerCampaign"),
    @NamedQuery(name = "Campaign.findByFromDate", query = "SELECT c FROM Campaign c WHERE c.fromDate = :fromDate"),
    @NamedQuery(name = "Campaign.findByToDate", query = "SELECT c FROM Campaign c WHERE c.toDate = :toDate"),
    @NamedQuery(name = "Campaign.findByFromTime", query = "SELECT c FROM Campaign c WHERE c.fromTime = :fromTime"),
    @NamedQuery(name = "Campaign.findByToTime", query = "SELECT c FROM Campaign c WHERE c.toTime = :toTime"),
    @NamedQuery(name = "Campaign.findByStatus", query = "SELECT c FROM Campaign c WHERE c.status = :status"),
    @NamedQuery(name = "Campaign.findByCreated", query = "SELECT c FROM Campaign c WHERE c.created = :created"),
    @NamedQuery(name = "Campaign.findByUpdated", query = "SELECT c FROM Campaign c WHERE c.updated = :updated"),
    @NamedQuery(name = "Campaign.findByIncludeDays", query = "SELECT c FROM Campaign c WHERE c.includeDays = :includeDays"),
    @NamedQuery(name = "Campaign.findByType", query = "SELECT c FROM Campaign c WHERE c.type = :type")})
public class Campaign implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "campaignId", nullable = false)
    private Integer campaignId;
    @Column(name = "callerId", length = 45)
    private String callerId;
    @Column(name = "name", length = 45)
    private String name;
    @Column(name = "maxRetriesPerDay")
    private Integer maxRetriesPerDay;
    @Column(name = "maxRetriesPerCampaign")
    private Integer maxRetriesPerCampaign;
    @Column(name = "fromDate")
    @Temporal(TemporalType.DATE)
    private Date fromDate;
    @Column(name = "toDate")
    @Temporal(TemporalType.DATE)
    private Date toDate;
    @Column(name = "fromTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fromTime;
    @Column(name = "toTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date toTime;
    @Column(name = "status", length = 23)
    private String status;
    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Column(name = "updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    @Column(name = "includeDays", length = 45)
    private String includeDays;
    @Column(name = "type", length = 22)
    private String type;
    @Lob
    @Column(name = "dataXML", length = 2147483647)
    private String dataXML;
    @JoinTable(name = "CampaignAudioFiles", joinColumns = {
        @JoinColumn(name = "Campaign", referencedColumnName = "campaignId", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "AudioFile", referencedColumnName = "audioFileId", nullable = false)})
    @ManyToMany
    private Collection<AudioFile> audioFileCollection;
    @JoinColumn(name = "catalog", referencedColumnName = "listId", nullable = false)
    @ManyToOne(optional = false)
    private Catalog catalog;
    @JoinColumn(name = "user", referencedColumnName = "userId", nullable = false)
    @ManyToOne(optional = false)
    private User user;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "campaign1")
    private Collection<CallDetailRecord> callDetailRecordCollection;

    public Campaign() {
    }

    public Campaign(Integer campaignId) {
        this.campaignId = campaignId;
    }

    public Integer getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }

    public String getCallerId() {
        return callerId;
    }

    public void setCallerId(String callerId) {
        this.callerId = callerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxRetriesPerDay() {
        return maxRetriesPerDay;
    }

    public void setMaxRetriesPerDay(Integer maxRetriesPerDay) {
        this.maxRetriesPerDay = maxRetriesPerDay;
    }

    public Integer getMaxRetriesPerCampaign() {
        return maxRetriesPerCampaign;
    }

    public void setMaxRetriesPerCampaign(Integer maxRetriesPerCampaign) {
        this.maxRetriesPerCampaign = maxRetriesPerCampaign;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getFromTime() {
        return fromTime;
    }

    public void setFromTime(Date fromTime) {
        this.fromTime = fromTime;
    }

    public Date getToTime() {
        return toTime;
    }

    public void setToTime(Date toTime) {
        this.toTime = toTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getIncludeDays() {
        return includeDays;
    }

    public void setIncludeDays(String includeDays) {
        this.includeDays = includeDays;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDataXML() {
        return dataXML;
    }

    public void setDataXML(String dataXML) {
        this.dataXML = dataXML;
    }

    @XmlTransient
    public Collection<AudioFile> getAudioFileCollection() {
        return audioFileCollection;
    }

    public void setAudioFileCollection(Collection<AudioFile> audioFileCollection) {
        this.audioFileCollection = audioFileCollection;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
        hash += (campaignId != null ? campaignId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Campaign)) {
            return false;
        }
        Campaign other = (Campaign) object;
        if ((this.campaignId == null && other.campaignId != null) || (this.campaignId != null && !this.campaignId.equals(other.campaignId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.phonytive.callup.entities.Campaign[ campaignId=" + campaignId + " ]";
    }

}
