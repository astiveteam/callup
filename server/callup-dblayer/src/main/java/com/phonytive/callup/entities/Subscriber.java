package com.phonytive.callup.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "Subscriber", catalog = "callup", schema = "", uniqueConstraints = {
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
    @JoinTable(name = "SubscriberLists", joinColumns = {
        @JoinColumn(name = "subscriber", referencedColumnName = "subscriberId", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "list", referencedColumnName = "listId", nullable = false)})
    @ManyToMany
    private List<com.phonytive.callup.entities.List> listList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subscriber1")
    private List<CallDetailRecord> callDetailRecordList;

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
    public List<com.phonytive.callup.entities.List> getListList() {
        return listList;
    }

    public void setListList(List<com.phonytive.callup.entities.List> listList) {
        this.listList = listList;
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
