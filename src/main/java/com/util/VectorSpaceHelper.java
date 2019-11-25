package com.util;

public class VectorSpaceHelper {
    public static int[][] multiplyTwoMatrices(int[][] vector1, int[][] vector2) throws Exception {
        int sum = 0;
        int[][] results = new int[vector1.length][vector2[0].length];


        if (vector1[0].length != vector2.length) throw new Exception("Dimensi Matrix Tidak Memenuhi");
        for (int i = 0; i < vector1.length; i++) {
            for (int j = 0; j < vector2[0].length; j++) {
                for (int k = 0; k < vector2.length; k++) {
                    sum = sum + vector1[i][k] * vector2[k][j];
                }

                results[i][j] = sum;
                sum = 0;
            }
        }

        return results;
    }

    public static double[] multiplyTwoMatrices(double[] vector1, double[] vector2) throws Exception {
        int sum = 0;
        if (vector1.length != vector2.length) throw new Exception("Dimensi Matrix Tidak Memenuhi");
        double[] results = new double[vector1.length];

        for (int i = 0; i < vector1.length; i++) {
            results[i] = vector1[i] * vector2[i];
        }
        return results;
    }

    public static double[][] multiplyTwoMatrices(double[][] vector1, double[][] vector2) throws Exception {
        double sum = 0;
        double[][] results = new double[vector1.length][vector2[0].length];



        if (vector1[0].length != vector2.length) throw new Exception("Dimensi Matrix Tidak Memenuhi");
        for (int i = 0; i < vector1.length; i++) {
            for (int j = 0; j < vector2[0].length; j++) {
                for (int k = 0; k < vector2.length; k++) {
                    sum = sum + vector1[i][k] * vector2[k][j];
                }

                results[i][j] = sum;
                sum = 0;
            }
        }

        return results;
    }
}
