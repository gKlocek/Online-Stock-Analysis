package com.vaadin;

import com.vaadin.addon.charts.model.DashStyle;
import com.vaadin.addon.charts.model.DataLabels;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.PlotOptionsLine;

import java.util.*;

import static com.vaadin.ArrayLastElement.*;
import static com.vaadin.SeriesToYList.GetYList;
import static java.lang.StrictMath.exp;
import static java.lang.StrictMath.max;
import static java.lang.StrictMath.pow;


public class ExponentialMovingAverage extends MovingAverage {

    public ExponentialMovingAverage(DataSeries series, int range) {
        ArrayList<Double> yPoint = GetYList(series,range);
        acc = MyUI.timeTick;
        acc = max(acc-yPoint.size(),0);
        pointsYqueue = new LinkedList<>();
        for(double it : yPoint) {
            pointsYqueue.addLast(it);
        }
        data = new DataSeries();
        accRes = pointsYqueue.getLast();
        SetName("Exponential");
        //System.out.println("Series: " + data.getName() + " done");
        SetPlotOptions();
    }

    @Override
    public double Calc(double y) {
        pointsYqueue.pollLast();
        acc += 1;
        accRes = 0;
        double num = 0;
        double den = 0;
        int i =0;
        double a;
        for (double it : pointsYqueue) {
            a = 1/((acc + i)+1);
            num += pow((1-a),pointsYqueue.size()-1-i)*it;
            den += pow((1-a),pointsYqueue.size()-1-i);
            i++;
        }
        return num/den;
    }

    //@Override
    public void SetPlotOptions(){
        opt = new PlotOptionsLine();
        opt.setDataLabels(new DataLabels(false));
        opt.setDashStyle(DashStyle.DASHDOT);
        data.setPlotOptions(opt);
    }

}
