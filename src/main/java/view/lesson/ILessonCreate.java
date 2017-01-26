package view.lesson;

import controller.lesson.CLessonCreate;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <view.lesson> created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 25 January 2017, 8:14 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("unused") public class ILessonCreate extends Application
{
    public ILessonCreate()
    {
    }

    public ILessonCreate(CLessonCreate controller)
    {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CLessonCreate controller)
    {
        @NotNull final FXMLLoader loader = new FXMLLoader(ILessonCreate.class.getResource("/layout/lesson/dialog_lesson_create.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override
    public void start(Stage primaryStage) throws UnsupportedEncodingException, SQLException
    {
        buildStage(primaryStage, new CLessonCreate());
    }

    private void buildStage(Stage primaryStage, CLessonCreate controller)
    {
        try
        {
            @NotNull final FXMLLoader loader = ILessonCreate.load(controller);
            primaryStage.setTitle("Tambah Pelajaran");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        }
        catch(IOException ignored)
        {
        }
    }
}
