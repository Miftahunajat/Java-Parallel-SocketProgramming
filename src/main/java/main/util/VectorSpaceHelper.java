package main.util;

public class VectorSpaceHelper {
//    public static int[] addTwoVector(int[] vector1, int[] vector2){
//        int[] result = new int[vector1.length];
//        for (int i = 0; i < vector1.length; i++) {
//            result[i] = vector1[i] + vector2[i];
//        }
//        return result;
//    }
//
//    public static int[] substractTwoVector(int[] vector1, int[] vector2){
//        int[] result = new int[vector1.length];
//        for (int i = 0; i < vector1.length; i++) {
//            result[i] = vector1[i] + vector2[i];
//        }
//        return result;
//    }
//
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
//
//    public static int[] multiplyTwoVector(StringVector stringVector){
//        return multiplyTwoVector(stringVector.getVector1(), stringVector.getVector2());
//    }
//
//    public static int[] divideTwoVector(int[] vector1, int[] vector2){
//        int[] result = new int[vector1.length];
//        for (int i = 0; i < vector1.length; i++) {
//            result[i] = vector1[i] + vector2[i];
//        }
//        return result;
//    }
}
