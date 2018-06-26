package com.vaadin;

import com.vaadin.addon.charts.model.DashStyle;
import com.vaadin.addon.charts.model.DataLabels;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.PlotOptionsLine;

import java.util.ArrayList;
import java.util.LinkedList;

import static com.vaadin.SeriesToYList.GetYList;

public class WeightedMovingAverage extends MovingAverage {
    public WeightedMovingAverage(DataSeries series, int n) {
        //this(GetYList(series,n));
        ArrayList<Double> yPoint = GetYList(series,n);
        accRes = 0;
        pointsYqueue = new LinkedList<>();
        for(double it : yPoint) {
            pointsYqueue.addLast(it);
        }
        data = new DataSeries();
        SetName("Weighted");
        SetPlotOptions();
    }
    @Override
    double Calc(double y) {
        pointsYqueue.pollLast();
        double num=0;
        double den=0;
        int pom = 1;
        for (double it : pointsYqueue) {
            num += pom*it;
            den += pom;
            pom ++;
        }
        //accRes = num/den;

        return num/den;
    }

    void SetPlotOptions() {
        opt = new PlotOptionsLine();
        opt.setDataLabels(new DataLabels(false));
        opt.setDashStyle(DashStyle.DASHDOT);
        data.setPlotOptions(opt);
    }
}
