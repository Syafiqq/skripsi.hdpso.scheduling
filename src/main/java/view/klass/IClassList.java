package view.klass;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 24 January 2017, 9:12 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import controller.klass.CClassList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class IClassList  extends Application {
    public IClassList() {
    }

    public IClassList(CClassList controller) {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CClassList controller) {
        @NotNull final FXMLLoader loader = new FXMLLoader(IClassList.class.getResource("/layout/klass/dialog_klass_list.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override
    public void start(Stage primaryStage) throws UnsupportedEncodingException, SQLException {
        buildStage(primaryStage, new CClassList());
    }

    private void buildStage(Stage primaryStage, CClassList controller) {
        try {
            @NotNull final FXMLLoader loader = IClassList.load(controller);
            primaryStage.setTitle("Daftar Kelas");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        } catch (IOException ignored) {
        }
    }
}
