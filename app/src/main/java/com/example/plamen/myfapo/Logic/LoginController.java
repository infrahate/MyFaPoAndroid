package com.example.plamen.myfapo.Logic;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.plamen.myfapo.Data.DataLinksInterface;
import com.example.plamen.myfapo.View.LoginInterface;
import com.example.plamen.myfapo.View.MainActivity;

/**
 * Created by Plamen on 02.08.2017.
 */

public class LoginController {
    private DataLinksInterface dataLinks;
    private LoginInterface loginInterface;

    public LoginController(DataLinksInterface dl,LoginInterface li){
        dataLinks = dl;
        loginInterface = li;
    }

    public String getFBloginLink(){
        return dataLinks.getFacebookLoginLink();
    }
    public String getTWloginLink(){
        return dataLinks.getTwitterLoginLink();
    }
    public String getLoginLink(){
        return dataLinks.getLoginLink();
    }
    public void onLoginSuccess(String loginHash){
        loginInterface.onLoginSuccess(loginHash);
    }
}
