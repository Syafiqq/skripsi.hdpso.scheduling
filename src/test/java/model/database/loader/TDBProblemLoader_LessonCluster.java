package model.database.loader;

import java.util.Arrays;
import model.database.component.DBClassroom;
import model.database.component.DBLesson;
import model.database.component.DBLessonCluster;
import model.database.component.DBLessonGroup;
import model.database.component.DBSchool;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.loader> created by : 
 * Name         : syafiq
 * Date / Time  : 01 November 2016, 6:42 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("FieldCanBeLocal") public class TDBProblemLoader_LessonCluster
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

    @Test public void lesson_cluster_test001()
    {
        System.out.println(this.loader.getLessonCluster().size());
        for(DBLessonCluster a : this.loader.getLessonCluster())
        {
            System.out.println(Arrays.toString(a.getClassrooms().stream().mapToInt(DBClassroom::getId).toArray()));
            System.out.println("\t" + Arrays.toString(a.getLessons().stream().mapToInt(DBLesson::getId).toArray()));
        }
    }

    @Test public void lesson_group_test001()
    {
        System.out.println(this.loader.getLessonCluster().size());
        for(DBLessonCluster a : this.loader.getLessonCluster())
        {
            System.out.println(Arrays.toString(a.getClassrooms().stream().mapToInt(DBClassroom::getId).toArray()));
            System.out.println("\t" + Arrays.toString(a.getLessons().stream().mapToInt(DBLesson::getId).toArray()));
            System.out.println("<===");
            for(DBLessonGroup b : a.getLessonGroup())
            {
                System.out.println(Arrays.toString(b.getClassrooms().stream().mapToInt(DBClassroom::getId).toArray()));
                System.out.println("\t" + Arrays.toString(b.getLessons().stream().mapToInt(DBLesson::getId).toArray()));
            }
            System.out.println("===>");
            System.out.println();
        }
    }
}
