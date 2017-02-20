package view.classroom;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 25 January 2017, 8:08 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import controller.classroom.CClassroomCreate;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class IClassroomCreate  extends Application {
    public IClassroomCreate() {
    }

    public IClassroomCreate(CClassroomCreate controller) {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CClassroomCreate controller) {
        @NotNull final FXMLLoader loader = new FXMLLoader(IClassroomCreate.class.getResource("/layout/classroom/dialog_classroom_create.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override
    public void start(Stage primaryStage) throws UnsupportedEncodingException, SQLException {
        buildStage(primaryStage, new CClassroomCreate());
    }

    private void buildStage(Stage primaryStage, CClassroomCreate controller) {
        try {
            @NotNull final FXMLLoader loader = IClassroomCreate.load(controller);
            primaryStage.setTitle("Tambah Ruangan");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        } catch (IOException ignored) {
        }
    }
}

