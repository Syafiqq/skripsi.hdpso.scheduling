package model.database.model;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 25 January 2017, 8:55 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */


import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import model.AbstractModel;
import model.database.component.DBAvailability;
import model.database.component.DBLecture;
import model.database.component.DBTimeOff;
import model.database.component.DBTimeOffContainer;
import model.database.component.metadata.DBMDay;
import model.database.component.metadata.DBMLecture;
import model.database.component.metadata.DBMPeriod;
import model.database.component.metadata.DBMSchool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings({"unused", "WeakerAccess"})
public class MLecture extends AbstractModel {
    public MLecture(@NotNull String dbPath) throws SQLException {
        super(dbPath);
    }

    public MLecture(@NotNull AbstractModel model) {
        super(model);
    }

    public List<DBLecture> getAllFromSchool(@NotNull final DBMSchool school) {
        return MLecture.getAllFromSchool(this, school);
    }

    @SuppressWarnings("WeakerAccess")
    public static List<DBLecture> getAllFromSchool(@NotNull final AbstractModel model, @NotNull final DBMSchool school) {
        @NotNull List<DBLecture> list = new LinkedList<>();
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `id`, `name` FROM `lecture` WHERE `school` = ? ORDER BY `id` ASC");
            statement.setInt(1, school.getId());
            @NotNull final ResultSet result = statement.executeQuery();
            while (result.next()) {
                list.add(
                        new DBLecture(
                                result.getInt("id"),
                                result.getString("name"),
                                school
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

    public static List<DBMLecture> getAllMetadataFromSchool(@NotNull final AbstractModel model, @NotNull final DBMSchool school) {
        @NotNull List<DBMLecture> list = new LinkedList<>();
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `id`, `name` FROM `lecture` WHERE `school` = ? ORDER BY `id` ASC");
            statement.setInt(1, school.getId());
            @NotNull final ResultSet result = statement.executeQuery();
            while (result.next()) {
                list.add(
                        new DBMLecture(
                                result.getInt("id"),
                                result.getString("name")
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

    public static DBMLecture insert(@NotNull final AbstractModel model, @NotNull final DBMSchool schoolMetadata, String name) {
        @Nullable DBMLecture lecture = null;
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("INSERT INTO `lecture`(`id`, `name`, `school`) VALUES (NULL, ?, ?)");
            statement.setString(1, name);
            statement.setInt(2, schoolMetadata.getId());
            statement.execute();
            statement.close();

            @NotNull final PreparedStatement callStatement = model.connection.prepareStatement("SELECT last_insert_rowid()");
            @NotNull final ResultSet callResult = callStatement.executeQuery();
            callResult.next();
            final int id = callResult.getInt("last_insert_rowid()");
            callResult.close();
            callStatement.close();
            model.close();

            lecture = MLecture.getMetadataFromID(model, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lecture;
    }

    @Nullable
    private static DBMLecture getMetadataFromID(@NotNull final AbstractModel model, final int id) {
        @Nullable DBMLecture lecture = null;
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `id`, `name` FROM `lecture` WHERE `id` = ? LIMIT 1");
            statement.setInt(1, id);
            @NotNull final ResultSet result = statement.executeQuery();
            while (result.next()) {
                lecture = new DBMLecture(
                        result.getInt("id"),
                        result.getString("name")
                );
            }
            result.close();
            statement.close();
            model.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lecture;
    }

    @SuppressWarnings("Duplicates")
    public static void insertTimeOff(@NotNull final AbstractModel model, @NotNull final DBMLecture lecture, @NotNull final DBLecture.TimeOffContainer container) {
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            model.connection.setAutoCommit(false);
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("INSERT INTO `lecture_timeoff`(`id`, `lecture`, `day`, `period`, `availability`) VALUES (NULL, ?, ?, ?, ?)");
            for (@NotNull final ObjectList<DBTimeOff> day : container.getAvailabilities()) {
                for (@NotNull final DBTimeOff period : day) {
                    statement.setInt(1, lecture.getId());
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

    public static void deleteTimeOff(@NotNull final AbstractModel model, @NotNull final DBMLecture lecture) {
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("DELETE FROM `lecture_timeoff` WHERE `lecture` = ?");
            statement.setInt(1, lecture.getId());
            statement.execute();
            statement.close();
            model.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(@NotNull final AbstractModel model, @NotNull final DBMLecture lecture) {
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("DELETE FROM `lecture` WHERE `id` = ?");
            statement.setInt(1, lecture.getId());
            statement.execute();
            statement.close();
            model.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DBLecture getFromMetadata(@NotNull final AbstractModel model, @NotNull final DBMSchool schoolMetadata, @NotNull final DBMLecture lectureMetadata) {
        @Nullable DBLecture lecture = null;
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `id`, `name`, `school` FROM `lecture` WHERE `id` = ? LIMIT 1");
            statement.setInt(1, lectureMetadata.getId());
            @NotNull final ResultSet result = statement.executeQuery();
            while (result.next()) {
                lecture = new DBLecture(
                        result.getInt("id"),
                        result.getString("name"),
                        schoolMetadata
                );
            }
            result.close();
            statement.close();
            model.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lecture;
    }

    @SuppressWarnings({"unchecked", "Duplicates"})
    public static void getTimeOff(@NotNull final AbstractModel model, @NotNull final DBLecture lecture, @NotNull final List<DBMDay> dayMetadata, @NotNull final List<DBMPeriod> periodMetadata, @NotNull final List<DBAvailability> availabilities) {
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final Int2ObjectMap<DBMDay> mapDay = new Int2ObjectLinkedOpenHashMap<>(dayMetadata.size());
            @NotNull final Int2ObjectMap<DBMPeriod> mapPeriod = new Int2ObjectLinkedOpenHashMap<>(periodMetadata.size());
            @NotNull final Int2ObjectMap<DBAvailability> mapAvailability = new Int2ObjectLinkedOpenHashMap<>(availabilities.size());
            for (@NotNull final DBMDay _day : dayMetadata) {
                mapDay.put(_day.getId(), _day);
            }
            for (@NotNull final DBMPeriod _period : periodMetadata) {
                mapPeriod.put(_period.getId(), _period);
            }
            for (@NotNull final DBAvailability _availability : availabilities) {
                mapAvailability.put(_availability.getId(), _availability);
            }

            @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `lecture_timeoff`.`id`, `lecture_timeoff`.`lecture`, `lecture_timeoff`.`day`, `lecture_timeoff`.`period`, `lecture_timeoff`.`availability` FROM `lecture_timeoff` LEFT OUTER JOIN `lecture` ON `lecture_timeoff`.`lecture` = `lecture`.`id` LEFT OUTER JOIN `active_day` ON `lecture_timeoff`.`day` = `active_day`.`id` LEFT OUTER JOIN `active_period` ON `lecture_timeoff`.`period` = `active_period`.`id` WHERE `lecture`.`id` = ? ORDER BY `active_day`.`position`, `active_period`.`position` ASC;");
            statement.setInt(1, lecture.getId());
            @NotNull final ResultSet result = statement.executeQuery();
            int dayIndex = -1;
            int periodIndex = -1;
            ObjectListIterator<ObjectList<DBTimeOff>> class_db = lecture.getTimeoff().getAvailabilities().listIterator();
            ObjectList<DBTimeOff> current_timeOff = null;
            while (result.next()) {
                if (dayIndex != result.getInt("day")) {
                    dayIndex = result.getInt("day");
                    periodIndex = -1;
                    current_timeOff = class_db.next();
                }

                assert current_timeOff != null;
                current_timeOff.set(++periodIndex, new DBTimeOff(result.getInt("id"), mapDay.get(result.getInt("day")), mapPeriod.get(result.getInt("period")), mapAvailability.get(result.getInt("availability"))));
            }
            result.close();
            statement.close();
            model.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void update(@NotNull final AbstractModel model, @NotNull final DBMLecture lecture, @NotNull final String name) {
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("UPDATE `lecture` SET `name` = ? WHERE `id` = ?");
            statement.setString(1, name);
            statement.setInt(2, lecture.getId());
            statement.execute();
            statement.close();
            model.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("Duplicates")
    public static void updateTimeOff(@NotNull final AbstractModel model, @NotNull final DBTimeOffContainer<DBLecture> timeOff) {
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            model.connection.setAutoCommit(false);
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("UPDATE `lecture_timeoff` SET `availability` = ? WHERE `id` = ? ");
            for (@NotNull final ObjectList<DBTimeOff> day : timeOff.getAvailabilities()) {
                for (@NotNull final DBTimeOff period : day) {
                    statement.setInt(1, period.getAvailability().getId());
                    statement.setInt(2, period.getId());
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

    public static void deleteBunch(@NotNull final AbstractModel model, @NotNull final List<DBMLecture> lectureMetadata) {
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            model.connection.setAutoCommit(false);
            @NotNull PreparedStatement statement = model.connection.prepareStatement("DELETE FROM `lecture_timeoff` WHERE `lecture` = ?");
            for (@NotNull final DBMLecture lecture : lectureMetadata) {
                statement.setInt(1, lecture.getId());
                statement.addBatch();
            }
            statement.executeBatch();
            statement.close();
            model.connection.commit();
            statement = model.connection.prepareStatement("DELETE FROM `lecture` WHERE `id` = ?");
            for (@NotNull final DBMLecture lecture : lectureMetadata) {
                statement.setInt(1, lecture.getId());
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