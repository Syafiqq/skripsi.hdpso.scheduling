package model.database.component;

import it.unimi.dsi.fastutil.objects.ObjectList;
import org.junit.Assert;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.component> created by : 
 * Name         : syafiq
 * Date / Time  : 24 October 2016, 7:31 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("unchecked") public class TDBClass
{
    @Test public void test_001()
    {
        final DBSchool school = new DBSchool(1, "Program Teknologi Informasi dan Ilmu Komputer Universitas Brawijaya", "PTIIK UB", "Jl. Veteran No.8, Ketawanggede, Kec. Lowokwaru, Kota Malang, Jawa Timur", "2013 - 2014", 0, 3, 2);
        Assert.assertNotNull(school);

        final DBAvailability availability = new DBAvailability(1, "Available", .5f);
        Assert.assertNotNull(availability);

        final DBDay day = new DBDay(1, 1, "Senin", "Sen", school);
        Assert.assertNotNull(day);

        final DBPeriod period = new DBPeriod(1, 1, "Senin", "Sen", "07:00:00", "07:50:00", school);
        Assert.assertNotNull(period);

        final DBClass                     class_1 = new DBClass(1, "1-TIF-A", school);
        ObjectList<ObjectList<DBTimeOff>> a       = class_1.getTimeoff().getAvailabilities();

        a.get(0).set(0, new DBTimeOff(1, day, period, availability));
        Assert.assertNotNull(a.get(0).get(0));
        Assert.assertNull(a.get(0).get(1));
        Assert.assertNull(a.get(0).get(2));
        Assert.assertNull(a.get(1).get(0));
        Assert.assertNull(a.get(1).get(1));
        Assert.assertNull(a.get(1).get(2));
    }
}
