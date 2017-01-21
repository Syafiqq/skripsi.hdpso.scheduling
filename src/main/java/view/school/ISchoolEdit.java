package view.school;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 20 January 2017, 8:05 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import controller.school.CSchoolEdit;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

@SuppressWarnings("unused")
public class ISchoolEdit extends Application {
    public ISchoolEdit() {
    }

    public ISchoolEdit(CSchoolEdit controller) {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CSchoolEdit controller) {
        @NotNull final FXMLLoader loader = new FXMLLoader(ISchoolEdit.class.getResource("/layout/school/dialog_school_edit.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override
    public void start(Stage primaryStage) {
        buildStage(primaryStage, new CSchoolEdit());
    }

    private void buildStage(Stage primaryStage, CSchoolEdit controller) {
        try {
            @NotNull final FXMLLoader loader = ISchoolEdit.load(controller);
            primaryStage.setTitle("Edit Jadwal");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        } catch (IOException ignored) {
        }
    }
}
