package view.menu;

import controller.menu.CMDeveloper;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <view.menu> created by : 
 * Name         : syafiq
 * Date / Time  : 01 January 2017, 4:45 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class IMDeveloper extends Application
{
    public IMDeveloper()
    {
    }

    public IMDeveloper(CMDeveloper controller)
    {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CMDeveloper controller)
    {
        @NotNull final FXMLLoader loader = new FXMLLoader(IMDeveloper.class.getResource("/layout/menu_developer.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override public void start(Stage primaryStage)
    {
        this.buildStage(primaryStage, new CMDeveloper());
    }

    private void buildStage(Stage primaryStage, CMDeveloper controller)
    {
        try
        {
            @NotNull final FXMLLoader loader = IMDeveloper.load(controller);
            primaryStage.setTitle("Menu Developer");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        }
        catch(IOException ignored)
        {
        }
    }
}
