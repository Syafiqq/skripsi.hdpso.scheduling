package model.database.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.AbstractModel;
import model.database.component.DBConstraint;
import model.database.component.metadata.DBMSchool;
import model.util.Converter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.model> created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 28 January 2017, 9:04 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("unused") public class MConstraint extends AbstractModel
{
    public MConstraint(@NotNull String dbPath) throws SQLException
    {
        super(dbPath);
    }

    public MConstraint(@NotNull AbstractModel model)
    {
        super(model);
    }

    public static void insert(@NotNull final AbstractModel model, @NotNull final DBMSchool school, @NotNull final DBConstraint constraint)
    {
        try
        {
            if(model.isClosed())
            {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("INSERT INTO `constraint_setting`(`id`, `subject`, `issubject`, `lecture`, `islecture`, `klass`, `isklass`, `classroom`, `isclassroom`, `lplacement`, `islplacement`, `cplacement`, `iscplacement`, `link`, `islink`, `allow`, `isallow`, `school`) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setDouble(1, constraint.getSubject());
            statement.setInt(2, Converter.integerToBooleanInteger(constraint.isSubject()));
            statement.setDouble(3, constraint.getLecture());
            statement.setInt(4, Converter.integerToBooleanInteger(constraint.isLecture()));
            statement.setDouble(5, constraint.getKlass());
            statement.setInt(6, Converter.integerToBooleanInteger(constraint.isKlass()));
            statement.setDouble(7, constraint.getClassroom());
            statement.setInt(8, Converter.integerToBooleanInteger(constraint.isClassroom()));
            statement.setDouble(9, constraint.getLPlacement());
            statement.setInt(10, Converter.integerToBooleanInteger(constraint.isLPlacement()));
            statement.setDouble(11, constraint.getCPlacement());
            statement.setInt(12, Converter.integerToBooleanInteger(constraint.isCPlacement()));
            statement.setDouble(13, constraint.getLink());
            statement.setInt(14, Converter.integerToBooleanInteger(constraint.isLink()));
            statement.setDouble(15, constraint.getAllow());
            statement.setInt(16, Converter.integerToBooleanInteger(constraint.isAllow()));
            statement.setInt(17, school.getId());
            statement.execute();
            statement.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static DBConstraint getFromSchool(@NotNull final AbstractModel model, @NotNull final DBMSchool school)
    {
        @Nullable DBConstraint constraint = null;
        try
        {
            if(model.isClosed())
            {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("SELECT `id`, `subject`, `issubject`, `lecture`, `islecture`, `klass`, `isklass`, `classroom`, `isclassroom`, `lplacement`, `islplacement`, `cplacement`, `iscplacement`, `link`, `islink`, `allow`, `isallow`, `school` FROM `constraint_setting` WHERE `school` = ? LIMIT 1");
            statement.setInt(1, school.getId());
            @NotNull final ResultSet result = statement.executeQuery();
            while(result.next())
            {
                assert school.getId() == result.getInt("school");
                constraint = new DBConstraint(
                        result.getInt("id"),
                        school,
                        result.getDouble("subject"),
                        result.getDouble("lecture"),
                        result.getDouble("klass"),
                        result.getDouble("classroom"),
                        result.getDouble("lplacement"),
                        result.getDouble("cplacement"),
                        result.getDouble("link"),
                        result.getDouble("allow"),
                        Converter.booleanIntegerToBoolean(result.getInt("issubject")),
                        Converter.booleanIntegerToBoolean(result.getInt("islecture")),
                        Converter.booleanIntegerToBoolean(result.getInt("isklass")),
                        Converter.booleanIntegerToBoolean(result.getInt("isclassroom")),
                        Converter.booleanIntegerToBoolean(result.getInt("islplacement")),
                        Converter.booleanIntegerToBoolean(result.getInt("iscplacement")),
                        Converter.booleanIntegerToBoolean(result.getInt("islink")),
                        Converter.booleanIntegerToBoolean(result.getInt("isallow"))
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
        return constraint;
    }

    public static void deleteFromSchool(@NotNull final AbstractModel model, @NotNull final DBMSchool school)
    {
        try
        {
            if(model.isClosed())
            {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("DELETE FROM `constraint_setting` WHERE `school` = ?");
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

    public static void update(@NotNull final AbstractModel model, @NotNull final DBConstraint constraint)
    {
        try
        {
            if(model.isClosed())
            {
                model.reconnect();
            }
            @NotNull final PreparedStatement statement = model.connection.prepareStatement("UPDATE `constraint_setting` SET `subject` = ?, `issubject` = ?, `lecture` = ?, `islecture` = ?, `klass` = ?, `isklass` = ?, `classroom` = ?, `isclassroom` = ?, `lplacement` = ?, `islplacement` = ?, `cplacement` = ?, `iscplacement` = ?, `link` = ?, `islink` = ?, `allow` = ?, `isallow` = ? WHERE `id` = ?");
            statement.setDouble(1, constraint.getSubject());
            statement.setInt(2, Converter.integerToBooleanInteger(constraint.isSubject()));
            statement.setDouble(3, constraint.getLecture());
            statement.setInt(4, Converter.integerToBooleanInteger(constraint.isLecture()));
            statement.setDouble(5, constraint.getKlass());
            statement.setInt(6, Converter.integerToBooleanInteger(constraint.isKlass()));
            statement.setDouble(7, constraint.getClassroom());
            statement.setInt(8, Converter.integerToBooleanInteger(constraint.isClassroom()));
            statement.setDouble(9, constraint.getLPlacement());
            statement.setInt(10, Converter.integerToBooleanInteger(constraint.isLPlacement()));
            statement.setDouble(11, constraint.getCPlacement());
            statement.setInt(12, Converter.integerToBooleanInteger(constraint.isCPlacement()));
            statement.setDouble(13, constraint.getLink());
            statement.setInt(14, Converter.integerToBooleanInteger(constraint.isLink()));
            statement.setDouble(15, constraint.getAllow());
            statement.setInt(16, Converter.integerToBooleanInteger(constraint.isAllow()));
            statement.setInt(17, constraint.getId());
            statement.execute();
            statement.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}
