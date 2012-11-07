package com.phonytive.callup.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "CallDetailRecord", catalog = "callup", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CallDetailRecord.findAll", query = "SELECT c FROM CallDetailRecord c"),
    @NamedQuery(name = "CallDetailRecord.findByCallDetailRecordId", query = "SELECT c FROM CallDetailRecord c WHERE c.callDetailRecordPK.callDetailRecordId = :callDetailRecordId"),
    @NamedQuery(name = "CallDetailRecord.findBySubscriber", query = "SELECT c FROM CallDetailRecord c WHERE c.callDetailRecordPK.subscriber = :subscriber"),
    @NamedQuery(name = "CallDetailRecord.findByCampaign", query = "SELECT c FROM CallDetailRecord c WHERE c.callDetailRecordPK.campaign = :campaign"),
    @NamedQuery(name = "CallDetailRecord.findByRate", query = "SELECT c FROM CallDetailRecord c WHERE c.rate = :rate"),
    @NamedQuery(name = "CallDetailRecord.findByResultCode", query = "SELECT c FROM CallDetailRecord c WHERE c.resultCode = :resultCode")})
public class CallDetailRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CallDetailRecordPK callDetailRecordPK;
    @Lob
    @Column(name = "time", length = 16777215)
    private String time;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "rate", precision = 22)
    private Double rate;
    @Column(name = "resultCode", length = 21)
    private String resultCode;
    @Lob
    @Column(name = "gatewayCdrId", length = 16777215)
    private String gatewayCdrId;
    @JoinColumn(name = "campaign", referencedColumnName = "campaignId", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Campaign campaign1;
    @JoinColumn(name = "subscriber", referencedColumnName = "subscriberId", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Subscriber subscriber1;

    public CallDetailRecord() {
    }

    public CallDetailRecord(CallDetailRecordPK callDetailRecordPK) {
        this.callDetailRecordPK = callDetailRecordPK;
    }

    public CallDetailRecord(int callDetailRecordId, int subscriber, int campaign) {
        this.callDetailRecordPK = new CallDetailRecordPK(callDetailRecordId, subscriber, campaign);
    }

    public CallDetailRecordPK getCallDetailRecordPK() {
        return callDetailRecordPK;
    }

    public void setCallDetailRecordPK(CallDetailRecordPK callDetailRecordPK) {
        this.callDetailRecordPK = callDetailRecordPK;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getGatewayCdrId() {
        return gatewayCdrId;
    }

    public void setGatewayCdrId(String gatewayCdrId) {
        this.gatewayCdrId = gatewayCdrId;
    }

    public Campaign getCampaign1() {
        return campaign1;
    }

    public void setCampaign1(Campaign campaign1) {
        this.campaign1 = campaign1;
    }

    public Subscriber getSubscriber1() {
        return subscriber1;
    }

    public void setSubscriber1(Subscriber subscriber1) {
        this.subscriber1 = subscriber1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (callDetailRecordPK != null ? callDetailRecordPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CallDetailRecord)) {
            return false;
        }
        CallDetailRecord other = (CallDetailRecord) object;
        if ((this.callDetailRecordPK == null && other.callDetailRecordPK != null) || (this.callDetailRecordPK != null && !this.callDetailRecordPK.equals(other.callDetailRecordPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.phonytive.callup.entities.CallDetailRecord[ callDetailRecordPK=" + callDetailRecordPK + " ]";
    }

}
