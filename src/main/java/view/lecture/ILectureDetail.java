package view.lecture;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 25 January 2017, 9:09 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import controller.lecture.CLectureDetail;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class ILectureDetail extends Application {
    public ILectureDetail() {
    }

    public ILectureDetail(CLectureDetail controller) {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CLectureDetail controller) {
        @NotNull final FXMLLoader loader = new FXMLLoader(ILectureDetail.class.getResource("/layout/lecture/dialog_lecture_detail.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override
    public void start(Stage primaryStage) throws UnsupportedEncodingException, SQLException {
        buildStage(primaryStage, new CLectureDetail());
    }

    private void buildStage(Stage primaryStage, CLectureDetail controller) {
        try {
            @NotNull final FXMLLoader loader = ILectureDetail.load(controller);
            primaryStage.setTitle("Detail Dosen");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        } catch (IOException ignored) {
        }
    }
}
