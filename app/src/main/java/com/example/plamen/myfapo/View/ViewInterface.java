package com.example.plamen.myfapo.View;

import com.example.plamen.myfapo.Data.SnapItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Plamen on 16.06.2017.
 */

public interface ViewInterface {
    void startDetailActivity(String dateAndTime, String message, int colorResource, String picture_link, ArrayList<String> comments, String posterName);
    void setUpAdapterAndView(List<SnapItem> listOfData);
}
