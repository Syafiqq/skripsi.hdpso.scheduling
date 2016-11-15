package model.dataset.loader;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.IntArrays;
import java.util.Arrays;
import model.database.component.DBSchool;
import model.database.loader.DBProblemLoader;
import model.dataset.component.DSLesson;
import model.dataset.component.DSLessonGroup;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.dataset.loader> created by : 
 * Name         : syafiq
 * Date / Time  : 08 November 2016, 11:36 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class TDatasetGenerator_LessonGroup
{
    private DBSchool         school;
    private DBProblemLoader  dbLoader;
    private DatasetGenerator dsLoader;

    @SuppressWarnings("Duplicates") @Before public void initialize()
    {
        this.school = new DBSchool(1, "Program Teknologi Informasi dan Ilmu Komputer Universitas Brawijaya", "PTIIK UB", "Jl. Veteran No.8, Ketawanggede, Kec. Lowokwaru, Kota Malang, Jawa Timur", "2013 - 2014", 0, 17, 5);
        Assert.assertNotNull(this.school);

        this.dbLoader = new DBProblemLoader(school);
        Assert.assertNotNull(this.dbLoader);

        this.dbLoader.loadData();

        this.dsLoader = new DatasetGenerator(this.dbLoader);
        Assert.assertNotNull(this.dsLoader);
    }

    @Test public void classTest_A_001()
    {
        Assert.assertEquals(7, this.dsLoader.getDataset().getLessonGroups().length);
    }

    @Test public void classTest_B_001()
    {
        for(@NotNull final DSLessonGroup lesson_group : this.dsLoader.getDataset().getLessonGroups())
        {
            System.out.println(Arrays.toString(lesson_group.getClassrooms()));
            System.out.println(Arrays.toString(lesson_group.getLessons()));
            System.out.println(Arrays.toString(lesson_group.getTimeDistributions()));
        }
    }

    @Test public void classTest_C_001()
    {
        for(@NotNull final DSLessonGroup lesson_group : this.dsLoader.getDataset().getLessonGroups())
        {
            final int[] tmp_classroom         = IntArrays.copy(lesson_group.getClassrooms());
            final int[] tmp_lesson            = IntArrays.copy(lesson_group.getLessons());
            final int[] tmp_time_distribution = IntArrays.copy(lesson_group.getTimeDistributions());

            IntArrays.quickSort(tmp_classroom);
            IntArrays.quickSort(tmp_lesson);

            System.out.println(Arrays.toString(tmp_classroom));
            System.out.println(Arrays.toString(tmp_lesson));
            System.out.println(Arrays.toString(tmp_time_distribution));

            System.out.println();
        }
    }

    @Test public void classTest_D_001()
    {
        final @NotNull Int2IntMap classroom_decoder = this.dsLoader.getDecoder().getClassrooms();
        final @NotNull Int2IntMap lesson_decoder    = this.dsLoader.getDecoder().getLessons();
        final @NotNull DSLesson[] lessons           = this.dsLoader.getDataset().getLessons();

        for(@NotNull final DSLessonGroup lesson_group : this.dsLoader.getDataset().getLessonGroups())
        {
            final int[] tmp_classroom         = IntArrays.copy(lesson_group.getClassrooms());
            final int[] tmp_lesson            = IntArrays.copy(lesson_group.getLessons());
            final int[] tmp_time_distribution = IntArrays.copy(lesson_group.getTimeDistributions());

            IntArrays.quickSort(tmp_classroom);
            IntArrays.quickSort(tmp_lesson);

            System.out.println(Arrays.toString(Arrays.stream(tmp_classroom).map(classroom_decoder::get).toArray()));
            System.out.println(Arrays.toString(Arrays.stream(tmp_lesson).map(operand -> lesson_decoder.getOrDefault(lessons[operand].getLessonParent(), lesson_decoder.get(operand))).toArray()));
            System.out.println(Arrays.toString(tmp_time_distribution));

            System.out.println();
        }
    }

    @Test public void lesson_group_classroom_test()
    {
        for(@NotNull final DSLessonGroup lesson_group : this.dsLoader.getDataset().getLessonGroups())
        {
            System.out.println(Arrays.toString(lesson_group.getClassrooms()));
        }
    }
}
