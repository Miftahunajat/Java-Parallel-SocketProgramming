import com.miftahunajat.util.Core;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class IrisDataTest {
    @Test
    public void irisTest() throws IOException {
        double[][] irisData = Core.readLargeCSV("iris.csv");
        System.out.println(Arrays.deepToString(irisData));
    }
}
