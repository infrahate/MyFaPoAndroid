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
    private int post_id;
    private int user_id;
    private int likes;
    private int dislikes;

    public SnapItem(String dateAndTime, String message, String picture_link, ArrayList<String> comments, String posterName, int likes, int dislikes, int post_id, int user_id) {
        this.dateAndTime = dateAndTime;
        this.message = message;
        this.picture_link = picture_link;
        this.comments = comments;
        this.posterName = posterName;
        this.post_id = post_id;
        this.user_id = user_id;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public SnapItem(int post_id,String post_pic){
        this.post_id = post_id;
        this.picture_link = post_pic;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
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
