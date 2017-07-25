package com.example.plamen.myfapo.Logic;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.plamen.myfapo.View.MainActivity;
import com.example.plamen.myfapo.View.NotificationsActicity;
import com.example.plamen.myfapo.View.UserHomeActivity;

/**
 * Created by Plamen on 25.07.2017.
 */

public final class NavigationController {

    private Context context;

    public NavigationController(Context c){
        context = c;
    }

    public void startMainActivity(){
        Intent i = new Intent(context,MainActivity.class);
        context.startActivity(i);
    }

    public void startProfileActivity(){
        Intent i = new Intent(context,UserHomeActivity.class);
        context.startActivity(i);
    }

    public void startNotificationActivity(){
        Intent i = new Intent(context,NotificationsActicity.class);
        context.startActivity(i);
    }

}
