package com.example.plamen.myfapo.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.plamen.myfapo.Logic.Controller;
import com.example.plamen.myfapo.Logic.NavigationController;
import com.example.plamen.myfapo.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Plamen on 25.07.2017.
 */


public class UserHomeActivity extends AppCompatActivity {

    private ImageView profilePic;

    ImageButton home;
    ImageButton notifications;

    private static final String EXTRA_DATE_AND_TIME = "EXTRA_DATE_AND_TIME";
    private static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    private static final String EXTRA_COLOR = "EXTRA_COLOR";
    private static final String EXTRA_PICTURE_LINK = "PICTURE_LINK";
    private static final String EXTRA_COMMENTS = "EXTRA_COMMENTS";
    private static final String EXTRA_POSTER_NAMER = "EXTRA_POSTER_NAME";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        final NavigationController navigationController = new NavigationController(this);

        home = (ImageButton) findViewById(R.id.navigation_bottom_home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationController.startMainActivity();
            }
        });

        notifications = (ImageButton) findViewById(R.id.navigation_bottom_notifications);
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationController.startNotificationActivity();
            }
        });

        profilePic = (ImageView) findViewById(R.id.user_home_profile_pic);

        GridView gridview = (GridView) findViewById(R.id.user_home_picture_grid);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent i = new Intent(getApplicationContext(),DetailActivity.class);
                i.putExtra(EXTRA_DATE_AND_TIME,"PlaceHolderDate");
                i.putExtra(EXTRA_MESSAGE,"Place Holder Message");
                i.putExtra(EXTRA_POSTER_NAMER,"Place Holder Username");
                i.putStringArrayListExtra(EXTRA_COMMENTS,new ArrayList<String>(Arrays.asList("Place Holder : Place Holder","Place Holder : Place Holder","Place Holder : Place Holder","Place Holder : Place Holder","Place Holder : Place Holder")));
                startActivity(i);
            }
        });
    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mThumbIds.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(mThumbIds[position]);
            return imageView;
        }

        // references to our images
        private Integer[] mThumbIds = {
                R.mipmap.placeholder, R.mipmap.placeholder,
                R.mipmap.placeholder, R.mipmap.placeholder,R.mipmap.placeholder,R.mipmap.placeholder,R.mipmap.placeholder,R.mipmap.placeholder,R.mipmap.placeholder,R.mipmap.placeholder,
                R.mipmap.placeholder,R.mipmap.placeholder,R.mipmap.placeholder,R.mipmap.placeholder,R.mipmap.placeholder
        };
    }
}
