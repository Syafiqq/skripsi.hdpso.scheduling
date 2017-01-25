package view.lecture;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 25 January 2017, 9:09 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import controller.lecture.CLectureCreate;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class ILectureCreate  extends Application {
    public ILectureCreate() {
    }

    public ILectureCreate(CLectureCreate controller) {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CLectureCreate controller) {
        @NotNull final FXMLLoader loader = new FXMLLoader(ILectureCreate.class.getResource("/layout/lecture/dialog_lecture_create.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override
    public void start(Stage primaryStage) throws UnsupportedEncodingException, SQLException {
        buildStage(primaryStage, new CLectureCreate());
    }

    private void buildStage(Stage primaryStage, CLectureCreate controller) {
        try {
            @NotNull final FXMLLoader loader = ILectureCreate.load(controller);
            primaryStage.setTitle("Tambah Dosen");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        } catch (IOException ignored) {
        }
    }
}