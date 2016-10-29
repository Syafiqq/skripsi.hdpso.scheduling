package model.database.loader;

import model.database.component.DBSchool;
import model.database.component.DBSubject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.loader> created by : 
 * Name         : syafiq
 * Date / Time  : 25 October 2016, 11:14 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class TDBProblemLoader_Subject
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

    @Test public void subjectTest_001()
    {
        for(final DBSubject db_subject : this.loader.getSubjects().values())
        {
            Assert.assertNotNull(db_subject);

            System.out.println(db_subject);
        }
    }

    @SuppressWarnings("Duplicates") @Test public void subjectTestTimeOff_001()
    {
        for(final DBSubject db_subject : this.loader.getSubjects().values())
        {
            Assert.assertNotNull(db_subject);

            System.out.println(db_subject.getName());
            System.out.println(db_subject.getTimeoff());
            System.out.println();
            System.out.println();
        }
    }
}
