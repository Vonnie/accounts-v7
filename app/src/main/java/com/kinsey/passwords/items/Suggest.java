package com.kinsey.passwords.items;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Yvonne on 2/18/2017.
 */

@Entity(tableName = "password_item_v2")
public class Suggest implements Serializable {
    public static final long serialVersionUID = 20170222l;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int id = 0;

    private int sequence = 0;

    @ColumnInfo(name = "password_text")
    private String password = "";

    @ColumnInfo(name = "notes")
    private String note = "";

    @ColumnInfo(name = "actvy_date")
    private Long actvyDate = new Date().getTime();

    @Ignore
    private int rating = 0;

    @Ignore
    private int newSequence = 0;

    public Suggest(String password, int sequence, Long actvyDate) {
//        this.id = id;
        this.password = password;
        this.sequence = sequence;
        this.actvyDate = actvyDate;
    }


    public void setId(int id) {
        this.id = id;
    }

    public Long getActvyDate() {
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

    public void setActvyDate(Long actvyDate) {
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
