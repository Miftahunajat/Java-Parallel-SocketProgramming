package com.clustering;

import com.samples.Clustering;
import com.util.Distance;
import com.util.Utils;

import java.util.Arrays;

public class ML {

    public double[] getVariance(double[][] data, int[] clusters) {
        int[] unique = Utils.getUnique(clusters);
        double[] Variance = new double[2];
        Arrays.fill(Variance, 0,2,0.0);
        double[] variance = new double[unique.length];
        double[][] centroid = new double[unique.length][data[0].length];
        int[] newUnique = new int[unique.length];
        double tmpl = 0.0;
        int[][] position = new int[unique.length][data.length];
        int[] initCentroid = new int[unique.length];
        Arrays.fill(initCentroid, 0, unique.length, 0);
        if (unique.length > 1) {
            int i;
            for(i = 0; i < data.length; ++i) {
                int cluster = clusters[i];
                position[cluster][initCentroid[cluster]] = i;
                initCentroid[cluster]++;
            }

            double distanceVector;
            double distance;
            for(i = 0; i < unique.length; ++i) {
                newUnique[i] = initCentroid[i];
                double[] tmp = Utils.initArray(data[0].length, 0.0);

                int j;
                for(j = 0; j < newUnique[i]; ++j) {
                    for(int t = 0; t < data[0].length; ++t) {
                        tmp[t] = tmp[t] + data[position[i][j]][t];
                    }
                }

                centroid[i] = new com.bayudwiyansatria.math.Math().Calculate(tmp, newUnique[i], "/");
                if (newUnique[i] == 1) {
                    variance[i] = 0.0;
                } else {
                    distanceVector = 0.0;

                    for(j = 0; j < newUnique[i]; ++j) {

                        distance = Distance.getDistance(data[position[i][j]], centroid[i]);
                        distanceVector += distance * distance;
                    }

                    variance[i] = distanceVector;
                }

                tmpl += variance[i];
            }

            double varianceWithin = tmpl / (double)(data.length - unique.length);
            distanceVector = 0.0;
            double[] grandMean = this.getCentroid(centroid);

            for(i = 0; i < unique.length; ++i) {
                distance = Distance.getDistance(centroid[i], grandMean);
                distanceVector += (double)newUnique[i] * distance * distance;
            }

            double varianceBetween = distanceVector / (double)(unique.length - 1);
            Variance[0] = varianceWithin;
            Variance[1] = varianceBetween;
        }

        return Variance;
    }

    public double[] getVariance(int[][] data, int[] clusters) {
        return this.getVariance(data, clusters);
    }

    public double[][] initCentroid(double[][] data, int NumberOfCluster) {
        double[][] centroid = new double[NumberOfCluster][data[0].length];
        double[][] min = new com.bayudwiyansatria.math.Math().getMin(data);
        double[][] max = new com.bayudwiyansatria.math.Math().getMax(data);

        for(int i = 0; i < NumberOfCluster; ++i) {
            for(int j = 0; j < data[0].length; ++j) {
                centroid[i][j] = new com.bayudwiyansatria.math.Math().getRandom(min[0][j], max[0][j]);
            }
        }

        return centroid;
    }

    public double[][] initCentroid(int[][] data, int NumberOfCluster) {
        double[][] centroid = new double[NumberOfCluster][data[0].length];
        int[][] min = new com.bayudwiyansatria.math.Math().getMin(data);
        int[][] max = new com.bayudwiyansatria.math.Math().getMax(data);

        for(int i = 0; i < NumberOfCluster; ++i) {
            for(int j = 0; j < data[0].length; ++j) {
                centroid[i][j] = new com.bayudwiyansatria.math.Math().getRandom(min[0][j], max[0][j]);
            }
        }

        return centroid;
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

    public double[] OptimalClusterNumber_Single(double[][] data, int Interval){
        int[] clusters = new int[data.length];
        int length = data.length;
        double[] output = new double[2];
        double[][] distanceMetric = new com.bayudwiyansatria.mat.Mat().getDistanceMetric(data);
        int NumberOfCluster = 2;
        double[] V = new com.bayudwiyansatria.mat.Mat().initArray(Interval + 1, 0.0);
        double[] density = new com.bayudwiyansatria.mat.Mat().initArray(Interval + 1, 0.0);
        int interval = Interval;

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

            int[] position = new com.bayudwiyansatria.utils.Utils().getFind(clusters, "=", left);

            for(int i = 0; i < position.length; ++i) {
                clusters[position[i]] = right;
            }

            clusters = this.getNormalLabel(clusters);
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

            if (length <= Interval + 1) {
                double[] variance = this.getVariance(data, clusters);
                V[interval] = variance[0] / variance[1];
                --interval;
            }
        } while(length > NumberOfCluster);

        for(int i = Interval - 1; i > 0; --i) {
            if (V[i - 1] >= V[i] && V[i + 1] > V[i]) {
                density[i] = V[i + 1] + V[i - 1] - 2.0 * V[i];
            }
        }

        double[] vMax = new com.bayudwiyansatria.math.Math().getMax(density);
        int optimum = (int)vMax[1] + 1;
        density[optimum - 1] = -1.0;
        double[] vRunner = new com.bayudwiyansatria.math.Math().getMax(density);
        double globalOptimum;
        if (vMax[0] == 0.0) {
            globalOptimum = 0.0;
        } else if (vRunner[0] == 0.0) {
            globalOptimum = 100.0;
        } else {
            globalOptimum = vMax[0] / vRunner[0];
        }

        output[0] = (double)optimum;
        output[1] = globalOptimum;
        return output;
    }

