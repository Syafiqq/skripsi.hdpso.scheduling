package view.menu;

import controller.menu.CMCategory;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <view.menu> created by : 
 * Name         : syafiq
 * Date / Time  : 01 January 2017, 4:30 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class IMCategory extends Application
{
    public IMCategory()
    {
    }

    public IMCategory(CMCategory controller)
    {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CMCategory controller)
    {
        @NotNull final FXMLLoader loader = new FXMLLoader(IMCategory.class.getResource("/layout/menu_category.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override public void start(Stage primaryStage)
    {
        this.buildStage(primaryStage, new CMCategory());
    }

    private void buildStage(Stage primaryStage, CMCategory controller)
    {
        try
        {
            @NotNull final FXMLLoader loader = IMCategory.load(controller);
            primaryStage.setTitle("Menu Cetegory");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        }
        catch(IOException ignored)
        {
        }
    }
}