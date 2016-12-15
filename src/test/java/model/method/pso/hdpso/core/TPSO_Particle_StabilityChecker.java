package model.method.pso.hdpso.core;

import model.dataset.component.DSLesson;
import model.dataset.component.DSLessonCluster;
import model.dataset.loader.DatasetGenerator;
import model.method.pso.hdpso.component.Position;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.core> created by : 
 * Name         : syafiq
 * Date / Time  : 27 November 2016, 7:30 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings({"WeakerAccess", "Duplicates"}) public class TPSO_Particle_StabilityChecker
{
    public static boolean checkAppearance(@NotNull final DatasetGenerator dsLoader, @NotNull final Position[] ds_positions)
    {
        boolean         isValid = true;
        final boolean[] check   = new boolean[dsLoader.getDataset().getLessons().length];
        Assert.assertEquals(1244, check.length);
        for(@NotNull Position positions : ds_positions)
        {
            for(int position : positions.getPosition())
            {
                check[position] = true;
            }
        }
        for(boolean c : check)
        {
            if(!c)
            {
                isValid = false;
                break;
            }
        }
        return isValid;
    }

    @SuppressWarnings("ConstantConditions") public static boolean checkConflict(@NotNull final DatasetGenerator dsLoader, @NotNull final Position[] positions)
    {
        boolean                    isValid         = true;
        @NotNull DSLesson[]        lessons         = dsLoader.getDataset().getLessons();
        @NotNull DSLessonCluster[] lesson_clusters = dsLoader.getDataset().getLessonClusters();
        @NotNull int[]             days            = dsLoader.getDataset().getDays();
        cluster:
        for(int c_cluster = -1, cs_cluster = lesson_clusters.length; ++c_cluster < cs_cluster; )
        {
            @NotNull DSLessonCluster lesson_cluster = lesson_clusters[c_cluster];
            @NotNull int[]           classrooms     = lesson_cluster.getClassrooms();
            @NotNull int[][][]       clustered_time = lesson_cluster.getClassroomClusteredTime();
            @NotNull int[]           position       = positions[c_cluster].getPosition();
            int                      c_position     = -1;
            for(int c_classroom = -1, cs_classroom = classrooms.length; ++c_classroom < cs_classroom; )
            {
                for(int c_day = -1, cs_day = days.length; ++c_day < cs_day; )
                {
                    int c_clustered  = 0;
                    int clustered    = clustered_time[c_classroom][c_day][++c_clustered];
                    int current_time = 0;
                    int current_sks;
                    while(true)
                    {
                        final DSLesson lesson = lessons[position[++c_position]];
                        current_sks = lesson == null ? 1 : lesson.getSks();
                        if(current_time + current_sks < clustered)
                        {
                            current_time += current_sks;
                        }
                        else if(current_time + current_sks == clustered)
                        {
                            current_time += current_sks;
                            try
                            {
                                clustered += clustered_time[c_classroom][c_day][++c_clustered];
                            }
                            catch(ArrayIndexOutOfBoundsException ignored)
                            {
                                break;
                            }
                        }
                        else
                        {
                            isValid = false;
                            continue cluster;
                        }
                    }
                }
            }
        }
        return isValid;
    }

    @SuppressWarnings("ConstantConditions") public static boolean checkAllowed(@NotNull final DatasetGenerator dsLoader, @NotNull final Position[] positions)
    {
        boolean                    isValid         = true;
        @NotNull DSLesson[]        lessons         = dsLoader.getDataset().getLessons();
        @NotNull DSLessonCluster[] lesson_clusters = dsLoader.getDataset().getLessonClusters();
        @NotNull int[]             days            = dsLoader.getDataset().getDays();
        cluster:
        for(int c_cluster = -1, cs_cluster = lesson_clusters.length; ++c_cluster < cs_cluster; )
        {
            @NotNull DSLessonCluster lesson_cluster = lesson_clusters[c_cluster];
            @NotNull int[]           classrooms     = lesson_cluster.getClassrooms();
            @NotNull int[][][]       clustered_time = lesson_cluster.getClassroomClusteredTime();
            @NotNull int[]           position       = positions[c_cluster].getPosition();
            int                      c_position     = -1;
            for(int c_classroom = -1, cs_classroom = classrooms.length; ++c_classroom < cs_classroom; )
            {
                for(int c_day = -1, cs_day = days.length; ++c_day < cs_day; )
                {
                    int c_clustered  = 0;
                    int clustered    = clustered_time[c_classroom][c_day][++c_clustered];
                    int current_time = 0;
                    int current_sks;
                    while(true)
                    {
                        final DSLesson lesson = lessons[position[++c_position]];
                        current_sks = lesson == null ? 1 : lesson.getSks();
                        if((lesson != null) && (!lesson.isLessonAllowed(c_classroom)))
                        {
                            isValid = false;
                            break cluster;
                        }
                        if(current_time + current_sks < clustered)
                        {
                            current_time += current_sks;
                        }
                        else if(current_time + current_sks == clustered)
                        {
                            current_time += current_sks;
                            try
                            {
                                clustered += clustered_time[c_classroom][c_day][++c_clustered];
                            }
                            catch(ArrayIndexOutOfBoundsException ignored)
                            {
                                break;
                            }
                        }
                        else
                        {
                            isValid = false;
                            continue cluster;
                        }
                    }
                }
            }
        }
        return isValid;
    }
}
