package model.database.model;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 21 January 2017, 5:38 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import it.unimi.dsi.fastutil.objects.ObjectList;
import model.AbstractModel;
import model.database.component.DBSchool;
import model.database.component.DBSubject;
import model.database.component.DBTimeOff;
import model.database.component.metadata.DBMSchool;
import model.database.component.metadata.DBMSubject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings({"unused", "WeakerAccess"})
public class MSubject extends AbstractModel {
    public MSubject(@NotNull String dbPath) throws SQLException {
        super(dbPath);
    }

    public MSubject(@NotNull AbstractModel model) {
        super(model);
    }

    public List<DBSubject> getAllFromSchool(@NotNull final DBSchool school) {
        return MSubject.getAllFromSchool(this, school);
    }

    public static List<DBSubject> getAllFromSchool(@NotNull final AbstractModel model, @NotNull final DBSchool school) {
        @NotNull List<DBSubject> subjectList = new LinkedList<>();
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `id`, `name`, `code` FROM subject WHERE `school` = ? ORDER BY `id` ASC");
            statement.setInt(1, school.getId());
            @NotNull final ResultSet result = statement.executeQuery();
            while (result.next()) {
                subjectList.add(
                        new DBSubject(
                                result.getInt("id"),
                                result.getString("name"),
                                result.getString("code"),
                                school
                        ));
            }
            result.close();
            statement.close();
            model.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjectList;
    }

    public static List<DBMSubject> getAllMetadataFromSchool(@NotNull final AbstractModel model, @NotNull final DBMSchool school) {
        @NotNull List<DBMSubject> list = new LinkedList<>();
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `id`, `name`, `code` FROM `subject` WHERE `school` = ? ORDER BY `id` ASC");
            statement.setInt(1, school.getId());
            @NotNull final ResultSet result = statement.executeQuery();
            while (result.next()) {
                list.add(
                        new DBMSubject(
                                result.getInt("id"),
                                result.getString("name"),
                                result.getString("code")
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

    public static DBMSubject insert(@NotNull final AbstractModel model, @NotNull final DBMSchool schoolMetadata, String name, String code) {
        @Nullable DBMSubject subject = null;
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("INSERT INTO `subject`(`id`, `name`, `code`, `school`) VALUES (NULL, ?, ?, ?)");
            statement.setString(1, name);
            statement.setString(2, code);
            statement.setInt(3, schoolMetadata.getId());
            statement.execute();
            statement.close();

            @NotNull final PreparedStatement callStatement = model.connection.prepareStatement("SELECT last_insert_rowid()");
            @NotNull final ResultSet callResult = callStatement.executeQuery();
            callResult.next();
            final int id = callResult.getInt("last_insert_rowid()");
            callResult.close();
            callStatement.close();
            model.close();

            subject = MSubject.getMetadataFromID(model, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subject;
    }

    @Nullable
    private static DBMSubject getMetadataFromID(@NotNull final AbstractModel model, final int id) {
        @Nullable DBMSubject subject = null;
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `id`, `name`, `code` FROM `subject` WHERE `id` = ? LIMIT 1");
            statement.setInt(1, id);
            @NotNull final ResultSet result = statement.executeQuery();
            while (result.next()) {
                subject = new DBMSubject(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("code")
                );
            }
            result.close();
            statement.close();
            model.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subject;
    }

    public static void insertTimeOff(@NotNull final AbstractModel model, @NotNull final DBMSubject subject, @NotNull final DBSubject.TimeOffContainer container) {
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            model.connection.setAutoCommit(false);
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("INSERT INTO subject_timeoff(id, subject, day, period, availability) VALUES (NULL, ?, ?, ?, ?)");
            for (@NotNull final ObjectList<DBTimeOff> day : container.getAvailabilities()) {
                for (@NotNull final DBTimeOff period : day) {
                    statement.setInt(1, subject.getId());
                    statement.setInt(2, period.getDay().getId());
                    statement.setInt(3, period.getPeriod().getId());
                    statement.setInt(4, period.getAvailability().getId());
                    statement.addBatch();
                }
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

    public static void deleteTimeOff(@NotNull final AbstractModel model, @NotNull final DBMSubject subject) {
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("DELETE FROM `subject_timeoff` WHERE `subject` = ?");
            statement.setInt(1, subject.getId());
            statement.execute();
            statement.close();
            model.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(@NotNull final AbstractModel model, @NotNull final DBMSubject subject) {
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("DELETE FROM `subject` WHERE `id` = ?");
            statement.setInt(1, subject.getId());
            statement.execute();
            statement.close();
            model.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
