package model.method.pso.hdpso.core;

import it.unimi.dsi.fastutil.ints.IntArrayList;
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
import org.jetbrains.annotations.Nullable;

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
            this.calculate(this.particles[c_particle]);
        }

        /*
        * Assign gBest with random data
        * */
        super.gBest = Data.newInstanceOnly(super.particles[0].getPBest());

        /*
        * Calculate particles fitness
        * */
        for(@NotNull final Particle particle : super.particles)
        {
            this.calculate(particle);
        }
    }

    @Override public void assignGBest()
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
                                final double fitness_8 = (0.500 * (lesson.isLessonAllowed(classroom) ? 10 : 0.1));

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

    @SuppressWarnings({"Duplicates", "ConstantConditions"}) @Override public void repair(@NotNull final Particle particle)
    {
        /*
        * Initialize cluster index
        * Initialize active days
        * */
        int                  i_cluster   = -1;
        @NotNull final int[] active_days = this.active_days;

        /*
        * Search all position cluster
        * */
        cluster:
        for(@NotNull final DSLessonCluster lesson_cluster : this.lesson_cluster)
        {
            /*
            * increment cluster index
            * */
            ++i_cluster;

            /*
            * try to repair
            * */
            try
            {
                /*
                * Initialize lesson id
                * Initialize repair property
                * */
                @NotNull final int[]          lesson_id       = particle.getData().getPosition(i_cluster).getPosition();
                @NotNull final RepairProperty repair_property = particle.getRepairProperty(i_cluster);
                @NotNull final boolean[][]    rp_absent       = repair_property.getAbsent();

                /*
                * reset repair property
                * */
                repair_property.resetAbsent();

                /*
                * Initialize lesson counter
                * Initialize Lesson according to lesson_id index lesson_counter
                * */
                int                c_lesson    = -1;
                @Nullable DSLesson lesson      = this.lessons[lesson_id[++c_lesson]];
                int                lesson_time = lesson == null ? 1 : lesson.getSks();

                /*
                * For all classroom in current lesson pool
                * */
                for(final int classroom : lesson_cluster.getClassrooms())
                {
                    /*
                    * For all operational day in current classroom
                    * */
                    for(final int day : this.active_days)
                    {
                        /*
                        * mark lesson counter as start of the day
                        * */
                        repair_property.set(classroom, day, c_lesson);

                        /*
                        * Initialize classroom clustered time from current classroom and day
                        * Initialize current sks
                        * */
                        @NotNull final int[] clustered_time = lesson_cluster.getClassroomClusteredTime(classroom, day);
                        int                  current_time   = 0;

                        /*
                        * For all time cluster in current day
                        * */
                        for(int c_cluster = 1, time_cluster = clustered_time[c_cluster], cs_cluster = clustered_time.length; c_cluster < cs_cluster; )
                        {
                            /*
                            * Check whether current lesson is null
                            * */
                            if(lesson == null)
                            {
                                final int future_time = current_time + lesson_time;
                                /*
                                 * Check whether incoming sks + current sks LESS than cluster capacity
                                 * */
                                if(future_time < time_cluster)
                                {
                                    /*
                                    * Append current lesson
                                    * Shift to next lesson
                                    * */
                                    current_time += lesson_time;
                                    lesson = this.lessons[lesson_id[++c_lesson]];
                                    lesson_time = lesson == null ? 1 : lesson.getSks();
                                }

                                /*
                                 * Check whether incoming sks + current sks equals sks cluster capacity so we change cluster time after that
                                 * */
                                else if(future_time == time_cluster)
                                {
                                    try
                                    {
                                        /*
                                        * Try to shift next lesson
                                        * */
                                        lesson = this.lessons[lesson_id[++c_lesson]];
                                        lesson_time = lesson == null ? 1 : lesson.getSks();
                                    }
                                    catch(ArrayIndexOutOfBoundsException ignored)
                                    {
                                        continue cluster;
                                    }

                                    /*
                                    * shift time cluster index
                                    * set current time to zero
                                    * */
                                    ++c_cluster;
                                    current_time = 0;

                                    /*
                                    * Try to shift time cluster
                                    * */
                                    try
                                    {
                                        time_cluster = clustered_time[c_cluster];
                                    }
                                    catch(ArrayIndexOutOfBoundsException ignored)
                                    {
                                    }
                                }
                            }
                            /*
                            * Check whether current lesson is not null (active lesson)
                            * */
                            else
                            {
                                /*
                                * Check whether current lesson is allowed in current classroom
                                * */
                                if(lesson.isLessonAllowed(classroom))
                                {
                                    final int future_time = current_time + lesson_time;
                                    /*
                                     * Check whether incoming sks + current sks LESS than cluster capacity
                                     * */
                                    if(future_time < time_cluster)
                                    {
                                        /*
                                        * Append current lesson
                                        * Shift to next lesson
                                        * */
                                        current_time += lesson_time;
                                        lesson = this.lessons[lesson_id[++c_lesson]];
                                        lesson_time = lesson == null ? 1 : lesson.getSks();
                                    }

                                    /*
                                     * Check whether incoming sks + current sks equals sks cluster capacity so we change cluster time after that
                                     * */
                                    else if(future_time == time_cluster)
                                    {
                                        try
                                        {
                                            /*
                                            * Try to shift next lesson
                                            * */
                                            lesson = this.lessons[lesson_id[++c_lesson]];
                                            lesson_time = lesson == null ? 1 : lesson.getSks();
                                        }
                                        catch(ArrayIndexOutOfBoundsException ignored)
                                        {
                                            continue cluster;
                                        }

                                        /*
                                        * shift time cluster index
                                        * set current time to zero
                                        * */
                                        ++c_cluster;
                                        current_time = 0;

                                        /*
                                        * Try to shift time cluster
                                        * */
                                        try
                                        {
                                            time_cluster = clustered_time[c_cluster];
                                        }
                                        catch(ArrayIndexOutOfBoundsException ignored)
                                        {
                                        }
                                    }

                                    /*
                                     * Check whether incoming sks + current sks overflow sks cluster capacity so we must change incoming lesson with lesson in which occupy remaining sks capacity
                                     * */
                                    else
                                    {
                                        /*
                                        * calculate time need
                                        * */
                                        final int need       = time_cluster - current_time;
                                        boolean   is_changed = false;

                                        /*
                                         * Find lesson which its sks completely equals remaining sks [search forward]
                                         * */
                                        for(int i_lookup = c_lesson, is_lookup = lesson_id.length; ++i_lookup < is_lookup; )
                                        {
                                            /*
                                            * Get lesson from lookup index
                                            * */
                                            @Nullable final DSLesson lesson_lookup = this.lessons[lesson_id[i_lookup]];
                                            /*
                                            * Check if lesson satisfy need time
                                            * */
                                            if(lesson_lookup == null)
                                            {
                                                if(need != 1)
                                                {
                                                    continue;
                                                }
                                            }
                                            else
                                            {
                                                if((lesson_lookup.getSks() != need) || !lesson_lookup.isLessonAllowed(classroom))
                                                {
                                                    continue;
                                                }
                                            }

                                            /*
                                            * Swap lesson id position
                                            * */
                                            is_changed = true;
                                            final int swap_temp = lesson_id[i_lookup];
                                            lesson_id[i_lookup] = lesson_id[c_lesson];
                                            lesson_id[c_lesson] = swap_temp;

                                            /*
                                            * Break the lookup
                                            * */
                                            break;
                                        }

                                        /*
                                         * If function above fail, find lesson which its sks less than need sks [search forward]
                                         * */
                                        if(!is_changed)
                                        {
                                            is_changed = false;
                                            for(int i_lookup = c_lesson, is_lookup = lesson_id.length; ++i_lookup < is_lookup; )
                                            {
                                                 /*
                                                * Get lesson from lookup index
                                                * */
                                                @Nullable final DSLesson lesson_lookup = this.lessons[lesson_id[i_lookup]];
                                                /*
                                                * Check if lesson satisfy need time
                                                * */
                                                if(lesson_lookup == null)
                                                {
                                                }
                                                else
                                                {
                                                    if((lesson_lookup.getSks() >= need) || !lesson_lookup.isLessonAllowed(classroom))
                                                    {
                                                        continue;
                                                    }
                                                }

                                                /*
                                                * Swap lesson id position
                                                * */
                                                is_changed = true;
                                                final int swap_temp = lesson_id[i_lookup];
                                                lesson_id[i_lookup] = lesson_id[c_lesson];
                                                lesson_id[c_lesson] = swap_temp;

                                                /*
                                                * break the lookup
                                                * */
                                                break;
                                            }

                                            // Addition : add function that check in possible classroom that completely equal like function block when lesson is not allowed
                                            /*
                                             * If function above fail, find lesson in all possible classrooms [search from its available classroom that already observed]
                                             * */
                                            if(!is_changed)
                                            {
                                                is_changed = false;
                                                final int remain = lesson_time - need;
                                                /*
                                                 * Lookup all its lesson available classroom
                                                 * */
                                                lookup_replacement:
                                                for(int classroom_lookup : lesson.getAvailableClassroom())
                                                {
                                                    /*
                                                    * Lookup operational day of current classroom
                                                    * */
                                                    for(int day_lookup : active_days)
                                                    {
                                                        int lookup_start = -1;
                                                        int lookup_end   = -1;
                                                        /*
                                                         * Check if current classroom and day is already observed for lookup_start
                                                         * */
                                                        if(rp_absent[classroom_lookup][day_lookup])
                                                        {
                                                            try
                                                            {
                                                                /*
                                                                 * Check if current classroom and the day after is already observed for lookup_end
                                                                 * */
                                                                if(rp_absent[classroom_lookup][day_lookup + 1])
                                                                {
                                                                    lookup_start = repair_property.getPosition(classroom_lookup, day_lookup) - 1;
                                                                    lookup_end = repair_property.getPosition(classroom_lookup, day_lookup + 1);
                                                                }
                                                            }
                                                            catch(ArrayIndexOutOfBoundsException ignored)
                                                            {
                                                                try
                                                                {
                                                                    /*
                                                                     * Check if current day and the classroom after is already observed for lookup_end
                                                                     * */
                                                                    if(rp_absent[classroom_lookup + 1][0])
                                                                    {
                                                                        lookup_start = repair_property.getPosition(classroom_lookup, day_lookup) - 1;
                                                                        lookup_end = repair_property.getPosition(classroom_lookup + 1, 0);
                                                                    }
                                                                }
                                                                /*
                                                                 * Assign lookup_end in the end of lesson_id list
                                                                 * */
                                                                catch(ArrayIndexOutOfBoundsException ignored1)
                                                                {
                                                                    lookup_start = repair_property.getPosition(classroom_lookup, day_lookup) - 1;
                                                                    lookup_end = lesson_id.length;
                                                                }
                                                            }
                                                        }

                                                        /*
                                                        * Check if lookup bound is discovered
                                                        * */
                                                        if(lookup_end != -1)
                                                        {
                                                            /*
                                                            * Try to replace lesson between specified bound
                                                            * */
                                                            if(this.exchangeAndReplace(lookup_start, lookup_end, need, remain, classroom, lesson_id, c_lesson, lesson_cluster.getClassroomClusteredTime(classroom_lookup, day_lookup)))
                                                            {
                                                                is_changed = true;
                                                                /*
                                                                * Shift all cluster index
                                                                * */
                                                                do
                                                                {
                                                                    try
                                                                    {
                                                                        repair_property.decrementIndex(classroom_lookup, ++day_lookup);
                                                                    }
                                                                    catch(ArrayIndexOutOfBoundsException ignored)
                                                                    {
                                                                        repair_property.decrementIndex(++classroom_lookup, day_lookup = 0);
                                                                    }
                                                                }
                                                                while((classroom_lookup != classroom) || (day_lookup != day));

                                                                /*
                                                                * Read previous lesson as current lesson
                                                                * */
                                                                lesson = this.lessons[lesson_id[--c_lesson]];
                                                                lesson_time = lesson == null ? 1 : lesson.getSks();
                                                                break lookup_replacement;
                                                            }
                                                        }
                                                    }
                                                }

                                                /*
                                                 * If function above fail, generate new schedule.
                                                 * */
                                                if(!is_changed)
                                                {
                                                    this.random(particle.getVelocityProperty().getPRandProperty(), particle.getVelocityProperty().getPRand(), i_cluster);
                                                    Position.replace(particle.getData().getPosition(i_cluster), particle.getVelocityProperty().getPRand(i_cluster));
                                                    continue cluster;
                                                }
                                            }
                                            /*
                                            * If swap lesson successful read current lesson again
                                            * */
                                            else
                                            {
                                                lesson = this.lessons[lesson_id[c_lesson]];
                                                lesson_time = lesson == null ? 1 : lesson.getSks();
                                            }
                                        }
                                        /*
                                        * If swap lesson successful read current lesson again
                                        * */
                                        else
                                        {
                                            lesson = this.lessons[lesson_id[c_lesson]];
                                            lesson_time = lesson == null ? 1 : lesson.getSks();
                                        }
                                    }
                                }
                                else
                                {
                                    /*
                                    * Why no lookup to allowed classroom that already discovered first ? <<< Next Issues
                                    * Well that depend on how many allowed classroom that already discovered
                                    * if none of allowed classroom is already discovered then this function block is skipped
                                    * */
                                    final int need       = time_cluster - current_time;
                                    boolean   is_changed = false;
                                    /*
                                     * Find lesson which its sks less or equals than remaining sks [search forward]
                                     * */
                                    for(int i_lookup = c_lesson, is_lookup = lesson_id.length; ++i_lookup < is_lookup; )
                                    {
                                        /*
                                        * Get lesson from lookup index
                                        * */
                                        @Nullable final DSLesson lesson_lookup = this.lessons[lesson_id[i_lookup]];
                                        /*
                                        * Check if lesson satisfy need time
                                        * */
                                        if(lesson_lookup == null)
                                        {
                                        }
                                        else
                                        {
                                            if((lesson_lookup.getSks() > need) || !lesson_lookup.isLessonAllowed(classroom))
                                            {
                                                continue;
                                            }
                                        }
                                        is_changed = true;

                                        /*
                                        * Swap lesson id position
                                        * */
                                        final int swap_temp = lesson_id[i_lookup];
                                        lesson_id[i_lookup] = lesson_id[c_lesson];
                                        lesson_id[c_lesson] = swap_temp;

                                        /*
                                        * Break the lookup
                                        * */
                                        break;
                                    }

                                    /*
                                     * If function above fail find lesson that equal current misplaced lesson
                                     * */
                                    if(!is_changed)
                                    {
                                        is_changed = false;
                                        /*
                                         * Lookup all its lesson available classroom
                                         * */
                                        /*
                                        * Try to shuffle available classroom first
                                        * */
                                        lookup_replacement:
                                        for(int classroom_lookup : lesson.getAvailableClassroom())
                                        {
                                            /*
                                            * Lookup operational day of current classroom
                                            * */
                                            for(int day_lookup : active_days)
                                            {
                                                int lookup_start = -1;
                                                int lookup_end   = -1;
                                                /*
                                                 * Check if current classroom and day is already observed for lookup_start
                                                 * */
                                                if(rp_absent[classroom_lookup][day_lookup])
                                                {
                                                    try
                                                    {
                                                        /*
                                                         * Check if current classroom and the day after is already observed for lookup_end
                                                         * */
                                                        if(rp_absent[classroom_lookup][day_lookup + 1])
                                                        {
                                                            lookup_start = repair_property.getPosition(classroom_lookup, day_lookup) - 1;
                                                            lookup_end = repair_property.getPosition(classroom_lookup, day_lookup + 1);
                                                        }
                                                    }
                                                    catch(ArrayIndexOutOfBoundsException ignored)
                                                    {
                                                        try
                                                        {
                                                            /*
                                                             * Check if current day and the classroom after is already observed for lookup_end
                                                             * */
                                                            if(rp_absent[classroom_lookup + 1][0])
                                                            {
                                                                lookup_start = repair_property.getPosition(classroom_lookup, day_lookup) - 1;
                                                                lookup_end = repair_property.getPosition(classroom_lookup + 1, 0);
                                                            }
                                                        }
                                                        /*
                                                         * Assign lookup_end in the end of lesson_id list
                                                         * */
                                                        catch(ArrayIndexOutOfBoundsException ignored1)
                                                        {
                                                            lookup_start = repair_property.getPosition(classroom_lookup, day_lookup) - 1;
                                                            lookup_end = lesson_id.length;
                                                        }
                                                    }
                                                }

                                                /*
                                                * Check if lookup bound is discovered
                                                * */
                                                if(lookup_end != -1)
                                                {
                                                    while(++lookup_start < lookup_end)
                                                    {
                                                        /*
                                                        * Get lesson from lookup index
                                                        * */
                                                        @Nullable final DSLesson lesson_lookup = this.lessons[lesson_id[lookup_start]];
                                                        /*
                                                        * Check if lesson satisfy need time
                                                        * */
                                                        if(lesson_lookup == null)
                                                        {
                                                            if(lesson_time != 1)
                                                            {
                                                                continue;
                                                            }
                                                        }
                                                        else
                                                        {
                                                            if((lesson_lookup.getSks() != lesson_time) || !lesson_lookup.isLessonAllowed(classroom))
                                                            {
                                                                continue;
                                                            }
                                                        }
                                                        is_changed = true;

                                                        /*
                                                        * Swap lesson id position
                                                        * */
                                                        final int swap_temp = lesson_id[lookup_start];
                                                        lesson_id[lookup_start] = lesson_id[c_lesson];
                                                        lesson_id[c_lesson] = swap_temp;

                                                        /*
                                                        * Break the lookup
                                                        * */
                                                        break lookup_replacement;
                                                    }
                                                }
                                            }
                                        }

                                        /*
                                         * If function above fail, find lesson in all possible classrooms [search from its available classroom that already observed]
                                         * */
                                        if(!is_changed)
                                        {
                                            is_changed = false;
                                            /*
                                             * Lookup all its lesson available classroom
                                             * */
                                            lookup_replacement:
                                            for(int classroom_lookup : lesson.getAvailableClassroom())
                                            {
                                                /*
                                                * Lookup operational day of current classroom
                                                * */
                                                for(int day_lookup : active_days)
                                                {
                                                    int lookup_start = -1;
                                                    int lookup_end   = -1;
                                                    /*
                                                     * Check if current classroom and day is already observed for lookup_start
                                                     * */
                                                    if(rp_absent[classroom_lookup][day_lookup])
                                                    {
                                                        try
                                                        {
                                                            /*
                                                             * Check if current classroom and the day after is already observed for lookup_end
                                                             * */
                                                            if(rp_absent[classroom_lookup][day_lookup + 1])
                                                            {
                                                                lookup_start = repair_property.getPosition(classroom_lookup, day_lookup) - 1;
                                                                lookup_end = repair_property.getPosition(classroom_lookup, day_lookup + 1);
                                                            }
                                                        }
                                                        catch(ArrayIndexOutOfBoundsException ignored)
                                                        {
                                                            try
                                                            {
                                                                /*
                                                                 * Check if current day and the classroom after is already observed for lookup_end
                                                                 * */
                                                                if(rp_absent[classroom_lookup + 1][0])
                                                                {
                                                                    lookup_start = repair_property.getPosition(classroom_lookup, day_lookup) - 1;
                                                                    lookup_end = repair_property.getPosition(classroom_lookup + 1, 0);
                                                                }
                                                            }
                                                            /*
                                                             * Assign lookup_end in the end of lesson_id list
                                                             * */
                                                            catch(ArrayIndexOutOfBoundsException ignored1)
                                                            {
                                                                lookup_start = repair_property.getPosition(classroom_lookup, day_lookup) - 1;
                                                                lookup_end = lesson_id.length;
                                                            }
                                                        }
                                                    }

                                                    /*
                                                    * Check if lookup bound is discovered
                                                    * */
                                                    if(lookup_end != -1)
                                                    {
                                                        is_changed = false;
                                                        final int remain = current_time - need;
                                                        /*
                                                        * Try to find replacement
                                                        * */
                                                        if(this.exchangeAndReplace(lookup_start, lookup_end, need, remain, classroom, lesson_id, c_lesson, lesson_cluster.getClassroomClusteredTime(classroom_lookup, day_lookup)))
                                                        {
                                                            is_changed = true;
                                                            /*
                                                            * Shift all cluster index
                                                            * */
                                                            do
                                                            {
                                                                try
                                                                {
                                                                    repair_property.decrementIndex(classroom_lookup, ++day_lookup);
                                                                }
                                                                catch(ArrayIndexOutOfBoundsException ignored)
                                                                {
                                                                    repair_property.decrementIndex(++classroom_lookup, day_lookup = 0);
                                                                }
                                                            }
                                                            while((classroom_lookup != classroom) || (day_lookup != day));

                                                            /*
                                                            * Read previous lesson as current lesson
                                                            * */
                                                            lesson = this.lessons[lesson_id[--c_lesson]];
                                                            lesson_time = lesson == null ? 1 : lesson.getSks();
                                                            break lookup_replacement;
                                                        }
                                                    }
                                                }
                                            }

                                            /*
                                             * If function above fail, generate random schedule.
                                             * */
                                            if(!is_changed)
                                            {
                                                this.random(particle.getVelocityProperty().getPRandProperty(), particle.getVelocityProperty().getPRand(), i_cluster);
                                                Position.replace(particle.getData().getPosition(i_cluster), particle.getVelocityProperty().getPRand(i_cluster));
                                                continue cluster;
                                            }
                                        }
                                        /*
                                        * If swap lesson successful read current lesson again
                                        * */
                                        else
                                        {
                                            lesson = this.lessons[lesson_id[c_lesson]];
                                            lesson_time = lesson == null ? 1 : lesson.getSks();
                                        }
                                    }
                                    /*
                                    * If swap lesson successful read current lesson again
                                    * */
                                    else
                                    {
                                        lesson = this.lessons[lesson_id[c_lesson]];
                                        lesson_time = lesson == null ? 1 : lesson.getSks();
                                    }
                                }
                            }
                        }
                    }
                }
            }
            catch(Exception ignored)
            {
                this.random(particle.getVelocityProperty().getPRandProperty(), particle.getVelocityProperty().getPRand(), i_cluster);
                Position.replace(particle.getData().getPosition(i_cluster), particle.getVelocityProperty().getPRand(i_cluster));
            }
        }
    }

    @SuppressWarnings({"Duplicates", "ConstantConditions"}) private boolean exchangeAndReplace(int lookup_start, int lookup_end, int need, int remain, int current_classroom, int[] lesson_id, int lesson_counter, int[] time) throws Exception
    {
        boolean is_found         = false;
        int     need_index       = -1;
        int     need_cluster     = -1;
        int     overflow_index   = -1;
        int     overflow_cluster = -1;
        int     time_index       = 1;
        int     current_sks      = 0;

        /*
         * Find lesson that equals number of sks need and allowed each other if replace happened.
         * */
        for(int i_lookup = lookup_start; ++i_lookup < lookup_end; )
        {
            /*
            * Get lesson from lookup index
            * */
            @Nullable final DSLesson lesson_need      = this.lessons[lesson_id[i_lookup]];
            final int                lesson_need_time = lesson_need == null ? 1 : lesson_need.getSks();
            current_sks += lesson_need_time;

            /*
            * Check if lesson satisfy need time
            * */
            if(lesson_need == null)
            {
                if(!(need == 1))
                {
                    /*
                    * Shift time cluster
                    * */
                    if(current_sks == time[time_index])
                    {
                        ++time_index;
                        current_sks = 0;
                    }
                    continue;
                }
            }
            else
            {
                if(!((lesson_need_time == need) && lesson_need.isLessonAllowed(current_classroom)))
                {
                    /*
                    * Shift time cluster
                    * */
                    if(current_sks == time[time_index])
                    {
                        ++time_index;
                        current_sks = 0;
                    }
                    continue;
                }
            }

            /*
            * Swap lesson id position
            * */
            need_index = i_lookup;
            need_cluster = time_index;
            is_found = true;
            break;
        }

        /*
         * Find lesson that equals number of sks overflow and allowed each other if replace happened.
         * */
        if(is_found)
        {
            is_found = false;
            current_sks = 0;
            time_index = 1;
            for(int i_lookup = lookup_start; ++i_lookup < lookup_end; )
            {
                /*
                * Get lesson from lookup index
                * */
                @Nullable final DSLesson lesson_remain      = this.lessons[lesson_id[i_lookup]];
                final int                lesson_remain_time = lesson_remain == null ? 1 : lesson_remain.getSks();
                current_sks += lesson_remain_time;

                /*
                * Check if lesson satisfy remaining time
                * */
                if(lesson_remain == null)
                {
                    if(!((remain == 1) && (i_lookup != need_index)))
                    {
                        /*
                        * Shift time cluster
                        * */
                        if(current_sks == time[time_index])
                        {
                            ++time_index;
                            current_sks = 0;
                        }
                        continue;
                    }
                }
                else
                {
                    if(!((lesson_remain_time == remain) && (i_lookup != need_index) && lesson_remain.isLessonAllowed(current_classroom)))
                    {
                        /*
                        * Shift time cluster
                        * */
                        if(current_sks == time[time_index])
                        {
                            ++time_index;
                            current_sks = 0;
                        }
                        continue;
                    }
                }

                /*
                * Swap lesson id position
                * */
                overflow_index = i_lookup;
                overflow_cluster = time_index;
                is_found = true;
                break;
            }
        }

        /*
         * check if lesson need and lesson overflow has been found
         * */
        if(is_found)
        {
            /*
             * check if both of lesson in same cluster
             * */
            if(need_cluster == overflow_cluster)
            {
                /*
                 * check lesson need next to lesson overflow
                 * */
                if(Math.abs(overflow_index - need_index) == 1)
                {
                    /*
                     * Arrange lesson position so need index < overflow index
                     * xonxxx -> xnoxxx
                     * */
                    if(need_index > overflow_index)
                    {
                        int index_temp = lesson_id[need_index];
                        lesson_id[need_index] = lesson_id[overflow_index];
                        lesson_id[overflow_index] = index_temp;

                        index_temp = overflow_index;
                        overflow_index = need_index;
                        need_index = index_temp;
                    }
                }
                else
                {
                    /*
                     * Arrange lesson position so need index < overflow index
                     * xoxnxxx -> xnoxxxx
                     * */
                    if(need_index > overflow_index)
                    {
                        final int index_temp = lesson_id[need_index];
                        lesson_id[need_index] = lesson_id[overflow_index + 1];
                        lesson_id[overflow_index + 1] = lesson_id[overflow_index];
                        lesson_id[overflow_index] = index_temp;

                        need_index = overflow_index;
                        overflow_index = need_index + 1;
                    }
                    /*
                     * Arrange lesson position so need index < overflow index
                     * xnxoxxx -> xnoxxxx
                     * */
                    else
                    {
                        final int index_temp = lesson_id[need_index + 1];
                        lesson_id[need_index + 1] = lesson_id[overflow_index];
                        lesson_id[overflow_index] = index_temp;

                        overflow_index = need_index + 1;
                    }
                }

                /*
                 * Begin to exchange and shift position
                 * */
                final int need_value     = lesson_id[need_index];
                final int overflow_value = lesson_id[overflow_index];

                System.arraycopy(lesson_id, overflow_index + 1, lesson_id, overflow_index, Math.abs(lesson_counter - overflow_index));

                lesson_id[need_index] = lesson_id[lesson_counter];
                lesson_id[lesson_counter - 1] = need_value;
                lesson_id[lesson_counter] = overflow_value;
                return true;
            }
            /*
             * lesson need and lesson overflow on different cluster time
             * xnx|xxx|oxx -> nox|xxx|xxx
             * */
            else
            {
                return this.simulateExchange(lookup_start, lookup_end, need_index, overflow_index, lesson_counter, time, lesson_id);
            }
        }

        /*
        * Fail to find lesson need and lesson overflow candidate
        * */
        return false;
    }

    private boolean simulateExchange(int lookup_start, int lookup_end, int need_index, int overflow_index, int lesson_counter, int[] time, int[] lesson_id) throws Exception
    {
        int last_index_sks       = 0;
        int cumulative_need_size = this.lessons[lesson_id[need_index]].getSks() + this.lessons[lesson_id[overflow_index]].getSks();

        //Priority Queue
        IntArrayList container = new IntArrayList(lookup_end - lookup_start - 1);
        IntArrayList fuel      = new IntArrayList(lookup_end - lookup_start - 2);

        /*
        * Fill fuel with lesson id in one day sort by its sks size descending
        * */
        lesson_lookup:
        for(int lesson_index = lookup_start; ++lesson_index < lookup_end; )
        {
            if((lesson_index != need_index) && (lesson_index != overflow_index))
            {
                int lesson_sks = this.lessons[lesson_id[lesson_index]].getSks();
                for(int fuel_index = -1, fuel_size = fuel.size(); ++fuel_index < fuel_size; )
                {
                    if(lesson_sks == last_index_sks)
                    {
                        fuel.add(lesson_index);
                        continue lesson_lookup;
                    }
                    if(lesson_sks > this.lessons[lesson_id[fuel.getInt(fuel_index)]].getSks())
                    {
                        fuel.add(fuel_index, lesson_index);
                        continue lesson_lookup;
                    }
                }
                fuel.add(lesson_index);
                last_index_sks = lesson_sks;
            }
        }

        /*
        * Fill lesson need and overflow wrapped by -1 lesson
        * */
        for(int fuel_index = -1, fuel_size = fuel.size(); ++fuel_index < fuel_size; )
        {
            if(cumulative_need_size >= this.lessons[lesson_id[fuel.getInt(fuel_index)]].getSks())
            {
                fuel.add(fuel_index, -1);
                break;
            }
        }

        /*
        * Fill container which fit each cluster
        * */
        int time_index  = 1;
        int current_sks = 0;
        int old_fuel_size;
        do
        {
            old_fuel_size = fuel.size();
            for(int fuel_index = -1, fuel_size = fuel.size(); ++fuel_index < fuel_size; )
            {
                final int fuel_lesson = fuel.getInt(fuel_index);
                int       temp_sks;
                /*
                * fill incoming sks size;
                * */
                try
                {
                    temp_sks = this.lessons[lesson_id[fuel_lesson]].getSks();
                }
                catch(ArrayIndexOutOfBoundsException ignored)
                {
                    temp_sks = cumulative_need_size;
                }

                /*
                * Check incoming sks fit current cluster time
                * */
                if(temp_sks + current_sks <= time[time_index])
                {
                    current_sks += temp_sks;

                    if(fuel_lesson == -1)
                    {
                        /*
                        * Add Lesson need and overflow
                        * */
                        container.add(need_index);
                        container.add(overflow_index);

                        /*
                        * Change need index and overflow index
                        * */
                        need_index = container.size() - 1 + lookup_start;
                        overflow_index = need_index + 1;
                    }
                    else
                    {
                        /*
                        * Add ordinary lesson
                        * */
                        container.add(fuel_lesson);
                    }

                    /*
                    * Shift lesson cluster time
                    * */
                    if(current_sks == time[time_index])
                    {
                        ++time_index;
                        current_sks = 0;
                    }

                    /*
                    * Remove Fuel
                    * */
                    fuel.removeInt(fuel_index);

                    /*
                    * Skip remaining search
                    * */
                    break;
                }
            }
        }
        while(old_fuel_size != fuel.size());

        /*
        * Check if arrangement fits all lesson cluster
        * */
        if(fuel.size() == 0)
        {
            /*
            * Convert container data with lesson id according to data
            * */
            for(int container_index = -1, container_size = container.size(); ++container_index < container_size; )
            {
                container.set(container_index, lesson_id[container.getInt(container_index)]);
            }

            /*
            * Arrange lesson id according container arrangement
            * */
            for(int lesson_id_temp : container)
            {
                lesson_id[++lookup_start] = lesson_id_temp;
            }

            /*
             * Begin to exchange and shift position
             * */
            final int need_value     = lesson_id[need_index];
            final int overflow_value = lesson_id[overflow_index];

            System.arraycopy(lesson_id, overflow_index + 1, lesson_id, overflow_index, Math.abs(lesson_counter - overflow_index));

            lesson_id[need_index] = lesson_id[lesson_counter];
            lesson_id[lesson_counter - 1] = need_value;
            lesson_id[lesson_counter] = overflow_value;
            return true;
        }
        else
        {
            /*
            * Fail to arrange lesson id
            * */
            return false;
        }
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
        try
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
                int c_lesson = -1;
                int id_lesson;
                try
                {
                    id_lesson = lesson_set[++c_lesson];
                }
                catch(ArrayIndexOutOfBoundsException ignored)
                {
                    continue;
                }

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
                    day:
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
                                        * Try to choose next lesson
                                        * Get lesson id of shuffled lesson set in current lesson counter
                                        * */
                                        id_lesson = lesson_set[++c_lesson];
                                    }
                                    catch(ArrayIndexOutOfBoundsException ignored)
                                    {
                                        /*
                                        * There are no lesson left so change another lesson group
                                        * */
                                        break classroom;
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
                                        * Try to expand observed capacity
                                        * increment appender_manager {observed lesson_time (time_length)} with pre incremented current lesson_time (time_length)
                                        * */
                                        appender_manager[1] += clustered_time[++appender_manager[0]];
                                    }
                                    catch(ArrayIndexOutOfBoundsException ignored)
                                    {
                                        try
                                        {
                                            /*
                                            * Try to choose next lesson
                                            * Get lesson id of shuffled lesson set in current lesson counter
                                            * */
                                            id_lesson = lesson_set[++c_lesson];
                                            /*
                                            * Current day has reaches maximum capacity so continue another day
                                            * */
                                            continue day;
                                        }
                                        catch(ArrayIndexOutOfBoundsException ignored1)
                                        {
                                            /*
                                            * There are no lesson left so change another lesson group
                                            * */
                                            break classroom;
                                        }
                                    }

                                    try
                                    {
                                        /*
                                        * Try to choose next lesson
                                        * Get lesson id of shuffled lesson set in current lesson counter
                                        * */
                                        id_lesson = lesson_set[++c_lesson];
                                    }
                                    catch(ArrayIndexOutOfBoundsException ignored)
                                    {
                                        /*
                                        * There are no lesson left so change another lesson group
                                        * */
                                        break classroom;
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

                                    if(remaining_time > 0)
                                    {
                                        /*
                                        * check if there are lesson available with time_length equal remaining lesson_time
                                        * */
                                        boolean fulfill = false;
                                        for(int c_remaining = remaining_time; c_remaining != 0; --c_remaining)
                                        {
                                            /*
                                            * Check whether lesson with remaining time remains
                                            * */
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
                                                    * Try to expand observed capacity
                                                    * increment appender_manager {observed lesson_time (time_length)} with pre incremented current lesson_time (time_length)
                                                    * */
                                                    appender_manager[1] += clustered_time[++appender_manager[0]];
                                                }
                                                catch(ArrayIndexOutOfBoundsException ignored)
                                                {
                                                    try
                                                    {
                                                        /*
                                                        * Try to choose next lesson
                                                        * Get lesson id of shuffled lesson set in current lesson counter
                                                        * */
                                                        id_lesson = lesson_set[++c_lesson];

                                                        /*
                                                        * Current day has reaches maximum capacity so continue another day
                                                        * */
                                                        continue day;
                                                    }
                                                    catch(ArrayIndexOutOfBoundsException ignored1)
                                                    {
                                                        /*
                                                        * There are no lesson left so change another lesson group
                                                        * */
                                                        break classroom;
                                                    }
                                                }

                                                try
                                                {
                                                    /*
                                                    * Try to choose next lesson
                                                    * Get lesson id of shuffled lesson set in current lesson counter
                                                    * */
                                                    id_lesson = lesson_set[++c_lesson];
                                                }
                                                catch(ArrayIndexOutOfBoundsException ignored)
                                                {
                                                    /*
                                                    * There are no lesson left so change another lesson group
                                                    * */
                                                    break classroom;
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
                                                * Try to expand observed capacity
                                                * increment appender_manager {observed lesson_time (time_length)} with pre incremented current lesson_time (time_length)
                                                * */
                                                appender_manager[1] += clustered_time[++appender_manager[0]];
                                            }
                                            catch(ArrayIndexOutOfBoundsException ignored)
                                            {
                                                /*
                                                * Current day has reaches maximum capacity so continue another day
                                                * */
                                                continue day;
                                            }
                                        }
                                    }
                                    else
                                    {
                                        try
                                        {
                                            /*
                                            * Try to expand observed capacity
                                            * increment appender_manager {observed lesson_time (time_length)} with pre incremented current lesson_time (time_length)
                                            * */
                                            appender_manager[1] += clustered_time[++appender_manager[0]];
                                        }
                                        catch(ArrayIndexOutOfBoundsException ignored)
                                        {
                                            /*
                                            * Current day has reaches maximum capacity so continue another day
                                            * */
                                            continue day;
                                        }
                                    }
                                }
                            }
                            else
                            {
                                /*
                                * Current day has reaches maximum capacity so continue another day
                                * */
                                continue day;
                            }
                        }
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

            System.arraycopy(full_schedule.toArray(), 0, data[c_cluster].getPosition(), 0, full_schedule.size());
        }
        catch(Exception ignored)
        {
            this.random(properties, data, c_cluster);
        }
    }

    public Particle getParticle(final int index)
    {
        return this.particles[index];
    }

    public void random(@NotNull final Particle particle)
    {
        this.random(particle.getVelocityProperty().getPRandProperty(), particle.getData().getPositions());
    }

    public double getFitness()
    {
        return this.gBest.getFitness();
    }
}
