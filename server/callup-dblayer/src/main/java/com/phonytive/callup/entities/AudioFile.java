package com.phonytive.callup.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "AudioFile", catalog = "callup", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AudioFile.findAll", query = "SELECT a FROM AudioFile a"),
    @NamedQuery(name = "AudioFile.findByAudioFileId", query = "SELECT a FROM AudioFile a WHERE a.audioFileId = :audioFileId"),
    @NamedQuery(name = "AudioFile.findByName", query = "SELECT a FROM AudioFile a WHERE a.name = :name"),
    @NamedQuery(name = "AudioFile.findByTime", query = "SELECT a FROM AudioFile a WHERE a.time = :time"),
    @NamedQuery(name = "AudioFile.findByFormat", query = "SELECT a FROM AudioFile a WHERE a.format = :format"),
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
    @Column(name = "format", length = 11)
    private String format;
    @Column(name = "description", length = 45)
    private String description;
    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Column(name = "gender", length = 11)
    private String gender;
    @Column(name = "hide", length = 45)
    private String hide;
    @JoinTable(name = "AudioFile_has_User", joinColumns = {
        @JoinColumn(name = "AudioFile", referencedColumnName = "audioFileId", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "User", referencedColumnName = "userId", nullable = false)})
    @ManyToMany
    private List<User> userList;
    @ManyToMany(mappedBy = "audioFileList")
    private List<Campaign> campaignList;

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

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
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

    public String getHide() {
        return hide;
    }

    public void setHide(String hide) {
        this.hide = hide;
    }

    @XmlTransient
    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @XmlTransient
    public List<Campaign> getCampaignList() {
        return campaignList;
    }

    public void setCampaignList(List<Campaign> campaignList) {
        this.campaignList = campaignList;
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
