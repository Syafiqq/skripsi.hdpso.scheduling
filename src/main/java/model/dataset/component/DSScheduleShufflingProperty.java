package model.dataset.component;

import it.unimi.dsi.fastutil.ints.IntArrays;
import java.util.Random;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.dataset.component> created by : 
 * Name         : syafiq
 * Date / Time  : 17 November 2016, 10:15 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings({"unused", "WeakerAccess"}) public class DSScheduleShufflingProperty
{
    @NotNull public final  Random      random;
    /*
    * @param time_distribution
    *   index
    *       1 : time index
    * @param day_set
    *   index
    *       1 : day position order
    * @param lesson_null_set
    *   index
    *       1 : cluster index
    *       2 : lesson null entry
    * @param classroom_set
    *   index
    *       1 : cluster index
    *       2 : group index
    *       3 : classrooms
    * @param lesson_set
    *   index
    *       1 : cluster index
    *       2 : group index
    *       3 : lessons
    * @param lesson_appender_manager
    *   index
    *       1 : cluster index
    *       2 : classroom index
    *       3 : day index
    *       4 : clustered_time
    * */
    @NotNull private final int[]       time_distribution;
    @NotNull private final int[]       day_set;
    @NotNull private final int[][]     lesson_null_set;
    @NotNull private final int[][][]   classrooms_set;
    @NotNull private final int[][][]   lessons_set;
    @NotNull private final int[][][][] lesson_appender_manager;

    public DSScheduleShufflingProperty(@NotNull int[] day_set, @NotNull int[][] lesson_null_set, @NotNull int[][][] classrooms_set, @NotNull int[][][] lessons_set, @NotNull int[][][][] clustered_classroom_property_set, @NotNull final int[] time_distribution)
    {
        this.day_set = day_set;
        this.lesson_null_set = lesson_null_set;
        this.classrooms_set = classrooms_set;
        this.lessons_set = lessons_set;
        this.lesson_appender_manager = clustered_classroom_property_set;
        this.time_distribution = time_distribution;
        this.random = new Random();
    }

    public static DSScheduleShufflingProperty newInstance(DSScheduleShufflingProperty properties)
    {
        final int[]       day_set_source                           = properties.day_set;
        final int[]       day_set_destination                      = new int[properties.day_set.length];
        final int[][]     lesson_null_set_source                   = properties.lesson_null_set;
        final int[][]     lesson_null_set_destination              = new int[properties.lesson_null_set.length][];
        final int[][][]   classroom_set_source                     = properties.classrooms_set;
        final int[][][]   classroom_set_destination                = new int[properties.classrooms_set.length][][];
        final int[][][]   lesson_set_source                        = properties.lessons_set;
        final int[][][]   lesson_set_destination                   = new int[properties.lessons_set.length][][];
        final int[][][][] clustered_classroom_property_source      = properties.lesson_appender_manager;
        final int[][][][] clustered_classroom_property_destination = new int[properties.lesson_appender_manager.length][][][];

        System.arraycopy(day_set_source, 0, day_set_destination, 0, day_set_source.length);

        for(int c_cluster = -1, c_cluster_s = classroom_set_source.length; ++c_cluster < c_cluster_s; )
        {
            classroom_set_destination[c_cluster] = new int[classroom_set_source[c_cluster].length][];
            lesson_null_set_destination[c_cluster] = new int[lesson_null_set_source[c_cluster].length];
            lesson_set_destination[c_cluster] = new int[lesson_set_source[c_cluster].length][];
            clustered_classroom_property_destination[c_cluster] = new int[clustered_classroom_property_source[c_cluster].length][][];

            System.arraycopy(lesson_null_set_source[c_cluster], 0, lesson_null_set_destination[c_cluster], 0, lesson_null_set_destination[c_cluster].length);
            for(int counter_entity_child = -1, entity_child_size = classroom_set_source[c_cluster].length; ++counter_entity_child < entity_child_size; )
            {
                classroom_set_destination[c_cluster][counter_entity_child] = new int[classroom_set_source[c_cluster][counter_entity_child].length];
                lesson_set_destination[c_cluster][counter_entity_child] = new int[lesson_set_source[c_cluster][counter_entity_child].length];

                System.arraycopy(classroom_set_source[c_cluster][counter_entity_child], 0, classroom_set_destination[c_cluster][counter_entity_child], 0, classroom_set_source[c_cluster][counter_entity_child].length);
                System.arraycopy(lesson_set_source[c_cluster][counter_entity_child], 0, lesson_set_destination[c_cluster][counter_entity_child], 0, lesson_set_source[c_cluster][counter_entity_child].length);
            }

            for(int c_classroom = -1, c_classroom_s = clustered_classroom_property_source[c_cluster].length; ++c_classroom < c_classroom_s; )
            {
                clustered_classroom_property_destination[c_cluster][c_classroom] = new int[day_set_source.length][2];
            }
        }

        return new DSScheduleShufflingProperty(day_set_destination, lesson_null_set_destination, classroom_set_destination, lesson_set_destination, clustered_classroom_property_destination, IntArrays.copy(properties.time_distribution));
    }


    public void reset_classroom_current_time()
    {
        for(int[][][] lesson_pool : this.lesson_appender_manager)
        {
            for(int[][] classrooms : lesson_pool)
            {
                for(int[] day : classrooms)
                {
                    for(int counter_property = -1, property_size = day.length; ++counter_property < property_size; )
                    {
                        day[counter_property] = 0;
                    }
                }
            }
        }
    }

    public void reset_classroom_current_time(final int pool_index)
    {
        for(int[][] classrooms : this.lesson_appender_manager[pool_index])
        {
            for(int[] day : classrooms)
            {
                for(int counter_property = -1, property_size = day.length; ++counter_property < property_size; )
                {
                    day[counter_property] = 0;
                }
            }
        }
    }

    public int[] getDaySet()
    {
        return this.day_set;
    }

    public int[][][] getLessonAppenderManager(final int cluster)
    {
        return this.lesson_appender_manager[cluster];
    }

    public int[] getLessonSet(final int cluster, final int group)
    {
        return this.lessons_set[cluster][group];
    }

    public int[] getClassroomSet(final int cluster, final int group)
    {
        return this.classrooms_set[cluster][group];
    }

    public int[] getTimeDistribution()
    {
        return this.time_distribution;
    }

    public int[] getLessonNullSet(final int cluster)
    {
        return this.lesson_null_set[cluster];
    }
}
