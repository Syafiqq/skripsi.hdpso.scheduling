package model.dataset.loader;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import java.util.Arrays;
import model.database.component.DBSchool;
import model.database.loader.DBProblemLoader;
import model.dataset.component.DSLessonCluster;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.dataset.loader> created by : 
 * Name         : syafiq
 * Date / Time  : 10 November 2016, 8:10 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("FieldCanBeLocal") public class TDatasetGenerator_LessonCluster
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
        System.out.println(DSLessonCluster.classroom_locator);
    }

    @Test public void lesson_cluster_test_for_comparison()
    {
        DSLessonCluster[] lesson_pool = this.dsLoader.getDataset().getLessonClusters();
        for(int i = -1, is = lesson_pool.length; ++i < is; )
        {
            Int2IntMap pol_clas_dec = lesson_pool[i].getClassroomDecoder();
            System.out.printf("[%4d]\t%s\n", lesson_pool[i].getClassroomTotal(), Arrays.toString(Arrays.stream(lesson_pool[i].getClassrooms()).map(operand -> this.dsLoader.getDecoder().getClassrooms().get(pol_clas_dec.get(operand))).toArray()));
            System.out.printf("[%4d]\t%s\n", lesson_pool[i].getLessonTotal(), Arrays.toString(Arrays.stream(lesson_pool[i].getLessons()).map(operand ->
                    this.dsLoader.getDecoder().getLessons().getOrDefault(this.dsLoader.getDataset().getLessons()[operand].getLessonParent(), this.dsLoader.getDecoder().getLessons().get(operand))
            ).toArray()));
            System.out.printf("[%4d]\t%s\n", lesson_pool[i].getLessonNullTotal(), Arrays.toString(lesson_pool[i].getLessonNull()));
            System.out.println();
        }
    }

    @Test public void test_lesson_cluster_real()
    {
        DSLessonCluster[] lesson_pool = this.dsLoader.getDataset().getLessonClusters();
        for(int i = -1, is = lesson_pool.length; ++i < is; )
        {
            System.out.printf("[%4d]\t%s\n", lesson_pool[i].getClassroomTotal(), Arrays.toString(lesson_pool[i].getClassrooms()));
            System.out.printf("[%4d]\t%s\n", lesson_pool[i].getLessonTotal(), Arrays.toString(lesson_pool[i].getLessons()));
            System.out.printf("[%4d]\t%s\n", lesson_pool[i].getLessonNullTotal(), Arrays.toString(lesson_pool[i].getLessonNull()));
            System.out.println();
        }
    }
}
