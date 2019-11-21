package main.util;

import java.util.Arrays;

public class StringVector {

    private double[][] matrixVector1;
    private double[][] matrixVector2;

    private double[] vector1;
    private double[] vector2;
    private char operator;
    private String stringOperations;

    public StringVector(double[][] vector1, double[][] vector2, char operator){
        this.matrixVector1 = vector1;
        this.matrixVector2 = vector2;
        this.operator = operator;
        this.stringOperations = Arrays.deepToString(vector1) +
                "||" +
                operator +
                "||" +
                Arrays.deepToString(vector2);
    }

    public StringVector(double[] vector1, double[] vector2, char operator){
        this.vector1 = vector1;
        this.vector2 = vector2;
        this.operator = operator;
        this.stringOperations = Arrays.toString(vector1) +
                "||" +
                operator +
                "||" +
                Arrays.toString(vector2);
    }

    public StringVector(String stringOperations, boolean isMatrix){
        if (isMatrix) {
            String[] stringsVector12andOperator = stringOperations.split("\\|\\|");

            if (stringsVector12andOperator.length != 3) {
//            System.out.println(Arrays.toString(stringsVector12andOperator));
                throw new UnsupportedOperationException("The Number Of Variabel must be 3");
            }
            this.matrixVector1 = toMatrixArray(stringsVector12andOperator[0]);
            this.operator = stringsVector12andOperator[1].toCharArray()[0];
            this.matrixVector2 = toMatrixArray(stringsVector12andOperator[2]);
            this.stringOperations = stringOperations;
        } else {

        }
    }

    public StringVector(String stringOperations){
        String[] stringsVector12andOperator = stringOperations.split("\\|\\|");

        if (stringsVector12andOperator.length != 3) {
//            System.out.println(Arrays.toString(stringsVector12andOperator));
            throw new UnsupportedOperationException("The Number Of Variabel must be 3");
        }
        this.vector1 = toDoubleArray(stringsVector12andOperator[0]);
        this.operator = stringsVector12andOperator[1].toCharArray()[0];
        this.vector2 = toDoubleArray(stringsVector12andOperator[2]);
        this.stringOperations = stringOperations;
    }

    public double[][] getMatrixVector1() {
        return matrixVector1;
    }

    public void setMatrixVector1(double[][] matrixVector1) {
        this.matrixVector1 = matrixVector1;
    }

    public double[][] getMatrixVector2() {
        return matrixVector2;
    }

    public void setMatrixVector2(double[][] matrixVector2) {
        this.matrixVector2 = matrixVector2;
    }

    public char getOperator() {
        return operator;
    }

    public void setOperator(char operator) {
        this.operator = operator;
    }

    public String getStringOperations() {
        return stringOperations;
    }

    public void setStringOperations(String stringOperations) {
        this.stringOperations = stringOperations;
    }

    private static double[] toDoubleArray(String input) {
        String beforeSplit = input.replaceAll("\\[|\\]|\\s", "");
        String[] split = beforeSplit.split("\\,");
        double[] result = new double[split.length];
        for (int i = 0; i < split.length; i++) {
            result[i] = Integer.parseInt(split[i]);
        }
        return result;
    }


    private static double[][] toMatrixArray(String input) {
        char[] charArray = input.toCharArray(); // [[1, 2, 3], [4, 5, 6], [7, 8, 9], [10, 11, 12]]
        int maxArrayCount = input.split("\\], \\[").length;

        int currentValue = 0;
        int currentIndex = 0;
        int currentIndexNestedArray = 0;

// TODO : Improve This Algorithm
        double[][] results = new double[maxArrayCount][3];
        for (int i = 0; i < input.toCharArray().length; ++i) {
            if (charArray[i] >= '0' && charArray[i] <= '9'){
                currentValue *= 10;
                currentValue += charArray[i] - '0';
            }
            if (charArray[i] == ','  && charArray[i-1] != ']'){
                results[currentIndex][currentIndexNestedArray] = currentValue;
                currentIndexNestedArray += 1;
                currentValue = 0;
            }
            if (i == input.toCharArray().length - 1 || (charArray[i] == ']' && charArray[i+1] == ',')){
                results[currentIndex][currentIndexNestedArray] = currentValue;
                currentIndex+=1;
                currentIndexNestedArray = 0;
                currentValue = 0;
            }
        }
//        String[] splitted = input.split(", ");
//        int[][] result = new int[splitted.length][];
//        for (int i = 0; i < splitted.length; i++) {
//            result[i] = toIntArray(splitted[i]);
//        }
        return results;
    }
}
