package main.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringVector {

    private int[][] vector1;
    private int[][] vector2;
    private char operator;
    private String stringOperations;

    public StringVector(int[][] vector1, int[][] vector2, char operator){
        this.vector1 = vector1;
        this.vector2 = vector2;
        this.operator = operator;
        this.stringOperations = Arrays.toString(vector1) +
                "||" +
                operator +
                "||" +
                Arrays.toString(vector2);
    }

    public StringVector(String stringOperations){
        String[] stringsVector12andOperator = stringOperations.split("\\|\\|");

        if (stringsVector12andOperator.length != 3){
//            System.out.println(Arrays.toString(stringsVector12andOperator));
            throw new UnsupportedOperationException("The Number Of Variabel must be 3");
        }
        this.vector1 = toMatrixArray(stringsVector12andOperator[0]);
        this.operator = stringsVector12andOperator[1].toCharArray()[0];
        this.vector2 = toMatrixArray(stringsVector12andOperator[2]);
        this.stringOperations = stringOperations;
    }

    public int[][] getVector1() {
        return vector1;
    }

    public void setVector1(int[][] vector1) {
        this.vector1 = vector1;
    }

    public int[][] getVector2() {
        return vector2;
    }

    public void setVector2(int[][] vector2) {
        this.vector2 = vector2;
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

    private static int[] toIntArray(String input) {
        String beforeSplit = input.replaceAll("\\[|\\]|\\s", "");
        String[] split = beforeSplit.split("\\,");
        int[] result = new int[split.length];
        for (int i = 0; i < split.length; i++) {
            result[i] = Integer.parseInt(split[i]);
        }
        return result;
    }

    private static int[][] toMatrixArray(String input) {
        char[] charArray = input.toCharArray(); // [[1, 2, 3], [4, 5, 6], [7, 8, 9], [10, 11, 12]]
        int maxArrayCount = input.split("\\], \\[").length;

        int currentValue = 0;
        int currentIndex = 0;
        int currentIndexNestedArray = 0;

// TODO : Improve This Algorithm
        int[][] results = new int[maxArrayCount][3];
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
