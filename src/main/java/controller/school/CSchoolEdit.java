package controller.school;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 20 January 2017, 8:05 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import model.database.component.DBSchool;
import model.database.component.DBSemester;
import model.database.core.DBType;
import model.database.model.MSchool;
import model.method.pso.hdpso.component.Setting;
import model.util.Session;
import model.util.pattern.observer.ObservableDBSchool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

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
    @Nullable
    private ObservableDBSchool oSchool;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        @NotNull final Session session = Session.getInstance();
        if (session.containsKey("school")) {
            this.oSchool = ((ObservableDBSchool) Session.getInstance().get("school"));
            @Nullable final DBSchool school = this.oSchool.getSchool();
            if (school != null) {
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
        }
    }

    public void onSchoolEditSavePressed(ActionEvent actionEvent) {
        if (this.oSchool != null) {
            @Nullable final DBSchool school = this.oSchool.getSchool();
            if (school != null) {
                try {
                    final int semester = ((RadioButton) this.semesterGroup.getSelectedToggle()).getText().toLowerCase().contentEquals(DBSemester.ODD.describe().toLowerCase()) ? DBSemester.ODD.ordinal() : DBSemester.EVEN.ordinal();
                    @NotNull final MSchool mSchool = new MSchool(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
                    mSchool.update(
                            school,
                            this.tfName.getText(),
                            this.tfNickname.getText(),
                            this.tfAddress.getText(),
                            this.tfAcademicYear.getText(),
                            semester,
                            school.getActivePeriod(),
                            school.getActiveDay());
                    @NotNull final Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Berhasil");
                    alert.setHeaderText(null);
                    alert.setContentText("Data Berhasil Dirubah !");

                    @NotNull final Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent()) {
                        if (result.get() == ButtonType.OK) {
                            this.onSchoolEditClosePressed(actionEvent);
                            school.setName(this.tfName.getText());
                            school.setNickname(this.tfNickname.getText());
                            school.setAddress(this.tfAddress.getText());
                            school.setAcademicYear(this.tfAcademicYear.getText());
                            school.setSemester(semester);
                            this.oSchool.update();
                        }
                    }
                } catch (SQLException | UnsupportedEncodingException ignored) {
                    System.err.println("Error Activating Database");
                    System.exit(-1);
                }
            } else {
                @NotNull final Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText(null);
                alert.setContentText("Sistem mengalami error. silahkan restart aplikasi");
                alert.showAndWait();
            }
        }
    }

    public void onSchoolEditClosePressed(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }
}
