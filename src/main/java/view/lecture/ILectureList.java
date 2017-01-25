package view.lecture;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 25 January 2017, 9:10 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import controller.lecture.CLectureList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class ILectureList  extends Application {
    public ILectureList() {
    }

    public ILectureList(CLectureList controller) {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CLectureList controller) {
        @NotNull final FXMLLoader loader = new FXMLLoader(ILectureList.class.getResource("/layout/lecture/dialog_lecture_list.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override
    public void start(Stage primaryStage) throws UnsupportedEncodingException, SQLException {
        buildStage(primaryStage, new CLectureList());
    }

    private void buildStage(Stage primaryStage, CLectureList controller) {
        try {
            @NotNull final FXMLLoader loader = ILectureList.load(controller);
            primaryStage.setTitle("Daftar Dosen");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        } catch (IOException ignored) {
        }
    }
}
