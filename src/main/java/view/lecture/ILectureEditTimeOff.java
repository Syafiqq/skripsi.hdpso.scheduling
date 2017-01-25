package view.lecture;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 25 January 2017, 9:10 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */


import controller.lecture.CLectureEditTimeOff;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class ILectureEditTimeOff extends Application {
    public ILectureEditTimeOff() {
    }

    public ILectureEditTimeOff(CLectureEditTimeOff controller) {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CLectureEditTimeOff controller) {
        @NotNull final FXMLLoader loader = new FXMLLoader(ILectureEditTimeOff.class.getResource("/layout/lecture/dialog_lecture_edit_timeoff.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override
    public void start(Stage primaryStage) throws UnsupportedEncodingException, SQLException {
        buildStage(primaryStage, new CLectureEditTimeOff());
    }

    private void buildStage(Stage primaryStage, CLectureEditTimeOff controller) {
        try {
            @NotNull final FXMLLoader loader = ILectureEditTimeOff.load(controller);
            primaryStage.setTitle("Edit TimeOff Dosen");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        } catch (IOException ignored) {
        }
    }
}
