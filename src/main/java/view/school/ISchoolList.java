package view.school;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 16 January 2017, 6:47 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import controller.school.CSchoolList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

@SuppressWarnings("unused")
public class ISchoolList extends Application {
    public ISchoolList() {
    }

    public ISchoolList(CSchoolList controller) {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CSchoolList controller) {
        @NotNull final FXMLLoader loader = new FXMLLoader(ISchoolList.class.getResource("/layout/school/dialog_school_list.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override
    public void start(Stage primaryStage) {
        buildStage(primaryStage, new CSchoolList());
    }

    private void buildStage(Stage primaryStage, CSchoolList controller) {
        try {
            @NotNull final FXMLLoader loader = ISchoolList.load(controller);
            primaryStage.setTitle("Utama");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        } catch (IOException ignored) {
        }
    }
}

