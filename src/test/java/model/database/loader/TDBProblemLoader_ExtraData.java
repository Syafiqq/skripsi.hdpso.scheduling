package model.database.loader;

import model.database.component.DBTimetable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.loader> created by : 
 * Name         : syafiq
 * Date / Time  : 29 October 2016, 9:17 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class TDBProblemLoader_ExtraData
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

    @Test public void test_001()
    {
        System.out.println("this.loader.getComplexLessonSize() = " + this.loader.getComplexLessonSize());
        System.out.println("this.loader.getTotalRegisteredTime() = " + this.loader.getTotalRegisteredTime());
        System.out.println("this.loader.getTimeDistributions() = " + this.loader.getTimeDistribution());
        System.out.println("this.loader.getOperatingClass() = " + this.loader.getOperatingClass());
        System.out.println("this.loader.getOperatingClassroom() = " + this.loader.getOperatingClassroom());
        System.out.println("this.loader.getOperatingLecture() = " + this.loader.getOperatingLecture());
        System.out.println("this.loader.getOperatingSubject() = " + this.loader.getOperatingSubject());
    }
}
