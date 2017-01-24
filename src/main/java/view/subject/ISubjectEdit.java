package view.subject;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 23 January 2017, 8:25 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import controller.subject.CSubjectEdit;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class ISubjectEdit  extends Application {
    public ISubjectEdit() {
    }

    public ISubjectEdit(CSubjectEdit controller) {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CSubjectEdit controller) {
        @NotNull final FXMLLoader loader = new FXMLLoader(ISubjectEdit.class.getResource("/layout/subject/dialog_subject_edit.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override
    public void start(Stage primaryStage) throws UnsupportedEncodingException, SQLException {
        buildStage(primaryStage, new CSubjectEdit());
    }

    private void buildStage(Stage primaryStage, CSubjectEdit controller) {
        try {
            @NotNull final FXMLLoader loader = ISubjectEdit.load(controller);
            primaryStage.setTitle("Edit Mata Kuliah");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        } catch (IOException ignored) {
        }
    }
}