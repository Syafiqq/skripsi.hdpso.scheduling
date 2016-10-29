package model.database.loader;

import model.database.component.DBLecture;
import model.database.component.DBSchool;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.loader> created by : 
 * Name         : syafiq
 * Date / Time  : 25 October 2016, 9:55 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class TDBProblemLoader_Lecture
{
    private DBSchool        school;
    private DBProblemLoader loader;

    @SuppressWarnings("Duplicates") @Before public void initialize()
    {
        this.school = new DBSchool(1, "Program Teknologi Informasi dan Ilmu Komputer Universitas Brawijaya", "PTIIK UB", "Jl. Veteran No.8, Ketawanggede, Kec. Lowokwaru, Kota Malang, Jawa Timur", "2013 - 2014", 0, 17, 5);
        Assert.assertNotNull(this.school);

        this.loader = new DBProblemLoader(school);
        Assert.assertNotNull(this.loader);

        this.loader.loadData();
    }

    @Test public void lectureTest_001()
    {
        for(final DBLecture db_lecture : this.loader.getLecturers().values())
        {
            Assert.assertNotNull(db_lecture);

            System.out.println(db_lecture);
        }
    }

    @SuppressWarnings("Duplicates") @Test public void lectureTestTimeOff_001()
    {
        for(final DBLecture db_lecture : this.loader.getLecturers().values())
        {
            Assert.assertNotNull(db_lecture);

            System.out.println(db_lecture.getName());
            System.out.println(db_lecture.getTimeoff());
            System.out.println();
            System.out.println();
        }
    }
}
