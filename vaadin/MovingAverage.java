package com.vaadin;

import com.vaadin.addon.charts.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static com.vaadin.ArrayLastElement.back;
import static com.vaadin.NextPoint.getPointY;


public abstract class MovingAverage {
    DataSeries data;
    PlotOptionsLine opt;
    LinkedList<Double> pointsYqueue;
    int acc;
    double accRes;
    //@Override
    public void SetNextPoint(long x, double y) {
        pointsYqueue.addFirst(y);
        //double nextY = getPointY(y);
        double nextY = Calc(y);
        data.add(new DataSeriesItem(x,nextY),true,false);
    }
    abstract double Calc(double y);

    public DataSeries GetDataSeries() {
        return data;
    }

    public DataSeriesItem GetLast() {
        return back(data);
    }

    public void SetName(String averageType) {
        String period = Integer.toString(pointsYqueue.size());
        data.setName(averageType + " " + period);
    }

}
