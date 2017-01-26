package model.database.model;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 25 January 2017, 7:44 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import model.AbstractModel;
import model.database.component.DBAvailability;
import model.database.component.DBClassroom;
import model.database.component.DBTimeOff;
import model.database.component.DBTimeOffContainer;
import model.database.component.metadata.DBMClassroom;
import model.database.component.metadata.DBMDay;
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
public class MClassroom extends AbstractModel {
    public MClassroom(@NotNull String dbPath) throws SQLException {
        super(dbPath);
    }

    public MClassroom(@NotNull AbstractModel model) {
        super(model);
    }

    public List<DBClassroom> getAllFromSchool(@NotNull final DBMSchool school) {
        return MClassroom.getAllFromSchool(this, school);
    }

    @SuppressWarnings("WeakerAccess")
    public static List<DBClassroom> getAllFromSchool(@NotNull final AbstractModel model, @NotNull final DBMSchool school) {
        @NotNull List<DBClassroom> list = new LinkedList<>();
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `id`, `name` FROM `classroom` WHERE `school` = ? ORDER BY `id` ASC");
            statement.setInt(1, school.getId());
            @NotNull final ResultSet result = statement.executeQuery();
            while (result.next()) {
                list.add(
                        new DBClassroom(
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

    public static List<DBMClassroom> getAllMetadataFromSchool(@NotNull final AbstractModel model, @NotNull final DBMSchool school) {
        @NotNull List<DBMClassroom> list = new LinkedList<>();
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `id`, `name` FROM `classroom` WHERE `school` = ? ORDER BY `id` ASC");
            statement.setInt(1, school.getId());
            @NotNull final ResultSet result = statement.executeQuery();
            while (result.next()) {
                list.add(
                        new DBMClassroom(
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

    public static DBMClassroom insert(@NotNull final AbstractModel model, @NotNull final DBMSchool schoolMetadata, String name) {
        @Nullable DBMClassroom classroom = null;
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("INSERT INTO `classroom`(`id`, `name`, `school`) VALUES (NULL, ?, ?)");
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

            classroom = MClassroom.getMetadataFromID(model, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classroom;
    }

    @Nullable
    private static DBMClassroom getMetadataFromID(@NotNull final AbstractModel model, final int id) {
        @Nullable DBMClassroom classroom = null;
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `id`, `name` FROM `classroom` WHERE `id` = ? LIMIT 1");
            statement.setInt(1, id);
            @NotNull final ResultSet result = statement.executeQuery();
            while (result.next()) {
                classroom = new DBMClassroom(
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
        return classroom;
    }

    @SuppressWarnings("Duplicates")
    public static void insertTimeOff(@NotNull final AbstractModel model, @NotNull final DBMClassroom classroom, @NotNull final DBClassroom.TimeOffContainer container) {
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            model.connection.setAutoCommit(false);
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("INSERT INTO `classroom_timeoff`(`id`, `classroom`, `day`, `period`, `availability`) VALUES (NULL, ?, ?, ?, ?)");
            for (@NotNull final ObjectList<DBTimeOff> day : container.getAvailabilities()) {
                for (@NotNull final DBTimeOff period : day) {
                    statement.setInt(1, classroom.getId());
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

    public static void deleteTimeOff(@NotNull final AbstractModel model, @NotNull final DBMClassroom classroom) {
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("DELETE FROM `classroom_timeoff` WHERE `classroom` = ?");
            statement.setInt(1, classroom.getId());
            statement.execute();
            statement.close();
            model.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(@NotNull final AbstractModel model, @NotNull final DBMClassroom classroom) {
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("DELETE FROM `classroom` WHERE `id` = ?");
            statement.setInt(1, classroom.getId());
            statement.execute();
            statement.close();
            model.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DBClassroom getFromMetadata(@NotNull final AbstractModel model, @NotNull final DBMSchool schoolMetadata, @NotNull final DBMClassroom classroomMetadata) {
        @Nullable DBClassroom classroom = null;
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `id`, `name`, `school` FROM `classroom` WHERE `id` = ? LIMIT 1");
            statement.setInt(1, classroomMetadata.getId());
            @NotNull final ResultSet result = statement.executeQuery();
            while (result.next()) {
                classroom = new DBClassroom(
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
        return classroom;
    }

    @SuppressWarnings({"unchecked", "Duplicates"})
    public static void getTimeOff(@NotNull final AbstractModel model, @NotNull final DBClassroom classroom, @NotNull final List<DBMDay> dayMetadata, @NotNull final List<DBMPeriod> periodMetadata, @NotNull final List<DBAvailability> availabilities) {
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

            @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `classroom_timeoff`.`id`, `classroom_timeoff`.`classroom`, `classroom_timeoff`.`day`, `classroom_timeoff`.`period`, `classroom_timeoff`.`availability` FROM `classroom_timeoff` LEFT OUTER JOIN `classroom` ON `classroom_timeoff`.`classroom` = `classroom`.`id` LEFT OUTER JOIN `active_day` ON `classroom_timeoff`.`day` = `active_day`.`id` LEFT OUTER JOIN `active_period` ON `classroom_timeoff`.`period` = `active_period`.`id` WHERE `classroom`.`id` = ? ORDER BY `active_day`.`position`, `active_period`.`position` ASC;");
            statement.setInt(1, classroom.getId());
            @NotNull final ResultSet result = statement.executeQuery();
            int dayIndex = -1;
            int periodIndex = -1;
            ObjectListIterator<ObjectList<DBTimeOff>> class_db = classroom.getTimeoff().getAvailabilities().listIterator();
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

    public static void update(@NotNull final AbstractModel model, @NotNull final DBMClassroom classroom, @NotNull final String name) {
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("UPDATE `classroom` SET `name` = ? WHERE `id` = ?");
            statement.setString(1, name);
            statement.setInt(2, classroom.getId());
            statement.execute();
            statement.close();
            model.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("Duplicates")
    public static void updateTimeOff(@NotNull final AbstractModel model, @NotNull final DBTimeOffContainer<DBClassroom> timeOff) {
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            model.connection.setAutoCommit(false);
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("UPDATE `classroom_timeoff` SET `availability` = ? WHERE `id` = ? ");
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

    public static void deleteBunch(@NotNull final AbstractModel model, @NotNull final List<DBMClassroom> classroomMetadata) {
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            model.connection.setAutoCommit(false);
            @NotNull PreparedStatement statement = model.connection.prepareStatement("DELETE FROM `classroom_timeoff` WHERE `classroom` = ?");
            for (@NotNull final DBMClassroom classroom : classroomMetadata) {
                statement.setInt(1, classroom.getId());
                statement.addBatch();
            }
            statement.executeBatch();
            statement.close();
            model.connection.commit();
            statement = model.connection.prepareStatement("DELETE FROM `classroom` WHERE `id` = ?");
            for (@NotNull final DBMClassroom classroom : classroomMetadata) {
                statement.setInt(1, classroom.getId());
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
