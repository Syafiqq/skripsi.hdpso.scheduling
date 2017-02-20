package controller.subject;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 23 January 2017, 8:24 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import model.AbstractModel;
import model.database.component.DBSubject;
import model.database.component.metadata.DBMSchool;
import model.database.component.metadata.DBMSubject;
import model.database.core.DBType;
import model.database.model.MSchool;
import model.database.model.MSubject;
import model.method.pso.hdpso.component.Setting;
import model.util.Dump;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@SuppressWarnings("WeakerAccess")
public class CSubjectEdit implements Initializable {
    @NotNull
    private final DBMSubject subject;
    public TextField tfName;
    public TextField tfCode;

    public CSubjectEdit(@NotNull final DBMSubject subject) {
        this.subject = subject;
    }

    public CSubjectEdit() throws UnsupportedEncodingException, SQLException {
        @NotNull final AbstractModel model = new MSchool(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
        @NotNull final DBMSchool schoolMetadata = Dump.schoolMetadata();
        @NotNull final List<DBMSubject> subjectMetadata = MSubject.getAllMetadataFromSchool(model, schoolMetadata);
        if (subjectMetadata.size() > 0) {
            this.subject = MSubject.getFromMetadata(model, schoolMetadata, subjectMetadata.get(0));
        } else {
            this.subject = new DBSubject(-1, "A", "B", schoolMetadata);
            System.exit(0);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tfName.setText(this.subject.getName());
        this.tfCode.setText(this.subject.getSubjectId());
    }

    public void onCloseSchoolEditPressed(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }

    public void onSaveSchoolEditPressed(ActionEvent actionEvent) {
        try {
            @NotNull final AbstractModel model = new MSubject(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
            MSubject.update(model,
                    this.subject,
                    this.tfName.getText(),
                    this.tfCode.getText()
            );
            @NotNull final Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Berhasil");
            alert.setHeaderText(null);
            alert.setContentText("Data Berhasil Dirubah !");

            @NotNull final Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    this.subjectUpdated(this.subject, this.tfName.getText(), this.tfCode.getText());
                    this.onCloseSchoolEditPressed(actionEvent);
                }
            }
        } catch (SQLException | UnsupportedEncodingException ignored) {
            System.err.println("Error Activating Database");
            System.exit(-1);
        }
    }

    public void subjectUpdated(@NotNull final DBMSubject subject, @NotNull final String name, @NotNull final String code) {

    }
}
