package view.subject;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 21 January 2017, 5:56 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import controller.subject.CSubjectCreate;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class ISubjectCreate extends Application {
    public ISubjectCreate() {
    }

    public ISubjectCreate(CSubjectCreate controller) {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CSubjectCreate controller) {
        @NotNull final FXMLLoader loader = new FXMLLoader(ISubjectCreate.class.getResource("/layout/subject/dialog_subject_create.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override
    public void start(Stage primaryStage) throws UnsupportedEncodingException, SQLException {
        buildStage(primaryStage, new CSubjectCreate());
    }

    private void buildStage(Stage primaryStage, CSubjectCreate controller) {
        try {
            @NotNull final FXMLLoader loader = ISubjectCreate.load(controller);
            primaryStage.setTitle("Tambah Mata Kuliah");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        } catch (IOException ignored) {
        }
    }
}
