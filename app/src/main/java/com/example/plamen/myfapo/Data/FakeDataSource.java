package com.example.plamen.myfapo.Data;

import android.app.LauncherActivity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.plamen.myfapo.MySingleton;
import com.example.plamen.myfapo.R;
import com.example.plamen.myfapo.View.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

/**
 * Created by Plamen on 16.06.2017.
 */

public class FakeDataSource implements DataSourceInterface {

    private static final int SIZE_OF_COLLECTION = 12;

    private final String[] datesAndTimes = {
            "6:30AM 06/01/2017",
            "9:26PM 04/22/2013",
            "2:01PM 12/02/2015",
            "2:43AM 09/7/2018",
    };

    private final String[] messages = {
            "Check out content like Fragmented Podcast to expose yourself to the knowledge, ideas, " +
                    "and opinions of experts in your field",
            "Look at Open Source Projects like Android Architecture Blueprints to see how experts" +
                    " design and build Apps",
            "Write lots of Code and Example Apps. Writing good Quality Code in an efficient manner "
                    + "is a Skill to be practiced like any other.",
            "If at first something doesn't make any sense, find another explanation. We all " +
                    "learn/teach different from each other. Find an explanation that speaks to you."
    };

    private final int[] colours = {
            R.color.RED,
            R.color.BLUE,
            R.color.GREEN,
            R.color.YELLOW
    };

    private final String posterNamer = "Alina";

    private final String pic_link = "http://www.jqueryscript.net/images/Add-Image-Or-Text-Watermarks-To-Images-with-jQuery-watermark.jpg";

    private final ArrayList<String> comments = new ArrayList<>();


    @Override
    public List<SnapItem> getListOfData() {
        ArrayList<SnapItem> listOfData = new ArrayList<>();


        Random random = new Random();
        for (int i = 0;i < 12;i++){

            int randOne = random.nextInt(4);
            int randTwo = random.nextInt(4);
            int randThree = random.nextInt(4);

            SnapItem snapItem = new SnapItem(
                    datesAndTimes[randOne],
                    messages[randTwo],
                    colours[randThree],
                    pic_link,
                    comments,
                    posterNamer
            );

            listOfData.add(snapItem);
        }
        return  listOfData;
    }

    @Override
    public List<SnapItem> getListOfData(Context context) {
        return null;
    }
}
