package model.database.model;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 23 January 2017, 12:28 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import model.AbstractModel;
import model.database.component.DBAvailability;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unused")
public class MAvailability extends AbstractModel {
    public MAvailability(@NotNull String dbPath) throws SQLException {
        super(dbPath);
    }

    public MAvailability(@NotNull AbstractModel model) {
        super(model);
    }

    public static List<DBAvailability> getAll(@NotNull final AbstractModel model) {
        @NotNull List<DBAvailability> list = new LinkedList<>();
        try {
            if (model.isClosed()) {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `id`, `name`, `value` FROM availability ORDER BY `id` ASC");
            @NotNull final ResultSet result = statement.executeQuery();
            while (result.next()) {
                list.add(
                        new DBAvailability(
                                result.getInt("id"),
                                result.getString("name"),
                                Double.parseDouble(result.getString("value"))
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
}
