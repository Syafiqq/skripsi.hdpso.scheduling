package view.subject;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 21 January 2017, 5:28 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import controller.subject.CSubjectList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class ISubjectList extends Application {
    public ISubjectList() {
    }

    public ISubjectList(CSubjectList controller) {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CSubjectList controller) {
        @NotNull final FXMLLoader loader = new FXMLLoader(ISubjectList.class.getResource("/layout/subject/dialog_subject_list.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override
    public void start(Stage primaryStage) throws UnsupportedEncodingException, SQLException {
        buildStage(primaryStage, new CSubjectList());
    }

    private void buildStage(Stage primaryStage, CSubjectList controller) {
        try {
            @NotNull final FXMLLoader loader = ISubjectList.load(controller);
            primaryStage.setTitle("Daftar Mata Kuliah");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        } catch (IOException ignored) {
        }
    }
}
