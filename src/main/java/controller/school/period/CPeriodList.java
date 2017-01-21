package controller.school.period;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 21 January 2017, 1:16 PM.
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
import model.database.component.DBPeriod;
import model.database.component.DBSchool;
import model.database.core.DBType;
import model.database.model.MPeriod;
import model.method.pso.hdpso.component.Setting;
import model.util.Session;
import model.util.pattern.observer.ObservableDBSchool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import view.school.period.IPeriodEdit;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class CPeriodList implements Initializable {
    @FXML public TableView<DBPeriod> periodList;
    @FXML public TableColumn<DBPeriod, String> columnName;
    @FXML public TableColumn<DBPeriod, String> columnNickname;
    @FXML public TableColumn<DBPeriod, String> columnStart;
    @FXML public TableColumn<DBPeriod, String> columnEnd;
    @FXML public TableColumn<DBPeriod, Integer> columnOrder;
    @Nullable private ObservableDBSchool oSchool;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        @NotNull final Session session = Session.getInstance();
        if (session.containsKey("school")) {
            this.oSchool = ((ObservableDBSchool) Session.getInstance().get("school"));
        }
        this.columnName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        this.columnNickname.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getNickname()));
        this.columnStart.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStart().toString()));
        this.columnEnd.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getEnd().toString()));
        this.columnOrder.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getPosition()).asObject());
        this.periodList.setItems(FXCollections.observableList(this.populatePeriod()));
    }

    private List<DBPeriod> populatePeriod() {
        if(this.oSchool != null) {
            @Nullable final DBSchool school = this.oSchool.getSchool();
            if (school != null) {
                try {
                    @NotNull final MPeriod mPeriod = new MPeriod(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
                    return mPeriod.getAllFromSchool(school);
                } catch (SQLException | UnsupportedEncodingException ignored) {
                    System.err.println("Error Activating Database");
                    System.exit(-1);
                }
            }
        }
        return Collections.emptyList();
    }

    public void onPeriodListEditPressed(ActionEvent actionEvent) {
        @Nullable final DBPeriod period = this.periodList.getSelectionModel().getSelectedItem();
        if (period == null) {
            this.notifySelectFirst();
        } else {
            @NotNull final Stage dialog = new Stage();
            dialog.setTitle("Edit Periode");

            try {
                dialog.setScene(new Scene(IPeriodEdit.load(new CPeriodEdit(period){
                    @Override
                    public void periodChanged() {
                        CPeriodList.this.periodList.setItems(FXCollections.observableList(CPeriodList.this.populatePeriod()));
                        super.periodChanged();
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
        alert.setContentText("Pilih salah satu data periode terlebih dahulu");
        alert.showAndWait();
    }

    public void onPeriodListClosePressed(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();

    }
}
