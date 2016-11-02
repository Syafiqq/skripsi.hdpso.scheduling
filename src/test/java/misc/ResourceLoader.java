package misc;

import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <misc> created by : 
 * Name         : syafiq
 * Date / Time  : 24 October 2016, 9:11 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("ConstantConditions") public class ResourceLoader
{
    @Test public void test_001()
    {
        System.out.println(ClassLoader.getSystemClassLoader().getResource("db/db.mcrypt").getPath());
    }
}
