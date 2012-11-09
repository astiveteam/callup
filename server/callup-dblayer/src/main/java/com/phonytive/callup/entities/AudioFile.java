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
@Table(name = "AudioFile")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AudioFile.findAll", query = "SELECT a FROM AudioFile a"),
    @NamedQuery(name = "AudioFile.findByAudioFileId", query = "SELECT a FROM AudioFile a WHERE a.audioFileId = :audioFileId"),
    @NamedQuery(name = "AudioFile.findByName", query = "SELECT a FROM AudioFile a WHERE a.name = :name"),
    @NamedQuery(name = "AudioFile.findByTime", query = "SELECT a FROM AudioFile a WHERE a.time = :time"),
    @NamedQuery(name = "AudioFile.findBySupportedFormat", query = "SELECT a FROM AudioFile a WHERE a.supportedFormat = :supportedFormat"),
    @NamedQuery(name = "AudioFile.findByDescription", query = "SELECT a FROM AudioFile a WHERE a.description = :description"),
    @NamedQuery(name = "AudioFile.findByCreated", query = "SELECT a FROM AudioFile a WHERE a.created = :created"),
    @NamedQuery(name = "AudioFile.findByGender", query = "SELECT a FROM AudioFile a WHERE a.gender = :gender"),
    @NamedQuery(name = "AudioFile.findByHide", query = "SELECT a FROM AudioFile a WHERE a.hide = :hide")})
public class AudioFile implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "audioFileId", nullable = false)
    private Integer audioFileId;
    @Column(name = "name", length = 45)
    private String name;
    @Column(name = "time")
    private Integer time;
    @Column(name = "supportedFormat", length = 11)
    private String supportedFormat;
    @Column(name = "description", length = 45)
    private String description;
    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Column(name = "gender", length = 11)
    private String gender;
    @Column(name = "hide")
    private Boolean hide;
    @ManyToMany(mappedBy = "audioFileCollection")
    private Collection<User> userCollection;
    @ManyToMany(mappedBy = "audioFileCollection")
    private Collection<Campaign> campaignCollection;

    public AudioFile() {
    }

    public AudioFile(Integer audioFileId) {
        this.audioFileId = audioFileId;
    }

    public Integer getAudioFileId() {
        return audioFileId;
    }

    public void setAudioFileId(Integer audioFileId) {
        this.audioFileId = audioFileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getSupportedFormat() {
        return supportedFormat;
    }

    public void setSupportedFormat(String supportedFormat) {
        this.supportedFormat = supportedFormat;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Boolean getHide() {
        return hide;
    }

    public void setHide(Boolean hide) {
        this.hide = hide;
    }

    @XmlTransient
    public Collection<User> getUserCollection() {
        return userCollection;
    }

    public void setUserCollection(Collection<User> userCollection) {
        this.userCollection = userCollection;
    }

    @XmlTransient
    public Collection<Campaign> getCampaignCollection() {
        return campaignCollection;
    }

    public void setCampaignCollection(Collection<Campaign> campaignCollection) {
        this.campaignCollection = campaignCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (audioFileId != null ? audioFileId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AudioFile)) {
            return false;
        }
        AudioFile other = (AudioFile) object;
        if ((this.audioFileId == null && other.audioFileId != null) || (this.audioFileId != null && !this.audioFileId.equals(other.audioFileId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.phonytive.callup.entities.AudioFile[ audioFileId=" + audioFileId + " ]";
    }

}
