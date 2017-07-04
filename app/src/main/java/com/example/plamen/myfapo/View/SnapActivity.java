package com.example.plamen.myfapo.View;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.example.plamen.myfapo.R;

import java.util.ArrayList;
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

    private boolean editing;
    private MultiAutoCompleteTextView inputTagView;
    private GridView tagCloudView;
    private ImageView photo_container;
    private Bitmap photo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_snap);

        editing = false;
        startCameraAction();

        photo_container = (ImageView) findViewById(R.id.cont_snap_activity_photo);
        tagCloudView = (GridView) findViewById(R.id.view_snap_activity_tag_cloud);

        //Delete when done testing.
        final List<String> mocks = new ArrayList<String>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.da_item,mocks);
        tagCloudView.setAdapter(adapter);


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
                        mocks.add(mocks.size(),editable.toString());
                        adapter.notifyDataSetChanged();
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
            startCameraIntent();
        }else {
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
            startCameraIntent();
        }

    }

}
