package com.util;


import com.bayudwiyansatria.io.IO;
import com.bayudwiyansatria.math.Math;

import java.io.*;

public class Core {
    private static Math math;
    private static IO io;

    public static Math getMatInstance(){
        if (math == null) math = new Math();
        return math;
    }

    public static IO getIoInstance(){
        if (io == null) io = new IO();
        return io;
    }





    public static double[][] readLargeCSV(String filePath){
        try {
            File inputF = new File(filePath);
            InputStream inputStream = new FileInputStream(inputF);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            double[][] results = bufferedReader.lines().map(line -> {
                String[] p = line.split(",");
                double[] item = new double[p.length];
                for (int i = 0; i < item.length; i++) {
                    item[i] = Double.parseDouble(p[i]);
                }
                return item;
            }).toArray(double[][]::new);
            return results;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Double[][] readLargeCSVWrapper(String filePath){
        try {
            File inputF = new File(filePath);
            InputStream inputStream = new FileInputStream(inputF);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            Double[][] results = bufferedReader.lines().map(line -> {
                String[] p = line.split(",");
                Double[] item = new Double[p.length];
                for (int i = 0; i < item.length; i++) {
                    item[i] = Double.parseDouble(p[i]);
                }
                return item;
            }).toArray(Double[][]::new);
            return results;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static double[] getCentroidsFromDouble(double[][] data){
        double[] retval = new double[data[0].length];
        for (int i = 0; i < data[0].length; i++) {
            double counter = 0;
            for (int j = 0; j < data.length; j++) {
                retval[i]+=data[j][i];
                counter++;
            }
            retval[i] = retval[i] / counter;
        }
        return retval;
    }

    public static Double[] getCentroidsFromDouble(Double[][] data){
        Double[] retval = new Double[data[0].length];
        for (int i = 0; i < data[0].length; i++) {
            double counter = 0;
            for (int j = 0; j < data.length; j++) {
                retval[i]+=data[j][i];
                counter++;
            }
            retval[i] = retval[i] / counter;
        }
        return retval;
    }

    public static double[][] joinMultipleArray(double[][] left, double[][] right){
        double[][] retval = new double[left.length + right.length][];
        for (int i = 0; i < left.length; i++) {
            retval[i] = left[i].clone();
        }
        for (int i = left.length; i<(left.length + right.length) ; i++){
            retval[i] = right[i-left.length].clone();
        }
        return retval;
    }

    public static double[] joinMultipleArray(double[] left, double[] right){
        double[] retval = new double[left.length + right.length];
        for (int i = 0; i < left.length; i++) {
            retval[i] = left[i];
        }
        for (int i = left.length; i<(left.length + right.length) ; i++){
            retval[i] = right[i-left.length];
        }
        return retval;
    }

    public static double[][] deepCopyIntMatrix(double[][] input) {
        if (input == null)
            return null;
        double[][] result = new double[input.length][];
        for (int r = 0; r < input.length; r++) {
            result[r] = input[r].clone();
        }
        return result;
    }
}
