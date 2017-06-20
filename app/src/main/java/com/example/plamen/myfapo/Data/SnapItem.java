package com.example.plamen.myfapo.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Plamen on 16.06.2017.
 */

public class SnapItem {
    private String dateAndTime;
    private String message;
    private String picture_link;
    private int colorRes;
    private ArrayList<String> comments;
    private String posterName;

    public SnapItem(String dateAndTime, String message, int colorRes,String picture_link, ArrayList<String> comments,String posterName) {
        this.dateAndTime = dateAndTime;
        this.message = message;
        this.colorRes = colorRes;
        this.picture_link = picture_link;
        this.comments = comments;
        this.posterName = posterName;
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }

    public String getPosterName() {

        return posterName;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public String getMessage() {
        return message;
    }

    public int getColorRes() {
        return colorRes;
    }

    public String getPicture_link() {
        return picture_link.replaceAll("[\\\\]","");
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setColorRes(int colorRes) {
        this.colorRes = colorRes;
    }

    public void setPicture_link(String picture_link) {
        this.picture_link = picture_link;
    }
}
