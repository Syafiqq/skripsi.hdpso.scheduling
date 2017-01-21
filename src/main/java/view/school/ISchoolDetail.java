package view.school;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 20 January 2017, 7:03 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import controller.school.CSchoolDetail;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

@SuppressWarnings("unused")
public class ISchoolDetail extends Application {
    public ISchoolDetail() {
    }

    public ISchoolDetail(CSchoolDetail controller) {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CSchoolDetail controller) {
        @NotNull final FXMLLoader loader = new FXMLLoader(ISchoolDetail.class.getResource("/layout/school/dialog_school_detail.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override
    public void start(Stage primaryStage) {
        buildStage(primaryStage, new CSchoolDetail());
    }

    private void buildStage(Stage primaryStage, CSchoolDetail controller) {
        try {
            @NotNull final FXMLLoader loader = ISchoolDetail.load(controller);
            primaryStage.setTitle("Detail Jadwal");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        } catch (IOException ignored) {
        }
    }
}
