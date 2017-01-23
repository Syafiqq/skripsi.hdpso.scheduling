package view.school.day;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 21 January 2017, 11:01 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import controller.school.day.CDayList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class IDayList extends Application {
    public IDayList() {
    }

    public IDayList(CDayList controller) {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CDayList controller) {
        @NotNull final FXMLLoader loader = new FXMLLoader(IDayList.class.getResource("/layout/school/day/dialog_day_list.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override
    public void start(Stage primaryStage) throws UnsupportedEncodingException, SQLException {
        buildStage(primaryStage, new CDayList());
    }

    private void buildStage(Stage primaryStage, CDayList controller) {
        try {
            @NotNull final FXMLLoader loader = IDayList.load(controller);
            primaryStage.setTitle("Detail Jadwal");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        } catch (IOException ignored) {
        }
    }
}
