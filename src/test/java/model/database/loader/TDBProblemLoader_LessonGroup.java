package model.database.loader;

import model.database.component.DBLesson;
import model.database.component.DBLessonGroup;
import model.database.component.DBSchool;
import model.database.component.metadata.DBMClassroom;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.loader> created by : 
 * Name         : syafiq
 * Date / Time  : 01 November 2016, 4:17 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("FieldCanBeLocal") public class TDBProblemLoader_LessonGroup
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

    @Test public void lesson_group_all_test001()
    {
        for(@NotNull final DBLessonGroup i : this.loader.getLessonGroup())
        {
            System.out.println(Arrays.toString(i.getClassrooms().stream().mapToInt(DBMClassroom::getId).toArray()));
            System.out.println("\t" + Arrays.toString(i.getLessons().stream().mapToInt(DBLesson::getId).toArray()));
        }
    }

    @Test public void lesson_group_classroom_test001()
    {
        for(@NotNull final DBLessonGroup i : this.loader.getLessonGroup())
        {
            System.out.println(Arrays.toString(i.getClassrooms().stream().mapToInt(DBMClassroom::getId).toArray()));
        }
    }
}
