package com.example.plamen.myfapo.Data;

import android.content.Context;

import java.util.List;

/**
 * Created by Plamen on 16.06.2017.
 */

public interface DataSourceInterface {
    List<SnapItem> getListOfData();
    List<SnapItem> getListOfData(Context context);
}
