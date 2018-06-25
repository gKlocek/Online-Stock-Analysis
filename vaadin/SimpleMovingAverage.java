package com.vaadin;

import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.DoubleConsumer;

import static com.vaadin.ArrayLastElement.*;
import static com.vaadin.SeriesToYList.GetYList;
import static java.lang.Integer.max;
import static java.util.Collections.reverse;


public class SimpleMovingAverage extends MovingAverage {
    public SimpleMovingAverage(ArrayList<Double> yList) {
        accRes = 0;
        pointsYqueue = new LinkedList<>();
        for (double it : yList) {
            pointsYqueue.addLast(it);
            accRes += it;
        }
        accRes = accRes/(double) yList.size();
        data = new DataSeries();
    }

    public SimpleMovingAverage(DataSeries series,int n) {
        this(GetYList(series,n));
    }

    @Override
    double Calc(double y) {
        double div = pointsYqueue.size();
        accRes += y/div;
        accRes -= ( pointsYqueue.pollLast() )/div;

        double res = accRes;
        return res;
    }
}
