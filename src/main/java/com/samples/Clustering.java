package com.samples;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;


public class Clustering {
    static int           countData;
    static int           countCluster;
    static String [][]   dataset    = new String[2500][2];
    static String [][][] cluster    = new String[2500][2500][2];
    static int           k;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        bacaFile("src/main/resources/2.5kbigdata.csv");
        ClusteringProcess();
//        launch(args);
    }
    
    public static void bacaFile(String lokasi_file) {
        countData  = 0;
                        
        try {
            FileReader fr = new FileReader(lokasi_file);
            BufferedReader br = new BufferedReader(fr);
            String text;
            
            while ((text = br.readLine()) != null) {
                String [] splitKalimat = text.split(",");
                dataset[countData] = splitKalimat;
                cluster[countData][0] = dataset[countData];
                countData++;           
            }
            countCluster = countData;
        } catch (IOException fnfe) {
            fnfe.getMessage();
        }
    }
    
    public static double findJarak(int j, int l, int current, int batas, 
            String[][] cluster1, String[][] cluster2){
        if (current == batas){
            return 0;
        }
        
        return Math.pow(Double.parseDouble(cluster1[j][current]) 
                - Double.parseDouble(cluster2[l][current]), 2) 
                +  findJarak(j, l, ++current, batas, cluster1, cluster2);
    }
    
    public static void ClusteringProcess() {
        ClusteringInput();
        
        while (countCluster > k) {
            ClusteringDistance();
        }
        PrintResult();
    }
    
    public static void ClusteringInput() {
        System.out.println("Masukkan K :");
        Scanner scanner = new Scanner(System.in);
        k               = scanner.nextInt();
    }
    
    public static String[][] findSecondCluster(int current) { 
        if (current >= countData) {
            return null;
        }
        
        if (cluster[current][0] == null) {
            findSecondCluster(++current);
        } 
        return cluster[current];
    }
    
    public static void ClusteringDistance() {
        
        for (int i = 0; i < countData; i++) {
            if (cluster[i][0] == null) {
                continue;
            }
            
            String [][] clusterTemp1 = cluster[i];
            double minJarak = 99999;
            String [][] clusterMin = new String [99][99];


            for (int m = 0; m < countData; m++) {
                if (cluster[m][0] == null || m == i) {
                    // Jika CLusternya tersebut sama maka lanjut
                    continue;
                }
                String [][] clusterTemp2 = findSecondCluster(m);
                double jarakTotalAntarCluster   = 0;
                double sumDataAntarCluster      = 0;
                
                for (int j = 0; j < clusterTemp1.length; j++) {
                    if (clusterTemp1[j][0] == null) {
                        break;
                    }

                    for (int l = 0; l < clusterTemp2.length; l++) {
                        if (clusterTemp2[l] == null || clusterTemp2[l][0] == null) {
                            break;
                        }

                        double jarakTitik = Math.sqrt(findJarak(j, l, 0, 
                                dataset[0].length, clusterTemp1, clusterTemp2));
                        jarakTotalAntarCluster += jarakTitik;
                        sumDataAntarCluster++;
                    }
                }
                double jarakAntarCluster = jarakTotalAntarCluster/sumDataAntarCluster;
                if (jarakAntarCluster < minJarak) {
                    minJarak = jarakAntarCluster;
                    clusterMin = clusterTemp2;
                }
            }
                        
            if (countCluster > k) {
                ClusteringFusion(clusterMin, i);
            } else {
                break;
            }
            
        }
    }
            
    public static void DataFusion(int  currentCluster, int i, 
            String [][] clusterMin, int j) {
        if (clusterMin[j][0] == null) {
            return;
        }
        
        cluster[currentCluster][i] = clusterMin[j];
        DataFusion(currentCluster, ++i, clusterMin, ++j);
    }
    
    public static void ClusteringFusion(String [][] clusterMin, 
            int currentCluster) {
        for (int i = 0; i < cluster[currentCluster].length; i++) {
            if (cluster[currentCluster][i][0] == null) {
                DataFusion(currentCluster, i, clusterMin, 0);
                --countCluster;
                
                for (int j = 0; j < countData; j++) {
                    if (cluster[j] == clusterMin) {
                        cluster[j][0] = null;
                        break;
                    }
                }
                break;
            }
            
        }
    }
    
    public static void PrintResult() {
        for (int i = 0; i < countData; i++) {
            if (cluster[i][0] == null) {
                continue;
            }
            
            System.out.println("\nCluster " + (i+1));
            for (int j = 0; j < cluster[i].length; j++) {
                if (cluster[i][j] == null || cluster[i][j][0] == null) {
                    break;
                }
                
                System.out.println(Arrays.toString(cluster[i][j]));
            }
        }
    }

}
