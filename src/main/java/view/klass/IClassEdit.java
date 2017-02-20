package view.klass;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 24 January 2017, 9:12 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import controller.klass.CClassEdit;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class IClassEdit extends Application {
    public IClassEdit() {
    }

    public IClassEdit(CClassEdit controller) {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CClassEdit controller) {
        @NotNull final FXMLLoader loader = new FXMLLoader(IClassEdit.class.getResource("/layout/klass/dialog_klass_edit.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override
    public void start(Stage primaryStage) throws UnsupportedEncodingException, SQLException {
        buildStage(primaryStage, new CClassEdit());
    }

    private void buildStage(Stage primaryStage, CClassEdit controller) {
        try {
            @NotNull final FXMLLoader loader = IClassEdit.load(controller);
            primaryStage.setTitle("Edit Kelas");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        } catch (IOException ignored) {
        }
    }
}