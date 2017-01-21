package model.database.model;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 21 January 2017, 8:01 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import model.AbstractModel;
import model.database.component.DBDay;
import model.database.component.DBSchool;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings({"unused", "WeakerAccess"})
public class MDay extends AbstractModel {
    public MDay(@NotNull String dbPath) throws SQLException {
        super(dbPath);
    }

    public MDay(@NotNull AbstractModel model) {
        super(model);
    }

    public void insertOnly(@NotNull final DBSchool school, String name, String nickname, int position) {
        this.insertOnly(school.getId(), name, nickname, position);
    }

    public void insertOnly(int schoolId, String name, String nickname, int position) {
        MDay.insertOnly(this, schoolId, name, nickname, position);
    }

    public void insertOnlyBulk(@NotNull final List<DBDay> days) {
        MDay.insertOnlyBulk(this, days);
    }

    public void update(final @NotNull DBDay day, String name, String nickname, int position) {
        MDay.update(this, day, name, nickname, position);
    }

    public List<DBDay> getAllFromSchool(@NotNull final DBSchool school) {
        return MDay.getAllFromSchool(this, school);
    }

    public void deleteFromSchool(@NotNull final DBSchool school) {
        this.deleteFromSchool(school.getId());
    }

    public void deleteFromSchool(int schoolId) {
        MDay.deleteFromSchool(this, schoolId);
    }

    public static void insertOnly(@NotNull final AbstractModel model, @NotNull final DBSchool school, String name, String nickname, int position) {
        MDay.insertOnly(model, school.getId(), name, nickname, position);
    }

    public static void insertOnly(@NotNull final AbstractModel model, int schoolId, String name, String nickname, int position) {
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("INSERT INTO `active_day`(`id`, `position`, `name`, `nick`, `school`) VALUES (NULL, ?, ?, ?, ?)");
            statement.setInt(1, position);
            statement.setString(2, name);
            statement.setString(3, nickname);
            statement.setInt(4, schoolId);
            statement.execute();
            statement.close();
            model.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertOnlyBulk(@NotNull final AbstractModel model, @NotNull final List<DBDay> days) {
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            model.connection.setAutoCommit(false);
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("INSERT INTO `active_day`(`id`, `position`, `name`, `nick`, `school`) VALUES (NULL, ?, ?, ?, ?)");
            for (@NotNull final DBDay day : days) {
                statement.setInt(1, day.getPosition());
                statement.setString(2, day.getName());
                statement.setString(3, day.getNickname());
                statement.setInt(4, day.getSchool().getId());
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
        MDay.deleteFromSchool(model, school.getId());
    }

    public static void deleteFromSchool(@NotNull final AbstractModel model, int schoolId) {
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("DELETE FROM `active_day` WHERE `school` = ?");
            statement.setInt(1, schoolId);
            statement.execute();
            statement.close();
            model.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static List<DBDay> getAllFromSchool(@NotNull final AbstractModel model, @NotNull DBSchool school) {
        @NotNull List<DBDay> dbDayList = new LinkedList<>();
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `id`, `name`, `nick`, `position`, `school` FROM active_day WHERE school = ? ORDER BY `position` ASC ");
            statement.setInt(1, school.getId());
            @NotNull final ResultSet result = statement.executeQuery();
            while (result.next()) {
                dbDayList.add(
                        new DBDay(
                                result.getInt("id"),
                                result.getInt("position"),
                                result.getString("name"),
                                result.getString("nick"),
                                school
                        ));
            }
            result.close();
            statement.close();
            model.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dbDayList;
    }

    private static void update(@NotNull final AbstractModel model, @NotNull final DBDay day, String name, String nickname, int position) {
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("UPDATE `active_day` SET `position` = ?, `name` = ?, `nick` = ?, `school` = ? WHERE `id` = ?");
            statement.setInt(1, position);
            statement.setString(2, name);
            statement.setString(3, nickname);
            statement.setInt(4, day.getSchool().getId());
            statement.setInt(5, day.getId());
            statement.execute();
            statement.close();
            model.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
