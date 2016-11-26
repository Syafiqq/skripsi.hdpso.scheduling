package model.method.pso.hdpso.core;

import it.unimi.dsi.fastutil.ints.IntArrays;
import java.util.Arrays;
import model.dataset.component.DSLesson;
import model.dataset.component.DSLessonCluster;
import model.dataset.component.DSLessonGroup;
import model.dataset.component.DSScheduleShufflingProperty;
import model.dataset.component.DSTimeOff;
import model.dataset.component.DSTimeOffPlacement;
import model.dataset.core.Dataset;
import model.dataset.core.DatasetConverter;
import model.dataset.loader.DatasetGenerator;
import model.method.pso.hdpso.component.Data;
import model.method.pso.hdpso.component.Particle;
import model.method.pso.hdpso.component.PlacementProperty;
import model.method.pso.hdpso.component.Position;
import model.method.pso.hdpso.component.RepairProperty;
import model.method.pso.hdpso.component.ScheduleContainer;
import model.method.pso.hdpso.component.Setting;
import model.method.pso.hdpso.component.Velocity;
import model.util.list.IntHList;
import org.apache.commons.math3.util.FastMath;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.core> created by : 
 * Name         : syafiq
 * Date / Time  : 22 November 2016, 4:14 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings({"WeakerAccess", "FieldCanBeLocal", "unused"}) public class PSO extends PSOOperation<Data, Velocity, Particle, DSScheduleShufflingProperty> implements ScheduleRandomable<Position[], DSScheduleShufflingProperty>, PositionRepairable<Particle>
{
    private final @NotNull int[]                       active_days;
    private final @NotNull int[]                       shuffled_active_days;
    private final @NotNull int[]                       active_periods;
    private final @NotNull int[]                       sks_distribution;
    private final @NotNull int[][][]                   clustered_classroom_time;
    private final @NotNull Setting                     setting;
    private final @NotNull DSTimeOff[]                 classes;
    private final @NotNull DSTimeOff[]                 classrooms;
    private final @NotNull DSTimeOff[]                 lecturers;
    private final @NotNull DSTimeOff[]                 subjects;
    private final @NotNull DSLesson[]                  lessons;
    private final @NotNull DSLessonGroup[]             lesson_group;
    private final @NotNull DSLessonCluster[]           lesson_cluster;
    private final @NotNull DSScheduleShufflingProperty shuffling_properties;
    private final @NotNull DatasetConverter            encoder;
    private final @NotNull DatasetConverter            decoder;

    public PSO(@NotNull final DatasetGenerator generator)
    {
        /*
        * Retrieve setting
        * */
        this.setting = Setting.getInstance();

        final @NotNull Dataset dataset = generator.getDataset();

        /*
        * Retrieve dataset
        * */
        this.active_days = dataset.getDays();
        this.shuffled_active_days = IntArrays.copy(this.active_days);
        this.active_periods = dataset.getPeriods();
        this.classes = dataset.getClasses();
        this.classrooms = dataset.getClassrooms();
        this.lecturers = dataset.getLecturers();
        this.subjects = dataset.getSubjects();
        this.lessons = dataset.getLessons();
        this.clustered_classroom_time = dataset.getClusteredClassroomTime();
        this.sks_distribution = dataset.getTimeDistribution();

        /*
        * Retrieve Working Set
        * */
        this.lesson_group = dataset.getLessonGroups();
        this.lesson_cluster = dataset.getLessonClusters();
        this.shuffling_properties = dataset.getShufflingProperty();

        /*
        * Retrieve Converter
        * */
        this.encoder = generator.getEncoder();
        this.decoder = generator.getDecoder();

        /*
        * Initialize current epoch
        * */
        super.cEpoch = 0;
    }

    @Override public void updateStoppingCondition()
    {
        /*
        * increment current epoch by 1
        * */
        ++super.cEpoch;
    }

    @Override public boolean isConditionSatisfied()
    {
        /*
        * Check whether current epoch/iteration reach upper limit
        * */
        return super.cEpoch == setting.max_epoch;
    }

    @Override public void initialize()
    {
        /*
        * Initialize swarm
        * */
        super.particles = new Particle[this.setting.max_particle];

        /*
        * Initialize particle within swarm
        * */
        for(int c_particle = -1, cs_particle = this.setting.max_particle; ++c_particle < cs_particle; )
        {
            /*
            * Initialize Repair property to record classroom and its day that already discovered
            * */
            @NotNull final RepairProperty[] repair = new RepairProperty[this.lesson_cluster.length];
            for(int c_cluster = -1, pool_size = this.lesson_cluster.length; ++c_cluster < pool_size; )
            {
                repair[c_cluster] = new RepairProperty(this.lesson_cluster[c_cluster].getClassroomTotal(), this.active_days.length);
            }

            /*
            * Initialize Placement property to check class and lecturer conflict.
            * */
            @NotNull final PlacementProperty placement_properties = new PlacementProperty(this.lecturers.length, this.classes.length, this.classrooms.length, this.active_days.length, this.active_periods.length, this.classrooms.length);

            /*
            * Assign new particle
            * */
            this.particles[c_particle] = new Particle(this.shuffling_properties, PSO.this, this.setting, placement_properties, repair);
        }

        /*
        * Assign gBest with random data
        * */
        super.gBest = Data.newInstance(super.particles[0].getPBest());

        /*
        * Calculate particles fitness
        * */
        for(@NotNull final Particle particle : super.particles)
        {
            this.calculate(particle);
        }
    }

    @Override protected void assignGBest()
    {
        /*
        * Sort particle by pBest value
        * */
        Arrays.sort(super.particles, Particle.particlePBestFitnessDescComparator);

        /*
        * Check if first particle (highest pBest value) is higher than current gBest fitness value
        * */
        if(super.particles[0].getPBest().getFitness() > super.gBest.getFitness())
        {
            Data.replaceData(super.gBest, super.particles[0].getPBest());
        }
    }

    @SuppressWarnings("ConstantConditions") @Override public void calculate(@NotNull final Particle particle)
    {
        /*
        * Initialize Conflict placement property
        * */
        @NotNull final PlacementProperty placement_property = particle.getPlacementProperty();
        final DSTimeOffPlacement[]       lecture_placement  = placement_property.getLecturePlacement();
        final DSTimeOffPlacement[]       class_placement    = placement_property.getClassPlacement();
        final IntHList                   lecture_fill       = placement_property.getLectureFill();
        final IntHList                   class_fill         = placement_property.getClassFill();

        /*
        * Initialize fitness value
        * */
        double fitness = 0.0;

        /*
        * Initialize cluster index
        * */
        int i_cluster = -1;
        for(@NotNull final DSLessonCluster lesson_cluster : this.lesson_cluster)
        {
            /*
            * Increment day index
            * */
            ++i_cluster;

            /*
            * Get lessons data in current cluster
            * */
            final int[] lessons = particle.getData().getPosition(i_cluster).getPosition();

            /*
            * Initialize lesson counter
            * */
            int c_lesson = -1;

            /*
            * Get lesson according to lesson counter
            * */
            DSLesson lesson = this.lessons[lessons[++c_lesson]];

            /*
            * Get current lesson sks
            * */
            int lesson_sks = lesson == null ? 1 : lesson.getSks();

            /*
            * Initialize sks counter
            * */
            int c_sks = 0;

            /*
            * Foreach classroom in current cluster
            * */
            for(final int classroom : lesson_cluster.getClassrooms())
            {
                /*
                * Initialize day index
                * Foreach day in current classroom
                * */
                int i_day = -1;
                for(final double[] day : lesson_cluster.getClassroomsTimeoff(classroom).getTimeoff())
                {
                    /*
                    * Increment day index
                    * */
                    ++i_day;

                    /*
                    * Initialize period index
                    * Foreach all period within day
                    * */
                    int i_period = -1;
                    for(final double period : day)
                    {
                       /*
                        * Increment period index
                        * */
                        ++i_period;

                        /*
                        * If current period time off is available
                        * */
                        if(period != 0.2)
                        {
                            /*
                            * Indicate if lesson is null
                            * */
                            if(lesson != null)
                            {
                                /*
                                * Get lecture and klass information from current lesson
                                * */
                                final int lecture = lesson.getLecture();
                                final int klass   = lesson.getKlass();

                                /*
                                * 1. Calculate subject timeoff
                                * 2. Calculate lecture timeoff and check if lecture is available
                                * 3. Calculate class timeoff
                                * 4. Calculate classroom timeoff
                                * 5. Check if lecture is available, if available check if lecture teach in same day and period (different classroom)
                                * 6. Check if klass attend in same day and period (different classroom)
                                * 7. Check if current lesson have linked lesson, if available check if current and its linked lesson is not the same day
                                * 8. Check if current lesson is allowed in classroom
                                * */
                                final double fitness_1 = (0.020 * (this.subjects[lesson.getSubject()].get(i_day, i_period)));
                                final double fitness_2 = (1.000 * (lecture == -1 ? 10 : (this.lecturers[lecture].get(i_day, i_period))));
                                final double fitness_3 = (0.040 * (this.classes[klass].get(i_day, i_period)));
                                final double fitness_4 = (0.001 * (period));
                                final double fitness_5 = (5.000 * (lecture == -1 ? 10 : (lecture_placement[lecture].putPlacementIfAbsent(i_day, i_period, lessons[c_lesson]) ? 10 : 0.1)));
                                final double fitness_6 = (5.000 * (class_placement[klass].putPlacementIfAbsent(i_day, i_period, lessons[c_lesson]) ? 10 : 0.1));
                                final double fitness_7 = (3.000 * (lesson.getLinkTotal() == 0 ? 10 : class_placement[klass].isNotTheSameDay(i_day, lesson.getLessonLink()) ? 10 : 0.1));
                                final double fitness_8 = (0.500 * (lesson.getAllowedClassroom(classroom) ? 10 : 0.1));

                                /*
                                * Accumulate fitness
                                * */
                                fitness += (fitness_1 + fitness_2 + fitness_3 + fitness_4 + fitness_5 + fitness_6 + fitness_7 + fitness_8);

                                /*
                                * fill lecture and klass for cleaning later
                                * */
                                if(lecture != -1)
                                {
                                    lecture_fill.add(lecture);
                                }
                                class_fill.add(klass);
                            }

                            /*
                            * check if counter sks have same value with lesson sks
                            * */
                            if(++c_sks == lesson_sks)
                            {
                                /*
                                * Refill current lesson data
                                * */
                                try
                                {
                                    lesson = this.lessons[lessons[++c_lesson]];
                                }
                                catch(ArrayIndexOutOfBoundsException ignored)
                                {
                                }
                                finally
                                {
                                    lesson_sks = lesson == null ? 1 : lesson.getSks();
                                    c_sks = 0;
                                }
                            }
                        }
                    }
                }
            }
        }

        /*
        * set particle fitness
        * */
        particle.setFitness(fitness);

        /*
        * reset placement
        * */
        placement_property.resetPlacement();
    }

    @Override public void repair(Particle data)
    {

    }

    @Override public Position[] random(@NotNull final DSScheduleShufflingProperty properties)
    {
        final Position[] random_schedule = new Position[this.lesson_cluster.length];
        for(int c_cluster = -1, cs_cluster = random_schedule.length; ++c_cluster < cs_cluster; )
        {
            random_schedule[c_cluster] = Position.newInstance(this.lesson_cluster[c_cluster].getLessonTotal() + this.lesson_cluster[c_cluster].getLessonNullTotal());
        }
        this.random(properties, random_schedule);
        return random_schedule;
    }

    @Override public void random(@NotNull final DSScheduleShufflingProperty properties, @NotNull final Position[] data)
    {
        for(int c_cluster = -1, cs_cluster = this.lesson_cluster.length; ++c_cluster < cs_cluster; )
        {
            this.random(properties, data, c_cluster);
        }
    }

    private void random(@NotNull final DSScheduleShufflingProperty properties, @NotNull final Position[] data, final int c_cluster)
    {
        /*
        * Reset classroom random property
        * */
        properties.reset_classroom_current_time(c_cluster);

        /*
        * 1. @lesson_cluster               : Select current cluster
        * 2. @lesson_distribution_window   : Calculate lesson distribution window, so we can distribute lesson all over classroom evenly
        * 3. @day_set                      : Get active day set
        * 4. @time_distribution            : Get time distribution template
        * 5. @lesson_null_set              : Get lesson null set current cluster
        * 6. @classroom_clustered_time     : Get classroom available time { classroom : day : {cumulative, window1, window2, window3}}
        * 7. @lesson_appender_manager      : Get classroom available property for current cluster {classroom : day : {current time(sks) : observed time (sks)} }
        * 8. @lesson_dataset               : Get lesson dataset
        * 9. @schedule_container           : Generate new schedule container
        * */
        @NotNull final DSLessonCluster lesson_cluster = this.lesson_cluster[c_cluster];

        final int                            lesson_distribution_window = (int) FastMath.ceil(lesson_cluster.getClassroomRegisteredTime() * 1f / lesson_cluster.getClassroomTotal() / this.active_days.length);
        @NotNull final int[]                 day_set                    = properties.getDaySet();
        @NotNull final int[]                 time_distribution          = properties.getTimeDistribution();
        @NotNull final int[]                 lesson_null_set            = properties.getLessonNullSet(c_cluster);
        @NotNull final int[][][]             classroom_clustered_time   = lesson_cluster.getClassroomClusteredTime();
        @NotNull final int[][][]             lesson_appender_manager    = properties.getLessonAppenderManager(c_cluster);
        @NotNull final DSLesson[]            lessons                    = this.lessons;
        @NotNull final ScheduleContainer[][] schedule_container         = properties.getScheduleContainer(c_cluster);
        @NotNull final IntHList              full_schedule              = properties.getFullScheduleContainer(c_cluster);

        properties.reset_schedule_container(c_cluster);
        full_schedule.reset();

        /*
        * Initialize lesson null counter
        * Shuffle lesson null
        * */
        int c_null = -1;
        IntArrays.shuffle(lesson_null_set, properties.random);

        /*
         * For each lesson set within lesson pool
         * */
        for(int c_lesson_group = -1, cs_lesson_group = lesson_cluster.getLessonGroupTotal(); ++c_lesson_group < cs_lesson_group; )
        {
            /*
            * 1. @lesson_set         : get lesson set at current cluster and its group
            * 2. @classroom_set      : get classroom set at current cluster and its group
            * */
            final int[] lesson_set    = properties.getLessonSet(c_cluster, c_lesson_group);
            final int[] classroom_set = properties.getClassroomSet(c_cluster, c_lesson_group);
            System.arraycopy(lesson_cluster.getLessonGroup(c_lesson_group).getTimeDistributions(), 0, time_distribution, 0, time_distribution.length);

            /*
            * Shuffle lesson and classroom
            * */
            IntArrays.shuffle(lesson_set, properties.random);
            IntArrays.shuffle(classroom_set, properties.random);

            /*
            * Retrieve lesson information
            * */
            int c_lesson  = -1;
            int id_lesson = lesson_set[++c_lesson];
            int classroom_filled_time;
            int lesson_time;

            /*
             * Fill Lesson inside Scheduler Container
             * Foreach classroom in classroom set
             * */
            classroom:
            for(final int classroom : classroom_set)
            {
                /*
                * Shuffle day set
                * */
                IntArrays.shuffle(day_set, properties.random);
                /*
                 * Foreach day in day set
                 * */
                for(final int day : day_set)
                {
                    /*
                    * 1. @appender_manager      : get classroom available appender_manager at current classroom and day {current classroom lesson_time cluster : total classroom observed lesson_time};
                    * 2. @clustered_time        : get classroom clustered lesson_time at current classroom and day;
                    * 3. @accumulated_time      : get accumulated total lesson_time at current clustered lesson_time;
                    * */
                    final int[] appender_manager = lesson_appender_manager[classroom][day];
                    final int[] clustered_time   = classroom_clustered_time[classroom][day];
                    final int   accumulated_time = clustered_time[0];

                    /*
                    * increment appender_manager {observed lesson_time (time_length)} with pre incremented current lesson_time (time_length)
                    * */
                    appender_manager[1] += clustered_time[++appender_manager[0]];

                    /*
                    * Looping while classroom filled lesson_time lesser than lesson distribution window
                    * */
                    while((classroom_filled_time = schedule_container[classroom][day].getSizeSKS()) < lesson_distribution_window)
                    {
                        /*
                        * Get time_length of current lesson id
                        * */
                        lesson_time = lessons[id_lesson].getSks();

                        /*
                        * Assign future size with classroom current size + new time_length
                        * */
                        int future_time = classroom_filled_time + lesson_time;

                        /*
                        * Check if new lesson is not overflow in current classroom and day
                        * */
                        if(future_time <= accumulated_time)
                        {
                            /*
                            * Check if new lesson is not overflow in observed current classroom and day
                            * */
                            if(future_time < appender_manager[1])
                            {
                                /*
                                * Append new lesson in current classroom and day;
                                * */
                                schedule_container[classroom][day].addSchedule(id_lesson, lesson_time);

                                /*
                                * Decrement lesson_time distribution
                                * */
                                --time_distribution[lesson_time];

                                try
                                {
                                    /*
                                    * Get lesson id of shuffled lesson set in current lesson counter
                                    * */
                                    id_lesson = lesson_set[++c_lesson];
                                }
                                catch(ArrayIndexOutOfBoundsException ignored)
                                {
                                    /*
                                    * decrement appender_manager {observed lesson_time (time_length)} with pos decremented current lesson_time (time_length)
                                    * */
                                    appender_manager[1] -= clustered_time[appender_manager[0]--];
                                    /*
                                    * change classroom
                                    * */
                                    continue classroom;
                                }
                            }
                            /*
                            * Check if new lesson is exactly equal in observed current classroom and day
                            * */
                            else if(future_time == appender_manager[1])
                            {
                                /*
                                * Append new lesson in current classroom and day;
                                * */
                                schedule_container[classroom][day].addSchedule(id_lesson, lesson_time);

                                /*
                                * Decrement lesson_time distribution
                                * */
                                --time_distribution[lesson_time];

                                try
                                {
                                    /*
                                    * increment appender_manager {observed lesson_time (time_length)} with pre incremented current lesson_time (time_length)
                                    * */
                                    appender_manager[1] += clustered_time[++appender_manager[0]];
                                }
                                catch(ArrayIndexOutOfBoundsException ignored)
                                {
                                    /*
                                    * rollback current lesson_time (time_length)
                                    * */
                                    --appender_manager[0];
                                }

                                try
                                {
                                    /*
                                    * Get lesson id of shuffled lesson set in current lesson counter
                                    * */
                                    id_lesson = lesson_set[++c_lesson];
                                }
                                catch(ArrayIndexOutOfBoundsException ignored)
                                {
                                    /*
                                    * decrement appender_manager {observed lesson_time (time_length)} with pos decremented current lesson_time (time_length)
                                    * */
                                    appender_manager[1] -= clustered_time[appender_manager[0]--];
                                    /*
                                    * Change classroom
                                    * */
                                    continue classroom;
                                }
                            }
                            /*
                            * Check if new lesson is overflow in observed current classroom and day
                            * */
                            else
                            {
                                /*
                                * Calculate remaining lesson_time;
                                * */
                                int remaining_time = appender_manager[1] - classroom_filled_time;

                                /*
                                * check if there are lesson available with time_length equal remaining lesson_time
                                * */
                                boolean fulfill = false;
                                for(int c_remaining = remaining_time; c_remaining != 0; --c_remaining)
                                {
                                    if(time_distribution[c_remaining] > 0)
                                    {
                                        /*
                                        * Search that lesson
                                        * */
                                        int c_lesson_remaining = c_lesson;
                                        while(lessons[lesson_set[++c_lesson_remaining]].getSks() != c_remaining)
                                        {

                                        }

                                        /*
                                        * Swap lesson
                                        * */
                                        final int value_temp = lesson_set[c_lesson];
                                        lesson_set[c_lesson] = lesson_set[c_lesson_remaining];
                                        lesson_set[c_lesson_remaining] = value_temp;

                                        /*
                                        * Update lesson information
                                        * */
                                        lesson_time = c_remaining;
                                        id_lesson = lesson_set[c_lesson];
                                        schedule_container[classroom][day].addSchedule(id_lesson, lesson_time);
                                        --time_distribution[lesson_time];

                                        try
                                        {
                                            /*
                                            * increment appender_manager {observed lesson_time (time_length)} with pre incremented current lesson_time (time_length)
                                            * */
                                            appender_manager[1] += clustered_time[++appender_manager[0]];
                                        }
                                        catch(ArrayIndexOutOfBoundsException ignored)
                                        {
                                            /*
                                            * rollback current lesson_time (time_length)
                                            * */
                                            --appender_manager[0];
                                        }

                                        try
                                        {
                                            /*
                                            * Get lesson id of shuffled lesson set in current lesson c_lesson_remaining
                                            * */
                                            id_lesson = lesson_set[++c_lesson];
                                        }
                                        catch(ArrayIndexOutOfBoundsException ignored)
                                        {
                                            /*
                                            * decrement appender_manager {observed lesson_time (time_length)} with pos decremented current lesson_time (time_length)
                                            * */
                                            appender_manager[1] -= clustered_time[appender_manager[0]--];
                                            /*
                                            * Change classroom
                                            * */
                                            continue classroom;
                                        }
                                        fulfill = true;
                                        break;
                                    }
                                }

                                if(!fulfill)
                                {
                                    /*
                                    * Fill remaining requirement with null lesson
                                    * */
                                    for(; remaining_time != 0; --remaining_time)
                                    {
                                        schedule_container[classroom][day].addSchedule(lesson_null_set[++c_null], 1);
                                    }

                                    try
                                    {
                                        /*
                                        * increment appender_manager {observed lesson_time (time_length)} with pre incremented current lesson_time (time_length)
                                        * */
                                        appender_manager[1] += clustered_time[++appender_manager[0]];
                                    }
                                    catch(ArrayIndexOutOfBoundsException ignored)
                                    {
                                        /*
                                        * rollback current lesson_time (time_length)
                                        * */
                                        --appender_manager[0];
                                    }
                                }
                            }
                        }
                        else
                        {
                            break;
                        }
                    }
                    /*
                    * decrement appender_manager {observed lesson_time (time_length)} with pos decremented current lesson_time (time_length)
                    * */
                    appender_manager[1] -= clustered_time[appender_manager[0]--];
                }
            }
        }

        /*
         * Fill remaining schedule container with null lesson
         * */
        for(int c_classroom = -1, cs_classroom = schedule_container.length; ++c_classroom < cs_classroom; )
        {
            for(int c_day = -1, cs_day = schedule_container[c_classroom].length; ++c_day < cs_day; )
            {
                full_schedule.addAll((schedule_container[c_classroom][c_day]));
                for(int cl_null = -1, cls_null = classroom_clustered_time[c_classroom][c_day][0] - schedule_container[c_classroom][c_day].getSizeSKS(); ++cl_null < cls_null; )
                {
                    full_schedule.add(lesson_null_set[++c_null]);
                }
            }
        }

        try
        {
            System.arraycopy(full_schedule.toArray(), 0, data[c_cluster].getPosition(), 0, full_schedule.size());
        }
        catch(ArrayIndexOutOfBoundsException ignored)
        {
            this.random(properties, data, c_cluster);
        }
    }

    public Particle getParticle(final int index)
    {
        return this.particles[index];
    }
}
