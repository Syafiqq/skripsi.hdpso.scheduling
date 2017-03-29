package model.database.component;

import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.Collections;
import model.database.component.metadata.DBMClassroom;
import org.junit.Assert;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.component> created by : 
 * Name         : syafiq
 * Date / Time  : 26 October 2016, 7:54 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class TDBLesson
{
    @Test public void test_001()
    {
        final DBTimetable school = new DBTimetable(1, "Program Teknologi Informasi dan Ilmu Komputer Universitas Brawijaya", "PTIIK UB", "Jl. Veteran No.8, Ketawanggede, Kec. Lowokwaru, Kota Malang, Jawa Timur", "2013 - 2014", 0, 3, 2);
        Assert.assertNotNull(school);

        final DBAvailability availability = new DBAvailability(1, "Available", .5f);
        Assert.assertNotNull(availability);

        final DBDay day = new DBDay(1, 1, "Senin", "Sen", school);
        Assert.assertNotNull(day);

        final DBPeriod period = new DBPeriod(1, 1, "Senin", "Sen", "07:00:00", "07:50:00", school);
        Assert.assertNotNull(period);

        final DBSubject   subject     = new DBSubject(1, "SPK", "TIF00000", school);
        final DBClass     klass       = new DBClass(1, "1-TIF-A", school);
        final DBLecture   lecture     = new DBLecture(1, "Achmad Arwan", school);
        final DBClassroom classroom_1 = new DBClassroom(1, "E2.1", school);
        final DBClassroom classroom_2 = new DBClassroom(2, "E2.2", school);

        Assert.assertNotNull(subject);
        Assert.assertNotNull(klass);
        Assert.assertNotNull(lecture);
        Assert.assertNotNull(classroom_1);
        Assert.assertNotNull(classroom_2);

        final DBLesson                lesson     = new DBLesson(1, subject, 3, 1, lecture, klass, 2);
        final ObjectList<DBMClassroom> classrooms = lesson.getClassrooms();
        Collections.addAll(classrooms, classroom_1, classroom_2);

        Assert.assertNotNull(lesson);

        System.out.println(lesson);
    }

    @Test public void test_002()
    {
        final DBTimetable school = new DBTimetable(1, "Program Teknologi Informasi dan Ilmu Komputer Universitas Brawijaya", "PTIIK UB", "Jl. Veteran No.8, Ketawanggede, Kec. Lowokwaru, Kota Malang, Jawa Timur", "2013 - 2014", 0, 3, 2);
        Assert.assertNotNull(school);

        final DBAvailability availability = new DBAvailability(1, "Available", .5f);
        Assert.assertNotNull(availability);

        final DBDay day = new DBDay(1, 1, "Senin", "Sen", school);
        Assert.assertNotNull(day);

        final DBPeriod period = new DBPeriod(1, 1, "Senin", "Sen", "07:00:00", "07:50:00", school);
        Assert.assertNotNull(period);

        final DBSubject   subject     = new DBSubject(1, "SPK", "TIF00000", school);
        final DBClass     klass       = new DBClass(1, "1-TIF-A", school);
        final DBClassroom classroom_1 = new DBClassroom(1, "E2.1", school);
        final DBClassroom classroom_2 = new DBClassroom(2, "E2.2", school);

        Assert.assertNotNull(subject);
        Assert.assertNotNull(klass);
        Assert.assertNotNull(classroom_1);
        Assert.assertNotNull(classroom_2);

        final DBLesson                lesson     = new DBLesson(1, subject, 3, 1, null, klass, 2);
        final ObjectList<DBMClassroom> classrooms = lesson.getClassrooms();
        Collections.addAll(classrooms, classroom_1, classroom_2);

        Assert.assertNotNull(lesson);

        System.out.println(lesson);
    }
}
