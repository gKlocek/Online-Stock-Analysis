package com.vaadin;

import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;

import java.util.ArrayList;

public class SeriesToYList {
    public static ArrayList<Double> GetYList(DataSeries ds,int n) {
        ArrayList<Double> ys = new ArrayList<>();
        for(DataSeriesItem it : ds.getData()) {
            ys.add((double) it.getY());
        }
        return new ArrayList<>(ys.subList(ys.size()-n,ys.size()));
    }

}
