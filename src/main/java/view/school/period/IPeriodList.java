package view.school.period;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 21 January 2017, 1:17 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import controller.school.period.CPeriodList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class IPeriodList extends Application {
    public IPeriodList() {
    }

    public IPeriodList(CPeriodList controller) {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CPeriodList controller) {
        @NotNull final FXMLLoader loader = new FXMLLoader(IPeriodList.class.getResource("/layout/school/period/dialog_period_list.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override
    public void start(Stage primaryStage) throws UnsupportedEncodingException, SQLException {
        buildStage(primaryStage, new CPeriodList());
    }

    private void buildStage(Stage primaryStage, CPeriodList controller) {
        try {
            @NotNull final FXMLLoader loader = IPeriodList.load(controller);
            primaryStage.setTitle("Detail Jadwal");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        } catch (IOException ignored) {
        }
    }
}
