package com.vaadin;

import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static com.vaadin.ArrayLastElement.back;
import static com.vaadin.NextPoint.getPointY;


public abstract class MovingAverage {
    DataSeries data;
    LinkedList<Double> pointsYqueue;
    int acc;
    double accRes;
    void SetNextPoint(long x,double y) {
        pointsYqueue.addFirst(y);
        double nextY = Calc(y);
        data.add(new DataSeriesItem(x,nextY),true,false);
    }
    abstract double Calc(double y);

    DataSeries GetDataSeries() {
        return data;
    }

    List<DataSeriesItem> GetData() {
        return data.getData();
    }

    DataSeriesItem GetLast() {
        return back(data);
    }
}
