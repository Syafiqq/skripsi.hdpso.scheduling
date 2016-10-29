package misc.math;

import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <misc.math> created by : 
 * Name         : syafiq
 * Date / Time  : 25 October 2016, 7:06 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class Logarithm
{
    @Test public void test_fastMath_001()
    {
        final int TEST_SIZE = 700;
        for(int i = -1, is = TEST_SIZE; ++i < is; )
        {
            final double cnt = FastMath.log(i);
            System.out.printf("%d = %.0g\n", i, cnt == Double.NEGATIVE_INFINITY ? 0 : FastMath.ceil(cnt));
        }
    }

    @Test public void test_math_001()
    {
        final int TEST_SIZE = 700;
        for(int i = -1, is = TEST_SIZE; ++i < is; )
        {
            final double cnt = Math.log(i);
            System.out.printf("%d = %.0g\n", i, cnt == Double.NEGATIVE_INFINITY ? 0 : Math.ceil(cnt));
        }
    }
}
