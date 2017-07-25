package com.example.plamen.myfapo.View;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plamen.myfapo.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.Manifest;

/**
 * Created by Plamen on 20.06.2017.
 */

public class SnapActivity extends AppCompatActivity{
    private static final int CAMERA_PERMISSION_REQUEST = 1;
    private static final int CAMERA_ACTIVITY_INTENT = 2;

    //TO-DO: Figure out how autocomplete will work for MultiAutoComplete and how to get things from server if neccessery
    //TO-DO: Figure out how permissions will work in Android M
    //BUG: When erasing charecters at tag cloud input it crashes when there are 0 charecters.

    private boolean editing;
    private MultiAutoCompleteTextView inputTagView;
    private RecyclerView tagCloudView;
    private ImageView photo_container;
    private Bitmap photo;
    private StaggeredGridLayoutManager lManager;
    private TagAdapter adapter;
    private TextView cancelSnap;
    private TextView sendSnap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_snap);

        editing = false;
        startCameraAction();

        photo_container = (ImageView) findViewById(R.id.cont_snap_activity_photo);
        photo_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),FullscreenImage.class);
                i.putExtra("Image",photo);
                startActivity(i);
            }
        });

        tagCloudView = (RecyclerView) findViewById(R.id.view_snap_activity_tag_cloud);
        lManager = new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.HORIZONTAL);
        tagCloudView.setLayoutManager(lManager);

        cancelSnap = (TextView) findViewById(R.id.send_snap_cancel_link);
        sendSnap = (TextView) findViewById(R.id.send_snap_link);

        cancelSnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sendSnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Do stuff to send snap to server here.
            }
        });


        adapter = new TagAdapter(getApplicationContext(),new String[]{});
        tagCloudView.setAdapter(adapter);
        //Delete when done testing.


        inputTagView = (MultiAutoCompleteTextView) findViewById(R.id.inp_snap_activity_tags);
        inputTagView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editing){
                    editing = true;
                    if(editable.charAt(editable.length()-1)==' '){
                        if(editable.length()>1){
                            adapter.addTagToCloud(editable.toString());
                            editable.clear();
                        }else {
                            editable.clear();
                        }
                    }
                    editing = false;
                }
            }
        });

    }

    private void startCameraAction(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M || ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            checkForCameraPermission();
        }else{
           startCameraIntent();
        }
    }

    private void checkForCameraPermission() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.CAMERA},CAMERA_PERMISSION_REQUEST);
        }else{
            startCameraIntent();
        }
    }

    private void startCameraIntent(){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i,CAMERA_ACTIVITY_INTENT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_PERMISSION_REQUEST && grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            //User granted permission
            startCameraIntent();
        }else {
            //Permission Denied
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Camera returns the photo taken. Photo saved to variable photo to send to server later.
        if(requestCode == CAMERA_ACTIVITY_INTENT && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            photo = (Bitmap) extras.get("data");
            photo_container.setImageBitmap(photo);
        }

        //If user did not like the picture taken and presses the X.
        if(requestCode == CAMERA_ACTIVITY_INTENT && resultCode == RESULT_CANCELED) {
            finish();
        }

    }

    public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {
        private Context context;
        private List<String> mDataSet;

        public TagAdapter(Context applicationContext, String[] strings) {
            mDataSet = new ArrayList<>(Arrays.asList(strings));
            context = applicationContext;
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            private TextView tag;
            private ViewGroup vg;

            public ViewHolder(View itemView) {
                super(itemView);
                tag = (TextView) itemView.findViewById(R.id.tag);
                tag.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                mDataSet.remove(getAdapterPosition());
                notifyDataSetChanged();
            }
        }

        @Override
        public TagAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.root_tag_cloud,parent,false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.tag.setText(mDataSet.get(position));
            holder.tag.setBackgroundColor(Color.CYAN);
        }

        @Override
        public int getItemCount() {
            return mDataSet.size();
        }

        public void addTagToCloud(String tag){
            mDataSet.add("#"+tag);
            notifyItemInserted(mDataSet.size());
        }
    }
}
