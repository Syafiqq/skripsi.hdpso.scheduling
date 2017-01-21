package model.database.model;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 21 January 2017, 6:19 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import model.AbstractModel;
import model.database.component.DBSchool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "unused"})
public class MSchool extends AbstractModel {
    public MSchool(String dbPath) throws SQLException {
        super(dbPath);
    }

    public MSchool(@NotNull AbstractModel model) {
        super(model);
    }

    @Nullable
    public DBSchool insert(String name, String nickname, String address, String academicYear, int semester, int activePeriod, int activeDay) {
        return MSchool.insert(this, name, nickname, address, academicYear, semester, activePeriod, activeDay);
    }

    @Nullable
    public DBSchool select(int schoolID) {
        return MSchool.select(this, schoolID);
    }

    public List<DBSchool> getAll() {
        return MSchool.getAll(this);
    }

    public void delete(@NotNull final DBSchool school) {
        this.delete(school.getId());
    }

    public void delete(int schoolId) {
        MSchool.delete(this, schoolId);
    }

    public void update(@NotNull final DBSchool school, String name, String nickname, String address, String academicYear, int semester, int activePeriod, int activeDay) {
        this.update(school.getId(), name, nickname, address, academicYear, semester, activePeriod, activeDay);
    }

    public void update(int schoolID, String name, String nickname, String address, String academicYear, int semester, int activePeriod, int activeDay) {
        MSchool.update(this, schoolID, name, nickname, address, academicYear, semester, activePeriod, activeDay);
    }

    @Nullable
    public static DBSchool insert(@NotNull final AbstractModel model, String name, String nickname, String address, String academicYear, int semester, int activePeriod, int activeDay) {
        @Nullable DBSchool insertedSchool = null;
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("INSERT INTO `school` (`id`, `name`, `nick`, `address`, `academic_year`, `semester`, `active_period`, `active_day`) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, name);
            statement.setString(2, nickname);
            statement.setString(3, address);
            statement.setString(4, academicYear);
            statement.setInt(5, semester);
            statement.setInt(6, activePeriod);
            statement.setInt(7, activeDay);
            statement.execute();
            statement.close();

            @NotNull final PreparedStatement callStatement = model.connection.prepareStatement("SELECT last_insert_rowid()");
            @NotNull final ResultSet callResult = callStatement.executeQuery();
            callResult.next();
            final int insertedID = callResult.getInt("last_insert_rowid()");
            callResult.close();
            callStatement.close();

            model.close();

            insertedSchool = MSchool.select(model, insertedID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return insertedSchool;
    }

    @Nullable
    public static DBSchool select(@NotNull final AbstractModel model, int schoolID) {
        @Nullable DBSchool school = null;
        if (schoolID > 0) {
            try {
                if (model.isClosed()) {
                    model.reconnect();
                }
                @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `id`, `name`, `nick`, `address`, `academic_year`, `semester`, `active_period`, `active_day` FROM `school` WHERE `id` = ? LIMIT 1");
                statement.setInt(1, schoolID);
                @NotNull final ResultSet result = statement.executeQuery();
                while (result.next()) {
                    school = new DBSchool(
                            result.getInt("id"),
                            result.getString("name"),
                            result.getString("nick"),
                            result.getString("address"),
                            result.getString("academic_year"),
                            result.getInt("semester"),
                            result.getInt("active_period"),
                            result.getInt("active_day")
                    );
                }
                result.close();
                statement.close();
                model.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return school;
    }

    public static List<DBSchool> getAll(@NotNull final AbstractModel model) {
        @NotNull List<DBSchool> dbSchoolList = new LinkedList<>();
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `id`, `name`, `nick`, `address`, `academic_year`, `semester`, `active_period`, `active_day` FROM `school` ORDER BY `id` ASC ");
            @NotNull final ResultSet result = statement.executeQuery();
            while (result.next()) {
                dbSchoolList.add(
                        new DBSchool(
                                result.getInt("id"),
                                result.getString("name"),
                                result.getString("nick"),
                                result.getString("address"),
                                result.getString("academic_year"),
                                result.getInt("semester"),
                                result.getInt("active_period"),
                                result.getInt("active_day")
                        ));
            }
            result.close();
            statement.close();
            model.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dbSchoolList;
    }

    public static void delete(@NotNull final AbstractModel model, @NotNull final DBSchool school) {
        MSchool.delete(model, school.getId());
    }

    public static void delete(@NotNull final AbstractModel model, int schoolId) {
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("DELETE FROM `school` WHERE `id`=?");
            statement.setInt(1, schoolId);
            statement.execute();
            statement.close();
            model.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void update(@NotNull final AbstractModel model, @NotNull final DBSchool school, String name, String nickname, String address, String academicYear, int semester, int activePeriod, int activeDay) {
        MSchool.update(model, school.getId(), name, nickname, address, academicYear, semester, activePeriod, activeDay);
    }

    public static void update(@NotNull final AbstractModel model, int schoolID, String name, String nickname, String address, String academicYear, int semester, int activePeriod, int activeDay) {
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("UPDATE `school` SET `name` = ?, `nick` = ?, `address` = ?, `academic_year` = ?, `semester` = ?, `active_period` = ?, `active_day` = ? WHERE `id` = ?");
            statement.setString(1, name);
            statement.setString(2, nickname);
            statement.setString(3, address);
            statement.setString(4, academicYear);
            statement.setInt(5, semester);
            statement.setInt(6, activePeriod);
            statement.setInt(7, activeDay);
            statement.setInt(8, schoolID);
            statement.execute();
            statement.close();
            model.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}