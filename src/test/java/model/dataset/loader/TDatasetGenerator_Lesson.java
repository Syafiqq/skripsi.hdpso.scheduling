package model.dataset.loader;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import java.util.Arrays;
import java.util.stream.IntStream;
import model.database.component.DBTimetable;
import model.database.loader.DBProblemLoader;
import model.dataset.component.DSLesson;
import model.dataset.core.Dataset;
import model.dataset.core.DatasetConverter;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.dataset.loader> created by : 
 * Name         : syafiq
 * Date / Time  : 05 November 2016, 5:38 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("FieldCanBeLocal") public class TDatasetGenerator_Lesson
{
    private DBTimetable      school;
    private DBProblemLoader  dbLoader;
    private DatasetGenerator dsLoader;

    @SuppressWarnings("Duplicates") @Before public void initialize()
    {
        this.school = new DBTimetable(1, "Program Teknologi Informasi dan Ilmu Komputer Universitas Brawijaya", "PTIIK UB", "Jl. Veteran No.8, Ketawanggede, Kec. Lowokwaru, Kota Malang, Jawa Timur", "2013 - 2014", 0, 17, 5);
        Assert.assertNotNull(this.school);

        this.dbLoader = new DBProblemLoader(school);
        Assert.assertNotNull(this.dbLoader);

        this.dbLoader.loadData();

        this.dsLoader = new DatasetGenerator(this.dbLoader);
        Assert.assertNotNull(this.dsLoader);
    }

    @Test public void classTest_A_001()
    {
        final @NotNull Dataset dataset = this.dsLoader.getDataset();
        System.out.printf("[%3d] %s\n", dataset.getLessons().length, Arrays.toString(Arrays.stream(dataset.getLessons()).mapToInt(value ->
        {
            try
            {
                return value.getLessonParent();
            }
            catch(NullPointerException ignored)
            {
                return -1;
            }
        }).toArray()));
    }

    @Test public void classTest_B_001()
    {
        final @NotNull DatasetConverter encoder = this.dsLoader.getEncoder();
        System.out.println(encoder.getLessons());
    }

    @Test public void classTest_C_001()
    {
        final @NotNull DatasetConverter decoder = this.dsLoader.getDecoder();
        System.out.println(decoder.getLessons());
    }

    @Test public void classTest_D_001()
    {
        final @NotNull Dataset dataset = this.dsLoader.getDataset();
        int                    counter = -1;
        System.out.printf("%3s\t:\t%-5s\t%-5s\t%-5s\t%-5s\t%-5s\n", "no", "subjt", "sks", "lectr", "klass", "prent");
        for(final DSLesson lesson : dataset.getLessons())
        {
            try
            {
                ++counter;
                System.out.printf("%3d\t:\t%-5d\t%-5d\t%-5d\t%-5d\t%-5d\n", counter, lesson.getSubject(), lesson.getSks(), lesson.getLecture(), lesson.getKlass(), lesson.getLessonParent());
            }
            catch(NullPointerException ignored)
            {
                System.out.printf("%3d\t:\t%-5d\t%-5d\t%-5d\t%-5d\t%-5d\n", counter, -1, -1, -1, -1, -1);
            }
        }
    }

    @Test public void classTest_E_001()
    {
        final @NotNull DatasetConverter decoder     = this.dsLoader.getDecoder();
        final @NotNull Int2IntMap       dec_lecture = decoder.getLecturers();
        final @NotNull Int2IntMap       dec_subject = decoder.getSubjects();
        final @NotNull Int2IntMap       dec_klass   = decoder.getClasses();
        final @NotNull Int2IntMap       dec_lesson  = decoder.getLessons();
        final @NotNull Dataset          dataset     = this.dsLoader.getDataset();
        int                             counter     = -1;
        System.out.printf("%3s\t:\t%-5s\t%-5s\t%-5s\t%-5s\t%-5s\n", "no", "subjt", "sks", "lectr", "klass", "prent");
        for(final DSLesson lesson : dataset.getLessons())
        {
            try
            {
                ++counter;
                System.out.printf("%3d\t:\t%-5d\t%-5d\t%-5d\t%-5d\t%-5d\n", counter, dec_subject.getOrDefault(lesson.getSubject(), null), lesson.getSks(), dec_lecture.getOrDefault(lesson.getLecture(), null), dec_klass.getOrDefault(lesson.getKlass(), null), dec_lesson.getOrDefault(lesson.getLessonParent(), null));
            }
            catch(NullPointerException ignored)
            {
                System.out.printf("%3d\t:\t%-5d\t%-5d\t%-5d\t%-5d\t%-5d\n", counter, -1, -1, -1, -1, -1);
            }
        }
    }

    @Test public void classTest_F_001()
    {
        final @NotNull DatasetConverter decoder       = this.dsLoader.getDecoder();
        final @NotNull Int2IntMap       dec_classroom = decoder.getClassrooms();
        final @NotNull Int2IntMap       dec_lesson    = decoder.getLessons();
        final @NotNull Dataset          dataset       = this.dsLoader.getDataset();
        int                             counter       = -1;
        System.out.printf("%3s\t%-5s\t%-5s\n", "no", "link", "available");
        for(final DSLesson lesson : dataset.getLessons())
        {
            ++counter;
            try
            {
                System.out.printf("%d\t%s\t%s\n",
                        counter,
                        Arrays.toString(Arrays.stream(lesson.getLessonLink()).map(dec_lesson::get).toArray()),
                        Arrays.toString(Arrays.stream(lesson.getAvailableClassroom()).map(dec_classroom::get).toArray()));
            }
            catch(NullPointerException ignored)
            {
                System.out.printf("%d\t%s\n", counter, null);
            }
        }
    }

    @Test public void classTest_G_001()
    {
        final @NotNull DatasetConverter encoder       = this.dsLoader.getEncoder();
        final @NotNull Int2IntMap       enc_classroom = encoder.getClassrooms();
        final @NotNull Dataset          dataset       = this.dsLoader.getDataset();
        int                             counter       = -1;
        IntStream.range(1, this.dbLoader.getClassrooms().size() + 1).forEach(value -> System.out.printf("%3d\t", value));
        System.out.println();
        IntStream.range(1, this.dbLoader.getClassrooms().size() + 1).map(enc_classroom::get).forEach(value -> System.out.printf("%3d\t", value));
        System.out.println();
        for(final DSLesson lesson : dataset.getLessons())
        {
            try
            {
                final boolean[] allowed_classrooms = lesson.getAllowedClassroom();
                IntStream.range(1, this.dbLoader.getClassrooms().size() + 1).map(operand ->
                {
                    try
                    {
                        return allowed_classrooms[enc_classroom.getOrDefault(operand, -1)] ? 1 : 0;
                    }
                    catch(ArrayIndexOutOfBoundsException ignored)
                    {
                        return 0;
                    }
                }).forEach(value -> System.out.printf("%3d\t", value));
                System.out.println();
            }
            catch(NullPointerException ignored)
            {
                System.out.printf("%d\t%s\n", counter, null);
            }
        }
    }

    @Test
    public void test_lesson_available_and_allowed_after_update_for_comparison()
    {
        int counter = -1;
        for(final DSLesson lesson : this.dsLoader.getDataset().getLessons())
        {
            System.out.println(++counter);
            try
            {
                System.out.println(Arrays.toString(lesson.getAvailableClassroom()));
                System.out.println(Arrays.toString(lesson.getAllowedClassroom()));
                System.out.println();
            }
            catch(NullPointerException ignored)
            {
                System.out.println("null");
            }
        }
    }


    @SuppressWarnings("ConstantConditions") @Test
    public void test_lesson_001()
    {
        int nullTotal = 0;
        int nonnulTotal = 0;
        int counter = 0;
        for(final DSLesson lesson : this.dsLoader.getDataset().getLessons())
        {
            System.out.println(++counter + "  " + (lesson == null));
            if(lesson == null)
            {
                ++nullTotal;
            }
            else
            {
                nonnulTotal += lesson.getSks();
            }
        }

        System.out.println("nullTotal = " + nullTotal);
        System.out.println("nonnulTotal = " + nonnulTotal);
        System.out.println(nonnulTotal + nullTotal);
    }
}
