package com.phonytive.callup.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "Campaign", catalog = "callup", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Campaign.findAll", query = "SELECT c FROM Campaign c"),
    @NamedQuery(name = "Campaign.findByCampaignId", query = "SELECT c FROM Campaign c WHERE c.campaignId = :campaignId"),
    @NamedQuery(name = "Campaign.findByCallerId", query = "SELECT c FROM Campaign c WHERE c.callerId = :callerId"),
    @NamedQuery(name = "Campaign.findByName", query = "SELECT c FROM Campaign c WHERE c.name = :name"),
    @NamedQuery(name = "Campaign.findByMaxRetriesPerDay", query = "SELECT c FROM Campaign c WHERE c.maxRetriesPerDay = :maxRetriesPerDay"),
    @NamedQuery(name = "Campaign.findByMaxRetriesPerCampaign", query = "SELECT c FROM Campaign c WHERE c.maxRetriesPerCampaign = :maxRetriesPerCampaign"),
    @NamedQuery(name = "Campaign.findByFrom", query = "SELECT c FROM Campaign c WHERE c.from = :from"),
    @NamedQuery(name = "Campaign.findByTo", query = "SELECT c FROM Campaign c WHERE c.to = :to"),
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
    @Column(name = "from")
    @Temporal(TemporalType.TIMESTAMP)
    private Date from;
    @Column(name = "to")
    @Temporal(TemporalType.TIMESTAMP)
    private Date to;
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
    private List<AudioFile> audioFileList;
    @JoinColumn(name = "list", referencedColumnName = "listId", nullable = false)
    @ManyToOne(optional = false)
    private com.phonytive.callup.entities.List list;
    @JoinColumn(name = "user", referencedColumnName = "userId", nullable = false)
    @ManyToOne(optional = false)
    private User user;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "campaign1")
    private List<CallDetailRecord> callDetailRecordList;

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

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
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
    public List<AudioFile> getAudioFileList() {
        return audioFileList;
    }

    public void setAudioFileList(List<AudioFile> audioFileList) {
        this.audioFileList = audioFileList;
    }

    public com.phonytive.callup.entities.List getList() {
        return list;
    }

    public void setList(com.phonytive.callup.entities.List list) {
        this.list = list;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @XmlTransient
    public List<CallDetailRecord> getCallDetailRecordList() {
        return callDetailRecordList;
    }

    public void setCallDetailRecordList(List<CallDetailRecord> callDetailRecordList) {
        this.callDetailRecordList = callDetailRecordList;
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
