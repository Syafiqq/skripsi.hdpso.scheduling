package model.database.loader;

import it.unimi.dsi.fastutil.ints.Int2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.database.component.DBAvailability;
import model.database.component.DBClass;
import model.database.component.DBClassroom;
import model.database.component.DBDay;
import model.database.component.DBLecture;
import model.database.component.DBLesson;
import model.database.component.DBLessonCluster;
import model.database.component.DBLessonGroup;
import model.database.component.DBPeriod;
import model.database.component.DBSchool;
import model.database.component.DBSubject;
import model.database.component.DBTimeOff;
import model.database.core.DBComponent;
import org.apache.commons.math3.util.FastMath;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.loader> created by : 
 * Name         : syafiq
 * Date / Time  : 24 October 2016, 8:29 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings({"WeakerAccess", "unused"}) public class DBProblemLoader extends DBComponent
{
    private DBSchool                      school;
    private Int2ObjectMap<DBDay>          days;
    private Int2ObjectMap<DBPeriod>       periods;
    private Int2ObjectMap<DBAvailability> availabilities;
    private Int2ObjectMap<DBClass>        classes;
    private Int2ObjectMap<DBClassroom>    classrooms;
    private Int2ObjectMap<DBLecture>      lecturers;
    private Int2ObjectMap<DBSubject>      subjects;
    private Int2ObjectMap<DBLesson>       lessons;
    private Int2IntMap                    time_distribution;
    private ObjectList<DBLessonGroup>     lesson_group;
    private ObjectList<DBLessonCluster>   lesson_cluster;
    private IntList                       operating_lecture;
    private IntList                       operating_subject;
    private IntList                       operating_classroom;
    private IntList                       operating_class;
    private int                           complex_lesson_size;
    private int                           total_registered_time;
    private int                           operating_classroom_allowed_lesson;

    public DBProblemLoader(@NotNull final DBSchool school)
    {
        this.school = school;
    }

    @Override public void activateDatabase()
    {
        final URL db = ClassLoader.getSystemClassLoader().getResource("db/db.mcrypt");
        if(db != null)
        {
            final String dbUrl;
            try
            {
                dbUrl = java.net.URLDecoder.decode("jdbc:sqlite:" + db.getPath(), "UTF-8");
            }
            catch(UnsupportedEncodingException ignored)
            {
                System.err.println("Error Parse DB");
                System.exit(-1);
                return;
            }
            try
            {
                super.connection = DriverManager.getConnection(dbUrl);
            }
            catch(SQLException ignored)
            {
                System.err.println("Error Activating Database");
                System.exit(-1);
            }
        }
        else
        {
            System.err.println("Error Finding Database");
            System.exit(-1);
        }
    }

    public void loadData()
    {
        final Runtime runtime = Runtime.getRuntime();
        runtime.runFinalization();
        runtime.gc();
        this.activateDatabase();
        this.generateSchoolCoreData();
        this.generateSchoolDependency();
        this.generateSchoolExtraInformation();
        this.deActivateDatabase();
        runtime.runFinalization();
        runtime.gc();
    }

    private void generateSchoolCoreData()
    {
        this.generateActiveDays();
        this.generateActivePeriods();
        this.generateAvailabilities();
    }

    private void generateActiveDays()
    {
        try
        {
            final int school = this.school.getId();

            /*
             * Get Active Days Size
             * */
            String query = "SELECT COUNT(`id`) AS 'count' FROM `active_day` WHERE `school` = ?";
            super.statement = super.connection.prepareStatement(query);
            super.statement.setInt(1, school);
            super.result_set = super.statement.executeQuery();
            super.result_set.next();
            final int size = super.result_set.getInt("count");

            this.days = new Int2ObjectLinkedOpenHashMap<>(size);
            final Int2ObjectMap<DBDay> days = this.days;

            /*
             * Get Active Days Data
             * */
            query = "SELECT `id`, `position`, `name`, `nick` FROM `active_day` WHERE `school` = ? ORDER BY `position` ASC";
            super.statement = super.connection.prepareStatement(query);
            super.statement.setInt(1, school);
            super.result_set = super.statement.executeQuery();
            final ResultSet result_set = super.result_set;

            while(result_set.next())
            {
                days.put(
                        result_set.getInt("id")
                        , new DBDay(result_set.getInt("id")
                                , result_set.getInt("position")
                                , result_set.getString("name")
                                , result_set.getString("nick")
                                , this.school));
            }
        }
        catch(SQLException ignored)
        {
            System.err.println("Generate Active Day");
            System.exit(-1);
        }
    }

    private void generateActivePeriods()
    {
        try
        {
            final int school = this.school.getId();

            /*
             * Get Active Periods Size
             * */
            String query = "SELECT COUNT(`id`) AS 'count' FROM `active_period` WHERE `school` = ?";
            super.statement = super.connection.prepareStatement(query);
            super.statement.setInt(1, school);
            super.result_set = super.statement.executeQuery();
            super.result_set.next();
            final int size = super.result_set.getInt("count");

            this.periods = new Int2ObjectLinkedOpenHashMap<>(size);
            Int2ObjectMap<DBPeriod> periods = this.periods;

            /*
             * Get Active Periods Data
             * */
            query = "SELECT `id`, `position`, `name`, `nick`, `start`, `end` FROM `active_period` WHERE `school` = ? ORDER BY `position` ASC";
            super.statement = super.connection.prepareStatement(query);
            super.statement.setInt(1, school);
            super.result_set = super.statement.executeQuery();
            final ResultSet result_set = super.result_set;

            while(result_set.next())
            {
                periods.put(
                        result_set.getInt("id")
                        , new DBPeriod(result_set.getInt("id")
                                , result_set.getInt("position")
                                , result_set.getString("name")
                                , result_set.getString("nick")
                                , result_set.getString("start")
                                , result_set.getString("end")
                                , this.school));
            }
        }
        catch(SQLException ignored)
        {
            System.err.println("Generate Active Period");
            System.exit(-1);
        }
    }

    private void generateAvailabilities()
    {
        try
        {
            /*
             * Get Active Periods Size
             * */
            String query = "SELECT COUNT(`id`) AS 'count' FROM `availability`";
            super.statement = super.connection.prepareStatement(query);
            super.result_set = super.statement.executeQuery();
            super.result_set.next();
            final int size = super.result_set.getInt("count");

            this.availabilities = new Int2ObjectLinkedOpenHashMap<>(size);
            final Int2ObjectMap<DBAvailability> availabilities = this.availabilities;

            /*
             * Get Active Periods Data
             * */
            query = "SELECT `id`, `name`, `value` FROM `availability` ORDER BY `id`";
            super.statement = super.connection.prepareStatement(query);
            super.result_set = super.statement.executeQuery();
            final ResultSet result_set = super.result_set;

            while(result_set.next())
            {
                availabilities.put(
                        result_set.getInt("id")
                        , new DBAvailability(
                                result_set.getInt("id")
                                , result_set.getString("name")
                                , result_set.getDouble("value")));
            }
        }
        catch(SQLException ignored)
        {
            System.err.println("Generate Availability");
            System.exit(-1);
        }
    }

    private void generateSchoolDependency()
    {
        this.generateClasses();
        this.generateClassroom();
        this.generateLecturers();
        this.generateSubjects();
        this.generateLessons();
    }

    @SuppressWarnings({"Duplicates", "unchecked"}) private void generateClasses()
    {
        try
        {
            final int school = this.school.getId();

            /*
             * Get Active Classes Size
             * */
            /* Query for class size in the specific school id*/
            String query = "SELECT COUNT(`id`) AS 'count' FROM `class` WHERE `school` = ?";
            super.statement = super.connection.prepareStatement(query);
            super.statement.setInt(1, school);
            super.result_set = super.statement.executeQuery();
            super.result_set.next();
            final int size = super.result_set.getInt("count");

            this.classes = new Int2ObjectLinkedOpenHashMap<>(size);
            final Int2ObjectMap<DBClass> classes = this.classes;

            /*
             * Get Active Classes Data
             * */
            /* Query for all class in the specific school id*/
            query = "SELECT `id`, `name` FROM `class` WHERE `school` = ? ORDER BY `id` ASC";
            super.statement = super.connection.prepareStatement(query);
            super.statement.setInt(1, school);
            super.result_set = super.statement.executeQuery();
            ResultSet result_set = super.result_set;

            while(result_set.next())
            {
                classes.put(
                        result_set.getInt("id")
                        , new DBClass(
                                result_set.getInt("id")
                                , result_set.getString("name")
                                , this.school));
            }

            /*
             * Get Active Class Time-Off each Classes
             * */
            /* Query for timeoff of all class in the specific school id*/
            query = "SELECT `class_timeoff`.`id`, `class_timeoff`.`class`, `class_timeoff`.`day`, `class_timeoff`.`period`, `class_timeoff`.`availability` FROM `class_timeoff` LEFT OUTER JOIN `class` ON `class_timeoff`.`class` = `class`.`id` LEFT OUTER JOIN `active_day` ON `class_timeoff`.`day` = `active_day`.id LEFT OUTER JOIN `active_period` ON `class_timeoff`.`period` = `active_period`.id WHERE `class`.`school` = ? ORDER BY `class`.`id`, `active_day`.`position`, `active_period`.`position` ASC;";
            super.statement = super.connection.prepareStatement(query);
            super.statement.setInt(1, school);
            super.result_set = super.statement.executeQuery();
            result_set = super.result_set;

            int                                       class_index     = -1;
            int                                       day_index       = -1;
            int                                       period_index    = -1;
            ObjectListIterator<ObjectList<DBTimeOff>> class_db        = null;
            ObjectList<DBTimeOff>                     current_timeOff = null;
            while(result_set.next())
            {
                if(class_index != result_set.getInt("class"))
                {
                    class_index = result_set.getInt("class");
                    class_db = classes.get(class_index).getTimeoff().getAvailabilities().listIterator();
                }

                if(day_index != result_set.getInt("day"))
                {
                    day_index = result_set.getInt("day");
                    period_index = -1;
                    assert class_db != null;

                    current_timeOff = class_db.next();
                }

                assert current_timeOff != null;
                current_timeOff.set(++period_index, new DBTimeOff(result_set.getInt("id"), this.days.get(result_set.getInt("day")), this.periods.get(result_set.getInt("period")), this.availabilities.get(result_set.getInt("availability"))));
            }
        }
        catch(SQLException ignored)
        {
            System.err.println("Generate Class");
            System.exit(-1);
        }
    }

    @SuppressWarnings({"Duplicates", "unchecked"}) private void generateClassroom()
    {
        try
        {
            final int school = this.school.getId();

            /*
             * Get Active Classroom Size
             * */
            /* Query for classroom size in the specific school id*/
            String query = "SELECT COUNT(`id`) AS 'count' FROM `classroom` WHERE `school` = ?";
            super.statement = super.connection.prepareStatement(query);
            super.statement.setInt(1, school);
            super.result_set = super.statement.executeQuery();
            super.result_set.next();
            final int size = super.result_set.getInt("count");

            this.classrooms = new Int2ObjectLinkedOpenHashMap<>(size);
            final Int2ObjectMap<DBClassroom> classrooms = this.classrooms;

            /*
             * Get Active Classroom Data
             * */
            /* Query for all classroom in the specific school id*/
            query = "SELECT `id`, `name` FROM `classroom` WHERE `school` = ? ORDER BY `id` ASC;";
            super.statement = super.connection.prepareStatement(query);
            super.statement.setInt(1, school);
            super.result_set = super.statement.executeQuery();
            ResultSet result_set = super.result_set;

            while(result_set.next())
            {
                classrooms.put(
                        result_set.getInt("id")
                        , new DBClassroom(
                                result_set.getInt("id")
                                , result_set.getString("name")
                                , this.school));
            }

            /*
             * Get Active Classroom Time-Off each Classes
             * */
            /* Query for timeoff of all classroom in the specific school id*/
            query = "SELECT `classroom_timeoff`.`id`, `classroom_timeoff`.`classroom`, `classroom_timeoff`.`day`, `classroom_timeoff`.`period`, `classroom_timeoff`.`availability` FROM `classroom_timeoff` LEFT OUTER JOIN `classroom` ON `classroom_timeoff`.`classroom` = `classroom`.`id` LEFT OUTER JOIN `active_day` ON `classroom_timeoff`.`day` = `active_day`.`id` LEFT OUTER JOIN `active_period` ON `classroom_timeoff`.`period` = `active_period`.`id` WHERE `classroom`.`school` = ? ORDER BY `classroom`.`id`, `active_day`.`position`, `active_period`.`position` ASC;";
            super.statement = super.connection.prepareStatement(query);
            super.statement.setInt(1, school);
            super.result_set = super.statement.executeQuery();
            result_set = super.result_set;

            int                                       classroom_index = -1;
            int                                       day_index       = -1;
            int                                       period_index    = -1;
            ObjectListIterator<ObjectList<DBTimeOff>> classroom_db    = null;
            ObjectList<DBTimeOff>                     current_timeOff = null;
            while(result_set.next())
            {
                if(classroom_index != result_set.getInt("classroom"))
                {
                    classroom_index = result_set.getInt("classroom");
                    classroom_db = classrooms.get(classroom_index).getTimeoff().getAvailabilities().listIterator();
                }

                if(day_index != result_set.getInt("day"))
                {
                    day_index = result_set.getInt("day");
                    period_index = -1;
                    assert classroom_db != null;

                    current_timeOff = classroom_db.next();
                }

                assert current_timeOff != null;
                current_timeOff.set(++period_index, new DBTimeOff(result_set.getInt("id"), this.days.get(result_set.getInt("day")), this.periods.get(result_set.getInt("period")), this.availabilities.get(result_set.getInt("availability"))));
            }
        }
        catch(SQLException ignored)
        {
            System.err.println("Generate Classroom");
            System.exit(-1);
        }
    }

    @SuppressWarnings({"Duplicates", "unchecked"}) private void generateLecturers()
    {
        try
        {
            final int school = this.school.getId();

            /*
             * Get Active Lecturer Size
             * */
            /* Query for lecturer size in the specific school id*/
            String query = "SELECT COUNT(`id`) AS 'count' FROM `lecture` WHERE `school` = ?";
            super.statement = super.connection.prepareStatement(query);
            super.statement.setInt(1, school);
            super.result_set = super.statement.executeQuery();
            super.result_set.next();
            final int size = super.result_set.getInt("count");

            this.lecturers = new Int2ObjectLinkedOpenHashMap<>(size);
            final Int2ObjectMap<DBLecture> lecturers = this.lecturers;

            /*
             * Get Active Lecturers Data
             * */
            /* Query for all lecturers in the specific school id*/
            query = "SELECT `id`, `name` FROM `lecture` WHERE `school` = ? ORDER BY `id` ASC;";
            super.statement = super.connection.prepareStatement(query);
            super.statement.setInt(1, school);
            super.result_set = super.statement.executeQuery();
            ResultSet result_set = super.result_set;

            while(result_set.next())
            {
                lecturers.put(
                        result_set.getInt("id")
                        , new DBLecture(
                                result_set.getInt("id")
                                , result_set.getString("name")
                                , this.school));
            }

            /*
             * Get Active Lecturer Time-Off each Classes
             * */
            /* Query for timeoff of all lecture in the specific school id*/
            query = "SELECT `lecture_timeoff`.`id`, `lecture_timeoff`.`lecture`, `lecture_timeoff`.`day`, `lecture_timeoff`.`period`, `lecture_timeoff`.`availability` FROM `lecture_timeoff` LEFT OUTER JOIN `lecture` ON `lecture_timeoff`.`lecture` = `lecture`.`id` LEFT OUTER JOIN `active_day` ON `lecture_timeoff`.`day` = `active_day`.`id` LEFT OUTER JOIN `active_period` ON `lecture_timeoff`.`period` = `active_period`.`id` WHERE `lecture`.`school` = ? ORDER BY `lecture`.`id`, `active_day`.`position`, `active_period`.`position` ASC;";
            super.statement = super.connection.prepareStatement(query);
            super.statement.setInt(1, school);
            super.result_set = super.statement.executeQuery();
            result_set = super.result_set;

            int                                       lecture_index   = -1;
            int                                       day_index       = -1;
            int                                       period_index    = -1;
            ObjectListIterator<ObjectList<DBTimeOff>> lecture_db      = null;
            ObjectList<DBTimeOff>                     current_timeOff = null;
            while(result_set.next())
            {
                if(lecture_index != result_set.getInt("lecture"))
                {
                    lecture_index = result_set.getInt("lecture");
                    lecture_db = lecturers.get(lecture_index).getTimeoff().getAvailabilities().listIterator();
                }

                if(day_index != result_set.getInt("day"))
                {
                    day_index = result_set.getInt("day");
                    period_index = -1;
                    assert lecture_db != null;

                    current_timeOff = lecture_db.next();
                }

                assert current_timeOff != null;
                current_timeOff.set(++period_index, new DBTimeOff(result_set.getInt("id"), this.days.get(result_set.getInt("day")), this.periods.get(result_set.getInt("period")), this.availabilities.get(result_set.getInt("availability"))));
            }
        }
        catch(SQLException ignored)
        {
            System.err.println("Generate Lecture");
            System.exit(-1);
        }

    }

    @SuppressWarnings({"Duplicates", "unchecked"}) private void generateSubjects()
    {
        try
        {
            final int school = this.school.getId();

            /*
             * Get Active Subject Size
             * */
            /* Query for subject size in the specific school id*/
            String query = "SELECT COUNT(`id`) AS 'count' FROM `subject` WHERE `school` = ?;";
            super.statement = super.connection.prepareStatement(query);
            super.statement.setInt(1, school);
            super.result_set = super.statement.executeQuery();
            super.result_set.next();
            final int size = super.result_set.getInt("count");

            this.subjects = new Int2ObjectLinkedOpenHashMap<>(size);
            final Int2ObjectMap<DBSubject> subjects = this.subjects;

            /*
             * Get Active Subject Data
             * */
            /* Query for all subject in the specific school id*/
            query = "SELECT `id`, `name`, `code` FROM `subject` WHERE `school` = ? ORDER BY `id` ASC;";
            super.statement = super.connection.prepareStatement(query);
            super.statement.setInt(1, school);
            super.result_set = super.statement.executeQuery();
            ResultSet result_set = super.result_set;

            while(result_set.next())
            {
                subjects.put(
                        result_set.getInt("id")
                        , new DBSubject(
                                result_set.getInt("id")
                                , result_set.getString("name")
                                , result_set.getString("code")
                                , this.school));
            }

            /*
             * Get Active Subject Time-Off each Classes
             * */
            /* Query for timeoff of all subject in the specific school id*/
            query = "SELECT `subject_timeoff`.`id`, `subject_timeoff`.`subject`, `subject_timeoff`.`day`, `subject_timeoff`.`period`, `subject_timeoff`.`availability` FROM `subject_timeoff` LEFT OUTER JOIN `subject` ON `subject_timeoff`.`subject` = `subject`.`id` LEFT OUTER JOIN `active_day` ON `subject_timeoff`.`day` = `active_day`.`id` LEFT OUTER JOIN `active_period` ON `subject_timeoff`.`period` = `active_period`.`id` WHERE `subject`.`school` = ? ORDER BY `subject`.`id`, `active_day`.`position`, `active_period`.`position` ASC;";
            super.statement = super.connection.prepareStatement(query);
            super.statement.setInt(1, school);
            super.result_set = super.statement.executeQuery();
            result_set = super.result_set;

            int                                       subject_index   = -1;
            int                                       day_index       = -1;
            int                                       period_index    = -1;
            ObjectListIterator<ObjectList<DBTimeOff>> subject_db      = null;
            ObjectList<DBTimeOff>                     current_timeOff = null;
            while(result_set.next())
            {
                if(subject_index != result_set.getInt("subject"))
                {
                    subject_index = result_set.getInt("subject");
                    subject_db = subjects.get(subject_index).getTimeoff().getAvailabilities().listIterator();
                }

                if(day_index != result_set.getInt("day"))
                {
                    day_index = result_set.getInt("day");
                    period_index = -1;
                    assert subject_db != null;

                    current_timeOff = subject_db.next();
                }

                assert current_timeOff != null;
                current_timeOff.set(++period_index, new DBTimeOff(result_set.getInt("id"), this.days.get(result_set.getInt("day")), this.periods.get(result_set.getInt("period")), this.availabilities.get(result_set.getInt("availability"))));
            }
        }
        catch(SQLException ignored)
        {
            System.err.println("Generate Subject");
            System.exit(-1);
        }

    }

    private void generateLessons()
    {
        try
        {
            final int school = this.school.getId();

            /* Query for all lessons in the specific school id*/
            String query = "SELECT COUNT(`lesson`.`id`)  AS 'count', SUM(`lesson`.`count`) AS 'extra', SUM(`lesson`.`count` * `lesson`.`sks`) AS 'registered' FROM `lesson` LEFT OUTER JOIN `subject` ON `lesson`.`subject` = `subject`.`id` WHERE `subject`.`school` = ?; ";
            super.statement = super.connection.prepareStatement(query);
            super.statement.setInt(1, school);
            super.result_set = super.statement.executeQuery();
            super.result_set.next();
            final int size = super.result_set.getInt("count");
            this.complex_lesson_size = super.result_set.getInt("extra");
            this.total_registered_time = super.result_set.getInt("registered");

            this.lessons = new Int2ObjectLinkedOpenHashMap<>(size);
            final Int2ObjectMap<DBLesson> lessons = this.lessons;

            /* Query for all lessons in the specific school id*/
            query = "SELECT `lesson`.`id`, `lesson`.`subject`, `lesson`.`lecture`, `lesson`.`sks`, `lesson`.`count`, `lesson`.`class`, COUNT(`lesson_available_classroom`.`id`) AS 'classroom_size'  FROM `lesson` LEFT OUTER JOIN `subject` ON `lesson`.`subject` = `subject`.`id` LEFT OUTER JOIN `lesson_available_classroom` ON `lesson`.`id` = `lesson_available_classroom`.`lesson`  WHERE `subject`.`school` = ?  GROUP BY `lesson`.`id`  ORDER BY `lesson`.`id` ASC;";
            super.statement = super.connection.prepareStatement(query);
            super.statement.setInt(1, school);
            super.result_set = super.statement.executeQuery();
            ResultSet result_set = super.result_set;

            while(result_set.next())
            {
                lessons.put(
                        result_set.getInt("id")
                        , new DBLesson(
                                result_set.getInt("id")
                                , this.subjects.get(result_set.getInt("subject"))
                                , result_set.getInt("sks")
                                , result_set.getInt("count")
                                , this.lecturers.get(result_set.getInt("lecture"))
                                , this.classes.get(result_set.getInt("class"))
                                , result_set.getInt("classroom_size")));
            }

            /*
             * Get Lesson available classroom
             * */
            /* Query for lesson available classroom in the specific school id*/
            query = "SELECT  `lesson`.`id`,  `lesson_available_classroom`.`classroom`  FROM `lesson_available_classroom`  LEFT OUTER JOIN `lesson` ON `lesson_available_classroom`.`lesson` = `lesson`.`id`  LEFT OUTER JOIN `subject` ON `lesson`.`subject` = `subject`.`id`  WHERE `subject`.`school` = ?  ORDER BY `lesson`.`id`, `lesson_available_classroom`.`classroom`  ASC;";
            super.statement = super.connection.prepareStatement(query);
            super.statement.setInt(1, school);
            super.result_set = super.statement.executeQuery();
            result_set = super.result_set;

            int                     lesson_index        = -1;
            ObjectList<DBClassroom> available_classroom = null;
            while(result_set.next())
            {
                if(lesson_index != result_set.getInt("id"))
                {
                    lesson_index = result_set.getInt("id");
                    available_classroom = lessons.get(lesson_index).getClassrooms();
                }

                assert available_classroom != null;
                available_classroom.add(this.classrooms.get(result_set.getInt("classroom")));
            }
        }
        catch(SQLException ignore)
        {
            System.err.println("Generate Lessons Data");
            System.exit(1);
        }
    }

    private void generateSchoolExtraInformation()
    {
        this.generateOperatingClassroom();
        this.generateOperatingClassroomAllowedLesson();
        this.generateOperatingClass();
        this.generateOperatingLecture();
        this.generateOperatingSubject();
        this.generateSchoolTimeDistribution();
        this.generateLessonGroup();
        this.generateLessonCluster();
    }

    private void generateOperatingClassroom()
    {
        try
        {
            final int school = this.school.getId();

            this.operating_classroom = new IntArrayList(this.classrooms.size());
            final IntList operating_classroom = this.operating_classroom;
            /*
             * Get Active Operational Classroom
             * */
            String query = "SELECT DISTINCT `classroom`.`id` AS `id` FROM `lesson_available_classroom`  LEFT OUTER JOIN `classroom` ON `lesson_available_classroom`.`classroom` = `classroom`.`id` WHERE `classroom`.`school` = ? ORDER BY `classroom`.`id`  ASC;";
            super.statement = super.connection.prepareStatement(query);
            super.statement.setInt(1, school);
            super.result_set = super.statement.executeQuery();
            final ResultSet result_set = super.result_set;
            while(result_set.next())
            {
                operating_classroom.add(result_set.getInt("id"));
            }
        }
        catch(SQLException ignored)
        {
            System.err.println("Generate Operating Classroom");
            System.exit(-1);
        }
    }

    private void generateOperatingClassroomAllowedLesson()
    {
        try
        {
            final int school = this.school.getId();
            /*
             * Get Active Operational Classroom
             * */
            String query = "SELECT COUNT(`classroom_timeoff`.`id`) AS 'active_classroom_operating_time' FROM `classroom_timeoff` WHERE `classroom_timeoff`.`classroom` IN ( SELECT DISTINCT `classroom`.`id` AS `id` FROM `lesson_available_classroom` LEFT OUTER JOIN `classroom` ON `lesson_available_classroom`.`classroom` = `classroom`.`id` WHERE `classroom`.`school` = ? ) AND `classroom_timeoff`.`availability` <> 1";
            super.statement = super.connection.prepareStatement(query);
            super.statement.setInt(1, school);
            super.result_set = super.statement.executeQuery();
            final ResultSet result_set = super.result_set;
            result_set.next();
            this.operating_classroom_allowed_lesson = result_set.getInt("active_classroom_operating_time");
        }
        catch(SQLException ignored)
        {
            System.err.println("Generate Operating Classroom Allowed Lesson");
            System.exit(-1);
        }
    }

    private void generateOperatingClass()
    {
        try
        {
            final int school = this.school.getId();

            this.operating_class = new IntArrayList(this.classes.size());
            final IntList operating_class = this.operating_class;
            /*
             * Get Active Operational Class
             * */
            String query = "SELECT  DISTINCT `class`.`id` AS `id` FROM `lesson`  LEFT OUTER JOIN `class` ON `lesson`.`class` = `class`.`id` WHERE `class`.`school` = ? ORDER BY `class`.`id`  ASC;";
            super.statement = super.connection.prepareStatement(query);
            super.statement.setInt(1, school);
            super.result_set = super.statement.executeQuery();
            final ResultSet result_set = super.result_set;
            while(result_set.next())
            {
                operating_class.add(result_set.getInt("id"));
            }
        }
        catch(SQLException ignored)
        {
            System.err.println("Generate Operating Class");
            System.exit(-1);
        }
    }

    private void generateOperatingLecture()
    {
        try
        {
            final int school = this.school.getId();

            this.operating_lecture = new IntArrayList(this.lecturers.size());
            final IntList operating_lecture = this.operating_lecture;
            /*
             * Get Active Operational Lecture
             * */
            String query = "SELECT DISTINCT `lecture`.`id` AS `id` FROM `lesson` LEFT OUTER JOIN `lecture` ON `lesson`.`lecture` = `lecture`.`id` WHERE `lecture`.`school` = ? ORDER BY `lecture`.`id` ASC;";
            super.statement = super.connection.prepareStatement(query);
            super.statement.setInt(1, school);
            super.result_set = super.statement.executeQuery();
            final ResultSet result_set = super.result_set;
            while(result_set.next())
            {
                operating_lecture.add(result_set.getInt("id"));
            }
        }
        catch(SQLException ignored)
        {
            System.err.println("Generate Operating Lecture");
            System.exit(-1);
        }
    }

    private void generateOperatingSubject()
    {
        try
        {
            final int school = this.school.getId();

            this.operating_subject = new IntArrayList(this.subjects.size());
            final IntList operating_subject = this.operating_subject;
            /*
             * Get Active Operational Subject
             * */
            String query = "SELECT DISTINCT `subject`.`id` AS `id` FROM `lesson` LEFT OUTER JOIN `subject` ON `lesson`.`subject` = `subject`.`id` WHERE `subject`.`school` = ? ORDER BY `subject`.`id` ASC;";
            super.statement = super.connection.prepareStatement(query);
            super.statement.setInt(1, school);
            super.result_set = super.statement.executeQuery();
            final ResultSet result_set = super.result_set;
            while(result_set.next())
            {
                operating_subject.add(result_set.getInt("id"));
            }
        }
        catch(SQLException ignored)
        {
            System.err.println("Generate Operating Subject");
            System.exit(-1);
        }
    }

    private void generateSchoolTimeDistribution()
    {
        try
        {
            final int school = this.school.getId();

            this.time_distribution = new Int2IntLinkedOpenHashMap(this.periods.size());
            final Int2IntMap time_distribution = this.time_distribution;

            /*
             * Get Time Distribution
             * */
            String query = "SELECT `lesson`.`sks`, SUM(`lesson`.`count`) AS 'total' FROM `lesson` LEFT OUTER JOIN `subject` ON `subject`.`id` = `lesson`.`subject` WHERE `subject`.`school` = ? GROUP BY `lesson`.`sks` ORDER BY `lesson`.`sks`;";
            super.statement = super.connection.prepareStatement(query);
            super.statement.setInt(1, school);
            super.result_set = super.statement.executeQuery();

            final ResultSet result_set = super.result_set;
            int             counter    = -1;
            while(result_set.next())
            {
                int sks = result_set.getInt("sks");
                while(++counter != sks)
                {
                    time_distribution.put(counter, 0);
                }
                time_distribution.put(sks, result_set.getInt("total"));
            }
        }
        catch(SQLException ignored)
        {
            System.err.println("Generate Time Distribution");
            System.exit(-1);
        }
    }

    private void generateLessonGroup()
    {
        final ObjectIterator<DBLesson>                                 lessons       = this.lessons.values().iterator();
        final Object2ObjectMap<ObjectList<DBClassroom>, DBLessonGroup> lesson_groups = new Object2ObjectLinkedOpenHashMap<>();
        final int                                                      lesson_size   = this.lessons.size();

        while(lessons.hasNext())
        {
            final DBLesson                dbLesson       = lessons.next();
            final ObjectList<DBClassroom> classroom_list = dbLesson.getClassrooms();
            if(!lesson_groups.containsKey(classroom_list))
            {
                lesson_groups.put(classroom_list, new DBLessonGroup(classroom_list, lesson_size));
            }
            lesson_groups.get(classroom_list).add(dbLesson);
        }

        List<DBLessonGroup> sorted_lesson_group = new ArrayList<>(lesson_groups.values());
        Collections.sort(sorted_lesson_group, (les_group_1, les_group_2) -> (int) FastMath.signum(les_group_1.getClassroomSize() - les_group_2.getClassroomSize()));

        this.lesson_group = new ObjectArrayList<>(sorted_lesson_group);
    }

    private void generateLessonCluster()
    {
        this.lesson_cluster = new ObjectArrayList<>(this.lesson_group.size());
        final ObjectList<DBLessonCluster> lesson_cluster = this.lesson_cluster;
        gate:
        for(final DBLessonGroup lesson_set : this.lesson_group)
        {
            for(final DBLessonCluster les_clus : lesson_cluster)
            {
                if(les_clus.isOnCluster(lesson_set))
                {
                    les_clus.add(lesson_set);
                    continue gate;
                }
            }
            lesson_cluster.add(new DBLessonCluster(lesson_set));
        }
    }

    @Override public void deActivateDatabase()
    {
        try
        {
            super.connection.close();
        }
        catch(SQLException | NullPointerException ignored)
        {
        }
        finally
        {
            super.connection = null;
        }
        try
        {
            super.statement.close();
        }
        catch(SQLException | NullPointerException ignored)
        {
        }
        finally
        {
            super.statement = null;
        }
        try
        {
            super.result_set.close();
        }
        catch(SQLException | NullPointerException ignored)
        {
        }
        finally
        {
            super.result_set = null;
        }
    }

    public DBSchool getSchool()
    {
        return this.school;
    }

    public Int2ObjectMap<DBDay> getDays()
    {
        return this.days;
    }

    public Int2ObjectMap<DBPeriod> getPeriods()
    {
        return this.periods;
    }

    public Int2ObjectMap<DBAvailability> getAvailabilities()
    {
        return this.availabilities;
    }

    public Int2ObjectMap<DBClass> getClasses()
    {
        return this.classes;
    }

    public Int2ObjectMap<DBClassroom> getClassrooms()
    {
        return this.classrooms;
    }

    public Int2ObjectMap<DBLecture> getLecturers()
    {
        return this.lecturers;
    }

    public Int2ObjectMap<DBSubject> getSubjects()
    {
        return this.subjects;
    }

    public Int2ObjectMap<DBLesson> getLessons()
    {
        return this.lessons;
    }

    public ObjectList<DBLessonGroup> getLessonGroup()
    {
        return this.lesson_group;
    }

    public ObjectList<DBLessonCluster> getLessonCluster()
    {
        return this.lesson_cluster;
    }

    public Int2IntMap getTimeDistribution()
    {
        return this.time_distribution;
    }

    public IntList getOperatingClassroom()
    {
        return this.operating_classroom;
    }

    public IntList getOperatingClass()
    {
        return this.operating_class;
    }

    public IntList getOperatingLecture()
    {
        return this.operating_lecture;
    }

    public IntList getOperatingSubject()
    {
        return this.operating_subject;
    }

    public int getComplexLessonSize()
    {
        return this.complex_lesson_size;
    }

    public int getTotalRegisteredTime()
    {
        return this.total_registered_time;
    }

    public int getOperatingClassroomAllowedLesson()
    {
        return this.operating_classroom_allowed_lesson;
    }

    public int getClassroomSize()
    {
        return this.classrooms.size();
    }

    public int getDaySize()
    {
        return this.days.size();
    }

    public DBClassroom getClassroom(final int classroom)
    {
        return this.classrooms.get(classroom);
    }
}
