package com.nixmash.springdata.jpa.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by daveburke on 12/19/16.
 */
@Entity
@Table(name = "user_data")
public class UserData implements Serializable {

    private static final long serialVersionUID = -706243242873257798L;

    @Id
    @Column(name = "user_id", nullable = false)
    protected long userId;

    @Basic
    @Column(name = "login_attempts", nullable = false)
    public int loginAttempts;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lastlogin_datetime", nullable = true)
    public Date lastloginDatetime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_datetime")
    public Date createdDatetime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "approved_datetime", nullable = true)
    public Date approvedDatetime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "invited_datetime")
    public Date invitedDatetime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "accepted_datetime")
    public Date acceptedDatetime;

    @Basic
    @Column(name = "invited_by_id", nullable = false)
    public long invitedById;

    @Basic
    @Column(name = "ip", length = 25)
    public String ip;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(int loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    public Date getLastloginDatetime() {
        return lastloginDatetime;
    }

    public void setLastloginDatetime(Date lastloginDatetime) {
        this.lastloginDatetime = lastloginDatetime;
    }

    public Date getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(Date createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    public Date getApprovedDatetime() {
        return approvedDatetime;
    }

    public void setApprovedDatetime(Date approvedDatetime) {
        this.approvedDatetime = approvedDatetime;
    }

    public Date getInvitedDatetime() {
        return invitedDatetime;
    }

    public void setInvitedDatetime(Date invitedDatetime) {
        this.invitedDatetime = invitedDatetime;
    }

    public Date getAcceptedDatetime() {
        return acceptedDatetime;
    }

    public void setAcceptedDatetime(Date acceptedDatetime) {
        this.acceptedDatetime = acceptedDatetime;
    }

    public long getInvitedById() {
        return invitedById;
    }

    public void setInvitedById(long invitedById) {
        this.invitedById = invitedById;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "userId=" + userId +
                ", loginAttempts=" + loginAttempts +
                ", lastloginDatetime=" + lastloginDatetime +
                ", createdDatetime=" + createdDatetime +
                ", approvedDatetime=" + approvedDatetime +
                ", invitedDatetime=" + invitedDatetime +
                ", acceptedDatetime=" + acceptedDatetime +
                ", invitedById=" + invitedById +
                ", ip='" + ip + '\'' +
                '}';
    }
}
