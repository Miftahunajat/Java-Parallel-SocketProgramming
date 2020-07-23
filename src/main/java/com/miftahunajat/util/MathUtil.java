package com.miftahunajat.util;

public class MathUtil {
    public static double[] division(double[] first, double[] second){
        int l1 = first.length;
        double[] res = new double[l1];
        if (first.length == second.length) {
            for(int c1 = 0; c1 < l1; ++c1) {
                res[c1] = first[c1] / second[c1];
            }
        } else {
            System.out.println("Recheck size of both array");
            System.exit(1);
        }

        return res;
    }
    public static double[] division(double[] first, int second){
        int l1 = first.length;
        double[] res = new double[l1];
        for(int c1 = 0; c1 < l1; ++c1)
            res[c1] = first[c1] / second;
        return res;
    }
}
