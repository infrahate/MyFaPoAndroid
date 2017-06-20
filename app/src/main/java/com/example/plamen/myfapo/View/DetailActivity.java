package com.example.plamen.myfapo.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.plamen.myfapo.HelperTools;
import com.example.plamen.myfapo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private static final String EXTRA_DATE_AND_TIME = "EXTRA_DATE_AND_TIME";
    private static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    private static final String EXTRA_COLOR = "EXTRA_COLOR";
    private static final String EXTRA_PICTURE_LINK = "PICTURE_LINK";
    private static final String EXTRA_COMMENTS = "EXTRA_COMMENTS";
    private static final String EXTRA_POSTER_NAMER = "EXTRA_POSTER_NAME";

    private TextView dateAndTime;
    private TextView message;
    private ImageView coloredBackground;
    private TextView posterNameView;
    private ListView commentsView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        /*I wouldn't normally pass all this Data via Intent, so understand that this is just a quick
        implementation to get things working for the Demo. I'd normally pass just a Unique id as an
        extra, and then retrieve the appropriate Data from a Service.*/
        Intent i = getIntent();
        String dateAndTimeExtra = i.getStringExtra(EXTRA_DATE_AND_TIME);
        String messageExtra = i.getStringExtra(EXTRA_MESSAGE);
        String picture_link = i.getStringExtra(EXTRA_PICTURE_LINK);
        ArrayList<String> comments = i.getStringArrayListExtra(EXTRA_COMMENTS);
        String posterName = i.getStringExtra(EXTRA_POSTER_NAMER);

        //int colorResourceExtra = i.getIntExtra(EXTRA_COLOR, 0);

        dateAndTime = (TextView) findViewById(R.id.lbl_date_and_time_header);
        dateAndTime.setText(dateAndTimeExtra);

        message = (TextView) findViewById(R.id.lbl_message_body);
        message.setText(messageExtra);

        coloredBackground = (ImageView) findViewById(R.id.cont_snap_image);
        Picasso.with(getApplicationContext()).load(picture_link).into(coloredBackground);

        posterNameView = (TextView) findViewById(R.id.lbl_poster_name);
        posterNameView.setText(posterName);

        commentsView = (ListView) findViewById(R.id.detail_comments);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),R.layout.da_item,comments);
        commentsView.setAdapter(adapter);
        HelperTools.setListViewHeightBasedOnItems(commentsView);

        /*
        coloredBackground.setBackgroundColor(
                ContextCompat.getColor(this, colorResourceExtra)
        );*/
    }

}
