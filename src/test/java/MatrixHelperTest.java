import com.util.Core;
import com.util.StringVector;
import com.util.VectorSpaceHelper;
import org.junit.Test;
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
}
