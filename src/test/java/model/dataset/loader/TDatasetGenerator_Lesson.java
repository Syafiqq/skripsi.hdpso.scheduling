package model.dataset.loader;

import it.unimi.dsi.fastutil.ints.Int2IntLinkedOpenHashMap;
import java.util.Arrays;
import model.database.component.DBSchool;
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
public class TDatasetGenerator_Lesson
{
    private DBSchool         school;
    private DBProblemLoader  dbLoader;
    private DatasetGenerator dsLoader;

    @SuppressWarnings("Duplicates") @Before public void initialize()
    {
        this.school = new DBSchool(1, "Program Teknologi Informasi dan Ilmu Komputer Universitas Brawijaya", "PTIIK UB", "Jl. Veteran No.8, Ketawanggede, Kec. Lowokwaru, Kota Malang, Jawa Timur", "2013 - 2014", 0, 17, 5);
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
        final @NotNull DatasetConverter         decoder     = this.dsLoader.getDecoder();
        final @NotNull Int2IntLinkedOpenHashMap dec_lecture = decoder.getLecturers();
        final @NotNull Int2IntLinkedOpenHashMap dec_subject = decoder.getSubjects();
        final @NotNull Int2IntLinkedOpenHashMap dec_klass   = decoder.getClasses();
        final @NotNull Int2IntLinkedOpenHashMap dec_lesson  = decoder.getLessons();
        final @NotNull Dataset                  dataset     = this.dsLoader.getDataset();
        int                                     counter     = -1;
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
}
