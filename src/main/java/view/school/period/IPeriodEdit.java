package view.school.period;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 21 January 2017, 1:57 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import controller.school.period.CPeriodEdit;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class IPeriodEdit extends Application {
    public IPeriodEdit() {
    }

    public IPeriodEdit(CPeriodEdit controller) {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CPeriodEdit controller) {
        @NotNull final FXMLLoader loader = new FXMLLoader(IPeriodEdit.class.getResource("/layout/school/period/dialog_period_edit.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override
    public void start(Stage primaryStage) throws UnsupportedEncodingException, SQLException {
        buildStage(primaryStage, new CPeriodEdit());
    }

    private void buildStage(Stage primaryStage, CPeriodEdit controller) {
        try {
            @NotNull final FXMLLoader loader = IPeriodEdit.load(controller);
            primaryStage.setTitle("Edit Period");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        } catch (IOException ignored) {
        }
    }
}
