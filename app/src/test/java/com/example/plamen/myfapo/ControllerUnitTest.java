package com.example.plamen.myfapo;

import android.app.LauncherActivity;

import com.example.plamen.myfapo.Data.DataSourceInterface;
import com.example.plamen.myfapo.Data.SnapItem;
import com.example.plamen.myfapo.Logic.Controller;
import com.example.plamen.myfapo.View.ViewInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ControllerUnitTest {

    @Mock
    DataSourceInterface dataSrouce;

    @Mock
    ViewInterface view;

    private static final SnapItem testItem = new SnapItem(
            "6:30AM 06/01/2017",
            "afssag asg sag ",
            R.color.RED
    );

    Controller controller;

    @Before
    public void setUpTest(){
        MockitoAnnotations.initMocks(this);
        controller = new Controller(view,dataSrouce);
    }

    @Test
    public void onGetListDataSuccess() throws Exception {

        ArrayList<SnapItem> listOfData = new ArrayList<>();
        listOfData.add(testItem);

        Mockito.when(dataSrouce.getListOfData()).thenReturn(listOfData);

        controller.getListFromDataSource();

        Mockito.verify(view).setUpAdapterAndView(listOfData);
    }

    @Test
    public void onListItemClicked(){
        controller.onListItemClick(testItem);

        Mockito.verify(view).startDetailActivity(testItem.getDateAndTime(),testItem.getMessage(),testItem.getColorRes());
    }
}