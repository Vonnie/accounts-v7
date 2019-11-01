package com.kinsey.passwords.items;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "passport_detail")
public class Profile {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int id = 0;

    @ColumnInfo(name = "passport_id")
    private int passportId = 0;

    private int sequence = 0;

    @ColumnInfo(name = "corporation_name")
    private String corpName = "";

    @ColumnInfo(name = "user_name")
    private String userName = "";

    @ColumnInfo(name = "user_email")
    private String userEmail = "";

    @ColumnInfo(name = "passport_note")
    private String note = "";

    @ColumnInfo(name = "open_date")
    private long openLong = 0;

    @ColumnInfo(name = "actvy_date")
    private long actvyLong = 0;

    @ColumnInfo(name = "corporation_website")
    private String corpWebsite = "";

    @ColumnInfo(name = "ref_from")
    private int refFrom = 0;

    @ColumnInfo(name = "ref_to")
    private int refTo = 0;

    @Ignore
    private int newSequence = 0;


    public Profile(int sequence,
                   String corpName,
                   String userName,
                   String userEmail,
                   String corpWebsite) {
        this.sequence = sequence;
        this.corpName = corpName;
        this.userName = userName;
        this.userEmail = userEmail;
        this.corpWebsite = corpWebsite;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPassportId(int passportId) {
        this.passportId = passportId;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setOpenLong(long openLong) {
        this.openLong = openLong;
    }

    public void setActvyLong(long actvyLong) {
        this.actvyLong = actvyLong;
    }

    public void setCorpWebsite(String corpWebsite) {
        this.corpWebsite = corpWebsite;
    }

    public void setRefFrom(int refFrom) {
        this.refFrom = refFrom;
    }

    public void setRefTo(int refTo) {
        this.refTo = refTo;
    }

    public void setNewSequence(int newSequence) {
        this.newSequence = newSequence;
    }

    public int getId() {
        return id;
    }

    public int getPassportId() {
        return passportId;
    }

    public int getSequence() {
        return sequence;
    }

    public String getCorpName() {
        return corpName;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getNote() {
        return note;
    }

    public long getOpenLong() {
        return openLong;
    }

    public long getActvyLong() {
        return actvyLong;
    }

    public String getCorpWebsite() {
        return corpWebsite;
    }

    public int getRefFrom() {
        return refFrom;
    }

    public int getRefTo() {
        return refTo;
    }

    public int getNewSequence() {
        return newSequence;
    }
}
