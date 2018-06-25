package com.vaadin;

import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;

import java.util.ArrayList;
import java.util.List;

public class ArrayLastElement {
    static <T> T back(List<T> myList) {
        return  myList.get(myList.size()-1);
    }
    static DataSeriesItem back(DataSeries myData) {return  myData.get(myData.getData().size()-1);}
    static <T> T popBack(List<T> myList) {
        T res = back( myList);
        myList.remove(myList.size()-1);
        return res;
    }
}
