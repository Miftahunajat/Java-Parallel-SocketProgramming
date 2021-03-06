package com.miftahunajat.util;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class Utils {
    public static int[] getUnique(int[] data) {
        SortedSet<Integer> set = new TreeSet<>();
        int[] dataLabel = new int[data.length];
        for ( int datum : data ) {
            set.add ( datum );
        }
        Iterator<?> element = set.iterator();
        int i;
        for(i = 0; element.hasNext(); ++i) {
            Integer _tmp = (Integer)element.next();
            dataLabel[i] = _tmp;
        }
        int[] newData = new int[i];
        System.arraycopy(dataLabel, 0, newData, 0, i);
        return newData;
    }

    public static double[] initArray(int length, double init_value) {
        double[] newArray = null;
        if (length > 1) {
            newArray = new double[length];

            for(int i = 0; i < length; ++i) {
                newArray[i] = init_value;
            }
        } else {
            System.out.println("Array length must be greater than zero!");
        }

        return newArray;
    }

    public static double[][] initMatrixRandom(int rows, int cols, double min, double max) {
        double[][] newArray = null;
        if (rows > 0 && cols > 0) {
            newArray = new double[rows][cols];

            for(int i = 0; i < rows; ++i) {
                for(int j = 0; j < cols; ++j) {
                    newArray[i][j] = java.lang.Math.random() * (max - min) + min;
                }
            }
        } else {
            System.out.println("Array length must be greater than zero!");
        }

        return newArray;
    }

    public static double[] getCentroid(double[][] data) {
        double[] centroid = new double[data[0].length];
        for(int i = 0; i < data[0].length; ++i) {
            double initCentroid = 0.0;
            for(int j = 0; j < data.length; ++j) {
                initCentroid = initCentroid + data[j][i];
            }
            centroid[i] = initCentroid / (double)data.length;
        }
        return centroid;
    }

}
