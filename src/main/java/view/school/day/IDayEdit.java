package view.school.day;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 21 January 2017, 12:03 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import controller.school.day.CDayEdit;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class IDayEdit extends Application {
    public IDayEdit() {
    }

    public IDayEdit(CDayEdit controller) {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CDayEdit controller) {
        @NotNull final FXMLLoader loader = new FXMLLoader(IDayEdit.class.getResource("/layout/school/day/dialog_day_edit.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override
    public void start(Stage primaryStage) throws UnsupportedEncodingException, SQLException {
        buildStage(primaryStage, new CDayEdit());
    }

    private void buildStage(Stage primaryStage, CDayEdit controller) {
        try {
            @NotNull final FXMLLoader loader = IDayEdit.load(controller);
            primaryStage.setTitle("Edit Jadwal");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        } catch (IOException ignored) {
        }
    }
}
