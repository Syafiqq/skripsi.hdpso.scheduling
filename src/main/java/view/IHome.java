package view;

import controller.CHome;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <view> created by : 
 * Name         : syafiq
 * Date / Time  : 01 January 2017, 2:25 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class IHome extends Application
{
    public IHome()
    {
    }

    public IHome(CHome controller)
    {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CHome controller)
    {
        @NotNull final FXMLLoader loader = new FXMLLoader(IHome.class.getResource("/layout/home.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override public void start(Stage primaryStage)
    {
        buildStage(primaryStage, new CHome());
    }

    private void buildStage(Stage primaryStage, CHome controller)
    {
        try
        {
            @NotNull final FXMLLoader loader = IHome.load(controller);
            primaryStage.setTitle("Home");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        }
        catch(IOException ignored)
        {
        }
    }
}
