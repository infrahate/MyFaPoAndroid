package com.example.plamen.myfapo.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.plamen.myfapo.Constants;
import com.example.plamen.myfapo.Data.SnapItem;
import com.example.plamen.myfapo.HelperTools;
import com.example.plamen.myfapo.Logic.Controller;
import com.example.plamen.myfapo.Logic.NavigationController;
import com.example.plamen.myfapo.MySingleton;
import com.example.plamen.myfapo.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Plamen on 25.07.2017.
 */


public class UserHomeActivity extends AppCompatActivity {

    private ImageView profilePic;
    private int user_id;

    ImageButton home;
    ImageButton notifications;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        Intent i = getIntent();

        //Get/store login_hash
        final String lHash = HelperTools.getHash(this);
        user_id = i.getIntExtra(Constants.EXTRA_POST_USER_ID,-1);

        Log.i("USER_ID",String.valueOf(user_id));
        //Navigation Init
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

        //Other initialization
        profilePic = (ImageView) findViewById(R.id.user_home_profile_pic);


        //Set up Grid view
        final GridView gridview = (GridView) findViewById(R.id.user_home_picture_grid);
        final ImageAdapter adapter = new ImageAdapter(this);
        gridview.setAdapter(adapter);

        //Get Home page of user, post pictures and post id's, based on the sent user_id.
        String url = "http://myfapo.jts-mr.com/services/home_page.php";


        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject asJson = new JSONObject(response);
                    Log.i("HelpMe",response);
                    for(int i=0;i<asJson.length();i++){
                        JSONObject item = asJson.getJSONObject(String.valueOf(i));
                        adapter.addToList(item.getInt("post_id"),item.getString("post_pic_link"));
                        adapter.notifyDataSetChanged();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Something went wrong with fetching data from the server. Please try again later",Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String,String>();
                params.put("login_hash",lHash);
                params.put("user_id",String.valueOf(user_id));
                return params;
            }
        };
       MySingleton.getInstance(getApplicationContext()).addToRequestque(request);

        //Set up onItemClick DetailActivity start
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent i = new Intent(getApplicationContext(),DetailActivity.class);
                i.putExtra(Constants.EXTRA_POST_PIC,adapter.getItemPicLink(position));
                i.putExtra(Constants.EXTRA_POST_ID,adapter.getItemIdint(position));
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
            return snapList.size();
        }

        public Object getItem(int position) {
            return snapList.get(position);
        }

        public String getItemPicLink(int position){return snapList.get(position).getPicture_link();}

        public long getItemId(int position) {
            return snapList.get(position).getPost_id();
        }
        public int getItemIdint(int position) {
            return snapList.get(position).getPost_id();
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

            Picasso.with(getApplicationContext()).load(snapList.get(position).getPicture_link()).into(imageView);
            return imageView;
        }

        // references to our images
        private List<SnapItem> snapList = new ArrayList<SnapItem>();

        public void addToList(int post_id,String post_link){
            snapList.add(new SnapItem(post_id,post_link));
        }
    }
}
