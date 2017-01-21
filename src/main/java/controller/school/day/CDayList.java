package controller.school.day;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 21 January 2017, 10:25 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.database.component.DBDay;
import model.database.component.DBSchool;
import model.database.core.DBType;
import model.database.model.MDay;
import model.method.pso.hdpso.component.Setting;
import model.util.Session;
import model.util.pattern.observer.ObservableDBSchool;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import view.school.day.IDayEdit;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class CDayList implements Initializable {
    @FXML public TableView<DBDay> dayList;
    @FXML public TableColumn<DBDay, String> columnName;
    @FXML public TableColumn<DBDay, String> columnNickname;
    @FXML public TableColumn<DBDay, Integer> columnOrder;
    @Nullable private ObservableDBSchool oSchool;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        @NotNull final Session session = Session.getInstance();
        if (session.containsKey("school")) {
            this.oSchool = ((ObservableDBSchool) Session.getInstance().get("school"));
        }
        this.columnName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        this.columnNickname.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getNickname()));
        this.columnOrder.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getPosition()).asObject());
        this.dayList.setItems(FXCollections.observableList(this.populateDay()));
    }

    @Contract(pure = true)
    @NotNull private List<DBDay> populateDay() {
        if(this.oSchool != null) {
            @Nullable final DBSchool school = this.oSchool.getSchool();
            if (school != null) {
                try {
                    @NotNull final MDay mDay = new MDay(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
                    return mDay.getAllFromSchool(school);
                } catch (SQLException | UnsupportedEncodingException ignored) {
                    System.err.println("Error Activating Database");
                    System.exit(-1);
                }
            }
        }
        return Collections.emptyList();
    }

    public void onDayListEditPressed(ActionEvent actionEvent) {
        @Nullable final DBDay day = this.dayList.getSelectionModel().getSelectedItem();
        if (day == null) {
            this.notifySelectFirst();
        } else {
            @NotNull final Stage dialog = new Stage();
            dialog.setTitle("Edit Hari");

            try {
                dialog.setScene(new Scene(IDayEdit.load(new CDayEdit(day){
                    @Override
                    public void dayUpdated() {
                        CDayList.this.dayList.setItems(FXCollections.observableList(CDayList.this.populateDay()));
                        super.dayUpdated();
                    }
                }).load()));
            } catch (IOException ignored) {
            }

            dialog.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();
        }
    }

    private void notifySelectFirst() {
        @NotNull final Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText("Pilih salah satu data hari terlebih dahulu");
        alert.showAndWait();
    }

    public void onDayListClosePressed(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }
}
