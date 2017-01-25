package controller.school;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 16 January 2017, 6:45 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.AbstractModel;
import model.database.component.metadata.DBMClass;
import model.database.component.metadata.DBMClassroom;
import model.database.component.metadata.DBMSchool;
import model.database.component.metadata.DBMSubject;
import model.database.core.DBType;
import model.database.model.*;
import model.method.pso.hdpso.component.Setting;
import model.util.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CSchoolList implements Initializable {
    public TableView<DBMSchool> schoolList;
    public TableColumn<DBMSchool, String> columnName;
    public TableColumn<DBMSchool, String> columnAcademicYear;
    public TableColumn<DBMSchool, String> columnSemester;
    public TableColumn<DBMSchool, Integer> columnPeriod;
    public TableColumn<DBMSchool, Integer> columnDay;

    public CSchoolList() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.columnName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        this.columnAcademicYear.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getAcademicYear()));
        this.columnSemester.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getSemester().describe()));
        this.columnDay.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getActiveDay()).asObject());
        this.columnPeriod.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getActivePeriod()).asObject());
        this.schoolList.setItems(FXCollections.observableList(this.populateSchool()));
    }

    private List<DBMSchool> populateSchool() {
        try {
            @NotNull final AbstractModel model = new MSchool(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
            return MSchool.getAllMetadata(model);
        } catch (SQLException | UnsupportedEncodingException ignored) {
            System.err.println("Error Activating Database");
            System.exit(-1);
        }
        return Collections.emptyList();
    }

    public void onTimetableLoadPressed(ActionEvent actionEvent) {
        @Nullable DBMSchool school = this.schoolList.getSelectionModel().getSelectedItem();
        if (school == null) {
            this.notifySelectFirst();
        } else {
            try {
                @NotNull final AbstractModel model = new MSchool(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
                Session.getInstance().put("school", school);
                Session.getInstance().put("day", MDay.getAllMetadataFromSchool(model, school));
                Session.getInstance().put("period", MPeriod.getAllMetadataFromSchool(model, school));
                Session.getInstance().put("availability", MAvailability.getAll(model));
                Session.getInstance().put("subject", MSubject.getAllMetadataFromSchool(model, school));
                Session.getInstance().put("klass", MClass.getAllMetadataFromSchool(model, school));
                Session.getInstance().put("classroom", MClassroom.getAllMetadataFromSchool(model, school));
            } catch (SQLException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            this.onListCancelPressed(actionEvent);
        }
    }

    public void onTimetableDeletePressed() {
        @Nullable final DBMSchool school = this.schoolList.getSelectionModel().getSelectedItem();
        if (school == null) {
            this.notifySelectFirst();
        } else {
            @NotNull Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Konfirmasi");
            alert.setHeaderText(null);
            alert.setContentText("Apakah Anda yakin untuk menghapus data ini ? ");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    try {
                        @NotNull final AbstractModel model = new MSchool(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
                        MSchool.delete(model, school);
                        MDay.deleteFromSchool(model, school);
                        MPeriod.deleteFromSchool(model, school);
                        @NotNull final List<DBMSubject> subjectMetadata = MSubject.getAllMetadataFromSchool(model, school);
                        MSubject.deleteBunch(model, subjectMetadata);
                        @NotNull final List<DBMClass> classMetadata = MClass.getAllMetadataFromSchool(model, school);
                        MClass.deleteBunch(model, classMetadata);
                        @NotNull final List<DBMClassroom> classroomMetadata = MClassroom.getAllMetadataFromSchool(model, school);
                        MClassroom.deleteBunch(model, classroomMetadata);
                        this.schoolList.setItems(FXCollections.observableList(this.populateSchool()));
                    } catch (SQLException | UnsupportedEncodingException ignored) {
                        System.err.println("Error Activating Database");
                        System.exit(-1);
                    }
                }
            }
        }
    }

    private void notifySelectFirst() {
        @NotNull final Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText("Pilih salah satu");
        alert.showAndWait();
    }

    public void onListCancelPressed(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }
}
