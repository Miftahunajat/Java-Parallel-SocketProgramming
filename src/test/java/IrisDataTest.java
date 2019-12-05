import com.bayudwiyansatria.io.IO;
import com.thread.MultiThreadManager;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class IrisDataTest {
    @Test
    public void irisTest() throws IOException {
        double[][] irisData = new IO().readCSV_double("iris");
        System.out.println(Arrays.deepToString(irisData));
//        double[] a = Arrays.;
        MultiThreadManager mtm = MultiThreadManager.getInstance();
        for (int i = 0; i < irisData.length; i++) {
            for (int j = 0; j < irisData.length; j++) {
                mtm.startResult(Arrays.toString(irisData[i]) + "||*||"+Arrays.toString(irisData[j]));
            }
        }
    }
}