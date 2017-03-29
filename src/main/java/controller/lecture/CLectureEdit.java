package controller.lecture;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 25 January 2017, 9:07 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import model.AbstractModel;
import model.database.component.DBLecture;
import model.database.component.metadata.DBMLecture;
import model.database.component.metadata.DBMSchool;
import model.database.core.DBType;
import model.database.model.MLecture;
import model.database.model.MTimetable;
import model.method.pso.hdpso.component.Setting;
import model.util.Dump;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("WeakerAccess")
public class CLectureEdit implements Initializable {
    @NotNull
    private final DBMLecture lecture;
    public TextField tfName;

    public CLectureEdit(@NotNull final DBMLecture lecture) {
        this.lecture = lecture;
    }

    public CLectureEdit() throws UnsupportedEncodingException, SQLException {
        @NotNull final AbstractModel    model           = new MTimetable(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
        @NotNull final DBMSchool        schoolMetadata  = Dump.schoolMetadata();
        @NotNull final List<DBMLecture> lectureMetadata = MLecture.getAllMetadataFromSchool(model, schoolMetadata);
        if (lectureMetadata.size() > 0) {
            this.lecture = MLecture.getFromMetadata(model, schoolMetadata, lectureMetadata.get(0));
        } else {
            this.lecture = new DBLecture(-1, "A", schoolMetadata);
            System.exit(0);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tfName.setText(this.lecture.getName());
    }

    public void onCloseLectureEditPressed(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }

    public void onSaveLectureEditPressed(ActionEvent actionEvent) {
        try {
            @NotNull final AbstractModel model = new MLecture(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
            MLecture.update(model,
                    this.lecture,
                    this.tfName.getText()
            );
            @NotNull final Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Berhasil");
            alert.setHeaderText(null);
            alert.setContentText("Data Berhasil Dirubah !");

            @NotNull final Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    this.lectureUpdated(this.lecture, this.tfName.getText());
                    this.onCloseLectureEditPressed(actionEvent);
                }
            }
        } catch (SQLException | UnsupportedEncodingException ignored) {
            System.err.println("Error Activating Database");
            System.exit(-1);
        }
    }

    public void lectureUpdated(@NotNull final DBMLecture lecture, @NotNull final String name) {

    }
}
