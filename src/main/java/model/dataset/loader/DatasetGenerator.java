package model.dataset.loader;

import it.unimi.dsi.fastutil.ints.Int2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntArrays;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import it.unimi.dsi.fastutil.objects.ObjectArrays;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Iterator;
import java.util.LinkedList;
import model.database.component.DBAvailability;
import model.database.component.DBClass;
import model.database.component.DBClassroom;
import model.database.component.DBDay;
import model.database.component.DBLecture;
import model.database.component.DBLesson;
import model.database.component.DBLessonCluster;
import model.database.component.DBLessonGroup;
import model.database.component.DBPeriod;
import model.database.component.DBSubject;
import model.database.component.DBTimeOff;
import model.database.loader.DBProblemLoader;
import model.dataset.component.DSLesson;
import model.dataset.component.DSLessonCluster;
import model.dataset.component.DSLessonGroup;
import model.dataset.component.DSTimeOff;
import model.dataset.core.Dataset;
import model.dataset.core.DatasetConverter;
import org.apache.commons.math3.util.FastMath;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.dataset.loader> created by : 
 * Name         : syafiq
 * Date / Time  : 02 November 2016, 8:57 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("WeakerAccess") public class DatasetGenerator
{
    private final @NotNull Dataset          dataset;
    private final @NotNull DatasetConverter encoder;
    private final @NotNull DatasetConverter decoder;

    public DatasetGenerator(final @NotNull DBProblemLoader loader)
    {
        this.dataset = new Dataset(loader);
        this.encoder = new DatasetConverter(loader);
        this.decoder = new DatasetConverter(loader);

        this.generateData(loader);
    }

    private void generateData(final @NotNull DBProblemLoader loader)
    {
        this.generateActiveDays(loader.getDays().values().iterator());
        this.generateActivePeriods(loader.getPeriods().values().iterator());
        this.generateClasses(loader.getClasses(), loader.getOperatingClass().iterator());
        this.generateClassroom(loader.getClassrooms(), loader.getOperatingClassroom().iterator());
        this.generateLecture(loader.getLecturers(), loader.getOperatingLecture().iterator());
        this.generateSubject(loader.getSubjects(), loader.getOperatingSubject().iterator());
        this.generateLesson(loader.getLessons().values());
        this.generateTimeDistribution(loader.getTimeDistribution().int2IntEntrySet());
        this.generateLessonGroup(loader.getLessonGroup().iterator());
        this.generateLessonCluster(loader.getLessonCluster().iterator(), loader.getComplexLessonSize());
    }

    private void generateActiveDays(final ObjectIterator<DBDay> db_days)
    {
        final @NotNull int[]      days    = this.dataset.getDays();
        final @NotNull Int2IntMap encoder = this.encoder.getActiveDays();
        final @NotNull Int2IntMap decoder = this.decoder.getActiveDays();

        for(int counter = 0; db_days.hasNext(); ++counter)
        {
            final int db_day_id = db_days.next().getId();
            encoder.put(db_day_id, counter);
            decoder.put(counter, db_day_id);

            days[counter] = counter;
        }
    }

    private void generateActivePeriods(final ObjectIterator<DBPeriod> db_periods)
    {
        final @NotNull int[]      periods = this.dataset.getPeriods();
        final @NotNull Int2IntMap encoder = this.encoder.getActivePeriods();
        final @NotNull Int2IntMap decoder = this.decoder.getActivePeriods();

        for(int counter = 0; db_periods.hasNext(); ++counter)
        {
            final int db_period_id = db_periods.next().getId();
            encoder.put(db_period_id, counter);
            decoder.put(counter, db_period_id);

            periods[counter] = counter;
        }
    }

    private void generateClasses(final @NotNull Int2ObjectMap<DBClass> db_classes, final @NotNull IntListIterator operating_classes)
    {
        final @NotNull DSTimeOff[] classes = this.dataset.getClasses();
        final @NotNull Int2IntMap  encoder = this.encoder.getClasses();
        final @NotNull Int2IntMap  decoder = this.decoder.getClasses();

        for(int counter = 0; operating_classes.hasNext(); ++counter)
        {
            final DBClass db_klass    = db_classes.get(operating_classes.next().intValue());
            final int     db_klass_id = db_klass.getId();
            encoder.put(db_klass_id, counter);
            decoder.put(counter, db_klass_id);

            classes[counter] = DSTimeOff.newInstance(db_klass.getTimeoff());
        }
    }

    @SuppressWarnings("unchecked") private void generateClassroom(final Int2ObjectMap<DBClassroom> db_classrooms, final @NotNull IntListIterator operating_classrooms)
    {
        final @NotNull DSTimeOff[]         classrooms                  = this.dataset.getClassrooms();
        final @NotNull Int2IntMap          encoder                     = this.encoder.getClassrooms();
        final @NotNull Int2IntMap          decoder                     = this.decoder.getClassrooms();
        final @NotNull int[][][]           clustered_classroom_time    = this.dataset.getClusteredClassroomTime();
        final @NotNull LinkedList<Integer> cluster_classroom_container = new LinkedList<>();

        for(int c_classroom = 0; operating_classrooms.hasNext(); ++c_classroom)
        {
            final DBClassroom db_classroom    = db_classrooms.get(operating_classrooms.next().intValue());
            final int         db_classroom_id = db_classroom.getId();
            encoder.put(db_classroom_id, c_classroom);
            decoder.put(c_classroom, db_classroom_id);

            final @NotNull ObjectList                       availabilities = db_classroom.getTimeoff().getAvailabilities();
            final @NotNull double                           timeoff[][]    = new double[availabilities.size()][];
            final ObjectListIterator<ObjectList<DBTimeOff>> dayIt          = availabilities.iterator();

            int accu_time = 0;
            int clst_time = 0;
            for(int c_day = 0; dayIt.hasNext(); ++c_day)
            {
                final ObjectList<DBTimeOff>         period   = dayIt.next();
                final ObjectListIterator<DBTimeOff> periodIt = period.iterator();

                final double[] periods = timeoff[c_day] = new double[period.size()];

                for(int c_period = 0; periodIt.hasNext(); ++c_period)
                {
                    final DBAvailability availability = periodIt.next().getAvailability();
                    periods[c_period] = availability.getValue();

                    if(availability.getId() == 1)
                    {
                        if(clst_time != 0)
                        {
                            accu_time += clst_time;
                            cluster_classroom_container.addLast(clst_time);
                            clst_time = 0;
                        }
                    }
                    else
                    {
                        ++clst_time;
                    }
                }
                if(clst_time != 0)
                {
                    accu_time += clst_time;
                    cluster_classroom_container.addLast(clst_time);
                }
                cluster_classroom_container.addFirst(accu_time);
                clustered_classroom_time[c_classroom][c_day] = cluster_classroom_container.stream().mapToInt(Integer::intValue).toArray();
                cluster_classroom_container.clear();
                accu_time = clst_time = 0;
            }

            classrooms[c_classroom] = new DSTimeOff(timeoff);
        }
    }

    private void generateLecture(final Int2ObjectMap<DBLecture> db_lecturers, IntListIterator operating_lecturers)
    {
        final @NotNull DSTimeOff[] lecturers = this.dataset.getLecturers();
        final @NotNull Int2IntMap  encoder   = this.encoder.getLecturers();
        final @NotNull Int2IntMap  decoder   = this.decoder.getLecturers();

        for(int counter = 0; operating_lecturers.hasNext(); ++counter)
        {
            final DBLecture db_lecture    = db_lecturers.get(operating_lecturers.next().intValue());
            final int       db_lecture_id = db_lecture.getId();
            encoder.put(db_lecture_id, counter);
            decoder.put(counter, db_lecture_id);

            lecturers[counter] = DSTimeOff.newInstance(db_lecture.getTimeoff());
        }
    }

    private void generateSubject(final Int2ObjectMap<DBSubject> db_subjects, IntListIterator operating_subjects)
    {
        final @NotNull DSTimeOff[] subjects = this.dataset.getSubjects();
        final @NotNull Int2IntMap  encoder  = this.encoder.getSubjects();
        final @NotNull Int2IntMap  decoder  = this.decoder.getSubjects();

        for(int counter = 0; operating_subjects.hasNext(); ++counter)
        {
            final DBSubject db_subject    = db_subjects.get(operating_subjects.next().intValue());
            final int       db_subject_id = db_subject.getId();
            encoder.put(db_subject_id, counter);
            decoder.put(counter, db_subject_id);

            subjects[counter] = DSTimeOff.newInstance(db_subject.getTimeoff());
        }
    }

    private void generateLesson(final @NotNull ObjectCollection<DBLesson> db_lessons)
    {
        final @NotNull DSLesson[] lessons = this.dataset.getLessons();
        final @NotNull Int2IntMap encoder = this.encoder.getLessons();
        final @NotNull Int2IntMap decoder = this.decoder.getLessons();

        int extra_counter = db_lessons.size() - 1;
        int counter       = -1;

        for(final @NotNull DBLesson lesson : db_lessons)
        {
            final int          db_lesson_count = lesson.getCount();
            final IntArrayList temp_link       = new IntArrayList(db_lesson_count);

            temp_link.add(counter + 1);
            for(int link_counter = 0; ++link_counter < db_lesson_count; )
            {
                temp_link.add(extra_counter + link_counter);
            }

            final int db_lesson_id = lesson.getId();

            ++counter;
            final int[] links = new int[temp_link.size() - 1];

            int link_counter = -1;
            for(final int link : temp_link)
            {
                if(link != counter)
                {
                    links[++link_counter] = link;
                }
            }

            encoder.put(db_lesson_id, counter);
            decoder.put(counter, db_lesson_id);

            lessons[counter] = DSLesson.newInstance(this.encoder, lesson, IntArrays.copy(links));

            for(int i = -1, is = db_lesson_count - 1; ++i < is; )
            {
                ++extra_counter;

                link_counter = -1;
                for(final int link : temp_link)
                {
                    if(link != extra_counter)
                    {
                        links[++link_counter] = link;
                    }
                }
                lessons[extra_counter] = DSLesson.newLinkInstance(this.encoder, lesson, counter, IntArrays.copy(links));
            }
        }
    }

    private void generateTimeDistribution(final ObjectSet<Int2IntMap.Entry> db_time_distribution)
    {
        final @NotNull int[] time_distribution = this.dataset.getTimeDistribution();
        for(Int2IntMap.Entry tmp_time_distribution : db_time_distribution)
        {
            time_distribution[tmp_time_distribution.getIntKey()] = tmp_time_distribution.getIntValue();
        }
    }

    @SuppressWarnings("ConstantConditions") private void generateLessonGroup(final @NotNull ObjectIterator<DBLessonGroup> db_lesson_group)
    {
        final @NotNull DSLessonGroup[] lesson_group                  = this.dataset.getLessonGroups();
        final @NotNull Int2IntMap      classroom_encoder             = this.encoder.getClassrooms();
        final @NotNull Int2IntMap      lesson_encoder                = this.encoder.getLessons();
        final int                      global_time_distribution_size = this.dataset.getTimeDistribution().length;

        for(int counter = -1; db_lesson_group.hasNext(); )
        {
            final @NotNull DBLessonGroup tmp_lesson_group = db_lesson_group.next();


            final @NotNull int[] classrooms = new int[tmp_lesson_group.getClassroomSize()];

            int data_counter = -1;
            for(DBClassroom classroom : tmp_lesson_group.getClassrooms())
            {
                classrooms[++data_counter] = classroom_encoder.get(classroom.getId());
            }

            final @NotNull int[] time_distribution = new int[global_time_distribution_size];
            int                  lesson_count      = 0;
            for(final DBLesson lesson : tmp_lesson_group.getLessons())
            {
                time_distribution[lesson.getSks()] += lesson.getCount();
                lesson_count += lesson.getCount();
            }

            final @NotNull int[] lessons = new int[lesson_count];
            data_counter = -1;
            for(final DBLesson lesson : tmp_lesson_group.getLessons())
            {
                final int lesson_id = lesson_encoder.get(lesson.getId());
                lessons[++data_counter] = lesson_id;

                for(final int link : this.dataset.getLesson(lesson_id).getLessonLink())
                {
                    lessons[++data_counter] = link;
                }
            }

            IntArrays.radixSort(lessons);

            lesson_group[++counter] = new DSLessonGroup(lessons, classrooms, time_distribution);
        }
    }

    @SuppressWarnings("ConstantConditions") private void generateLessonCluster(final @NotNull Iterator<DBLessonCluster> db_lesson_cluster, int registered_lesson_size)
    {
        @NotNull final DSLessonCluster[] lesson_clusters             = this.dataset.getLessonClusters();
        @NotNull final DSLessonGroup[]   ds_lesson_groups            = this.dataset.getLessonGroups();
        @NotNull final DSTimeOff[]       ds_classroom_timeoff        = this.dataset.getClassrooms();
        @NotNull final int[][][]         ds_clustered_classroom_time = this.dataset.getClusteredClassroomTime();
        @NotNull final Int2IntMap        classroom_encoder           = this.encoder.getClassrooms();
        @NotNull final Int2IntMap        lesson_encoder              = this.encoder.getLessons();

        for(int c_cluster = -1; db_lesson_cluster.hasNext(); )
        {
            @NotNull final DBLessonCluster tmp_lesson_cluster = db_lesson_cluster.next();
            @NotNull final int[]           classroom_check    = tmp_lesson_cluster.getClassrooms().stream().mapToInt(value -> classroom_encoder.get(value.getId())).toArray();

            int                            tmp_pre_allocated_time = 0;
            @NotNull final DSLessonGroup[] tmp_lesson_groups      = new DSLessonGroup[tmp_lesson_cluster.getLessonGroupSize()];
            int                            c_data                 = -1;
            for(final @NotNull DSLessonGroup lesson_group : ds_lesson_groups)
            {
                if(IntArrays.binarySearch(classroom_check, lesson_group.getClassroom(0)) >= 0)
                {
                    tmp_lesson_groups[++c_data] = lesson_group;
                    final int[] ds_time_distribution = lesson_group.getTimeDistributions();

                    for(int c_distribution = -1, cs_distribution = ds_time_distribution.length; ++c_distribution < cs_distribution; )
                    {
                        tmp_pre_allocated_time += (c_distribution * (ds_time_distribution[c_distribution]));
                    }
                }
            }

            @NotNull final int[]      tmp_classrooms        = new int[tmp_lesson_cluster.getClassroomSize()];
            @NotNull final Int2IntMap tmp_classroom_encoder = new Int2IntLinkedOpenHashMap(tmp_classrooms.length);
            @NotNull final Int2IntMap tmp_classroom_decoder = new Int2IntLinkedOpenHashMap(tmp_classrooms.length);
            c_data = -1;
            for(final @NotNull DBClassroom db_classroom : tmp_lesson_cluster.getClassrooms())
            {
                final int encoded_classroom = classroom_encoder.get(db_classroom.getId());
                tmp_classroom_encoder.put(encoded_classroom, ++c_data);
                tmp_classroom_decoder.put(c_data, encoded_classroom);
                tmp_classrooms[c_data] = c_data;
            }

            int                        tmp_classoom_available_time  = 0;
            @NotNull final DSTimeOff[] tmp_timeoff                  = new DSTimeOff[tmp_classrooms.length];
            @NotNull final int[][][]   tmp_clustered_classroom_time = new int[tmp_classrooms.length][][];
            for(final int tmp_classroom : tmp_classrooms)
            {
                tmp_timeoff[tmp_classroom] = ds_classroom_timeoff[tmp_classroom_decoder.get(tmp_classroom)];
                tmp_clustered_classroom_time[tmp_classroom] = ds_clustered_classroom_time[tmp_classroom_decoder.get(tmp_classroom)];
                for(final @NotNull int[] tmp_clustered_classroom_time_day : tmp_clustered_classroom_time[tmp_classroom])
                {
                    tmp_classoom_available_time += tmp_clustered_classroom_time_day[0];
                }
            }

            int tmp_lesson_size = 0;
            for(@NotNull final DBLesson db_lesson : tmp_lesson_cluster.getLessons())
            {
                tmp_lesson_size += db_lesson.getCount();
            }

            @NotNull final int[] tmp_lessons = new int[tmp_lesson_size];
            c_data = -1;
            for(@NotNull final DBLesson db_lesson : tmp_lesson_cluster.getLessons())
            {
                final int lesson_id = lesson_encoder.get(db_lesson.getId());
                tmp_lessons[++c_data] = lesson_id;

                for(final int link : this.dataset.getLesson(lesson_id).getLessonLink())
                {
                    tmp_lessons[++c_data] = link;
                }
            }

            IntArrays.radixSort(tmp_lessons);

            final int            tmp_lesson_null_count = tmp_classoom_available_time - tmp_pre_allocated_time;
            @NotNull final int[] tmp_lessons_null      = new int[tmp_lesson_null_count];
            for(int c_null = -1; ++c_null < tmp_lesson_null_count; )
            {
                tmp_lessons_null[c_null] = registered_lesson_size++;
            }

            assert tmp_lesson_null_count > 0;

            lesson_clusters[++c_cluster] = new DSLessonCluster(tmp_lesson_groups, tmp_lessons, tmp_lessons_null, tmp_classrooms, tmp_timeoff, tmp_clustered_classroom_time, tmp_classroom_encoder, tmp_classroom_decoder);
        }

        ObjectArrays.quickSort(lesson_clusters, (cluster_1, cluster_2) -> (int) FastMath.signum(cluster_1.getClassroomLength() - cluster_2.getClassroomLength()));
        DSLessonCluster.rearrangeLocator(lesson_clusters);
    }

    public @NotNull Dataset getDataset()
    {
        return this.dataset;
    }

    public @NotNull DatasetConverter getEncoder()
    {
        return this.encoder;
    }

    public @NotNull DatasetConverter getDecoder()
    {
        return this.decoder;
    }
}
