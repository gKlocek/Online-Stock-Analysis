package com.vaadin;

import javax.servlet.annotation.WebServlet;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;

import java.util.ArrayList;
import java.util.List;

import static com.vaadin.ArrayLastElement.back;
import static com.vaadin.NextPoint.getPointY;
import static com.vaadin.addon.charts.examples.AbstractVaadinChartExample.runWhileAttached;


@Theme("mytheme")
public class MyUI extends UI {
    final VerticalLayout layout = new VerticalLayout();
    List<MovingAverage> averages = new ArrayList<>();
    Chart chart;
    Configuration configuration;

    private void addNewSpline(MovingAverage series) {
        layout.removeComponent(chart);
        averages.add(series);
        configuration.addSeries(back(averages).GetDataSeries());
        chart.setConfiguration(configuration);
        layout.addComponent(chart);
    }

    private int readNumber(TextField number) {
        int period;
        if(number.getValue()!=null)
            period = Integer.parseInt(number.getValue());
        else
            period = 5;
        return period;
    }

    private void setAxes(long width,long pixelInterval) {
        long acc = System.currentTimeMillis();
        XAxis xAxis = configuration.getxAxis();
        xAxis.setType(AxisType.DATETIME);
        xAxis.setMax(acc + width);
        xAxis.setTickPixelInterval(pixelInterval);

        YAxis yAxis = configuration.getyAxis();
        yAxis.setTitle(new AxisTitle("Value ($)"));
        yAxis.setPlotLines(new PlotLine(0, 1, new SolidColor("#808080")));
    }

    private void initSeries(DataSeries series,int ticks) {
        double prev = 1;
        for (int i = -ticks; i <= 0; i++) {
            final double y = getPointY(prev);
            series.add(new DataSeriesItem(
                    System.currentTimeMillis() + i * 1000, y));
            prev = (double) back(series).getY();
        }
    }

    public static int timeTick = 0;
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        chart = new Chart();
        DataSeries series = new DataSeries();
        layout.setMargin(true);
        setContent(layout);
        final TextField number = new TextField();
        number.setCaption("Type your MovingAveragePeriodNumber (seconds):");

        Button button1 = new Button("Simple");
        Button button2 = new Button("Exponential");
        Button button3 = new Button("Weigthed");

        chart.setWidth("1000px");
        configuration = chart.getConfiguration();
        configuration.getChart().setType(ChartType.SPLINE);
        configuration.getTitle().setText("EUR/USD");
        configuration.getLegend().setEnabled(true);

        setAxes(400000,150);

        PlotOptionsLine myOpts = new PlotOptionsLine();
        myOpts.setDashStyle(DashStyle.SOLID);
        series.setPlotOptions(myOpts);
        series.setName("EUR/USD");


        button1.addClickListener(e -> {
            MovingAverage series2 = new SimpleMovingAverage(series,readNumber(number));
            addNewSpline(series2);
        });

        button2.addClickListener(e -> {
            MovingAverage series2 = new ExponentialMovingAverage(series,readNumber(number));
            addNewSpline(series2);
        });

        button3.addClickListener(e -> {
            MovingAverage series2 = new WeightedMovingAverage(series,readNumber(number));
            addNewSpline(series2);
        });

        initSeries(series,20);
        runWhileAttached(chart, new Runnable() {
            @Override
            public void run() {
                timeTick +=1;
                final double prev = (double) back(series.getData()).getY();
                final long x = System.currentTimeMillis();
                final double y = getPointY(prev);

                series.add(new DataSeriesItem(x, y), true, false);
                final double z = (double) back(series).getY();
                try {
                    for (MovingAverage it : averages) {
                        it.SetNextPoint(x,z);
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }, 1000, 1000);

        configuration.addSeries(series);
        chart.drawChart(configuration);
        layout.addComponents(number,button1,button2,button3,chart);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
