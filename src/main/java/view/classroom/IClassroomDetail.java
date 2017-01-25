package view.classroom;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 25 January 2017, 8:08 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import controller.classroom.CClassroomDetail;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class IClassroomDetail extends Application {
    public IClassroomDetail() {
    }

    public IClassroomDetail(CClassroomDetail controller) {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CClassroomDetail controller) {
        @NotNull final FXMLLoader loader = new FXMLLoader(IClassroomDetail.class.getResource("/layout/classroom/dialog_classroom_detail.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override
    public void start(Stage primaryStage) throws UnsupportedEncodingException, SQLException {
        buildStage(primaryStage, new CClassroomDetail());
    }

    private void buildStage(Stage primaryStage, CClassroomDetail controller) {
        try {
            @NotNull final FXMLLoader loader = IClassroomDetail.load(controller);
            primaryStage.setTitle("Detail Ruangan");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        } catch (IOException ignored) {
        }
    }
}
