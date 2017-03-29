package model.database.component;

import org.junit.Assert;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.component> created by :
 * Name         : syafiq
 * Date / Time  : 23 October 2016, 11:24 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class TDBPeriod
{
    @Test public void test_001()
    {
        final DBTimetable school = new DBTimetable(1, "Program Teknologi Informasi dan Ilmu Komputer Universitas Brawijaya", "PTIIK UB", "Jl. Veteran No.8, Ketawanggede, Kec. Lowokwaru, Kota Malang, Jawa Timur", "2013 - 2014", 0, 17, 5);
        Assert.assertNotNull(school);

        final DBPeriod period = new DBPeriod(1, 1, "Senin", "Sen", "07:00:00", "07:50:00", school);
        Assert.assertNotNull(period);

        System.out.println(period);
    }
}
