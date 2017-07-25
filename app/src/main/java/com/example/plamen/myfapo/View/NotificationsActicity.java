package com.example.plamen.myfapo.View;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.example.plamen.myfapo.Logic.NavigationController;
import com.example.plamen.myfapo.R;

public class NotificationsActicity extends AppCompatActivity {

    private ImageButton home;
    private ImageButton profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        final NavigationController navigationController = new NavigationController(this);

        home = (ImageButton) findViewById(R.id.navigation_bottom_home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationController.startMainActivity();
            }
        });

        profile = (ImageButton) findViewById(R.id.navigation_bottom_profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationController.startProfileActivity();
            }
        });
    }
}
