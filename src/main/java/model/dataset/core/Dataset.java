package model.dataset.core;

import it.unimi.dsi.fastutil.ints.IntArrays;
import model.database.loader.DBProblemLoader;
import model.dataset.component.DSLesson;
import model.dataset.component.DSLessonCluster;
import model.dataset.component.DSLessonGroup;
import model.dataset.component.DSScheduleShufflingProperty;
import model.dataset.component.DSTimeOff;
import model.method.pso.hdpso.component.ScheduleContainer;
import model.util.list.IntHList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.dataset.core> created by : 
 * Name         : syafiq
 * Date / Time  : 02 November 2016, 8:58 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings({"FieldCanBeLocal", "unused"}) public class Dataset
{
    private final @NotNull int[]             days;
    private final @NotNull int[]             periods;
    private final @NotNull int[]             time_distribution;
    private final @NotNull int[][][]         clustered_classroom_time;
    private final @NotNull DSTimeOff[]       classes;
    private final @NotNull DSTimeOff[]       classrooms;
    private final @NotNull DSTimeOff[]       lecturers;
    private final @NotNull DSTimeOff[]       subjects;
    private final @NotNull DSLesson[]        lessons;
    private final @NotNull DSLessonGroup[]   lesson_group;
    private final @NotNull DSLessonCluster[] lesson_cluster;

    public Dataset(final @NotNull DBProblemLoader loader)
    {
        this.days = new int[loader.getDays().size()];
        this.periods = new int[loader.getPeriods().size()];
        this.classes = new DSTimeOff[loader.getOperatingClass().size()];
        this.classrooms = new DSTimeOff[loader.getOperatingClassroom().size()];
        this.lecturers = new DSTimeOff[loader.getOperatingLecture().size()];
        this.subjects = new DSTimeOff[loader.getOperatingSubject().size()];
        this.lessons = new DSLesson[loader.getOperatingClassroomAllowedLesson() - loader.getTotalRegisteredTime() + loader.getComplexLessonSize()];
        this.time_distribution = new int[loader.getTimeDistribution().size()];
        this.lesson_group = new DSLessonGroup[loader.getLessonGroup().size()];
        this.lesson_cluster = new DSLessonCluster[loader.getLessonCluster().size()];
        this.clustered_classroom_time = new int[this.classrooms.length][this.days.length][];
    }

    @NotNull public int[] getDays()
    {
        return this.days;
    }

    @NotNull public int[] getPeriods()
    {
        return this.periods;
    }

    @NotNull public int[] getTimeDistribution()
    {
        return this.time_distribution;
    }

    @NotNull public DSTimeOff[] getClasses()
    {
        return this.classes;
    }

    @NotNull public DSTimeOff[] getClassrooms()
    {
        return this.classrooms;
    }

    @NotNull public DSTimeOff[] getLecturers()
    {
        return this.lecturers;
    }

    @NotNull public DSTimeOff[] getSubjects()
    {
        return this.subjects;
    }

    @NotNull public DSLesson[] getLessons()
    {
        return this.lessons;
    }

    @NotNull public DSLessonGroup[] getLessonGroups()
    {
        return this.lesson_group;
    }

    @NotNull public DSLessonCluster[] getLessonClusters()
    {
        return this.lesson_cluster;
    }

    @NotNull public DSTimeOff getKlass(final int klass_id)
    {
        return this.classes[klass_id];
    }

    @NotNull public DSTimeOff getClassroom(final int classroom_id)
    {
        return this.classrooms[classroom_id];
    }

    @NotNull public DSTimeOff getLecturer(final int lecturer_id)
    {
        return this.lecturers[lecturer_id];
    }

    @NotNull public DSTimeOff getSubject(final int subject_id)
    {
        return this.subjects[subject_id];
    }

    @Nullable public DSLesson getLesson(final int lesson_id)
    {
        return this.lessons[lesson_id];
    }

    @NotNull public DSLessonGroup getLessonGroup(final int lesson_group_id)
    {
        return this.lesson_group[lesson_group_id];
    }

    @NotNull public DSLessonCluster getLessonClusters(final int lesson_cluster_id)
    {
        return this.lesson_cluster[lesson_cluster_id];
    }

    @NotNull public int[][][] getClusteredClassroomTime()
    {
        return this.clustered_classroom_time;
    }

    public int getClassroomSize()
    {
        return this.classrooms.length;
    }

    @NotNull public DSScheduleShufflingProperty getShufflingProperty()
    {
        @NotNull final DSLessonCluster[] lesson_clusters       = this.lesson_cluster;
        final int                        lesson_cluster_length = lesson_clusters.length;
        final int                        period_length         = this.periods.length;

        @NotNull final int[]                   day_set                 = IntArrays.copy(this.days);
        @NotNull final int[][]                 lesson_null_set         = new int[lesson_cluster_length][];
        @NotNull final int[][][]               classroom_set           = new int[lesson_cluster_length][][];
        @NotNull final int[][][]               lesson_set              = new int[lesson_cluster_length][][];
        @NotNull final int[][][][]             lesson_appender_manager = new int[lesson_cluster_length][][][];
        @NotNull final ScheduleContainer[][][] schedule_container      = new ScheduleContainer[lesson_cluster_length][][];
        @NotNull final IntHList[]              full_schedule           = new IntHList[lesson_cluster_length];
        for(int c_cluster = -1, cs_lesson_cluster = lesson_clusters.length; ++c_cluster < cs_lesson_cluster; )
        {
            final DSLessonCluster lesson_cluster = lesson_clusters[c_cluster];

            lesson_null_set[c_cluster] = new int[lesson_cluster.getLessonNullTotal()];
            classroom_set[c_cluster] = new int[lesson_cluster.getLessonGroupTotal()][];
            lesson_set[c_cluster] = new int[classroom_set.length][];
            lesson_appender_manager[c_cluster] = new int[lesson_cluster.getClassroomTotal()][day_set.length][2];
            schedule_container[c_cluster] = new ScheduleContainer[lesson_cluster.getClassroomTotal()][day_set.length];
            full_schedule[c_cluster] = new IntHList(lesson_cluster.getLessonTotal() + lesson_cluster.getLessonNullTotal());

            System.arraycopy(lesson_cluster.getLessonNull(), 0, lesson_null_set[c_cluster], 0, lesson_null_set[c_cluster].length);
            for(int c_group = -1, cs_lesson_group = lesson_cluster.getLessonGroupTotal(); ++c_group < cs_lesson_group; )
            {
                final DSLessonGroup lesson_group = lesson_cluster.getLessonGroup(c_group);
                classroom_set[c_cluster][c_group] = IntArrays.copy(lesson_group.getClassrooms());
                lesson_set[c_cluster][c_group] = IntArrays.copy(lesson_group.getLessons());
            }

            for(int c_classroom = -1, cs_classroom = schedule_container[c_cluster].length; ++c_classroom < cs_classroom; )
            {
                for(int c_day = -1, cs_day = schedule_container[c_cluster][c_classroom].length; ++c_day < cs_day; )
                {
                    schedule_container[c_cluster][c_classroom][c_day] = new ScheduleContainer(period_length);
                }
            }
        }

        return new DSScheduleShufflingProperty(day_set, lesson_null_set, classroom_set, lesson_set, lesson_appender_manager, IntArrays.copy(this.time_distribution), schedule_container, full_schedule);
    }
}
