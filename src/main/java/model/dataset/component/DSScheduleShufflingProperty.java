package model.dataset.component;

import it.unimi.dsi.fastutil.ints.IntArrays;
import java.util.Random;
import model.method.pso.hdpso.component.ScheduleContainer;
import model.util.list.IntHList;
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
    @NotNull public final  Random                  random;
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
    @NotNull private final int[]                   time_distribution;
    @NotNull private final int[]                   day_set;
    @NotNull private final int[][]                 lesson_null_set;
    @NotNull private final int[][][]               classrooms_set;
    @NotNull private final int[][][]               lessons_set;
    @NotNull private final int[][][][]             lesson_appender_manager;
    @NotNull private final ScheduleContainer[][][] schedule_container;
    @NotNull private final IntHList[]              full_schedule;

    public DSScheduleShufflingProperty(@NotNull int[] day_set, @NotNull int[][] lesson_null_set, @NotNull int[][][] classrooms_set, @NotNull int[][][] lessons_set, @NotNull int[][][][] clustered_classroom_property_set, @NotNull final int[] time_distribution, @NotNull final ScheduleContainer[][][] schedule_container, @NotNull IntHList[] full_schedule)
    {
        this.day_set = day_set;
        this.lesson_null_set = lesson_null_set;
        this.classrooms_set = classrooms_set;
        this.lessons_set = lessons_set;
        this.lesson_appender_manager = clustered_classroom_property_set;
        this.time_distribution = time_distribution;
        this.schedule_container = schedule_container;
        this.full_schedule = full_schedule;
        this.random = new Random();
    }

    public static DSScheduleShufflingProperty newInstance(DSScheduleShufflingProperty properties)
    {
        @NotNull final int[]                   day_set_source                           = properties.day_set;
        @NotNull final int[]                   day_set_destination                      = new int[day_set_source.length];
        @NotNull final int[][]                 lesson_null_set_source                   = properties.lesson_null_set;
        @NotNull final int[][]                 lesson_null_set_destination              = new int[lesson_null_set_source.length][];
        @NotNull final int[][][]               classroom_set_source                     = properties.classrooms_set;
        @NotNull final int[][][]               classroom_set_destination                = new int[classroom_set_source.length][][];
        @NotNull final int[][][]               lesson_set_source                        = properties.lessons_set;
        @NotNull final int[][][]               lesson_set_destination                   = new int[lesson_set_source.length][][];
        @NotNull final int[][][][]             clustered_classroom_property_source      = properties.lesson_appender_manager;
        @NotNull final int[][][][]             clustered_classroom_property_destination = new int[clustered_classroom_property_source.length][][][];
        @NotNull final ScheduleContainer[][][] schedule_container_source                = properties.schedule_container;
        @NotNull final ScheduleContainer[][][] schedule_container_destination           = new ScheduleContainer[schedule_container_source.length][][];
        @NotNull final IntHList[]              full_schedule_source                     = properties.full_schedule;
        @NotNull final IntHList[]              full_schedule_destination                = new IntHList[full_schedule_source.length];

        System.arraycopy(day_set_source, 0, day_set_destination, 0, day_set_source.length);

        for(int c_cluster = -1, cs_cluster = classroom_set_source.length; ++c_cluster < cs_cluster; )
        {
            classroom_set_destination[c_cluster] = new int[classroom_set_source[c_cluster].length][];
            lesson_null_set_destination[c_cluster] = new int[lesson_null_set_source[c_cluster].length];
            lesson_set_destination[c_cluster] = new int[lesson_set_source[c_cluster].length][];
            clustered_classroom_property_destination[c_cluster] = new int[clustered_classroom_property_source[c_cluster].length][][];
            schedule_container_destination[c_cluster] = new ScheduleContainer[schedule_container_source[c_cluster].length][day_set_source.length];
            full_schedule_destination[c_cluster] = IntHList.newInstance(full_schedule_source[c_cluster]);

            System.arraycopy(lesson_null_set_source[c_cluster], 0, lesson_null_set_destination[c_cluster], 0, lesson_null_set_destination[c_cluster].length);
            for(int counter_entity_child = -1, entity_child_size = classroom_set_source[c_cluster].length; ++counter_entity_child < entity_child_size; )
            {
                classroom_set_destination[c_cluster][counter_entity_child] = new int[classroom_set_source[c_cluster][counter_entity_child].length];
                lesson_set_destination[c_cluster][counter_entity_child] = new int[lesson_set_source[c_cluster][counter_entity_child].length];

                System.arraycopy(classroom_set_source[c_cluster][counter_entity_child], 0, classroom_set_destination[c_cluster][counter_entity_child], 0, classroom_set_source[c_cluster][counter_entity_child].length);
                System.arraycopy(lesson_set_source[c_cluster][counter_entity_child], 0, lesson_set_destination[c_cluster][counter_entity_child], 0, lesson_set_source[c_cluster][counter_entity_child].length);
            }

            for(int c_classroom = -1, cs_classroom = clustered_classroom_property_source[c_cluster].length; ++c_classroom < cs_classroom; )
            {
                clustered_classroom_property_destination[c_cluster][c_classroom] = new int[day_set_source.length][2];
                for(int c_day = -1, cs_day = schedule_container_source[c_cluster][c_classroom].length; ++c_day < cs_day; )
                {
                    schedule_container_destination[c_cluster][c_classroom][c_day] = ScheduleContainer.newInstance(schedule_container_source[c_cluster][c_classroom][c_day]);
                }
            }
        }

        return new DSScheduleShufflingProperty(day_set_destination, lesson_null_set_destination, classroom_set_destination, lesson_set_destination, clustered_classroom_property_destination, IntArrays.copy(properties.time_distribution), schedule_container_destination, full_schedule_destination);
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

    public void reset_classroom_current_time(final int cluster)
    {
        for(int[][] classrooms : this.lesson_appender_manager[cluster])
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

    public void reset_schedule_container(final int cluster)
    {
        for(@NotNull ScheduleContainer[] schedule_containers : this.schedule_container[cluster])
        {
            for(@NotNull ScheduleContainer schedule_container : schedule_containers)
            {
                schedule_container.reset();
            }
        }
    }

    @NotNull public int[] getDaySet()
    {
        return this.day_set;
    }

    @NotNull public int[][][] getLessonAppenderManager(final int cluster)
    {
        return this.lesson_appender_manager[cluster];
    }

    @NotNull public int[] getLessonSet(final int cluster, final int group)
    {
        return this.lessons_set[cluster][group];
    }

    @NotNull public int[] getClassroomSet(final int cluster, final int group)
    {
        return this.classrooms_set[cluster][group];
    }

    @NotNull public int[] getTimeDistribution()
    {
        return this.time_distribution;
    }

    @NotNull public int[] getLessonNullSet(final int cluster)
    {
        return this.lesson_null_set[cluster];
    }

    @NotNull public ScheduleContainer[][] getScheduleContainer(final int cluster)
    {
        return this.schedule_container[cluster];
    }

    @NotNull public IntHList getFullScheduleContainer(final int cluster)
    {
        return this.full_schedule[cluster];
    }
}
