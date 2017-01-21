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
import model.database.component.DBSchool;
import model.database.core.DBType;
import model.database.model.MDay;
import model.database.model.MPeriod;
import model.database.model.MSchool;
import model.method.pso.hdpso.component.Setting;
import model.util.Session;
import model.util.pattern.observer.ObservableDBSchool;
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
    public TableView<DBSchool> schoolList;
    public TableColumn<DBSchool, String> columnName;
    public TableColumn<DBSchool, String> columnAcademicYear;
    public TableColumn<DBSchool, String> columnSemester;
    public TableColumn<DBSchool, Integer> columnPeriod;
    public TableColumn<DBSchool, Integer> columnDay;

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

    private List<DBSchool> populateSchool() {
        try {
            @NotNull final MSchool mSchool = new MSchool(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
            return mSchool.getAll();
        } catch (SQLException | UnsupportedEncodingException ignored) {
            System.err.println("Error Activating Database");
            System.exit(-1);
        }
        return Collections.emptyList();
    }

    public void onTimetableLoadPressed(ActionEvent actionEvent) {
        @Nullable DBSchool school = this.schoolList.getSelectionModel().getSelectedItem();
        if (school == null) {
            this.notifySelectFirst();
        } else {
            if (!Session.getInstance().containsKey("school")) {
                Session.getInstance().put("school", new ObservableDBSchool(school));
            } else {
                ((ObservableDBSchool) Session.getInstance().get("school")).setSchool(school);
            }
            this.onListCancelPressed(actionEvent);
        }
    }

    public void onTimetableDeletePressed() {
        @Nullable final DBSchool school = this.schoolList.getSelectionModel().getSelectedItem();
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
                        @NotNull final MSchool mSchool = new MSchool(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
                        mSchool.delete(school);
                        MDay.deleteFromSchool(mSchool, school);
                        MPeriod.deleteFromSchool(mSchool, school);
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
