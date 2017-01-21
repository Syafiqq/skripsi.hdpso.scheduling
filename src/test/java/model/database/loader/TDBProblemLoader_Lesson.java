package model.database.loader;

import model.database.component.DBClassroom;
import model.database.component.DBLesson;
import model.database.component.DBSchool;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.loader> created by : 
 * Name         : syafiq
 * Date / Time  : 26 October 2016, 8:04 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class TDBProblemLoader_Lesson {
    private DBSchool school;
    private DBProblemLoader loader;

    @SuppressWarnings("Duplicates")
    @Before
    public void initialize() {
        this.school = new DBSchool(1, "Program Teknologi Informasi dan Ilmu Komputer Universitas Brawijaya", "PTIIK UB", "Jl. Veteran No.8, Ketawanggede, Kec. Lowokwaru, Kota Malang, Jawa Timur", "2013 - 2014", 0, 17, 5);
        Assert.assertNotNull(this.school);

        this.loader = new DBProblemLoader(school);
        Assert.assertNotNull(this.loader);

        this.loader.loadData();
    }

    @Test
    public void lectureTest_001() {
        for (final DBLesson db_lesson : this.loader.getLessons().values()) {
            Assert.assertNotNull(db_lesson);

            System.out.println(db_lesson);
        }
    }

    @Test
    public void testDetail() {
/*        final Int2ObjectMap<DBSubject> subjects = this.loader.getSubjects();
        final Int2ObjectMap<DBLecture> lecturers = this.loader.getLecturers();
        final Int2ObjectMap<DBClass> classes = this.loader.getClasses();
        final Int2ObjectMap<DBClassroom> classrooms = this.loader.getClassrooms();*/
        for (final DBLesson db_lesson : this.loader.getLessons().values()) {
            int id = db_lesson.getId();
            String subject = db_lesson.getSubject().getName();
            int sks = db_lesson.getSks();
            int count = db_lesson.getCount();
            String lecture = db_lesson.getLecture() == null ? "-" : db_lesson.getLecture().getName();
            String klass = db_lesson.getKlass().getName();
            String classroom = Arrays.toString(db_lesson.getClassrooms().stream().map(DBClassroom::getName).toArray());
            System.out.printf("%d\t%s\t%d\t%d\t%s\t%s\t%s\n", id, subject, sks, count, lecture, klass, classroom.replace('[', '\0').replace(']', '\0').trim());

        }
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void lectureTestTimeOff_001() {
        for (final DBLesson db_lesson : this.loader.getLessons().values()) {
            Assert.assertNotNull(db_lesson);

            System.out.print(db_lesson.getId() + " : ");
            System.out.println(Arrays.toString(db_lesson.getClassrooms().stream().map(DBClassroom::getId).toArray()));
        }
    }
}
