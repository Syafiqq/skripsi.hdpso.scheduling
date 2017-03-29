package controller.school;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 20 January 2017, 8:05 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import model.AbstractModel;
import model.database.component.DBSchool;
import model.database.component.DBSemester;
import model.database.component.metadata.DBMSchool;
import model.database.core.DBType;
import model.database.model.MTimetable;
import model.method.pso.hdpso.component.Setting;
import model.util.Dump;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("WeakerAccess")
public class CSchoolEdit implements Initializable {
    @FXML
    public TextField tfName;
    @FXML
    public TextField tfNickname;
    @FXML
    public TextField tfAddress;
    @FXML
    public TextField tfAcademicYear;
    @FXML
    public RadioButton rbOdd;
    @FXML
    public ToggleGroup semesterGroup;
    @FXML
    public RadioButton rbEven;
    @NotNull
    private DBSchool school;

    public CSchoolEdit(@NotNull final DBSchool school) {
        this.school = school;
    }

    public CSchoolEdit() throws UnsupportedEncodingException, SQLException {
        @NotNull final DBMSchool     schoolMetadata = Dump.schoolMetadata();
        @NotNull final AbstractModel model          = new MTimetable(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
        this.school = MTimetable.getFromMetadata(model, schoolMetadata);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tfName.setText(school.getName());
        this.tfNickname.setText(school.getNickname());
        this.tfAddress.setText(school.getAddress());
        this.tfAcademicYear.setText(school.getAcademicYear());
        switch (school.getSemester()) {
            case ODD: {
                this.rbOdd.setSelected(true);
            }
            break;
            default: {
                this.rbEven.setSelected(true);
            }
        }
    }

    public void onSchoolEditSavePressed(ActionEvent actionEvent) {
        try {
            final int                    semester = ((RadioButton) this.semesterGroup.getSelectedToggle()).getText().toLowerCase().contentEquals(DBSemester.ODD.describe().toLowerCase()) ? DBSemester.ODD.ordinal() : DBSemester.EVEN.ordinal();
            @NotNull final AbstractModel model    = new MTimetable(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
            MTimetable.update(model,
                    school,
                    this.tfName.getText(),
                    this.tfNickname.getText(),
                    this.tfAddress.getText(),
                    this.tfAcademicYear.getText(),
                    semester,
                    this.school.getActivePeriod(),
                    this.school.getActiveDay());
            @NotNull final Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Berhasil");
            alert.setHeaderText(null);
            alert.setContentText("Data Berhasil Dirubah !");

            @NotNull final Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    this.schoolUpdated(
                            this.tfName.getText(),
                            this.tfNickname.getText(),
                            this.tfAddress.getText(),
                            this.tfAcademicYear.getText(),
                            semester,
                            this.school.getActiveDay(),
                            this.school.getActivePeriod()
                            );
                    this.onSchoolEditClosePressed(actionEvent);
                }
            }
        } catch (SQLException | UnsupportedEncodingException ignored) {
            System.err.println("Error Activating Database");
            System.exit(-1);
        }
    }

    public void schoolUpdated(String name, String nickname, String address, String academicYear, int semester, int activeDay, int activePeriod) {
    }


    public void onSchoolEditClosePressed(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }
}
