package model.database.model;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 24 January 2017, 8:31 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import model.AbstractModel;
import model.database.component.DBAvailability;
import model.database.component.DBClass;
import model.database.component.DBTimeOff;
import model.database.component.DBTimeOffContainer;
import model.database.component.metadata.DBMClass;
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

@SuppressWarnings("unused")
public class MClass extends AbstractModel {
    public MClass(@NotNull String dbPath) throws SQLException {
        super(dbPath);
    }

    public MClass(@NotNull AbstractModel model) {
        super(model);
    }

    public List<DBClass> getAllFromSchool(@NotNull final DBMSchool school) {
        return MClass.getAllFromSchool(this, school);
    }

    @SuppressWarnings("WeakerAccess")
    public static List<DBClass> getAllFromSchool(@NotNull final AbstractModel model, @NotNull final DBMSchool school) {
        @NotNull List<DBClass> list = new LinkedList<>();
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `id`, `name` FROM `class` WHERE `school` = ? ORDER BY `id` ASC");
            statement.setInt(1, school.getId());
            @NotNull final ResultSet result = statement.executeQuery();
            while (result.next()) {
                list.add(
                        new DBClass(
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

    public static List<DBMClass> getAllMetadataFromSchool(@NotNull final AbstractModel model, @NotNull final DBMSchool school) {
        @NotNull List<DBMClass> list = new LinkedList<>();
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `id`, `name` FROM `class` WHERE `school` = ? ORDER BY `id` ASC");
            statement.setInt(1, school.getId());
            @NotNull final ResultSet result = statement.executeQuery();
            while (result.next()) {
                list.add(
                        new DBMClass(
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

    public static DBMClass insert(@NotNull final AbstractModel model, @NotNull final DBMSchool schoolMetadata, String name) {
        @Nullable DBMClass klass = null;
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("INSERT INTO `class`(`id`, `name`, `school`) VALUES (NULL, ?, ?)");
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

            klass = MClass.getMetadataFromID(model, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return klass;
    }

    @Nullable
    private static DBMClass getMetadataFromID(@NotNull final AbstractModel model, final int id) {
        @Nullable DBMClass klass = null;
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `id`, `name` FROM `class` WHERE `id` = ? LIMIT 1");
            statement.setInt(1, id);
            @NotNull final ResultSet result = statement.executeQuery();
            while (result.next()) {
                klass = new DBMClass(
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
        return klass;
    }

    @SuppressWarnings("Duplicates")
    public static void insertTimeOff(@NotNull final AbstractModel model, @NotNull final DBMClass klass, @NotNull final DBClass.TimeOffContainer container) {
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            model.connection.setAutoCommit(false);
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("INSERT INTO `class_timeoff`(`id`, `class`, `day`, `period`, `availability`) VALUES (NULL, ?, ?, ?, ?)");
            for (@NotNull final ObjectList<DBTimeOff> day : container.getAvailabilities()) {
                for (@NotNull final DBTimeOff period : day) {
                    statement.setInt(1, klass.getId());
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

    public static void deleteTimeOff(@NotNull final AbstractModel model, @NotNull final DBMClass klass) {
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("DELETE FROM `class_timeoff` WHERE `class` = ?");
            statement.setInt(1, klass.getId());
            statement.execute();
            statement.close();
            model.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(@NotNull final AbstractModel model, @NotNull final DBMClass klass) {
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("DELETE FROM `class` WHERE `id` = ?");
            statement.setInt(1, klass.getId());
            statement.execute();
            statement.close();
            model.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DBClass getFromMetadata(@NotNull final AbstractModel model, @NotNull final DBMSchool schoolMetadata, @NotNull final DBMClass classMetadata) {
        @Nullable DBClass klass = null;
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `id`, `name`, `school` FROM `class` WHERE `id` = ? LIMIT 1");
            statement.setInt(1, classMetadata.getId());
            @NotNull final ResultSet result = statement.executeQuery();
            while (result.next()) {
                klass = new DBClass(
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
        return klass;
    }

    @SuppressWarnings({"unchecked", "Duplicates"})
    public static void getTimeOff(@NotNull final AbstractModel model, @NotNull final DBClass klass, @NotNull final Int2ObjectMap<DBMDay> mapDay, @NotNull final Int2ObjectMap<DBMPeriod> mapPeriod, @NotNull final Int2ObjectMap<DBAvailability> mapAvailability) {
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `class_timeoff`.`id`, `class_timeoff`.`class`, `class_timeoff`.`day`, `class_timeoff`.`period`, `class_timeoff`.`availability` FROM `class_timeoff` LEFT OUTER JOIN `class` ON `class_timeoff`.`class` = `class`.`id` LEFT OUTER JOIN `active_day` ON `class_timeoff`.`day` = `active_day`.`id` LEFT OUTER JOIN `active_period` ON `class_timeoff`.`period` = `active_period`.`id` WHERE `class`.`id` = ? ORDER BY `active_day`.`position`, `active_period`.`position` ASC;");
            statement.setInt(1, klass.getId());
            @NotNull final ResultSet result = statement.executeQuery();
            int dayIndex = -1;
            int periodIndex = -1;
            ObjectListIterator<ObjectList<DBTimeOff>> class_db = klass.getTimeoff().getAvailabilities().listIterator();
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

    public static void update(@NotNull final AbstractModel model, @NotNull final DBMClass klass, @NotNull final String name) {
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("UPDATE `class` SET `name` = ? WHERE `id` = ?");
            statement.setString(1, name);
            statement.setInt(2, klass.getId());
            statement.execute();
            statement.close();
            model.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("Duplicates")
    public static void updateTimeOff(@NotNull final AbstractModel model, @NotNull final DBTimeOffContainer<DBClass> timeOff) {
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            model.connection.setAutoCommit(false);
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("UPDATE `class_timeoff` SET `availability` = ? WHERE `id` = ? ");
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

    public static void deleteBunch(@NotNull final AbstractModel model, @NotNull final List<DBMClass> classMetadata) {
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            model.connection.setAutoCommit(false);
            @NotNull PreparedStatement statement = model.connection.prepareStatement("DELETE FROM `class_timeoff` WHERE `class` = ?");
            for (@NotNull final DBMClass klass : classMetadata) {
                statement.setInt(1, klass.getId());
                statement.addBatch();
            }
            statement.executeBatch();
            statement.close();
            model.connection.commit();
            statement = model.connection.prepareStatement("DELETE FROM `class` WHERE `id` = ?");
            for (@NotNull final DBMClass klass : classMetadata) {
                statement.setInt(1, klass.getId());
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