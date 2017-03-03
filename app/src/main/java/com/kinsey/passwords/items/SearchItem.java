package com.kinsey.passwords.items;

/**
 * Created by Yvonne on 3/2/2017.
 */

public class SearchItem {

    private int _Id = 0;
    private int suggestId = 0;
    private String contactId = "";
    private String corpName = "";
    private String idCorpName = "";
    private String phoneNum = "";
    private String phoneNumUnformat = "";
    private String email = "";
    private String website = "";

    public int getId() {
        return _Id;
    }

    public String getContactId() {
        return contactId;
    }

    public String getCorpName() {
        return corpName;
    }

    public String getEmail() {
        return email;
    }

    public String getIdCorpName() {
        return idCorpName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getPhoneNumUnformat() {
        return phoneNumUnformat;
    }

    public int getSuggestId() {
        return suggestId;
    }

    public String getWebsite() {
        return website;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIdCorpName(String idCorpName) {
        this.idCorpName = idCorpName;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setPhoneNumUnformat(String phoneNumUnformat) {
        this.phoneNumUnformat = phoneNumUnformat;
    }

    public void setSuggestId(int suggestId) {
        this.suggestId = suggestId;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
