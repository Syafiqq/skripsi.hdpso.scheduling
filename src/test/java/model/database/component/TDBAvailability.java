package model.database.component;

import org.junit.Assert;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.component> created by : 
 * Name         : syafiq
 * Date / Time  : 23 October 2016, 8:16 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class TDBAvailability
{
    @Test public void test_001()
    {
        final DBAvailability availability = new DBAvailability(1, "Available", .5f);
        Assert.assertNotNull(availability);

        System.out.println(availability);
    }
}
