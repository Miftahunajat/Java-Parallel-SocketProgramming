package com.util;

public class Distance {
    public static double getDistance(double[] p1, double[] p2) {
        double distance = 0.0;
        for(int i = 0; i < p1.length; ++i) {
            double difference = p2[i] - p1[i];
            distance += difference * difference;
        }
        return Math.sqrt(distance);
    }
}
