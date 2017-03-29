package model.dataset.loader;

import model.database.component.DBTimetable;
import model.database.loader.DBProblemLoader;
import org.junit.Assert;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.dataset.loader> created by : 
 * Name         : syafiq
 * Date / Time  : 03 November 2016, 9:50 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class TDatasetGenerator_Silent
{
    @SuppressWarnings("Duplicates") @Test public void initialize()
    {
        DBTimetable school = new DBTimetable(1, "Program Teknologi Informasi dan Ilmu Komputer Universitas Brawijaya", "PTIIK UB", "Jl. Veteran No.8, Ketawanggede, Kec. Lowokwaru, Kota Malang, Jawa Timur", "2013 - 2014", 0, 17, 5);
        Assert.assertNotNull(school);

        DBProblemLoader dbLoader = new DBProblemLoader(school);
        Assert.assertNotNull(dbLoader);

        dbLoader.loadData();

        DatasetGenerator dsLoader = new DatasetGenerator(dbLoader);
        Assert.assertNotNull(dsLoader);
    }
}
