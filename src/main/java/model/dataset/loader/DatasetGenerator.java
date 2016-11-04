package model.dataset.loader;

import it.unimi.dsi.fastutil.ints.Int2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntArrays;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import model.database.component.DBClass;
import model.database.component.DBClassroom;
import model.database.component.DBDay;
import model.database.component.DBLecture;
import model.database.component.DBLesson;
import model.database.component.DBPeriod;
import model.database.component.DBSubject;
import model.database.loader.DBProblemLoader;
import model.dataset.component.DSLesson;
import model.dataset.component.DSTimeOff;
import model.dataset.core.Dataset;
import model.dataset.core.DatasetConverter;
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
        this.generateActiveDays(loader.getDays().values());
        this.generateActivePeriods(loader.getPeriods().values());
        this.generateClasses(loader.getClasses(), loader.getOperatingClass().iterator());
        this.generateClassroom(loader.getClassrooms(), loader.getOperatingClassroom().iterator());
        this.generateLecture(loader.getLecturers(), loader.getOperatingLecture().iterator());
        this.generateSubject(loader.getSubjects(), loader.getOperatingSubject().iterator());
        this.generateLesson(loader.getLessons().values());
    }

    private void generateActiveDays(final ObjectCollection<DBDay> db_days)
    {
        final @NotNull int[]                    days    = this.dataset.getDays();
        final @NotNull Int2IntLinkedOpenHashMap encoder = this.encoder.getActiveDays();
        final @NotNull Int2IntLinkedOpenHashMap decoder = this.decoder.getActiveDays();

        int counter = -1;
        for(final DBDay day : db_days)
        {
            final int db_day_id = day.getId();
            days[++counter] = counter;
            encoder.put(db_day_id, counter);
            decoder.put(counter, db_day_id);
        }
    }

    private void generateActivePeriods(ObjectCollection<DBPeriod> db_periods)
    {
        final @NotNull int[]                    periods = this.dataset.getPeriods();
        final @NotNull Int2IntLinkedOpenHashMap encoder = this.encoder.getActivePeriods();
        final @NotNull Int2IntLinkedOpenHashMap decoder = this.decoder.getActivePeriods();

        int counter = -1;
        for(final DBPeriod db_period : db_periods)
        {
            final int db_period_id = db_period.getId();
            periods[++counter] = counter;
            encoder.put(db_period_id, counter);
            decoder.put(counter, db_period_id);
        }
    }

    private void generateClasses(final @NotNull Int2ObjectLinkedOpenHashMap<DBClass> db_classes, final @NotNull IntListIterator operating_classes)
    {
        final @NotNull DSTimeOff[]              classes = this.dataset.getClasses();
        final @NotNull Int2IntLinkedOpenHashMap encoder = this.encoder.getClasses();
        final @NotNull Int2IntLinkedOpenHashMap decoder = this.decoder.getClasses();

        for(int counter = 0; operating_classes.hasNext(); ++counter)
        {
            final DBClass db_klass    = db_classes.get(operating_classes.next().intValue());
            final int     db_klass_id = db_klass.getId();
            classes[counter] = DSTimeOff.newInstance(db_klass.getTimeoff());
            encoder.put(db_klass_id, counter);
            decoder.put(counter, db_klass_id);
        }
    }

    private void generateClassroom(final @NotNull Int2ObjectLinkedOpenHashMap<DBClassroom> db_classrooms, final @NotNull IntListIterator operating_classrooms)
    {
        final @NotNull DSTimeOff[]              classrooms = this.dataset.getClassrooms();
        final @NotNull Int2IntLinkedOpenHashMap encoder    = this.encoder.getClassrooms();
        final @NotNull Int2IntLinkedOpenHashMap decoder    = this.decoder.getClassrooms();

        for(int counter = 0; operating_classrooms.hasNext(); ++counter)
        {
            final DBClassroom db_classroom    = db_classrooms.get(operating_classrooms.next().intValue());
            final int         db_classroom_id = db_classroom.getId();
            classrooms[counter] = DSTimeOff.newInstance(db_classroom.getTimeoff());
            encoder.put(db_classroom_id, counter);
            decoder.put(counter, db_classroom_id);
        }
    }

    private void generateLecture(Int2ObjectLinkedOpenHashMap<DBLecture> db_lecturers, IntListIterator operating_lecturers)
    {
        final @NotNull DSTimeOff[]              lecturers = this.dataset.getLecturers();
        final @NotNull Int2IntLinkedOpenHashMap encoder   = this.encoder.getLecturers();
        final @NotNull Int2IntLinkedOpenHashMap decoder   = this.decoder.getLecturers();

        for(int counter = 0; operating_lecturers.hasNext(); ++counter)
        {
            final DBLecture db_lecture    = db_lecturers.get(operating_lecturers.next().intValue());
            final int       db_lecture_id = db_lecture.getId();
            lecturers[counter] = DSTimeOff.newInstance(db_lecture.getTimeoff());
            encoder.put(db_lecture_id, counter);
            decoder.put(counter, db_lecture_id);
        }
    }

    private void generateSubject(Int2ObjectLinkedOpenHashMap<DBSubject> db_subjects, IntListIterator operating_subjects)
    {
        final @NotNull DSTimeOff[]              subjects = this.dataset.getSubjects();
        final @NotNull Int2IntLinkedOpenHashMap encoder  = this.encoder.getSubjects();
        final @NotNull Int2IntLinkedOpenHashMap decoder  = this.decoder.getSubjects();

        for(int counter = 0; operating_subjects.hasNext(); ++counter)
        {
            final DBSubject db_subject    = db_subjects.get(operating_subjects.next().intValue());
            final int       db_subject_id = db_subject.getId();
            subjects[counter] = DSTimeOff.newInstance(db_subject.getTimeoff());
            encoder.put(db_subject_id, counter);
            decoder.put(counter, db_subject_id);
        }
    }

    private void generateLesson(ObjectCollection<DBLesson> db_lessons)
    {
        final @NotNull DSLesson[]               lessons = this.dataset.getLessons();
        final @NotNull Int2IntLinkedOpenHashMap encoder = this.encoder.getLessons();
        final @NotNull Int2IntLinkedOpenHashMap decoder = this.decoder.getLessons();

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

            lessons[counter] = DSLesson.newInstance(this.encoder, lesson, IntArrays.copy(links));
            encoder.put(db_lesson_id, counter);
            decoder.put(counter, db_lesson_id);

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
