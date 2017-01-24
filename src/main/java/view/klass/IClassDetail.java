package view.klass;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 24 January 2017, 9:12 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import controller.klass.CClassDetail;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class IClassDetail extends Application {
    public IClassDetail() {
    }

    public IClassDetail(CClassDetail controller) {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CClassDetail controller) {
        @NotNull final FXMLLoader loader = new FXMLLoader(IClassDetail.class.getResource("/layout/klass/dialog_klass_detail.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override
    public void start(Stage primaryStage) throws UnsupportedEncodingException, SQLException {
        buildStage(primaryStage, new CClassDetail());
    }

    private void buildStage(Stage primaryStage, CClassDetail controller) {
        try {
            @NotNull final FXMLLoader loader = IClassDetail.load(controller);
            primaryStage.setTitle("Detail Kelas");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        } catch (IOException ignored) {
        }
    }
}
