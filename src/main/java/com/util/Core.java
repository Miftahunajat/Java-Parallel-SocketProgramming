package com.util;


import java.io.*;
import java.util.List;

public class Core {
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
            retval[i] = 0.0;
            for (int j = 0; j < data.length; j++) {
//                System.out.println(i + " " + Arrays.toString(data[0]));
                retval[i]= retval[i] + data[j][i];
                counter++;
            }
            retval[i] = retval[i] / counter;
        }
        return retval;
    }

    public static double[][] joinMultipleArray(double[][] left, double[][] right){
        double[][] retval = new double[left.length + right.length][left[0].length];
        for (int i = 0; i < left.length; i++) {
//            retval[i] = left[i].clone();
            System.arraycopy(left[i], 0, retval[i], 0, left[i].length);

        }
        for (int i = left.length; i<(left.length + right.length) ; i++){
//            retval[i] = right[i-left.length].clone();
            System.arraycopy(right[i-left.length], 0, retval[i], 0, right[i-left.length].length);
        }
        return retval;
    }

    public static Double[][] joinMultipleArray(Double[][] left, Double[][] right){
        //Makek SYstem arraycopy
        Double[][] retval = new Double[left.length + right.length][left[0].length];
        for (int i = 0; i < left.length; i++) {
//            retval[i] = left[i].clone();
//            retval[i] = new Double[left[i].length];
            System.arraycopy(left[i], 0, retval[i], 0, left[i].length);
        }
        for (int i = left.length; i<(left.length + right.length) ; i++){
//            retval[i] = right[i-left.length].clone();
//            retval[i] = new Double[right[i-left.length].length];
            System.arraycopy(right[i-left.length], 0, retval[i], 0, right[i-left.length].length);
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

    public static Double[][] convertListArrayToDouble(List<Double[]> doubles) {
        Double[][] retVal = new Double[doubles.size()][];
        for (int i = 0; i < doubles.size(); i++) {
            retVal[i] = doubles.get(i);
        }
        return retVal;
    }

    public static int[] copyArray(int[] data) {
        int[] newArray = new int[data.length];
        System.arraycopy(data, 0, newArray, 0, data.length);
        return newArray;
    }

    public static double[] copyArray(double[] data) {
        double[] newArray = new double[data.length];
        System.arraycopy(data, 0, newArray, 0, data.length);
        return newArray;
    }

    public String[] copyArray(String[] data) {
        String[] newArray = new String[data.length];
        System.arraycopy(data, 0, newArray, 0, data.length);
        return newArray;
    }

    public int[][] copyArray(int[][] data) {
        int[][] newArray = new int[data.length][];

        for(int i = 0; i < data.length; ++i) {
            int dimension = data[i].length;
            newArray[i] = new int[dimension];
            System.arraycopy(data[i], 0, newArray[i], 0, dimension);
        }

        return newArray;
    }

    public double[][] copyArray(double[][] data) {
        double[][] newArray = new double[data.length][];

        for(int i = 0; i < data.length; ++i) {
            int dimension = data[i].length;
            newArray[i] = new double[dimension];
            System.arraycopy(data[i], 0, newArray[i], 0, dimension);
        }

        return newArray;
    }

    public String[][] copyArray(String[][] data) {
        String[][] newArray = new String[data.length][];

        for(int i = 0; i < data.length; ++i) {
            int dimension = data[i].length;
            newArray[i] = new String[dimension];
            System.arraycopy(data[i], 0, newArray[i], 0, dimension);
        }

        return newArray;
    }

    public static double[][] transposeMatrix(double[][] data) {
        int rows = data.length;
        int cols = data[0].length;
        double[][] output = new double[cols][rows];
        for(int i = 0; i < rows; ++i) {
            for(int j = 0; j < cols; ++j) {
                output[j][i] = data[i][j];
            }
        }
        return output;
    }

    public static double[] getMax(double[] data) {
        double[] max = new double[]{data[0], 0};
        for(int i = 1; i < data.length; ++i) {
            if (data[i] > max[0]) {
                max[0] = data[i];
                max[1] = i;
            }
        }
        return max;
    }
}
