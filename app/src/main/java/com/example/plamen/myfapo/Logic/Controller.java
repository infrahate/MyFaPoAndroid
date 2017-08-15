package com.example.plamen.myfapo.Logic;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.plamen.myfapo.Data.DataLinksInterface;
import com.example.plamen.myfapo.Data.DataSourceInterface;
import com.example.plamen.myfapo.Data.SnapItem;
import com.example.plamen.myfapo.HelperTools;
import com.example.plamen.myfapo.MySingleton;
import com.example.plamen.myfapo.View.MainActivity;
import com.example.plamen.myfapo.View.ViewInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Plamen on 16.06.2017.
 */

public class Controller {

    //TO-DO: Figure out how to move volley requests to data section. Callbacks maybe?
    //TO-DO: Make a class that deals with providing objects the correct links. Further abstraction for making integration of load-balancing easier in the future.

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

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String lHash = preferences.getString("login_hash", "");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,"http://myfapo.jts-mr.com/services/get_timeline.php?login_hash="+lHash, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response){
                        ArrayList<SnapItem> listOfData = new ArrayList<>();
                        Log.i("timeline",response.toString());
                        for(int i=0;i<response.length();i++){
                            try {
                                //Get Single item from stack
                                JSONObject item = response.getJSONObject(String.valueOf(i));

                                //Get comments for item
                                ArrayList<String> comments = new ArrayList<>();
                                JSONObject joComments = item.getJSONObject("comments");
                                for (int j=joComments.length()-1;j>joComments.length()-3;j--) comments.add(HelperTools.fromHtml(joComments.getJSONObject(String.valueOf(j)).getString("user"))+" : "+HelperTools.fromHtml(joComments.getJSONObject(String.valueOf(j)).getString("comment")));

                                //Construct snap item
                                SnapItem snap = new SnapItem(item.getString("date_and_time"),"",item.getString("post_pic_link"),comments,item.getString("username"),item.getInt("likes"),item.getInt("dislikes"),item.getInt("post_id"),item.getInt("user_id"));
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
        view.startDetailActivity(testItem.getPost_id(),testItem.getPicture_link());
    }
}
