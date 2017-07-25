package com.example.plamen.myfapo.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.plamen.myfapo.R;

/**
 * Created by Plamen on 25.07.2017.
 */

public class FullscreenImage extends AppCompatActivity {

    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_fullscreen_view);

        Intent i = getIntent();
        image = (ImageView) findViewById(R.id.fullscreen_image_cont);
        image.setImageBitmap((Bitmap)i.getParcelableExtra("Image"));
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
