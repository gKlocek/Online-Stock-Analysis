package com.vaadin;

import java.util.Random;


public abstract class NextPoint {
    private static Random random = new Random();
    static double getPointY(double prev) {
        double y = random.nextDouble() - 0.5;
        y = y*0.1;
        double prevY = prev;
        y= Math.min(2,Math.max(prevY + y,0));
        return y;
    }
}
