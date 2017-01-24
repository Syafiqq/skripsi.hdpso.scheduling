package view.klass;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 24 January 2017, 9:12 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import controller.klass.CClassEditTimeOff;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class IClassEditTimeOff extends Application {
    public IClassEditTimeOff() {
    }

    public IClassEditTimeOff(CClassEditTimeOff controller) {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CClassEditTimeOff controller) {
        @NotNull final FXMLLoader loader = new FXMLLoader(IClassEditTimeOff.class.getResource("/layout/klass/dialog_klass_edit_timeoff.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override
    public void start(Stage primaryStage) throws UnsupportedEncodingException, SQLException {
        buildStage(primaryStage, new CClassEditTimeOff());
    }

    private void buildStage(Stage primaryStage, CClassEditTimeOff controller) {
        try {
            @NotNull final FXMLLoader loader = IClassEditTimeOff.load(controller);
            primaryStage.setTitle("Edit TimeOff Kelas");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        } catch (IOException ignored) {
        }
    }
}