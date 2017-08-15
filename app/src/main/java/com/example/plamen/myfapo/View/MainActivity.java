package com.example.plamen.myfapo.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.plamen.myfapo.Constants;
import com.example.plamen.myfapo.Data.DataSource;
import com.example.plamen.myfapo.Data.SnapItem;
import com.example.plamen.myfapo.HelperTools;
import com.example.plamen.myfapo.Logic.Controller;
import com.example.plamen.myfapo.Logic.NavigationController;
import com.example.plamen.myfapo.MySingleton;
import com.example.plamen.myfapo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ViewInterface{

    private List<SnapItem> listOfData;

    private LayoutInflater layoutInflater;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;

    private Controller controller;

    private String lHash;

    Button takeSnap;
    ImageButton profile;
    ImageButton notifications;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lHash = HelperTools.getHash(this);

        final NavigationController navigationController = new NavigationController(this);

        recyclerView = (RecyclerView) findViewById(R.id.rec_main_activity);
        layoutInflater = getLayoutInflater();

        controller = new Controller(this,new DataSource(),getApplicationContext());

        takeSnap = (Button) findViewById(R.id.snap_take);
        takeSnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTakeSnapActivity(getApplicationContext());
            }
        });

        profile = (ImageButton) findViewById(R.id.navigation_bottom_profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               navigationController.startProfileActivity();
            }
        });

        notifications = (ImageButton) findViewById(R.id.navigation_bottom_notifications);
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationController.startNotificationActivity();
            }
        });


    }

    @Override
    public void startDetailActivity(int post_id,String link) {
        Intent i = new Intent(this,DetailActivity.class);
        i.putExtra(Constants.EXTRA_POST_ID,post_id);
        i.putExtra(Constants.EXTRA_POST_PIC,link);
        startActivity(i);
    }

    @Override
    public void startTakeSnapActivity(Context context) {
        Intent i = new Intent(this,SnapActivity.class);
        startActivity(i);
    }


    @Override
    public void setUpAdapterAndView(List<SnapItem> listOfData) {
        this.listOfData = listOfData;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder>{

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = layoutInflater.inflate(R.layout.item_data,parent,false);
            return new CustomViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final CustomViewHolder holder, final int position) {
            final SnapItem currentItem = listOfData.get(position);

            Log.i("agag",String.valueOf(currentItem.getPost_id()));
            //set pic
            Picasso.with(getApplicationContext()).load(currentItem.getPicture_link()).into(holder.coloredCircle);

            //set poster message
            holder.Message.setText(
                    currentItem.getMessage()
            );

            //set date and time.
            holder.dateAndTime.setText(
                    currentItem.getDateAndTime()
            );

            //set comments
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),R.layout.da_item,currentItem.getComments());
            holder.comments.setAdapter(adapter);
            HelperTools.setListViewHeightBasedOnItems(holder.comments);
            holder.comments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    startDetailActivity(currentItem.getPost_id(),currentItem.getPicture_link());
                }
            });


            //set poster name
            holder.posterName.setText(
                    currentItem.getPosterName()
            );
            holder.posterName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(),UserHomeActivity.class);
                    i.putExtra(Constants.EXTRA_POST_USER_ID,currentItem.getUser_id());
                    startActivity(i);
                }
            });

            //set likes/dilike and ajust ratio

            holder.likes.setText(String.valueOf(currentItem.getLikes()));
            holder.likeThumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = "http://myfapo.jts-mr.com/services/like_dislike.php";
                    StringRequest request = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        int past = Integer.parseInt(holder.likes.getText().toString());
                                        if(response.contains("inserted")){
                                            holder.likes.setText(String.valueOf(past+1));
                                        }else if(response.contains("deleted")){
                                            holder.likes.setText(String.valueOf(past-1));
                                        }
                                    }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();
                                }
                            }){
                                @Override
                                protected Map<String, String> getParams(){
                                    Map<String,String> params = new HashMap<String,String>();
                                    params.put("login_hash",lHash);
                                    params.put("post_id",String.valueOf(currentItem.getPost_id()));
                                    params.put("int_type",String.valueOf(1));
                                    return params;
                                }
                            };
                    MySingleton.getInstance(getApplicationContext()).addToRequestque(request);
                }
            });
            holder.dislikes.setText(String.valueOf(currentItem.getDislikes()));
            holder.dislikeThumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = "http://myfapo.jts-mr.com/services/like_dislike.php";
                    StringRequest request = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    int past = Integer.parseInt(holder.dislikes.getText().toString());
                                    if(response.contains("inserted")){
                                        holder.dislikes.setText(String.valueOf(past+1));
                                    }else if(response.contains("deleted")){
                                        holder.dislikes.setText(String.valueOf(past-1));
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams(){
                            Map<String,String> params = new HashMap<String,String>();
                            params.put("login_hash",lHash);
                            params.put("post_id",String.valueOf(currentItem.getPost_id()));
                            params.put("int_type",String.valueOf(2));
                            return params;
                        }
                    };
                    MySingleton.getInstance(getApplicationContext()).addToRequestque(request);
                }
            });
            int likes = currentItem.getLikes();
            int dislike = currentItem.getDislikes();

            if(likes==0){
                holder.progressBar.setProgress(0);
            }else if(dislike==0){
                holder.progressBar.setProgress(100);
            }else{
                if(likes<dislike) {
                    holder.progressBar.setProgress((likes / dislike) * 100);
                } else {
                    holder.progressBar.setProgress(((dislike / likes)*100)-100);
                }
            }

        }

        @Override
        public int getItemCount() {
            return listOfData.size();
        }

        class CustomViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/{

            private ImageView coloredCircle;
            private TextView dateAndTime;
            private TextView Message;
            private ViewGroup container;
            private ListView comments;
            private TextView posterName;
            private TextView likes;
            private TextView dislikes;
            private ProgressBar progressBar;
            private ImageView likeThumb;
            private ImageView dislikeThumb;

            public CustomViewHolder(View itemView) {
                super(itemView);

                this.coloredCircle = (ImageView) itemView.findViewById(R.id.imv_list_item_circle);
                this.dateAndTime = (TextView) itemView.findViewById(R.id.lbl_date_and_time_header);
                this.Message = (TextView) itemView.findViewById(R.id.lbl_item_description);
                this.container = (ViewGroup) itemView.findViewById(R.id.root_list_item);
                this.comments = (ListView) itemView.findViewById(R.id.lbl_snap_comments);
                this.posterName = (TextView) itemView.findViewById(R.id.lbl_snap_item_postername);
                this.likes = (TextView) itemView.findViewById(R.id.count_thumbs_up);
                this.dislikes = (TextView) itemView.findViewById(R.id.count_thumbs_down);
                this.progressBar = (ProgressBar) itemView.findViewById(R.id.ratio_like_dislike);
                this.likeThumb = (ImageView) itemView.findViewById(R.id.img_thums_up);
                this.dislikeThumb = (ImageView) itemView.findViewById(R.id.img_thums_down);

                //this.container.setOnClickListener(this);
            }
            /*
            @Override
            public void onClick(View view) {
                SnapItem snapItem = listOfData.get(this.getAdapterPosition());
                controller.onListItemClick(snapItem);
            }
            */

        }

    }
}
