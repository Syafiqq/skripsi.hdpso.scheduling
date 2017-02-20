package view.subject;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 23 January 2017, 3:39 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import controller.subject.CSubjectDetail;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class ISubjectDetail extends Application {
    public ISubjectDetail() {
    }

    public ISubjectDetail(CSubjectDetail controller) {
        this.buildStage(new Stage(), controller);
    }

    public static FXMLLoader load(@NotNull CSubjectDetail controller) {
        @NotNull final FXMLLoader loader = new FXMLLoader(ISubjectDetail.class.getResource("/layout/subject/dialog_subject_detail.fxml"));
        loader.setController(controller);
        return loader;
    }

    @Override
    public void start(Stage primaryStage) throws UnsupportedEncodingException, SQLException {
        buildStage(primaryStage, new CSubjectDetail());
    }

    private void buildStage(Stage primaryStage, CSubjectDetail controller) {
        try {
            @NotNull final FXMLLoader loader = ISubjectDetail.load(controller);
            primaryStage.setTitle("Detail Mata Kuliah");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        } catch (IOException ignored) {
        }
    }
}
