package com.zjclugger.buyer.ui.meeting;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @title <br>
 * Created by King.Zi on 2020/7/17.<br>
 * Copyright (c) 2020 zjclugger.com
 */
public class ReminderMeeting implements Serializable {
    private String id;
    private long startTime;
    private long endTime;
    private String title;
    private String address;
    private String details;
    private long owner;
    private int autoInvite;
    private String conferenceNumber;
    private long createTime;
    private String password;
    private String conferenceControlPassword;
    private Set<String> participants = new HashSet();
    private Set<String> singleRecordParticipants = new HashSet();
    private int week;
    private int meetingRoomType = -1;
    private int autoRecord;
    private String mainImage;

    public ReminderMeeting() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return this.endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetails() {
        return this.details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public long getOwner() {
        return this.owner;
    }

    public void setOwner(long owner) {
        this.owner = owner;
    }

    public int getAutoInvite() {
        return this.autoInvite;
    }

    public void setAutoInvite(int autoInvite) {
        this.autoInvite = autoInvite;
    }

    public String getConferenceNumber() {
        return this.conferenceNumber;
    }

    public void setConferenceNumber(String conferenceNumber) {
        this.conferenceNumber = conferenceNumber;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public Set<String> getParticipants() {
        return this.participants;
    }

    public void setParticipants(Set<String> participants) {
        this.participants = participants;
    }

    public int getWeek() {
        return this.week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMeetingRoomType() {
        return this.meetingRoomType;
    }

    public void setMeetingRoomType(int meetingRoomType) {
        this.meetingRoomType = meetingRoomType;
    }

    public String getConferenceControlPassword() {
        return this.conferenceControlPassword;
    }

    public void setConferenceControlPassword(String conferenceControlPassword) {
        this.conferenceControlPassword = conferenceControlPassword;
    }

    public int getAutoRecord() {
        return this.autoRecord;
    }

    public void setAutoRecord(int autoRecord) {
        this.autoRecord = autoRecord;
    }

    public String getMainImage() {
        return this.mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public Set<String> getSingleRecordParticipants() {
        return this.singleRecordParticipants;
    }

    public void setSingleRecordParticipants(Set<String> singleRecordParticipants) {
        this.singleRecordParticipants = singleRecordParticipants;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            ReminderMeeting that = (ReminderMeeting) o;
            if (this.startTime != that.startTime) {
                return false;
            } else if (this.endTime != that.endTime) {
                return false;
            } else if (this.owner != that.owner) {
                return false;
            } else {
                if (this.id != null) {
                    if (!this.id.equals(that.id)) {
                        return false;
                    }
                } else if (that.id != null) {
                    return false;
                }

                if (!this.title.equals(that.title)) {
                    return false;
                } else {
                    if (this.details != null) {
                        if (this.details.equals(that.details)) {
                            return this.conferenceNumber != null ?
                                    this.conferenceNumber.equals(that.conferenceNumber) :
                                    that.conferenceNumber == null;
                        }
                    } else if (that.details == null) {
                        return this.conferenceNumber != null ?
                                this.conferenceNumber.equals(that.conferenceNumber) :
                                that.conferenceNumber == null;
                    }

                    return false;
                }
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.id != null ? this.id.hashCode() : 0;
        result = 31 * result + (int) (this.startTime ^ this.startTime >>> 32);
        result = 31 * result + (int) (this.endTime ^ this.endTime >>> 32);
        result = 31 * result + this.title.hashCode();
        result = 31 * result + (this.details != null ? this.details.hashCode() : 0);
        result = 31 * result + (int) (this.owner ^ this.owner >>> 32);
        result = 31 * result + (this.conferenceNumber != null ? this.conferenceNumber.hashCode()
                : 0);
        return result;
    }

    public String toString() {
        return "ReminderMeeting{id='" + this.id + '\'' + ", startTime=" + this.startTime + ", " +
                "endTime=" + this.endTime + ", title='" + this.title + '\'' + ", address='" + this.address + '\'' + ", details='" + this.details + '\'' + ", owner=" + this.owner + ", autoInvite=" + this.autoInvite + ", conferenceNumber='" + this.conferenceNumber + '\'' + ", createTime=" + this.createTime + ", password='" + this.password + '\'' + ", conferenceControlPassword='" + this.conferenceControlPassword + '\'' + ", participants=" + this.participants + ", week=" + this.week + ", meetingRoomType=" + this.meetingRoomType + ", autoRecord=" + this.autoRecord + ", mainImage='" + this.mainImage + '\'' + '}';
    }
}
