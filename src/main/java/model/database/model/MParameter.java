package model.database.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.AbstractModel;
import model.database.component.DBParameter;
import model.database.component.metadata.DBMSchool;
import model.method.pso.hdpso.component.Setting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.model> created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 26 January 2017, 6:58 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("unused") public class MParameter extends AbstractModel
{
    public MParameter(@NotNull String dbPath) throws SQLException
    {
        super(dbPath);
    }

    public MParameter(@NotNull AbstractModel model)
    {
        super(model);
    }

    public static void insert(@NotNull final AbstractModel model, @NotNull final DBMSchool school, @NotNull final DBParameter parameter)
    {
        try
        {
            if(model.isClosed())
            {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("INSERT INTO param_setting(`id`, `glob_min`, `glob_max`, `bloc_min`, `bloc_max`, `brand_min`, `brand_max`, `iteration`, `particle`, `processor`, `method`, `is_multiprocess`, `school`) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setDouble(1, parameter.getgLobMin());
            statement.setDouble(2, parameter.getgLobMax());
            statement.setDouble(3, parameter.getbLocMin());
            statement.setDouble(4, parameter.getbLocMax());
            statement.setDouble(5, parameter.getbRandMin());
            statement.setDouble(6, parameter.getbRandMax());
            statement.setInt(7, parameter.getIteration());
            statement.setInt(8, parameter.getParticle());
            statement.setInt(9, parameter.getProcessor());
            statement.setInt(10, Setting.getVelocity(parameter.getMethod()));
            statement.setInt(11, !parameter.isMultiThread() ? 1 : 0);
            statement.setInt(12, school.getId());
            statement.execute();
            statement.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static DBParameter getFromSchool(@NotNull final AbstractModel model, @NotNull final DBMSchool school)
    {
        @Nullable DBParameter parameter = null;
        try
        {
            if(model.isClosed())
            {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `id`, `glob_min`, `glob_max`, `bloc_min`, `bloc_max`, `brand_min`, `brand_max`, `iteration`, `particle`, `processor`, `method`, `is_multiprocess`, `school` FROM `param_setting` WHERE `school` = ? LIMIT 1");
            statement.setInt(1, school.getId());
            @NotNull final ResultSet result = statement.executeQuery();
            while(result.next())
            {
                assert school.getId() == result.getInt("school");
                parameter = new DBParameter(
                        result.getInt("id"),
                        school,
                        result.getDouble("glob_min"),
                        result.getDouble("glob_max"),
                        result.getDouble("bloc_min"),
                        result.getDouble("bloc_max"),
                        result.getDouble("brand_min"),
                        result.getDouble("brand_max"),
                        result.getInt("iteration"),
                        result.getInt("particle"),
                        result.getInt("processor"),
                        Setting.getVelocity(result.getInt("method")),
                        result.getInt("is_multiprocess") != 0
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
        return parameter;
    }

    public static void deleteFromSchool(@NotNull final AbstractModel model, @NotNull final DBMSchool school)
    {
        try
        {
            if(model.isClosed())
            {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("DELETE FROM `param_setting` WHERE `school` = ?");
            statement.setInt(1, school.getId());
            statement.execute();
            statement.close();
            model.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void update(@NotNull final AbstractModel model, @NotNull final DBParameter parameter)
    {
        try
        {
            if(model.isClosed())
            {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("UPDATE param_setting SET `glob_min` = ?, `glob_max` = ?, `bloc_min` = ?, `bloc_max` = ?, `brand_min` = ?, `brand_max` = ?, `iteration` = ?, `particle` = ?, `processor` = ?, `method` = ?, `is_multiprocess` = ? WHERE `id` = ?");
            statement.setDouble(1, parameter.getgLobMin());
            statement.setDouble(2, parameter.getgLobMax());
            statement.setDouble(3, parameter.getbLocMin());
            statement.setDouble(4, parameter.getbLocMax());
            statement.setDouble(5, parameter.getbRandMin());
            statement.setDouble(6, parameter.getbRandMax());
            statement.setInt(7, parameter.getIteration());
            statement.setInt(8, parameter.getParticle());
            statement.setInt(9, parameter.getProcessor());
            statement.setInt(10, Setting.getVelocity(parameter.getMethod()));
            statement.setInt(11, !parameter.isMultiThread() ? 1 : 0);
            statement.setInt(12, parameter.getId());
            statement.execute();
            statement.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}
