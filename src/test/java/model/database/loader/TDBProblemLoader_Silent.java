package model.database.loader;

import model.database.component.DBTimetable;
import org.junit.Assert;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.loader> created by : 
 * Name         : syafiq
 * Date / Time  : 25 October 2016, 8:17 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class TDBProblemLoader_Silent
{
    private DBTimetable     school;
    private DBProblemLoader loader;

    @SuppressWarnings("Duplicates") @Test public void initialize()
    {
        this.school = new DBTimetable(1, "Program Teknologi Informasi dan Ilmu Komputer Universitas Brawijaya", "PTIIK UB", "Jl. Veteran No.8, Ketawanggede, Kec. Lowokwaru, Kota Malang, Jawa Timur", "2013 - 2014", 0, 17, 5);
        Assert.assertNotNull(this.school);

        this.loader = new DBProblemLoader(school);
        Assert.assertNotNull(this.loader);

        this.loader.loadData();
    }
}
