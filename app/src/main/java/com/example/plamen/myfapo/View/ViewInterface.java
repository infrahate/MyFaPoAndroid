package com.example.plamen.myfapo.View;

import android.content.Context;
import android.content.Intent;

import com.example.plamen.myfapo.Data.SnapItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Plamen on 16.06.2017.
 */

public interface ViewInterface {
    void startDetailActivity(int post_id,String link);
    void setUpAdapterAndView(List<SnapItem> listOfData);
    void startTakeSnapActivity(Context context);
}
