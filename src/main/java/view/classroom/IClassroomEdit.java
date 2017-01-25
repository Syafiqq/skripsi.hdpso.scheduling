package view.classroom;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 25 January 2017, 8:08 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import controller.classroom.CClassroomEdit;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class IClassroomEdit extends Application {
    public IClassroomEdit() {
    }

    public IClassroomEdit(CClassroomEdit controller) {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CClassroomEdit controller) {
        @NotNull final FXMLLoader loader = new FXMLLoader(IClassroomEdit.class.getResource("/layout/classroom/dialog_classroom_edit.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override
    public void start(Stage primaryStage) throws UnsupportedEncodingException, SQLException {
        buildStage(primaryStage, new CClassroomEdit());
    }

    private void buildStage(Stage primaryStage, CClassroomEdit controller) {
        try {
            @NotNull final FXMLLoader loader = IClassroomEdit.load(controller);
            primaryStage.setTitle("Edit Ruangan");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        } catch (IOException ignored) {
        }
    }
}
