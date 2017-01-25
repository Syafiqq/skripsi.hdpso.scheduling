package view.classroom;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 25 January 2017, 8:08 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import controller.classroom.CClassroomEditTimeOff;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class IClassroomEditTimeOff extends Application {
    public IClassroomEditTimeOff() {
    }

    public IClassroomEditTimeOff(CClassroomEditTimeOff controller) {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CClassroomEditTimeOff controller) {
        @NotNull final FXMLLoader loader = new FXMLLoader(IClassroomEditTimeOff.class.getResource("/layout/classroom/dialog_classroom_edit_timeoff.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override
    public void start(Stage primaryStage) throws UnsupportedEncodingException, SQLException {
        buildStage(primaryStage, new CClassroomEditTimeOff());
    }

    private void buildStage(Stage primaryStage, CClassroomEditTimeOff controller) {
        try {
            @NotNull final FXMLLoader loader = IClassroomEditTimeOff.load(controller);
            primaryStage.setTitle("Edit TimeOff Ruangan");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        } catch (IOException ignored) {
        }
    }
}
