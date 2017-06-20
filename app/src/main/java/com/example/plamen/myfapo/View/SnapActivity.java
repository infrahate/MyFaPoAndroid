package com.example.plamen.myfapo.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.MultiAutoCompleteTextView;

import com.example.plamen.myfapo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Plamen on 20.06.2017.
 */

public class SnapActivity extends AppCompatActivity{

    private boolean editing;
    private MultiAutoCompleteTextView inputTagView;
    private GridView tagCloudView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_snap);

        editing = false;

        tagCloudView = (GridView) findViewById(R.id.view_snap_activity_tag_cloud);

        //Delete when done testing.
        final List<String> mocks = new ArrayList<>();
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

}
