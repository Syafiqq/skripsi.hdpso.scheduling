package view.lesson;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 25 January 2017, 1:35 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import controller.lesson.CLessonList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class ILessonList extends Application {
    public ILessonList() {
    }

    public ILessonList(CLessonList controller) {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CLessonList controller) {
        @NotNull final FXMLLoader loader = new FXMLLoader(ILessonList.class.getResource("/layout/lesson/dialog_lesson_list.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override
    public void start(Stage primaryStage) throws UnsupportedEncodingException, SQLException {
        buildStage(primaryStage, new CLessonList());
    }

    private void buildStage(Stage primaryStage, CLessonList controller) {
        try {
            @NotNull final FXMLLoader loader = ILessonList.load(controller);
            primaryStage.setTitle("Daftar Pelajaran");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        } catch (IOException ignored) {
        }
    }
}