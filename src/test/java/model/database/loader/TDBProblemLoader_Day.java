package model.database.loader;

import model.database.component.DBDay;
import model.database.component.DBTimetable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.loader> created by : 
 * Name         : syafiq
 * Date / Time  : 24 October 2016, 9:31 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class TDBProblemLoader_Day
{
    private DBTimetable     school;
    private DBProblemLoader loader;

    @SuppressWarnings("Duplicates") @Before public void initialize()
    {
        this.school = new DBTimetable(1, "Program Teknologi Informasi dan Ilmu Komputer Universitas Brawijaya", "PTIIK UB", "Jl. Veteran No.8, Ketawanggede, Kec. Lowokwaru, Kota Malang, Jawa Timur", "2013 - 2014", 0, 17, 5);
        Assert.assertNotNull(this.school);

        this.loader = new DBProblemLoader(school);
        Assert.assertNotNull(this.loader);

        this.loader.loadData();
    }

    @Test public void dayTest_001()
    {
        for(final DBDay day : this.loader.getDays().values())
        {
            Assert.assertNotNull(day);
            System.out.println(day);
        }
    }
}
