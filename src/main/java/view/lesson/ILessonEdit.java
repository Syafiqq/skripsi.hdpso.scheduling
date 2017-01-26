package view.lesson;

import controller.lesson.CLessonEdit;
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
 * Date / Time  : 26 January 2017, 7:57 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("unused") public class ILessonEdit extends Application
{
    public ILessonEdit()
    {
    }

    public ILessonEdit(CLessonEdit controller)
    {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CLessonEdit controller)
    {
        @NotNull final FXMLLoader loader = new FXMLLoader(ILessonEdit.class.getResource("/layout/lesson/dialog_lesson_edit.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override
    public void start(Stage primaryStage) throws UnsupportedEncodingException, SQLException
    {
        buildStage(primaryStage, new CLessonEdit());
    }

    private void buildStage(Stage primaryStage, CLessonEdit controller)
    {
        try
        {
            @NotNull final FXMLLoader loader = ILessonEdit.load(controller);
            primaryStage.setTitle("Edit Pelajaran");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        }
        catch(IOException ignored)
        {
        }
    }
}

