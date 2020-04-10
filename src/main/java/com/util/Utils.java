package com.util;

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
}
