package model.database.loader;

import model.database.component.DBClassroom;
import model.database.component.DBTimetable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.loader> created by : 
 * Name         : syafiq
 * Date / Time  : 25 October 2016, 8:04 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class TDBProblemLoader_Classroom
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

    @Test public void classroomTest_001()
    {
        for(final DBClassroom db_classroom : this.loader.getClassrooms().values())
        {
            Assert.assertNotNull(db_classroom);

            System.out.println(db_classroom);
        }
    }

    @SuppressWarnings("Duplicates") @Test public void classroomTestTimeOff_001()
    {
        for(final DBClassroom db_classroom : this.loader.getClassrooms().values())
        {
            Assert.assertNotNull(db_classroom);

            System.out.println(db_classroom.getId());
            System.out.println(db_classroom.getName());
            System.out.println(db_classroom.getTimeoff());
            System.out.println();
            System.out.println();
        }
    }
}
