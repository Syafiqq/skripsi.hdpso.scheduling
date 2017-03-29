package model.dataset.loader;

import java.util.Arrays;
import model.database.component.DBTimetable;
import model.database.loader.DBProblemLoader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.dataset.loader> created by : 
 * Name         : syafiq
 * Date / Time  : 08 November 2016, 8:13 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class TDatasetGenerator_ClusteredClassroomTime
{
    private DBTimetable      school;
    private DBProblemLoader  dbLoader;
    private DatasetGenerator dsLoader;

    @SuppressWarnings("Duplicates") @Before public void initialize()
    {
        this.school = new DBTimetable(1, "Program Teknologi Informasi dan Ilmu Komputer Universitas Brawijaya", "PTIIK UB", "Jl. Veteran No.8, Ketawanggede, Kec. Lowokwaru, Kota Malang, Jawa Timur", "2013 - 2014", 0, 17, 5);
        Assert.assertNotNull(this.school);

        this.dbLoader = new DBProblemLoader(school);
        Assert.assertNotNull(this.dbLoader);

        this.dbLoader.loadData();

        this.dsLoader = new DatasetGenerator(this.dbLoader);
        Assert.assertNotNull(this.dsLoader);
    }

    @Test public void testClassroomAvailableTime()
    {
        System.out.println("Classroom Available Time");

        int counter_classroom = -1;
        for(int classrooms[][] : this.dsLoader.getDataset().getClusteredClassroomTime())
        {
            System.out.printf("Classroom : [%d]\n", ++counter_classroom);
            int counter_day = -1;
            for(int days[] : classrooms)
            {
                System.out.printf("\tDay [%d] : %s\n", ++counter_day, Arrays.toString(days));
            }

        }
    }
}
