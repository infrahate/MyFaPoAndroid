package com.example.plamen.myfapo.Data;

/**
 * Created by Plamen on 02.08.2017.
 */

public final class DataLinks implements DataLinksInterface {

    public DataLinks(){}

    @Override
    public String getFacebookLoginLink() {
        return "http://myfapo.jts-mr.com/login.php?method=facebook";
    }

    @Override
    public String getTwitterLoginLink() {
        return "http://myfapo.jts-mr.com/login.php?method=twitter";
    }

    @Override
    public String getLoginLink() {
        return "http://myfapo.jts-mr.com/login.php";
    }
}