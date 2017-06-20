package com.example.plamen.myfapo.Logic;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.plamen.myfapo.Data.DataSourceInterface;
import com.example.plamen.myfapo.Data.SnapItem;
import com.example.plamen.myfapo.MySingleton;
import com.example.plamen.myfapo.View.ViewInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Plamen on 16.06.2017.
 */

public class Controller {

    private ViewInterface view;
    private DataSourceInterface dataSource;
    private Context context;

    public Controller(ViewInterface view, DataSourceInterface dataSource,Context context) {
        this.view = view;
        this.dataSource = dataSource;
        this.context = context;

        getListFromDataSource();
    }

    public void getListFromDataSource() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,"http://myfapo.jts-mr.com/service.php", null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response){
                        ArrayList<SnapItem> listOfData = new ArrayList<>();
                        for(int i=0;i<response.length();i++){
                            try {
                                //Get Single item from stack
                                JSONObject item = response.getJSONObject(String.valueOf(i));

                                //Get comments for item
                                ArrayList<String> comments = new ArrayList<>();
                                JSONObject joComments = item.getJSONObject("comments");
                                for (int j=0;j<joComments.length();j++) comments.add(joComments.getJSONObject(String.valueOf(j)).getString("commentUser")+" : "+joComments.getJSONObject(String.valueOf(j)).getString("comment"));

                                //Construct snap item
                                SnapItem snap = new SnapItem("","",0,item.getString("sPostPic"),comments,item.getString("postUser"));
                                listOfData.add(snap);

                                //SetUp adapter with response data
                                view.setUpAdapterAndView(listOfData);

                            } catch (JSONException e) {
                                Log.e("Timeline get error","Help me!!!");
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.e("Volley Error","Error when getting timeline items");
                        error.printStackTrace();
                    }
                });
        MySingleton.getInstance(context).addToRequestque(request);
    }

    public void onListItemClick(SnapItem testItem) {
        view.startDetailActivity(testItem.getDateAndTime(),testItem.getMessage(),testItem.getColorRes(),testItem.getPicture_link(),testItem.getComments(),testItem.getPosterName());
    }
}
