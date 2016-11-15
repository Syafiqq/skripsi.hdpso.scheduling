package model.dataset.component;

import it.unimi.dsi.fastutil.ints.Int2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.dataset.component> created by : 
 * Name         : syafiq
 * Date / Time  : 08 November 2016, 3:56 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("unused") public class DSLessonCluster
{
    public static  Int2IntMap classroom_locator;
    private static int        object_counter;

    static
    {
        DSLessonCluster.classroom_locator = new Int2IntLinkedOpenHashMap(Byte.MAX_VALUE * 4);
        DSLessonCluster.object_counter = -1;
    }

    @NotNull private final DSLessonGroup[] lesson_groups;
    @NotNull private final int[]           lessons;
    @NotNull private final int[]           lessons_null;
    @NotNull private final int[]           classrooms;
    @NotNull private final int[][][]       clustered_classroom_time;
    @NotNull private final DSTimeOff[]     classrooms_timeoff;
    @NotNull private final Int2IntMap      classroom_encoder;
    @NotNull private final Int2IntMap      classroom_decoder;

    public DSLessonCluster(@NotNull DSLessonGroup[] lesson_groups, @NotNull int[] lessons, @NotNull int[] lessons_null, @NotNull int[] classrooms, @NotNull DSTimeOff[] classrooms_timeoff, @NotNull int[][][] clustered_classroom_time, @NotNull Int2IntMap classroom_encoder, @NotNull Int2IntMap classroom_decoder)
    {
        this.lesson_groups = lesson_groups;
        this.lessons = lessons;
        this.lessons_null = lessons_null;
        this.classrooms = classrooms;
        this.classrooms_timeoff = classrooms_timeoff;
        this.clustered_classroom_time = clustered_classroom_time;
        this.classroom_encoder = classroom_encoder;
        this.classroom_decoder = classroom_decoder;

        DSLessonCluster.rearrangeLocator(++DSLessonCluster.object_counter, this);
    }

    public static void rearrangeLocator(@NotNull final DSLessonCluster[] lesson_clusters)
    {
        DSLessonCluster.classroom_locator.clear();
        DSLessonCluster.object_counter = -1;
        for(final @NotNull DSLessonCluster lesson_cluster : lesson_clusters)
        {
            DSLessonCluster.rearrangeLocator(++DSLessonCluster.object_counter, lesson_cluster);
        }
    }

    private static void rearrangeLocator(int index, @NotNull final DSLessonCluster cluster)
    {
        @NotNull final Int2IntMap classroom_decoder = cluster.classroom_decoder;
        for(final int classroom : cluster.classrooms)
        {
            DSLessonCluster.classroom_locator.put(classroom_decoder.get(classroom), index);
        }
    }

    public DSLessonGroup[] getLessonGroups()
    {
        return this.lesson_groups;
    }

    public DSTimeOff[] getClassroomsTimeoff()
    {
        return this.classrooms_timeoff;
    }

    @NotNull public int[] getLessons()
    {
        return this.lessons;
    }

    @NotNull public int[] getLessonNull()
    {
        return this.lessons_null;
    }

    @NotNull public int[] getClassrooms()
    {
        return this.classrooms;
    }

    public int[][][] getClassroomAvailableTime()
    {
        return this.clustered_classroom_time;
    }

    public Int2IntMap getClassroomEncoder()
    {
        return this.classroom_encoder;
    }

    public Int2IntMap getClassroomDecoder()
    {
        return this.classroom_decoder;
    }

    public int getLessonLength()
    {
        return this.lessons.length;
    }

    public int getLessonNullLength()
    {
        return this.lessons_null.length;
    }

    public int getClassroomLength()
    {
        return this.classrooms.length;
    }

}
