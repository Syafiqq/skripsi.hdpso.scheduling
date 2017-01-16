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
import model.method.pso.hdpso.component.Setting;
import model.util.Session;
import model.util.pattern.observer.ObservableDBSchool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.*;
import java.util.*;

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
            @NotNull final Connection connection = DriverManager.getConnection(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
            @NotNull final PreparedStatement statement = connection.prepareStatement("SELECT `id`, `name`, `nick`, `address`, `academic_year`, `semester`, `active_period`, `active_day` FROM `school` ORDER BY `id` ASC ");
            @NotNull final ResultSet result = statement.executeQuery();
            @NotNull List<DBSchool> dbSchoolList = new LinkedList<>();
            while (result.next()) {
                dbSchoolList.add(
                        new DBSchool(
                                result.getInt("id"),
                                result.getString("name"),
                                result.getString("nick"),
                                result.getString("address"),
                                result.getString("academic_year"),
                                result.getInt("semester"),
                                result.getInt("active_period"),
                                result.getInt("active_day")
                        ));
            }
            result.close();
            statement.close();
            connection.close();
            return dbSchoolList;
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
        @Nullable DBSchool school = this.schoolList.getSelectionModel().getSelectedItem();
        if (school == null) {
            this.notifySelectFirst();
        } else {
            @NotNull Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure to delete this?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    try {
                        @NotNull final Connection connection = DriverManager.getConnection(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
                        @NotNull final PreparedStatement statement = connection.prepareStatement("DELETE FROM `school` WHERE `id`=?");
                        statement.setInt(1, school.getId());
                        statement.execute();
                        statement.close();
                        connection.close();
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
        alert.setContentText("Select some item first");

        alert.showAndWait();
    }

    public void onListCancelPressed(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }
}
