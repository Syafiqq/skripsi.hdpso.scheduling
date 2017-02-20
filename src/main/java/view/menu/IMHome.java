package view.menu;

import controller.menu.CMHome;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <view> created by : 
 * Name         : syafiq
 * Date / Time  : 01 January 2017, 3:24 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class IMHome extends Application
{
    public IMHome()
    {
    }

    public IMHome(CMHome controller)
    {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CMHome controller)
    {
        @NotNull final FXMLLoader loader = new FXMLLoader(IMHome.class.getResource("/layout/menu_home.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override public void start(Stage primaryStage)
    {
        this.buildStage(primaryStage, new CMHome());
    }

    private void buildStage(Stage primaryStage, CMHome controller)
    {
        try
        {
            @NotNull final FXMLLoader loader = IMHome.load(controller);
            primaryStage.setTitle("Menu Home");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        }
        catch(IOException ignored)
        {
        }
    }
}