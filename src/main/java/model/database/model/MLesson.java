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
import model.AbstractModel;
import model.database.component.metadata.*;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class MLesson extends AbstractModel
{
    public MLesson(@NotNull String dbPath) throws SQLException {
        super(dbPath);
    }

    public MLesson(@NotNull AbstractModel model) {
        super(model);
    }


    public static List<DBMLesson> getAllMetadataFromSchool(@NotNull final AbstractModel model, @NotNull final DBMSchool schoolMetadata, @NotNull final List<DBMSubject> subjectMetadata, @NotNull final List<DBMClass> classMetadata, @NotNull final List<DBMLecture> lectureMetadata) {
        @NotNull List<DBMLesson> list = new LinkedList<>();
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final Int2ObjectMap<DBMSubject> mapSubject = new Int2ObjectLinkedOpenHashMap<>(subjectMetadata.size());
            @NotNull final Int2ObjectMap<DBMClass> mapClass = new Int2ObjectLinkedOpenHashMap<>(classMetadata.size());
            @NotNull final Int2ObjectMap<DBMLecture> mapLecture = new Int2ObjectLinkedOpenHashMap<>(lectureMetadata.size());
            for (@NotNull final DBMSubject _subject : subjectMetadata) {
                mapSubject.put(_subject.getId(), _subject);
            }
            for (@NotNull final DBMClass _class : classMetadata) {
                mapClass.put(_class.getId(), _class);
            }
            for (@NotNull final DBMLecture _lecture : lectureMetadata) {
                mapLecture.put(_lecture.getId(), _lecture);
            }

            @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `lesson`.`id`, `lesson`.`subject`, `lesson`.`sks`, `lesson`.`count`, `lesson`.`lecture`, `lesson`.`class` FROM `lesson` LEFT OUTER JOIN `subject` ON `lesson`.`subject` = `subject`.`id` WHERE `subject`.`school` = ?  GROUP BY `lesson`.`id`  ORDER BY `lesson`.`id` ASC");
            statement.setInt(1, schoolMetadata.getId());
            @NotNull final ResultSet result = statement.executeQuery();
            while (result.next()) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void deleteBunch(@NotNull final AbstractModel model, @NotNull final List<DBMLesson> lessonMetadata) {
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            model.connection.setAutoCommit(false);
            @NotNull PreparedStatement statement = model.connection.prepareStatement("DELETE FROM `lesson_available_classroom` WHERE `lesson` = ?");
            for (@NotNull final DBMLesson lesson : lessonMetadata) {
                statement.setInt(1, lesson.getId());
                statement.addBatch();
            }
            statement.executeBatch();
            statement.close();
            model.connection.commit();
            statement = model.connection.prepareStatement("DELETE FROM `lesson` WHERE `id` = ?");
            for (@NotNull final DBMLesson lesson : lessonMetadata) {
                statement.setInt(1, lesson.getId());
                statement.addBatch();
            }
            statement.executeBatch();
            statement.close();
            model.connection.commit();
            model.connection.setAutoCommit(true);
            model.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
