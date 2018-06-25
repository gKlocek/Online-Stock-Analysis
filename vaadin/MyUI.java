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
import java.util.Random;

import static com.vaadin.ArrayLastElement.back;
import static com.vaadin.NextPoint.getPointY;
import static com.vaadin.addon.charts.examples.AbstractVaadinChartExample.runWhileAttached;


@Theme("mytheme")
public class MyUI extends UI {
    final VerticalLayout layout = new VerticalLayout();
    List<MovingAverage> averages = new ArrayList<>();

    Random random = new Random();
    public static int timeTick = 0;
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Chart chart = new Chart();
        DataSeries series = new DataSeries();
        layout.setMargin(true);
        setContent(layout);
        final TextField number = new TextField();
        number.setCaption("Type your MovingAverageLength (seconds):");
        Button button1 = new Button("Simple");
        Button button2 = new Button("Exponential");
        chart.setWidth("1000px");
        Configuration configuration = chart.getConfiguration();
        configuration.getChart().setType(ChartType.SPLINE);
        configuration.getTitle().setText("EUR/USD");
        long acc = System.currentTimeMillis();
        XAxis xAxis = configuration.getxAxis();
        xAxis.setType(AxisType.DATETIME);

        xAxis.setMax(acc + 400000);
        xAxis.setTickPixelInterval(150);

        YAxis yAxis = configuration.getyAxis();
        yAxis.setTitle(new AxisTitle("Value"));
        yAxis.setPlotLines(new PlotLine(0, 1, new SolidColor("#808080")));

        configuration.getTooltip().setEnabled(true);
        configuration.getLegend().setEnabled(true);

        series.setPlotOptions(new PlotOptionsSpline());
        series.setName("Random data");


        button1.addClickListener(e -> {
            int period;
            if(number.getValue()!=null)
                period = Integer.parseInt(number.getValue());
            else
                period = 5;

            MovingAverage series2 = new SimpleMovingAverage(series,period);

            layout.removeComponent(chart);
            averages.add(series2);
            configuration.addSeries(back(averages).GetDataSeries());
            chart.setConfiguration(configuration);
            layout.addComponent(chart);
        });

        button2.addClickListener(e -> {
            int period;
            if(number.getValue()!=null)
                period = Integer.parseInt(number.getValue());
            else
                period = 5;

            MovingAverage series2 = new ExponentialMovingAverage(series,period);

            layout.removeComponent(chart);
            averages.add(series2);
            configuration.addSeries(back(averages).GetDataSeries());
            chart.setConfiguration(configuration);
            layout.addComponent(chart);
        });

        double prev = 0.5;
        for (int i = -50; i <= 0; i++) {
            final double y = getPointY(prev);
            series.add(new DataSeriesItem(
                    System.currentTimeMillis() + i * 1000, y));
            prev = (double) back(series).getY();
        }
        long start = System.currentTimeMillis();
        runWhileAttached(chart, new Runnable() {
            @Override
            public void run() {
                final double prev = (double) back(series.getData()).getY();
                final long x = System.currentTimeMillis();
                final double y = getPointY(prev);
                timeTick +=1;

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
        layout.addComponents(number,button1,button2,chart);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
