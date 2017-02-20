package view.school;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 16 January 2017, 4:56 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import controller.school.CSchoolNew;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

@SuppressWarnings("unused")
public class ISchoolNew extends Application {
    public ISchoolNew() {
    }

    public ISchoolNew(CSchoolNew controller) {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CSchoolNew controller) {
        @NotNull final FXMLLoader loader = new FXMLLoader(ISchoolNew.class.getResource("/layout/school/dialog_school_create.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override
    public void start(Stage primaryStage) {
        buildStage(primaryStage, new CSchoolNew());
    }

    private void buildStage(Stage primaryStage, CSchoolNew controller) {
        try {
            @NotNull final FXMLLoader loader = ISchoolNew.load(controller);
            primaryStage.setTitle("Utama");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        } catch (IOException ignored) {
        }
    }
}
