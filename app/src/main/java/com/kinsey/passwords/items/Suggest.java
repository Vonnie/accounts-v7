package com.kinsey.passwords.items;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Yvonne on 2/18/2017.
 */

public class Suggest implements Serializable {
    public static final long serialVersionUID = 20170222l;

    private int id = 0;
    private int sequence = 0;
    private String password = "";
    private String note = "";
    private Date actvyDate = null;
    private int rating = 0;
    private int newSequence = 0;

    public Suggest(int id, String password, int sequence) {
        this.id = id;
        this.password = password;
        this.sequence = sequence;
    }

    public Date getActvyDate() {
        return actvyDate;
    }

    public int getId() {
        return id;
    }

    public String getNote() {
        return note;
    }

    public String getPassword() {
        return password;
    }

    public int getRating() {
        return rating;
    }

    public int getSequence() {
        return sequence;
    }

    public int getNewSequence() {
        return newSequence;
    }

    public void setActvyDate(Date actvyDate) {
        this.actvyDate = actvyDate;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public void setNewSequence(int newSequence) {
        this.newSequence = newSequence;
    }

    @Override
    public String toString() {
        return "Suggest{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", sequence=" + sequence +
                ", actvyDate=" + actvyDate +
                ", rating=" + rating +
                ", note='" + note + '\'' +
                '}';
    }
}
