import main.util.VectorSpaceHelper;
import org.junit.Test;
import static org.junit.Assert.*;

public class MatrixHelperTest {
    @Test
    public void calculateMatrix() throws Exception {
        int mat1[][] = new int[][]{{1,2,3}, {4,5,6}};
        int mat2[][] = new int[][]{{9}, {8}, {7}};
        int[][] expedtedResults = new int[][]{ {46}, {118} };
        int[][] results = VectorSpaceHelper.multiplyTwoMatrices(mat1, mat2);
        assertArrayEquals(expedtedResults, results);
    }
}
