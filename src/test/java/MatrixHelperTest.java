import com.util.Core;
import com.util.StringVector;
import com.util.VectorSpaceHelper;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import static org.junit.Assert.*;

public class MatrixHelperTest {
    @Test
    public void calculateMatrix() throws Exception {
        int mat1[][] = new int[][]{{1,2,3}, {4,5,6}};
        int mat2[][] = new int[][]{{9}, {8}, {7}};
        int[][] expedtedResults = new int[][]{ {46}, {118} };
        int[][] results = Core.getMatInstance().vectorMultiplication(mat1, mat2);
        assertArrayEquals(expedtedResults, results);
    }

    @Test
    public void calculateTimeOnMatrix() throws Exception {
        for (int i = 0; i < 600_000; i++) {

            int mat1[][] = new int[][]{{1,2,3}, {4,5,6}};
            int mat2[][] = new int[][]{{9}, {8}, {7}};
            int[][] expedtedResults = new int[][]{ {46}, {118} };
            int[][] results = VectorSpaceHelper.multiplyTwoMatrices(mat1, mat2);
            assertArrayEquals(expedtedResults, results);
        }
    }

    @Test
    public void calculateTimeOnMatrix2() throws Exception {
        for (int i = 0; i < 600_000; i++) {

            int[][] mat1 = new int[][]{{1,2,3}, {4,5,6}};
            int[][] mat2 = new int[][]{{9}, {8}, {7}};
            int[][] expedtedResults = new int[][]{ {46}, {118} };
            int[][] results = Core.getMatInstance().vectorMultiplication(mat1, mat2);
            assertArrayEquals(expedtedResults, results);
        }
    }

    @Test
    public void toMatrixArrayTest(){
        String test = "[[1, 2], [4, 5], [7, 8], [10, 11]]||*||[[1, 2, 3], [4, 5, 6], [7, 8, 9], [10, 11, 12]]";
        StringVector sv = new StringVector(test, true);
        double[][] expectedResults = new double[][]{{1, 2}, {4, 5}, {7, 8}, {10, 11}};
        assertArrayEquals(expectedResults, sv.getMatrixVector1());
    }

    @Test
    public void bigCalculation() throws Exception {
        double[][] mat1 = new com.bayudwiyansatria.mat.Mat().initArrayRandom(10,1000,1,1000.0);
        double[][] mat2 = new com.bayudwiyansatria.mat.Mat().initArrayRandom(1000,10_000,1,1000.0);
        for (int i = 0; i < 60; i++) {
            double[][] mat3 = VectorSpaceHelper.multiplyTwoMatrices(mat1, mat2);
        }
    }

    @Test
    public void bigConvertToString() throws Exception {
        double[][] mat1 = new com.bayudwiyansatria.mat.Mat().initArrayRandom(10,1000,1,1000.0);
        double[][] mat2 = new com.bayudwiyansatria.mat.Mat().initArrayRandom(1000,10_000,1,1000.0);
        double[][] mat3 = VectorSpaceHelper.multiplyTwoMatrices(mat1, mat2);
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ObjectOutputStream objectOutputStream =
                new ObjectOutputStream(byteArrayOutputStream);
        for (int i = 0; i < 60; i++) {
            objectOutputStream.writeObject(mat1);
            objectOutputStream.flush();
            final byte[] byteArray1 = byteArrayOutputStream.toByteArray();

            objectOutputStream.writeObject(mat2);
            objectOutputStream.flush();
            final byte[] byteArray2 = byteArrayOutputStream.toByteArray();

            final ByteArrayInputStream byteArrayInputStream =
                    new ByteArrayInputStream(byteArray1);
            final ObjectInputStream objectInputStream =
                    new ObjectInputStream(byteArrayInputStream);
            final double[][] stringArray2 = (double[][]) objectInputStream.readObject();
            System.out.println(Arrays.deepToString(stringArray2));

            assertArrayEquals(mat1, stringArray2);
//            System.out.println(mat1. + "||*||" + Arrays.deepToString(mat2));
        }

    }
}
