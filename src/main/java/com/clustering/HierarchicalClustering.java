package com.clustering;

import com.bayudwiyansatria.mat.Vector;

import java.util.Arrays;

public class HierarchicalClustering {

    public int[] optimizedCentroidLinkage(double[][] data, int numberOfCluster){
        int[] clusters = new int[data.length];
        double[][] centroids = new double[data.length][data[0].length];
        int currentClusters = data.length;
        for(int i = 0; i < data.length; i++) {
            clusters[i] = i;
            centroids[i] = data[i];
        }

        do {
            double minDistance = Double.MAX_VALUE;
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data.length; j++) {
                    if (centroids[i] == centroids[j]) continue;
                    double distance = new Vector().getDistance(centroids[i], centroids[j] );
                }
            }
             currentClusters--;
        }while (currentClusters < numberOfCluster);
//        new Vector().getDistance()
        return new int[]{0};
    }

    public int[] SingleLinkage(double[][] data, int NumberOfCluster) {
        double[][] distanceMetric = new com.bayudwiyansatria.mat.Vector().getDistanceMetric(data);
        int[] clusters = new int[data.length];
        int length = data.length;

        for(int i = 0; i < data.length; i++) {
            clusters[i] = i;
        }

        int counter = 0;
        do {
            System.out.println(length);
            int x = 1;
            int y = 0;
            double distance = distanceMetric[x - 1][y];

            for(int i = 0; i < (data.length-1); ++i) {
                for(int j = 0; j <= i; ++j) {
                    if (distance < 0.0 || distanceMetric[i][j] < distance && distanceMetric[i][j] > -1.0) {
                        distance = distanceMetric[i][j];
                        x = i + 1;
                        y = j;
                    }
                }
            }

            int left;
            int right;
            if (clusters[x] < clusters[y]) {
                left = clusters[y];
                right = clusters[x];
            } else {
                left = clusters[x];
                right = clusters[y];
            }

            int[] position = new com.bayudwiyansatria.utils.Utils().getFind(clusters, "=", left);

            for(int i = 0; i < position.length; ++i) {
                clusters[position[i]] = right;
            }

            position = new com.bayudwiyansatria.utils.Utils().getFind(clusters, "=", right);
            --length;

            for(int i = 0; i < position.length; ++i) {
                for(int j = 0; j < position.length; ++j) {
                    if (i != j) {
                        if (position[i] > position[j]) {
                            distanceMetric[position[i] - 1][position[j]] = -1.0;
                        } else {
                            distanceMetric[position[j] - 1][position[i]] = -1.0;
                        }
                    }
                }
            }

            for(int i = 0; i < data.length - 1; ++i) {
                double member = -1.0;
                double value = -1.0;

                for(int j = 0; j < position.length; ++j) {
                    if (i + 1 > position[j]) {
                        value = distanceMetric[i][position[j]];
                    } else if (i + 1 < position[j]) {
                        value = distanceMetric[position[j] - 1][i + 1];
                    }

                    if (member < 0.0 || value < member) {
                        member = value;
                    }
                }

                if (member > -1.0) {
                    for(int j = 0; j < position.length; ++j) {
                        if (i + 1 > position[j]) {
                            distanceMetric[i][position[j]] = member;
                        } else if (i + 1 < position[j]) {
                            distanceMetric[position[j] - 1][i + 1] = member;
                        }
                    }
                }
            }
        } while(length > NumberOfCluster);

        clusters = this.getNormalLabel(clusters);

        return clusters;
    }

    public int[] SingleLinkage(int[][] data, int NumberOfCluster) {
        int[] clusters = this.SingleLinkage(new com.bayudwiyansatria.utils.Utils().int_to_double(data), NumberOfCluster);
        return clusters;
    }

    public int[] SingleLinkage(String[][] data, int NumberOfCluster){
        int cluster[] = null;
        return cluster;
    }

    public int[] CentroidLinkage(double[][] data, int NumberOfCluster) {
        double[][] distanceMetric = new com.bayudwiyansatria.mat.Mat().getDistanceMetric(data);
        int[] clusters = new int[data.length];
        double[][] newData = new com.bayudwiyansatria.mat.Mat().copyArray(data);
        int length = data.length;

        for(int i = 0; i < data.length; i++) {
            clusters[i] = i;
        }

        while(length > NumberOfCluster) {
            int x = 1;
            int y = 0;
            double distance = distanceMetric[x - 1][y];

            for(int i = 0; i < data.length - 1; ++i) {
                for(int j = 0; j <= i; ++j) {
                    if (distance < 0.0 || distanceMetric[i][j] < distance && distanceMetric[i][j] > -1.0) {
                        distance = distanceMetric[i][j];
                        x = i + 1;
                        y = j;
                    }
                }
            }

            int left;
            int right;
            if (clusters[x] < clusters[y]) {
                left = clusters[y];
                right = clusters[x];
            } else {
                left = clusters[x];
                right = clusters[y];
            }

            int[] position = new com.bayudwiyansatria.utils.Utils().getFind(clusters, "=", left);

            for(int i = 0; i < position.length; ++i) {
                clusters[position[i]] = right;
            }

            position = new com.bayudwiyansatria.utils.Utils().getFind(clusters, "=", right);
            --length;

            for(int i = 0; i < position.length - 1; ++i) {
                for(int j = i + 1; j < position.length; ++j) {
                    if (position[i] > position[j]) {
                        distanceMetric[position[i] - 1][position[j]] = -1.0;
                    } else {
                        distanceMetric[position[j] - 1][position[i]] = -1.0;
                    }
                }
            }

            System.out.println(length);
            clusters = this.getNormalLabel(clusters);

            int[][] member = new int[length][];
            int[] nMember = new int[length];
            double[][] centroid = new double[length][data[0].length];
            System.arraycopy(newData, 0, centroid, 0, right);

            for(int i = 0; i < length; ++i) {
                member[i] = new com.bayudwiyansatria.utils.Utils().getFind(clusters, "=", i);
                nMember[i] = member[i].length;
            }

            double[][] tmp = new double[nMember[right]][];

            for(int i = 0; i < nMember[right]; ++i) {
                tmp[i] = data[member[right][i]];
            }

            centroid[right] = this.getCentroid(tmp);
            System.arraycopy(newData, right + 1, centroid, right + 1, left - right - 1);
            System.arraycopy(newData, left + 1, centroid, left, length - left);
            newData = centroid;
            x = right;

            for(y = 0; y < length; ++y) {
                if (x != y) {
                    double DistanceBetweenCentroid = new com.bayudwiyansatria.mat.Mat().getDistance(newData[x], newData[y]);

                    for(int i = 0; i < nMember[x]; ++i) {
                        for(int j = 0; j < nMember[y]; ++j) {
                            if (member[x][i] > member[y][j]) {
                                distanceMetric[member[x][i] - 1][member[y][j]] = DistanceBetweenCentroid;
                            } else {
                                distanceMetric[member[y][j] - 1][member[x][i]] = DistanceBetweenCentroid;
                            }
                        }
                    }
                }
            }
        }

        return clusters;
    }

    public int[] CentroidLinkage(int[][] data, int NumberOfCluster) {
        int[] clusters = this.CentroidLinkage(new com.bayudwiyansatria.utils.Utils().int_to_double(data), NumberOfCluster);
        return clusters;
    }

    public int[] CentroidLinkage(String[][] data, int NumberOfCluster){
        int cluster[] = null;
        return cluster;
    }

    public int[] CompleteLinkage(double[][] data, int NumberOfCluster) {
        double[][] distanceMetric = new com.bayudwiyansatria.mat.Mat().getDistanceMetric(data);
        double[][] distanceTable = new com.bayudwiyansatria.mat.Mat().copyArray(distanceMetric);
        int[] clusters = new int[data.length];
        int length = data.length;

        for(int i = 0; i < data.length; i++) {
            clusters[i] = i;
        }

        do {
            int x = 1;
            int y = 0;
            double distance = distanceMetric[x - 1][y];

            for(int i = 0; i < data.length - 1; ++i) {
                for(int j = 0; j <= i; ++j) {
                    if (distance < 0.0 || distanceMetric[i][j] < distance && distanceMetric[i][j] > -1.0) {
                        distance = distanceMetric[i][j];
                        x = i + 1;
                        y = j;
                    }
                }
            }

            int left;
            int right;
            if (clusters[x] < clusters[y]) {
                left = clusters[y];
                right = clusters[x];
            } else {
                left = clusters[x];
                right = clusters[y];
            }

            int[] cluster = new com.bayudwiyansatria.utils.Utils().getFind(clusters, "=", left);

            for (int item : cluster) {
                clusters[item] = right;
            }

            cluster = new com.bayudwiyansatria.utils.Utils().getFind(clusters, "=", right);
            --length;

            for(int i = 0; i < cluster.length - 1; ++i) {
                for(int j = i + 1; j < cluster.length; ++j) {
                    if (cluster[i] > cluster[j]) {
                        distanceMetric[cluster[i] - 1][cluster[j]] = -1.0;
                    } else {
                        distanceMetric[cluster[j] - 1][cluster[i]] = -1.0;
                    }
                }
            }

            clusters = this.getNormalLabel(clusters);
            int[][] member = new int[length][];
            int[] nMember = new int[length];

            for(int i = 0; i < length; ++i) {
                member[i] = new com.bayudwiyansatria.utils.Utils().getFind(clusters, "=", i);
                nMember[i] = member[i].length;
            }

            x = right;

            for(y = 0; y < length; ++y) {
                if (x != y) {
                    double maxValue = -1.0;

                    for(int i = 0; i < nMember[x]; ++i) {
                        for(int j = 0; j < nMember[y]; ++j) {
                            double value;
                            if (member[x][i] > member[y][j]) {
                                value = distanceTable[member[x][i] - 1][member[y][j]];
                            } else {
                                value = distanceTable[member[y][j] - 1][member[x][i]];
                            }

                            if (maxValue < 0.0 || value > maxValue) {
                                maxValue = value;
                            }
                        }
                    }

                    for(int i = 0; i < nMember[x]; ++i) {
                        for(int j = 0; j < nMember[y]; ++j) {
                            if (member[x][i] > member[y][j]) {
                                distanceMetric[member[x][i] - 1][member[y][j]] = maxValue;
                            } else {
                                distanceMetric[member[y][j] - 1][member[x][i]] = maxValue;
                            }
                        }
                    }
                }
            }
        } while(length > NumberOfCluster);
        return clusters;
    }

    public int[] CompleteLinkage(int[][] data, int NumberOfCluster) {
        int[] clusters = this.CompleteLinkage(new com.bayudwiyansatria.utils.Utils().int_to_double(data), NumberOfCluster);
        return clusters;
    }

    public int[] CompleteLinkage(String[][] data, int NumberOfCluster){
        int cluster[] = null;
        return cluster;
    }

    public int[] AverageLinkage(double[][] data, int NumberOfCluster){
        double[][] distanceMetric = new com.bayudwiyansatria.mat.Mat().getDistanceMetric(data);
        double[][] distanceTable = new com.bayudwiyansatria.mat.Mat().copyArray(distanceMetric);
        int[] clusters = new int[data.length];
        int length = data.length;

        for(int i = 0; i < data.length; i++) {
            clusters[i] = i;
        }

        do {
            int x = 1;
            int y = 0;
            double distance = distanceMetric[x - 1][y];

            for(int i = 0; i < data.length - 1; ++i) {
                for(int j = 0; j <= i; ++j) {
                    if (distance < 0.0 || distanceMetric[i][j] < distance && distanceMetric[i][j] > -1.0) {
                        distance = distanceMetric[i][j];
                        x = i + 1;
                        y = j;
                    }
                }
            }

            int left;
            int right;
            if (clusters[x] < clusters[y]) {
                left = clusters[y];
                right = clusters[x];
            } else {
                left = clusters[x];
                right = clusters[y];
            }

            int[] cluster = new com.bayudwiyansatria.utils.Utils().getFind(clusters, "=", left);

            for(int i = 0; i < cluster.length; ++i) {
                clusters[cluster[i]] = right;
            }

            cluster = new com.bayudwiyansatria.utils.Utils().getFind(clusters, "=", right);
            --length;

            for(int i = 0; i < cluster.length - 1; ++i) {
                for(int j = i + 1; j < cluster.length; ++j) {
                    if (cluster[i] > cluster[j]) {
                        distanceMetric[cluster[i] - 1][cluster[j]] = -1.0;
                    } else {
                        distanceMetric[cluster[j] - 1][cluster[i]] = -1.0;
                    }
                }
            }

            clusters = this.getNormalLabel(clusters);
            int[][] member = new int[length][];
            int[] nMember = new int[length];

            for(int i = 0; i < length; ++i) {
                member[i] = new com.bayudwiyansatria.utils.Utils().getFind(clusters, "=", i);
                nMember[i] = member[i].length;
            }

            x = right;

            for(y = 0; y < length; ++y) {
                if (x != y) {
                    double mean = 0.0;

                    for(int i = 0; i < nMember[x]; ++i) {
                        for(int j = 0; j < nMember[y]; ++j) {
                            double value;
                            if (member[x][i] > member[y][j]) {
                                value = distanceTable[member[x][i] - 1][member[y][j]];
                            } else {
                                value = distanceTable[member[y][j] - 1][member[x][i]];
                            }

                            mean = mean + value;
                        }
                    }

                    mean = mean / (nMember[x] * nMember[y]);

                    for(int i = 0; i < nMember[x]; ++i) {
                        for(int j = 0; j < nMember[y]; ++j) {
                            if (member[x][i] > member[y][j]) {
                                distanceMetric[member[x][i] - 1][member[y][j]] = mean;
                            } else {
                                distanceMetric[member[y][j] - 1][member[x][i]] = mean;
                            }
                        }
                    }
                }
            }
        } while(length > NumberOfCluster);

        return clusters;
    }

    public int[] AverageLinkage(int[][] data, int NumberOfCluster){
        int[] clusters = this.AverageLinkage(new com.bayudwiyansatria.utils.Utils().int_to_double(data), NumberOfCluster);
        return clusters;
    }

    public int[] AverageLinkage(String[][] data, int NumberOfCluster){
        int cluster[] = null;
        return cluster;
    }

    public int[] getNormalLabel(int[] clusters) {
        int[] label = new com.bayudwiyansatria.mat.Mat().copyArray(clusters);
        int x = 0;
        int y = 0;
        int clusterCenter = -1;
        for(int i = 0; i < clusters.length; ++i) {
            if (label[i] > clusterCenter) {
                clusterCenter = label[i];
                ++y;
            }
        }

        do {
            int[] clusterLabel = new com.bayudwiyansatria.utils.Utils().getFind(label, "=", x);
            if (clusterLabel.length == 0) {
                int i;
                for(i = 0; i < clusters.length && label[i] < x; ++i) {
                }

                if (i < clusters.length) {
                    int[] cluster = new com.bayudwiyansatria.utils.Utils().getFind(clusters, "=", clusters[i]);

                    for(int j = 0; j < cluster.length; ++j) {
                        label[cluster[j]] = x;
                    }
                }
            }
            ++x;
        } while(x < y);

        return label;
    }

    public double[] getCentroid(double[][] data) {
        double[] centroid = new double[data[0].length];
        for(int i = 0; i < data[0].length; ++i) {
            double initCentroid = 0.0;
            for(int j = 0; j < data.length; ++j) {
                initCentroid = initCentroid + data[j][i];
            }
            centroid[i] = initCentroid / (double)data.length;
        }
        return centroid;
    }
}
