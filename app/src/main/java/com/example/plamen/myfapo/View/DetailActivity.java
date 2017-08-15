package com.example.plamen.myfapo.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.plamen.myfapo.HelperTools;
import com.example.plamen.myfapo.MySingleton;
import com.example.plamen.myfapo.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.plamen.myfapo.Constants;

public class DetailActivity extends AppCompatActivity {



    private TextView dateAndTime;
    private TextView message;
    private ImageView pic;
    private TextView posterNameView;
    private ListView commentsView;
    private EditText commentBox;
    private int post_id; //post id passed to activity
    private ArrayAdapter<String> adapter; //Comment adapter


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final String lHash = HelperTools.getHash(this);
        Intent i = getIntent();
        post_id = i.getIntExtra(Constants.EXTRA_POST_ID,-1);
        final String link = i.getStringExtra(Constants.EXTRA_POST_PIC);

        Log.i("postid",String.valueOf(post_id));
        //Initialize views
        this.posterNameView = (TextView) findViewById(R.id.lbl_poster_name);
        this.pic = (ImageView) findViewById(R.id.cont_snap_image);
        this.commentsView = (ListView) findViewById(R.id.detail_comments);

        this.commentBox = (EditText) findViewById(R.id.detail_activity_comment_box);
        commentBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                final String message = textView.getText().toString();
                if (message.length()>0 && i == EditorInfo.IME_ACTION_SEND) {
                    String url = "http://myfapo.jts-mr.com/services/send_comment.php";
                    // Handle event
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.contains("200")){
                                Toast.makeText(getApplicationContext(),"Comment sent",Toast.LENGTH_LONG).show();
                                commentBox.setText("");
                                commentBox.clearFocus();
                                adapter.add(message);
                                adapter.notifyDataSetChanged();
                                HelperTools.setListViewHeightBasedOnItems(commentsView);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("login_hash",lHash);
                            params.put("post_id",String.valueOf(post_id));
                            params.put("message",message);
                            return params;
                        }
                    };
                    MySingleton.getInstance(getApplicationContext()).addToRequestque(request);
                    handled = true;
                }
                return handled;
            }
        });

        String url="http://myfapo.jts-mr.com/services/get_single_post.php";

        Picasso.with(getApplicationContext()).load(link).into(pic);
        StringRequest request =  new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);

                    Log.i("ad",json.toString());
                    posterNameView.setText(json.getString("username"));


                    //Get comments for item
                    ArrayList<String> comments = new ArrayList<>();
                    JSONObject joComments = json.getJSONObject("comments");
                    for (int j=0;j<joComments.length();j++) comments.add(HelperTools.fromHtml(joComments.getJSONObject(String.valueOf(j)).getString("user"))+" : "+HelperTools.fromHtml(joComments.getJSONObject(String.valueOf(j)).getString("comment")));

                    adapter = new ArrayAdapter<>(getApplicationContext(),R.layout.da_item,comments);
                    commentsView.setAdapter(adapter);
                    HelperTools.setListViewHeightBasedOnItems(commentsView);
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("login_hash",lHash);
                params.put("post_id",String.valueOf(post_id));
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestque(request);
        }


    }