    public double[] OptimalClusterNumber_Centroid(double[][] data, int Interval) {
        int[] clusters = new int[data.length];
        double[][] newData = new com.bayudwiyansatria.mat.Mat().copyArray(data);
        int length = data.length;
        double[] output = new double[2];
        double[][] distanceMetric = new com.bayudwiyansatria.mat.Mat().getDistanceMetric(data);
        int NumberOfCluster = 2;
        double[] V = new com.bayudwiyansatria.mat.Mat().initArray(Interval + 1, 0.0);
        double[] density = new com.bayudwiyansatria.mat.Mat().initArray(Interval + 1, 0.0);
        int interval = Interval;

        int i;
        for(i = 0; i < data.length; clusters[i] = i++) {
        }

        double[] vMax;
        do {
            int x = 1;
            int y = 0;
            double distance = distanceMetric[x - 1][y];

            int j;
            for(i = 0; i < data.length - 1; ++i) {
                for(j = 0; j <= i; ++j) {
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

            for(i = 0; i < position.length; ++i) {
                clusters[position[i]] = right;
            }

            clusters = this.getNormalLabel(clusters);
            position = new com.bayudwiyansatria.utils.Utils().getFind(clusters, "=", right);
            --length;

            for(i = 0; i < position.length - 1; ++i) {
                for(j = i + 1; j < position.length; ++j) {
                    if (position[i] > position[j]) {
                        distanceMetric[position[i] - 1][position[j]] = -1.0;
                    } else {
                        distanceMetric[position[j] - 1][position[i]] = -1.0;
                    }
                }
            }

            int[][] member = new int[length][];
            int[] nMember = new int[length];
            double[][] centroid = new double[length][data[0].length];
            System.arraycopy(newData, 0, centroid, 0, right);

            for(i = 0; i < length; ++i) {
                member[i] = new com.bayudwiyansatria.utils.Utils().getFind(clusters, "=", i);
                nMember[i] = member[i].length;
            }

            double[][] tmp = new double[nMember[right]][];

            for(i = 0; i < nMember[right]; ++i) {
                tmp[i] = data[member[right][i]];
            }

            centroid[right] = this.getCentroid(tmp);
            System.arraycopy(newData, right + 1, centroid, right + 1, left - right - 1);
            System.arraycopy(newData, left + 1, centroid, left, length - left);
            newData = centroid;
            x = right;

            for(y = 0; y < length; ++y) {
                if (x != y) {
                    double DistanceBetweenCentroid = new com.bayudwiyansatria.mat.Mat().getDistanceRelative (newData[x], newData[y]);

                    for(i = 0; i < nMember[x]; ++i) {
                        for(j = 0; j < nMember[y]; ++j) {
                            if (member[x][i] > member[y][j]) {
                                distanceMetric[member[x][i] - 1][member[y][j]] = DistanceBetweenCentroid;
                            } else {
                                distanceMetric[member[y][j] - 1][member[x][i]] = DistanceBetweenCentroid;
                            }
                        }
                    }
                }
            }

            if (length <= Interval + 1) {
                vMax = this.getVariance(data, clusters);
                V[interval] = vMax[0] / vMax[1];
                --interval;
            }
        } while(length > NumberOfCluster);

        for(i = Interval - 1; i > 0; --i) {
            if (V[i - 1] > V[i] && V[i + 1] >= V[i]) {
                density[i] = V[i + 1] + V[i - 1] - 2.0 * V[i];
            }
        }

        vMax = new com.bayudwiyansatria.math.Math().getMax(density);
        int optimum = (int)vMax[1] + 1;
        density[optimum - 1] = -1.0;
        double[] vRunner = new com.bayudwiyansatria.math.Math().getMax(density);
        double globalOptimum;
        if (vMax[0] == 0.0) {
            globalOptimum = 0.0;
        } else if (vRunner[0] == 0.0) {
            globalOptimum = 100.0;
        } else {
            globalOptimum = vMax[0] / vRunner[0];
        }

        output[0] = (double)optimum;
        output[1] = globalOptimum;
        return output;
    }
}