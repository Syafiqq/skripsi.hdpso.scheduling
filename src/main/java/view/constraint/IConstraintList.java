package view.constraint;

import controller.constraint.CConstraintList;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <view.constraint> created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 28 January 2017, 9:51 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("unused")
public class IConstraintList extends Application
{
    public IConstraintList()
    {
    }

    public IConstraintList(CConstraintList controller)
    {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CConstraintList controller)
    {
        @NotNull final FXMLLoader loader = new FXMLLoader(IConstraintList.class.getResource("/layout/constraint/dialog_constraint_list.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override
    public void start(Stage primaryStage) throws UnsupportedEncodingException, SQLException
    {
        buildStage(primaryStage, new CConstraintList());
    }

    private void buildStage(Stage primaryStage, CConstraintList controller)
    {
        try
        {
            @NotNull final FXMLLoader loader = IConstraintList.load(controller);
            primaryStage.setTitle("Constraint");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        }
        catch(IOException ignored)
        {
        }
    }
}

