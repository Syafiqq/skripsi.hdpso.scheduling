package view.subject;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 24 January 2017, 6:45 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import controller.subject.CSubjectEditTimeOff;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class ISubjectEditTimeOff extends Application {
    public ISubjectEditTimeOff() {
    }

    public ISubjectEditTimeOff(CSubjectEditTimeOff controller) {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CSubjectEditTimeOff controller) {
        @NotNull final FXMLLoader loader = new FXMLLoader(ISubjectEditTimeOff.class.getResource("/layout/subject/dialog_subject_edit_timeoff.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override
    public void start(Stage primaryStage) throws UnsupportedEncodingException, SQLException {
        buildStage(primaryStage, new CSubjectEditTimeOff());
    }

    private void buildStage(Stage primaryStage, CSubjectEditTimeOff controller) {
        try {
            @NotNull final FXMLLoader loader = ISubjectEditTimeOff.load(controller);
            primaryStage.setTitle("EditTimeOff Mata Kuliah");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        } catch (IOException ignored) {
        }
    }
}