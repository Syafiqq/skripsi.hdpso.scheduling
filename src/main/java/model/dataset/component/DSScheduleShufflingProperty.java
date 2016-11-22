package model.dataset.component;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.dataset.component> created by : 
 * Name         : syafiq
 * Date / Time  : 17 November 2016, 10:15 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings({"unused", "WeakerAccess"}) public class DSScheduleShufflingProperty
{
    /*
    * @param day_set
    *   index
    *       1 : day position order
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
    * @param clustered_classroom_property_set
    *   index
    *       1 : cluster index
    *       2 : classroom index
    *       3 : day index
    *       4 : clustered_time
    * */
    private final int[]       day_set;
    private final int[][][]   classrooms_set;
    private final int[][][]   lessons_set;
    private final int[][][][] clustered_classroom_property_set;

    public DSScheduleShufflingProperty(int[] day_set, int[][][] classrooms_set, int[][][] lessons_set, int[][][][] clustered_classroom_property_set)
    {
        this.day_set = day_set;
        this.classrooms_set = classrooms_set;
        this.lessons_set = lessons_set;
        this.clustered_classroom_property_set = clustered_classroom_property_set;
    }

    public static DSScheduleShufflingProperty newInstance(DSScheduleShufflingProperty properties)
    {
        final int[]       day_set_source                           = properties.day_set;
        final int[]       day_set_destination                      = new int[properties.day_set.length];
        final int[][][]   classroom_set_source                     = properties.classrooms_set;
        final int[][][]   classroom_set_destination                = new int[properties.classrooms_set.length][][];
        final int[][][]   lesson_set_source                        = properties.lessons_set;
        final int[][][]   lesson_set_destination                   = new int[properties.lessons_set.length][][];
        final int[][][][] clustered_classroom_property_source      = properties.clustered_classroom_property_set;
        final int[][][][] clustered_classroom_property_destination = new int[properties.clustered_classroom_property_set.length][][][];

        System.arraycopy(day_set_source, 0, day_set_destination, 0, day_set_source.length);

        for(int c_cluster = -1, c_cluster_s = classroom_set_source.length; ++c_cluster < c_cluster_s; )
        {
            classroom_set_destination[c_cluster] = new int[classroom_set_source[c_cluster].length][];
            lesson_set_destination[c_cluster] = new int[lesson_set_source[c_cluster].length][];
            clustered_classroom_property_destination[c_cluster] = new int[clustered_classroom_property_source[c_cluster].length][][];

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

        return new DSScheduleShufflingProperty(day_set_destination, classroom_set_destination, lesson_set_destination, clustered_classroom_property_destination);
    }


    public void reset_classroom_current_time()
    {
        for(int[][][] lesson_pool : this.clustered_classroom_property_set)
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

    public void reset_classroom_current_time(int pool_index)
    {
        for(int[][] classrooms : this.clustered_classroom_property_set[pool_index])
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
