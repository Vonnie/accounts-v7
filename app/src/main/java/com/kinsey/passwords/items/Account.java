package com.kinsey.passwords.items;

import java.io.Serializable;

/**
 * Created by Yvonne on 2/18/2017.
 */

public class Account implements Serializable {
    public static final long serialVersionUID = 20170221l;

    // Account fields
//    public static class Columns {
//        public static final String _ID = BaseColumns._ID;
//        public static final String ACCOUNT_CORP_NAME = "Corp_Name";
//        public static final String ACCOUNT_CORP_WEBSITE = "Corp_Website";
//        public static final String ACCOUNT_USER_NAME = "User_Name";
//        public static final String ACCOUNT_USER_EMAIL = "User_Email";
//
//        private Columns() {
//            // private constructor to prevent instantiation
//        }
//    }

    /**
     * The URI to access the Tasks table
     */
//    public static final Uri CONTENT_URI = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TABLE_NAME);
//
//    static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + "." + TABLE_NAME;
//    static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + "." + TABLE_NAME;
//
//    static Uri buildTaskUri(long taskId) {
//        return ContentUris.withAppendedId(CONTENT_URI, taskId);
//    }
//
//    static long getTaskId(Uri uri) {
//        return ContentUris.parseId(uri);
//    }


    private int id = 0;
    private int passportId = 0;
    private int sequence = 0;
    private String corpName = "";
    private String userName = "";
    private String userEmail = "";
    private String note = "";
    private long openLong = 0;
    private long actvyLong = 0;
    private String corpWebsite = "";
    private int refFrom = 0;
    private int refTo = 0;
    private int newSequence = 0;

    public Account() {}

    public Account(int _Id,
                   String corpName,
                   String userName,
                   String userEmail,
                   String corpWebsite,
                   int sequence) {
        this.id = _Id;
        this.corpName = corpName;
        this.userName = userName;
        this.userEmail = userEmail;
        this.corpWebsite = corpWebsite;
        this.sequence = sequence;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getActvyLong() {
        return actvyLong;
    }

    public String getCorpName() {
        return corpName;
    }

    public String getCorpWebsite() {
        return corpWebsite;
    }

    public String getNote() {
        return note;
    }

    public long getOpenLong() {
        return openLong;
    }

    public int getPassportId() {
        return passportId;
    }

    public int getRefFrom() {
        return refFrom;
    }

    public int getRefTo() {
        return refTo;
    }

    public int getSequence() {
        return sequence;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setActvyLong(long actvyLong) {
        this.actvyLong = actvyLong;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public void setCorpWebsite(String corpWebsite) {
        this.corpWebsite = corpWebsite;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setOpenLong(long openLong) {
        this.openLong = openLong;
    }

    public void setPassportId(int passportId) {
        this.passportId = passportId;
    }

    public void setRefFrom(int refFrom) {
        this.refFrom = refFrom;
    }

    public void setRefTo(int refTo) {
        this.refTo = refTo;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getNewSequence() {
        return newSequence;
    }

    public void setNewSequence(int newSequence) {
        this.newSequence = newSequence;
    }

    @Override
    public String toString() {
        return "Account{" +
                "corpName='" + corpName + '\'' +
                ", _Id=" + id +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", sequence=" + sequence +
                ", corpWebsite=" + corpWebsite +
                ", note=" + note +
                '}';
    }
}
