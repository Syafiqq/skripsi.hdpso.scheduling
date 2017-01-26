package model.database.model;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 25 January 2017, 11:38 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import model.AbstractModel;
import model.database.component.DBLesson;
import model.database.component.metadata.DBMClass;
import model.database.component.metadata.DBMClassroom;
import model.database.component.metadata.DBMLecture;
import model.database.component.metadata.DBMLesson;
import model.database.component.metadata.DBMSchool;
import model.database.component.metadata.DBMSubject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused") public class MLesson extends AbstractModel
{
    public MLesson(@NotNull String dbPath) throws SQLException
    {
        super(dbPath);
    }

    public MLesson(@NotNull AbstractModel model)
    {
        super(model);
    }


    public static List<DBMLesson> getAllMetadataFromSchool(@NotNull final AbstractModel model, @NotNull final DBMSchool schoolMetadata, @NotNull final List<DBMSubject> subjectMetadata, @NotNull final List<DBMClass> classMetadata, @NotNull final List<DBMLecture> lectureMetadata)
    {
        @NotNull List<DBMLesson> list = new LinkedList<>();
        try
        {
            if(model.isClosed())
            {
                model.reconnect();
            }
            @NotNull final Int2ObjectMap<DBMSubject> mapSubject = new Int2ObjectLinkedOpenHashMap<>(subjectMetadata.size());
            @NotNull final Int2ObjectMap<DBMClass>   mapClass   = new Int2ObjectLinkedOpenHashMap<>(classMetadata.size());
            @NotNull final Int2ObjectMap<DBMLecture> mapLecture = new Int2ObjectLinkedOpenHashMap<>(lectureMetadata.size());
            for(@NotNull final DBMSubject _subject : subjectMetadata)
            {
                mapSubject.put(_subject.getId(), _subject);
            }
            for(@NotNull final DBMClass _class : classMetadata)
            {
                mapClass.put(_class.getId(), _class);
            }
            for(@NotNull final DBMLecture _lecture : lectureMetadata)
            {
                mapLecture.put(_lecture.getId(), _lecture);
            }

            @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `lesson`.`id`, `lesson`.`subject`, `lesson`.`sks`, `lesson`.`count`, `lesson`.`lecture`, `lesson`.`class` FROM `lesson` LEFT OUTER JOIN `subject` ON `lesson`.`subject` = `subject`.`id` WHERE `subject`.`school` = ?  ORDER BY `lesson`.`id` ASC");
            statement.setInt(1, schoolMetadata.getId());
            @NotNull final ResultSet result = statement.executeQuery();
            while(result.next())
            {
                list.add(new DBMLesson(
                        result.getInt("id"),
                        mapSubject.getOrDefault(result.getInt("subject"), null),
                        result.getInt("sks"),
                        result.getInt("count"),
                        mapLecture.getOrDefault(result.getInt("lecture"), null),
                        mapClass.getOrDefault(result.getInt("class"), null)
                ));
            }
            result.close();
            statement.close();
            model.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return list;
    }

    public static List<DBMLesson> getAllMetadataFromSubject(@NotNull final AbstractModel model, @NotNull final DBMSubject subjectMetadata, @NotNull final List<DBMClass> classMetadata, @NotNull final List<DBMLecture> lectureMetadata)
    {
        @NotNull List<DBMLesson> list = new LinkedList<>();
        try
        {
            if(model.isClosed())
            {
                model.reconnect();
            }
            @NotNull final Int2ObjectMap<DBMClass>   mapClass   = new Int2ObjectLinkedOpenHashMap<>(classMetadata.size());
            @NotNull final Int2ObjectMap<DBMLecture> mapLecture = new Int2ObjectLinkedOpenHashMap<>(lectureMetadata.size());
            for(@NotNull final DBMClass _class : classMetadata)
            {
                mapClass.put(_class.getId(), _class);
            }
            for(@NotNull final DBMLecture _lecture : lectureMetadata)
            {
                mapLecture.put(_lecture.getId(), _lecture);
            }

            @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `lesson`.`id`, `lesson`.`subject`, `lesson`.`sks`, `lesson`.`count`, `lesson`.`lecture`, `lesson`.`class` FROM `lesson` LEFT OUTER JOIN `subject` ON `lesson`.`subject` = `subject`.`id` WHERE `subject`.`id` = ? ORDER BY `lesson`.`id` ASC");
            statement.setInt(1, subjectMetadata.getId());
            @NotNull final ResultSet result = statement.executeQuery();
            while(result.next())
            {
                assert result.getInt("subject") == subjectMetadata.getId();
                list.add(new DBMLesson(
                        result.getInt("id"),
                        subjectMetadata,
                        result.getInt("sks"),
                        result.getInt("count"),
                        mapLecture.getOrDefault(result.getInt("lecture"), null),
                        mapClass.getOrDefault(result.getInt("class"), null)
                ));
            }
            result.close();
            statement.close();
            model.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return list;
    }

    public static List<DBMLesson> getAllMetadataFromClass(@NotNull final AbstractModel model, @NotNull final DBMClass classMetadata, @NotNull final List<DBMSubject> subjectMetadata, @NotNull final List<DBMLecture> lectureMetadata)
    {
        @NotNull List<DBMLesson> list = new LinkedList<>();
        try
        {
            if(model.isClosed())
            {
                model.reconnect();
            }
            @NotNull final Int2ObjectMap<DBMSubject> mapSubject = new Int2ObjectLinkedOpenHashMap<>(subjectMetadata.size());
            @NotNull final Int2ObjectMap<DBMLecture> mapLecture = new Int2ObjectLinkedOpenHashMap<>(lectureMetadata.size());
            for(@NotNull final DBMSubject _subject : subjectMetadata)
            {
                mapSubject.put(_subject.getId(), _subject);
            }
            for(@NotNull final DBMLecture _lecture : lectureMetadata)
            {
                mapLecture.put(_lecture.getId(), _lecture);
            }

            @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `lesson`.`id`, `lesson`.`subject`, `lesson`.`sks`, `lesson`.`count`, `lesson`.`lecture`, `lesson`.`class` FROM `lesson` LEFT OUTER JOIN `class` ON `lesson`.`class` = `class`.`id` WHERE `class`.`id` = ? ORDER BY `lesson`.`id` ASC");
            statement.setInt(1, classMetadata.getId());
            @NotNull final ResultSet result = statement.executeQuery();
            while(result.next())
            {
                assert result.getInt("class") == classMetadata.getId();
                list.add(new DBMLesson(
                        result.getInt("id"),
                        mapSubject.getOrDefault(result.getInt("subject"), null),
                        result.getInt("sks"),
                        result.getInt("count"),
                        mapLecture.getOrDefault(result.getInt("lecture"), null),
                        classMetadata
                ));
            }
            result.close();
            statement.close();
            model.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return list;
    }

    public static List<DBMLesson> getAllMetadataFromClassroom(@NotNull final AbstractModel model, @NotNull final DBMClassroom classroomMetadata, @NotNull final List<DBMSubject> subjectMetadata, @NotNull final List<DBMClass> classMetadata, @NotNull final List<DBMLecture> lectureMetadata)
    {
        @NotNull List<DBMLesson> list = new LinkedList<>();
        try
        {
            if(model.isClosed())
            {
                model.reconnect();
            }
            @NotNull final Int2ObjectMap<DBMSubject> mapSubject = new Int2ObjectLinkedOpenHashMap<>(subjectMetadata.size());
            @NotNull final Int2ObjectMap<DBMClass>   mapClass   = new Int2ObjectLinkedOpenHashMap<>(classMetadata.size());
            @NotNull final Int2ObjectMap<DBMLecture> mapLecture = new Int2ObjectLinkedOpenHashMap<>(lectureMetadata.size());
            for(@NotNull final DBMSubject _subject : subjectMetadata)
            {
                mapSubject.put(_subject.getId(), _subject);
            }
            for(@NotNull final DBMClass _class : classMetadata)
            {
                mapClass.put(_class.getId(), _class);
            }
            for(@NotNull final DBMLecture _lecture : lectureMetadata)
            {
                mapLecture.put(_lecture.getId(), _lecture);
            }

            @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `lesson`.`id`, `lesson`.`subject`, `lesson`.`sks`, `lesson`.`count`, `lesson`.`lecture`, `lesson`.`class` FROM `lesson` LEFT OUTER JOIN `lesson_available_classroom` ON `lesson`.`id` = `lesson_available_classroom`.`lesson` LEFT OUTER JOIN `classroom` ON `lesson_available_classroom`.`classroom` = `classroom`.`id` WHERE `classroom`.`id` = ? ORDER BY `lesson`.`id` ASC");
            statement.setInt(1, classroomMetadata.getId());
            @NotNull final ResultSet result = statement.executeQuery();
            while(result.next())
            {
                list.add(new DBMLesson(
                        result.getInt("id"),
                        mapSubject.getOrDefault(result.getInt("subject"), null),
                        result.getInt("sks"),
                        result.getInt("count"),
                        mapLecture.getOrDefault(result.getInt("lecture"), null),
                        mapClass.getOrDefault(result.getInt("class"), null)
                ));
            }
            result.close();
            statement.close();
            model.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return list;
    }

    public static List<DBMLesson> getAllMetadataFromLecture(@NotNull final AbstractModel model, @NotNull final DBMLecture lectureMetadata, @NotNull final List<DBMSubject> subjectMetadata, @NotNull final List<DBMClass> classMetadata)
    {
        @NotNull List<DBMLesson> list = new LinkedList<>();
        try
        {
            if(model.isClosed())
            {
                model.reconnect();
            }
            @NotNull final Int2ObjectMap<DBMSubject> mapSubject = new Int2ObjectLinkedOpenHashMap<>(subjectMetadata.size());
            @NotNull final Int2ObjectMap<DBMClass>   mapClass   = new Int2ObjectLinkedOpenHashMap<>(classMetadata.size());
            for(@NotNull final DBMSubject _subject : subjectMetadata)
            {
                mapSubject.put(_subject.getId(), _subject);
            }
            for(@NotNull final DBMClass _class : classMetadata)
            {
                mapClass.put(_class.getId(), _class);
            }

            @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `lesson`.`id`, `lesson`.`subject`, `lesson`.`sks`, `lesson`.`count`, `lesson`.`lecture`, `lesson`.`class` FROM `lesson` LEFT OUTER JOIN `lecture` ON `lesson`.`lecture` = `lecture`.`id` WHERE `lecture`.`id` = ? ORDER BY `lesson`.`id` ASC");
            statement.setInt(1, lectureMetadata.getId());
            @NotNull final ResultSet result = statement.executeQuery();
            while(result.next())
            {
                assert result.getInt("lecture") == lectureMetadata.getId();
                list.add(new DBMLesson(
                        result.getInt("id"),
                        mapSubject.getOrDefault(result.getInt("subject"), null),
                        result.getInt("sks"),
                        result.getInt("count"),
                        lectureMetadata,
                        mapClass.getOrDefault(result.getInt("class"), null)
                ));
            }
            result.close();
            statement.close();
            model.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return list;
    }

    public static void deleteBunch(@NotNull final AbstractModel model, @NotNull final List<DBMLesson> lessonMetadata)
    {
        try
        {
            if(model.isClosed())
            {
                model.reconnect();
            }
            model.connection.setAutoCommit(false);
            @NotNull PreparedStatement statement = model.connection.prepareStatement("DELETE FROM `lesson_available_classroom` WHERE `lesson` = ?");
            for(@NotNull final DBMLesson lesson : lessonMetadata)
            {
                statement.setInt(1, lesson.getId());
                statement.addBatch();
            }
            statement.executeBatch();
            statement.close();
            model.connection.commit();
            statement = model.connection.prepareStatement("DELETE FROM `lesson` WHERE `id` = ?");
            for(@NotNull final DBMLesson lesson : lessonMetadata)
            {
                statement.setInt(1, lesson.getId());
                statement.addBatch();
            }
            statement.executeBatch();
            statement.close();
            model.connection.commit();
            model.connection.setAutoCommit(true);
            model.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static DBLesson insertAndGet(@NotNull final AbstractModel model, @NotNull final DBLesson lesson)
    {
        @Nullable DBLesson newLesson = null;
        try
        {
            if(model.isClosed())
            {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("INSERT INTO lesson(id, subject, sks, count, lecture, class) VALUES (NULL, ?, ?, ?, ?, ?)");
            statement.setInt(1, lesson.getSubject().getId());
            statement.setInt(2, lesson.getSks());
            statement.setInt(3, lesson.getCount());
            statement.setInt(4, lesson.getLecture() == null ? -1 : lesson.getLecture().getId());
            statement.setInt(5, lesson.getKlass().getId());
            statement.execute();
            statement.close();

            @NotNull final PreparedStatement callStatement = model.connection.prepareStatement("SELECT last_insert_rowid()");
            @NotNull final ResultSet         callResult    = callStatement.executeQuery();
            callResult.next();
            final int id = callResult.getInt("last_insert_rowid()");
            callResult.close();
            callStatement.close();
            model.close();

            newLesson = new DBLesson(id, lesson.getSubject(), lesson.getSks(), lesson.getCount(), lesson.getLecture(), lesson.getKlass(), lesson.getClassrooms().size());
            newLesson.getClassrooms().addAll(lesson.getClassrooms());
            MLesson.insertAvailableClassroom(model, newLesson);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return newLesson;
    }

    public static void insertAvailableClassroom(@NotNull final AbstractModel model, @NotNull final DBLesson lesson)
    {
        try
        {
            if(model.isClosed())
            {
                model.reconnect();
            }
            model.connection.setAutoCommit(false);
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("INSERT INTO lesson_available_classroom(id, lesson, classroom) VALUES (NULL, ?, ?)");
            for(@NotNull final DBMClassroom classroom : lesson.getClassrooms())
            {
                statement.setInt(1, lesson.getId());
                statement.setInt(2, classroom.getId());
                statement.addBatch();
            }
            statement.executeBatch();
            statement.close();
            model.connection.commit();
            model.connection.setAutoCommit(true);
            model.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void deleteAvailableClassroom(@NotNull final AbstractModel model, @NotNull final DBMLesson lessonMetadata)
    {
        try
        {
            if(model.isClosed())
            {
                model.reconnect();
            }
            @NotNull PreparedStatement statement = model.connection.prepareStatement("DELETE FROM `lesson_available_classroom` WHERE `lesson` = ?");
            statement.setInt(1, lessonMetadata.getId());
            statement.execute();
            statement.close();
            model.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void delete(@NotNull final AbstractModel model, @NotNull final DBMLesson lessonMetadata)
    {
        try
        {
            if(model.isClosed())
            {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("DELETE FROM `lesson` WHERE `id` = ?");
            statement.setInt(1, lessonMetadata.getId());
            statement.execute();
            statement.close();
            model.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("ConstantConditions") public static DBLesson getFromMetadata(@NotNull final AbstractModel model, @NotNull final DBMLesson lessonMetadata, @NotNull final List<DBMSubject> subjectMetadata, @NotNull final List<DBMClass> classMetadata, @NotNull final List<DBMLecture> lectureMetadata)
    {
        @Nullable DBLesson lesson = null;
        try
        {
            if(model.isClosed())
            {
                model.reconnect();
            }

            @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `lesson`.`id`, `lesson`.`subject`, `lesson`.`lecture`, `lesson`.`sks`, `lesson`.`count`, `lesson`.`class`, COUNT(`lesson_available_classroom`.`id`) AS 'classroom_size'  FROM `lesson` LEFT OUTER JOIN `lesson_available_classroom` ON `lesson`.`id` = `lesson_available_classroom`.`lesson`  WHERE `lesson`.`id` = ?  GROUP BY `lesson`.`id` LIMIT 1");
            statement.setInt(1, lessonMetadata.getId());
            @NotNull final ResultSet result = statement.executeQuery();
            while(result.next())
            {
                @NotNull final Optional<DBMSubject> _subject = subjectMetadata.stream().filter(oSubject ->
                {
                    try
                    {
                        return oSubject.getId() == result.getInt("subject");
                    }
                    catch(SQLException e)
                    {
                        return false;
                    }
                }).findFirst();
                @NotNull final Optional<DBMLecture> _lecture = lectureMetadata.stream().filter(oLecture ->
                {
                    try
                    {
                        return oLecture.getId() == result.getInt("lecture");
                    }
                    catch(SQLException e)
                    {
                        return false;
                    }
                }).findFirst();
                @NotNull final Optional<DBMClass> _class = classMetadata.stream().filter(oClass ->
                {
                    try
                    {
                        return oClass.getId() == result.getInt("class");
                    }
                    catch(SQLException e)
                    {
                        return false;
                    }
                }).findFirst();

                lesson = new DBLesson(
                        result.getInt("id"),
                        _subject.isPresent() ? _subject.get() : null,
                        result.getInt("sks"),
                        result.getInt("count"),
                        _lecture.isPresent() ? _lecture.get() : null,
                        _class.isPresent() ? _class.get() : null,
                        result.getInt("classroom_size")
                );
            }
            result.close();
            statement.close();
            model.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return lesson;
    }

    public static void getAvailableClassroom(@NotNull final AbstractModel model, @NotNull final DBLesson lesson, @NotNull final List<DBMClassroom> classroomMetadata)
    {
        try
        {
            @NotNull List<DBMClassroom> classrooms = lesson.getClassrooms();
            if(model.isClosed())
            {
                model.reconnect();
            }
            @NotNull final Int2ObjectMap<DBMClassroom> mapClassroom = new Int2ObjectLinkedOpenHashMap<>(classroomMetadata.size());
            for(@NotNull final DBMClassroom _clasroom : classroomMetadata)
            {
                mapClassroom.put(_clasroom.getId(), _clasroom);
            }

            @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `lesson_available_classroom`.`classroom`  FROM `lesson_available_classroom`  LEFT OUTER JOIN `lesson` ON `lesson_available_classroom`.`lesson` = `lesson`.`id`  WHERE `lesson`.`id` = ?  ORDER BY `lesson_available_classroom`.`classroom` ASC");
            statement.setInt(1, lesson.getId());
            @NotNull final ResultSet result = statement.executeQuery();
            while(result.next())
            {
                classrooms.add(mapClassroom.get(result.getInt("classroom")));
            }
            result.close();
            statement.close();
            model.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void updateMetadata(@NotNull final AbstractModel model, @NotNull final DBMLesson lessonMetadata)
    {
        try
        {
            if(model.isClosed())
            {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("UPDATE `lesson` SET `lecture` = ?, `count` = ?, `class` = ?, `sks` = ?, `subject` = ? WHERE `id` = ?");
            statement.setInt(1, lessonMetadata.getLecture() == null ? -1 : lessonMetadata.getLecture().getId());
            statement.setInt(2, lessonMetadata.getCount());
            statement.setInt(3, lessonMetadata.getKlass().getId());
            statement.setInt(4, lessonMetadata.getSks());
            statement.setInt(5, lessonMetadata.getSubject().getId());
            statement.setInt(5, lessonMetadata.getId());
            statement.execute();
            statement.close();
            model.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}
