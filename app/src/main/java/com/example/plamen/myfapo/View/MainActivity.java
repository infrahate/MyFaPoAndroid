package com.example.plamen.myfapo.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.plamen.myfapo.Data.DataSource;
import com.example.plamen.myfapo.Data.SnapItem;
import com.example.plamen.myfapo.HelperTools;
import com.example.plamen.myfapo.Logic.Controller;
import com.example.plamen.myfapo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewInterface {

    private static final String EXTRA_DATA_AND_TIME = "EXTRA_DATE_AND_TIME";
    private static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    private static final String EXTRA_COLOR = "EXTRA_COLOR";
    private static final String EXTRA_PICTURE_LINK = "PICTURE_LINK";
    private static final String EXTRA_COMMENTS = "EXTRA_COMMENTS";
    private static final String EXTRA_POSTER_NAMER = "EXTRA_POSTER_NAME";

    private List<SnapItem> listOfData;

    private LayoutInflater layoutInflater;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;

    private Controller controller;

    Button takeSnap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.rec_main_activity);
        layoutInflater = getLayoutInflater();

        controller = new Controller(this,new DataSource(),getApplicationContext());

        takeSnap = (Button) findViewById(R.id.snap_take);

    }

    @Override
    public void startDetailActivity(String dateAndTime, String message, int colorResource, String picture_link, ArrayList<String> comments, String posterName) {
        Intent i = new Intent(this,DetailActivity.class);
        i.putExtra(EXTRA_DATA_AND_TIME,dateAndTime);
        i.putExtra(EXTRA_MESSAGE,message);
        i.putExtra(EXTRA_COLOR,colorResource);
        i.putExtra(EXTRA_PICTURE_LINK,picture_link);
        i.putExtra(EXTRA_COMMENTS,comments);
        i.putExtra(EXTRA_POSTER_NAMER,posterName);
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
        public void onBindViewHolder(CustomViewHolder holder, int position) {
            SnapItem currentItem = listOfData.get(position);
            Picasso.with(getApplicationContext()).load(currentItem.getPicture_link()).into(holder.coloredCircle);
            holder.Message.setText(
                    currentItem.getMessage()
            );

            holder.dateAndTime.setText(
                    currentItem.getDateAndTime()
            );
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),R.layout.da_item,currentItem.getComments());
            holder.comments.setAdapter(adapter);
            HelperTools.setListViewHeightBasedOnItems(holder.comments);
            holder.posterName.setText(
                    currentItem.getPosterName()
            );
        }

        @Override
        public int getItemCount() {
            return listOfData.size();
        }

        class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            private ImageView coloredCircle;
            private TextView dateAndTime;
            private TextView Message;
            private ViewGroup container;
            private ListView comments;
            private TextView posterName;

            public CustomViewHolder(View itemView) {
                super(itemView);

                this.coloredCircle = (ImageView) itemView.findViewById(R.id.imv_list_item_circle);
                this.dateAndTime = (TextView) itemView.findViewById(R.id.lbl_date_and_time_header);
                this.Message = (TextView) itemView.findViewById(R.id.lbl_item_description);
                this.container = (ViewGroup) itemView.findViewById(R.id.root_list_item);
                this.comments = (ListView) itemView.findViewById(R.id.lbl_snap_comments);
                this.posterName = (TextView) itemView.findViewById(R.id.lbl_snap_item_postername);

                this.container.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                SnapItem snapItem = listOfData.get(this.getAdapterPosition());
                controller.onListItemClick(snapItem);
            }
        }

    }
}
