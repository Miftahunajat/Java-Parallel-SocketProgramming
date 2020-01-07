package com.util;

public class VectorSpaceHelper {

    public static double[][] substractTwoMatrices(double[][] vector1, double[][] vector2) throws Exception {
        if (vector1[0].length != vector2[0].length) throw new Exception("Dimensi Matrix Tidak Memenuhi");
        if (vector1.length != vector2.length) throw new Exception("Dimensi Matrix Tidak Memenuhi");

        double[][] results = new double[vector1.length][vector1[0].length];

        for (int i = 0; i < results.length; i++) {
            for (int j = 0; j < results[0].length; j++) {
                results[i][j] = vector1[i][j] - vector2[i][j];
            }
        }

        return results;
    }

    public static Double[][] substractTwoMatricesWrapper(double[][] vector1, double[][] vector2) throws Exception {
        if (vector1[0].length != vector2[0].length) throw new Exception("Dimensi Matrix Tidak Memenuhi");
        if (vector1.length != vector2.length) throw new Exception("Dimensi Matrix Tidak Memenuhi");

        Double[][] results = new Double[vector1.length][vector1[0].length];

        for (int i = 0; i < results.length; i++) {
            for (int j = 0; j < results[0].length; j++) {
                results[i][j] = vector1[i][j] - vector2[i][j];
            }
        }

        return results;
    }


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

    public static Double getDistances(Double[] vector1, Double[] vector2) {
        double retval = 0;
        for (int i = 0; i < vector1.length; i++) {
            retval += Math.pow((vector1[i] - vector2[i]),2);
        }
        return Math.sqrt(retval);
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

    public static Double[][] multiplyTwoMatrices(Double[][] vector1, Double[][] vector2) throws Exception {
        double sum = 0;
        Double[][] results = new Double[vector1.length][vector2[0].length];



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
