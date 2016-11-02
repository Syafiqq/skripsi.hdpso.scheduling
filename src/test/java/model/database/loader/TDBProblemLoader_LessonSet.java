package model.database.loader;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.Arrays;
import model.database.component.DBClassroom;
import model.database.component.DBLesson;
import model.database.component.DBLessonSet;
import model.database.component.DBSchool;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.loader> created by : 
 * Name         : syafiq
 * Date / Time  : 01 November 2016, 4:17 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("FieldCanBeLocal") public class TDBProblemLoader_LessonSet
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

    @Test public void lesson_set_test001()
    {
        for(Object2ObjectMap.Entry<ObjectList<DBClassroom>, DBLessonSet> i : this.loader.getLessonSet().object2ObjectEntrySet())
        {
            System.out.println(Arrays.toString(i.getKey().stream().mapToInt(DBClassroom::getId).toArray()));
            System.out.println("\t" + Arrays.toString(i.getValue().getLessons().stream().mapToInt(DBLesson::getId).toArray()));
        }
    }
}
