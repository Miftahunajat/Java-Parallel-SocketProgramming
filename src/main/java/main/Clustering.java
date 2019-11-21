package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.chart.NumberAxis;
//import javafx.scene.chart.ScatterChart;
//import javafx.scene.chart.XYChart;
//import javafx.stage.Stage;


public class Clustering {
    static int           countData;
    static int           countCluster;
    static String [][]   dataset    = new String[999][99];
    static String [][][] cluster    = new String[999][99][99];
    static int           k;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        bacaFile("iris.csv");
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

//    @Override
//    public void start(Stage stage) throws Exception {
//        stage.setTitle("Scatter Chart");
//        final NumberAxis xAxis = new NumberAxis(0, 200, 10);
//        final NumberAxis yAxis = new NumberAxis(0, 200, 10);
//        final ScatterChart<Number,Number> sc = new
//            ScatterChart<Number,Number>(xAxis,yAxis);
//        xAxis.setLabel("X");
//        yAxis.setLabel("Y");
//        sc.setTitle("Data with Average Linkage Algorithm");
//
//        XYChart.Series[] series = new XYChart.Series[k];
//
//        int p = 0;
//        for (int i = 0; i < countData; i++) {
//            if (cluster[i][0] == null) {
//                continue;
//            }
//
//            series[p] = new XYChart.Series();
//            series[p].setName("Cluster " + (i+1));
//            for (int j = 0; j < cluster[i].length; j++) {
//                if (cluster[i][j] == null || cluster[i][j][0] == null) {
//                    break;
//                }
//
//                series[p].getData().add(new XYChart.Data(Double.parseDouble(cluster[i][j][0]),
//                        Double.parseDouble(cluster[i][j][1])));
//            }
//            p++;
//        }
//
//        sc.getData().addAll(series);
//        Scene scene  = new Scene(sc, 500, 400);
//        stage.setScene(scene);
//        stage.show();
//    }
}
