package view.parameter;

import controller.parameter.CParameterList;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <view.parameter> created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 26 January 2017, 6:50 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("unused")
public class IParameterList extends Application
{
    public IParameterList()
    {
    }

    public IParameterList(CParameterList controller)
    {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CParameterList controller)
    {
        @NotNull final FXMLLoader loader = new FXMLLoader(IParameterList.class.getResource("/layout/parameter/dialog_parameter_list.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override
    public void start(Stage primaryStage) throws UnsupportedEncodingException, SQLException
    {
        buildStage(primaryStage, new CParameterList());
    }

    private void buildStage(Stage primaryStage, CParameterList controller)
    {
        try
        {
            @NotNull final FXMLLoader loader = IParameterList.load(controller);
            primaryStage.setTitle("Parameter");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        }
        catch(IOException ignored)
        {
        }
    }
}
