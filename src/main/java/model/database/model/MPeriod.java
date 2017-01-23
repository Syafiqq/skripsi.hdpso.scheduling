package model.database.model;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 21 January 2017, 8:02 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import model.AbstractModel;
import model.database.component.DBPeriod;
import model.database.component.DBSchool;
import model.database.component.metadata.DBMPeriod;
import model.database.component.metadata.DBMSchool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "unused"})
public class MPeriod extends AbstractModel {
    public MPeriod(@NotNull String dbPath) throws SQLException {
        super(dbPath);
    }

    public MPeriod(@NotNull AbstractModel model) {
        super(model);
    }

    public void insertOnly(@NotNull final DBSchool school, String name, String nickname, String start, String end, int position) {
        MPeriod.insertOnly(this, school.getId(), name, nickname, start, end, position);
    }

    public void insertOnly(int schoolId, String name, String nickname, String start, String end, int position) {
        MPeriod.insertOnly(this, schoolId, name, nickname, start, end, position);
    }

    public void insertOnlyBulk(@NotNull final List<DBPeriod> periods) {
        MPeriod.insertOnlyBulk(this, periods);
    }

    public void update(@NotNull final DBPeriod period, String name, String nickname, String start, String end, int position) {
        MPeriod.update(this, period, name, nickname, start, end, position);
    }

    public List<DBPeriod> getAllFromSchool(@NotNull final DBSchool school) {
        return MPeriod.getAllFromSchool(this, school);
    }

    public List<DBMPeriod> getAllMetadataFromSchool(@NotNull final DBMSchool school){
        return MPeriod.getAllMetadataFromSchool(this, school);
    }

    public void deleteFromSchool(@NotNull final DBSchool school) {
        MPeriod.deleteFromSchool(this, school.getId());
    }

    public void deleteFromSchool(@NotNull final DBMSchool school) {
        MPeriod.deleteFromSchool(this, school);
    }

    public void deleteFromSchool(int schoolId) {
        MPeriod.deleteFromSchool(this, schoolId);
    }

    public static void insertOnly(@NotNull final AbstractModel model, @NotNull final DBSchool school, String name, String nickname, String start, String end, int position) {
        MPeriod.insertOnly(model, school.getId(), name, nickname, start, end, position);
    }

    public static void insertOnly(@NotNull final AbstractModel model, int schoolId, String name, String nickname, String start, String end, int position) {
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("INSERT INTO `active_period`(`id`, `position`, `name`, `nick`, `start`, `end`, `school`) VALUES (NULL, ?, ?, ?, ?, ?, ?)");
            statement.setInt(1, position);
            statement.setString(2, name);
            statement.setString(3, nickname);
            statement.setString(4, start);
            statement.setString(5, end);
            statement.setInt(6, schoolId);
            statement.execute();
            statement.close();
            model.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertOnlyBulk(@NotNull final AbstractModel model, @NotNull final List<DBPeriod> periods) {
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            model.connection.setAutoCommit(false);
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("INSERT INTO `active_period`(`id`, `position`, `name`, `nick`, `start`, `end`, `school`) VALUES (NULL, ?, ?, ?, ?, ?, ?)");
            for (@NotNull final DBPeriod period : periods) {
                statement.setInt(1, period.getPosition());
                statement.setString(2, period.getName());
                statement.setString(3, period.getNickname());
                statement.setString(4, period.getStart().toString());
                statement.setString(5, period.getEnd().toString());
                statement.setInt(6, period.getSchool().getId());
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

    public static void deleteFromSchool(@NotNull final AbstractModel model, @NotNull final DBSchool school) {
        MPeriod.deleteFromSchool(model, school.getId());
    }

    public static void deleteFromSchool(@NotNull final AbstractModel model, @NotNull final DBMSchool school) {
        MPeriod.deleteFromSchool(model, school.getId());
    }

    public static void deleteFromSchool(@NotNull final AbstractModel model, int schoolId) {
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("DELETE FROM `active_period` WHERE `school` = ?");
            statement.setInt(1, schoolId);
            statement.execute();
            statement.close();
            model.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<DBPeriod> getAllFromSchool(@NotNull final AbstractModel model, @NotNull final DBSchool school) {
        @NotNull List<DBPeriod> dbPeriodList = new LinkedList<>();
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `id`, `name`, `nick`, `start`, `end`, `position`, `school` FROM `active_period` WHERE school = ? ORDER BY `position` ASC ");
            statement.setInt(1, school.getId());
            @NotNull final ResultSet result = statement.executeQuery();
            while (result.next()) {
                dbPeriodList.add(
                        new DBPeriod(
                                result.getInt("id"),
                                result.getInt("position"),
                                result.getString("name"),
                                result.getString("nick"),
                                result.getString("start"),
                                result.getString("end"),
                                school
                        ));
            }
            result.close();
            statement.close();
            model.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dbPeriodList;
    }

    public static List<DBMPeriod> getAllMetadataFromSchool(@NotNull final AbstractModel model, @NotNull final DBMSchool school) {
        @NotNull List<DBMPeriod> dbPeriodList = new LinkedList<>();
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `id`, `name`, `nick`, `start`, `end`, `position`  FROM `active_period` WHERE school = ? ORDER BY `position` ASC ");
            statement.setInt(1, school.getId());
            @NotNull final ResultSet result = statement.executeQuery();
            while (result.next()) {
                dbPeriodList.add(
                        new DBMPeriod(
                                result.getInt("id"),
                                result.getInt("position"),
                                result.getString("name"),
                                result.getString("nick"),
                                result.getString("start"),
                                result.getString("end")
                        ));
            }
            result.close();
            statement.close();
            model.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dbPeriodList;
    }

    public static void update(@NotNull final AbstractModel model, @NotNull final DBPeriod period, String name, String nickname, String start, String end, int position) {
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("UPDATE `active_period` SET `position` = ?,  `name` = ?, `nick` = ?, `start` = ?, `end` = ?,  `school` = ? WHERE `id` = ?");
            statement.setInt(1, position);
            statement.setString(2, name);
            statement.setString(3, nickname);
            statement.setString(4, start);
            statement.setString(5, end);
            statement.setInt(6, period.getSchool().getId());
            statement.setInt(7, period.getId());
            statement.execute();
            statement.close();
            model.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DBPeriod getFromMetadata(@NotNull final AbstractModel model, @NotNull final DBMSchool school, @NotNull final DBMPeriod metadata) {
        @Nullable DBPeriod period = null;
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `id`, `name`, `nick`, `start`, `end`, `position`, `school` FROM `active_period` WHERE `id` = ? ORDER BY `position` ASC LIMIT 1");
            statement.setInt(1, metadata.getId());
            @NotNull final ResultSet result = statement.executeQuery();
            while (result.next()) {
                period = new DBPeriod(
                                result.getInt("id"),
                                result.getInt("position"),
                                result.getString("name"),
                                result.getString("nick"),
                                result.getString("start"),
                                result.getString("end"),
                                school
                        );
            }
            result.close();
            statement.close();
            model.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return period;
    }
}
