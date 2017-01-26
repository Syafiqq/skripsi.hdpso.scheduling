package view.lesson;

import controller.lesson.CLessonDetail;
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
 * Date / Time  : 26 January 2017, 7:14 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("unused") public class ILessonDetail extends Application
{
    public ILessonDetail()
    {
    }

    public ILessonDetail(CLessonDetail controller)
    {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CLessonDetail controller)
    {
        @NotNull final FXMLLoader loader = new FXMLLoader(ILessonDetail.class.getResource("/layout/lesson/dialog_lesson_detail.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override
    public void start(Stage primaryStage) throws UnsupportedEncodingException, SQLException
    {
        buildStage(primaryStage, new CLessonDetail());
    }

    private void buildStage(Stage primaryStage, CLessonDetail controller)
    {
        try
        {
            @NotNull final FXMLLoader loader = ILessonDetail.load(controller);
            primaryStage.setTitle("Detail Pelajaran");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        }
        catch(IOException ignored)
        {
        }
    }
}

