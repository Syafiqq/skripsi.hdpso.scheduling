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
import model.AbstractModel;
import model.database.component.DBPeriod;
import model.database.component.metadata.DBMPeriod;
import model.database.component.metadata.DBMSchool;
import model.database.core.DBType;
import model.database.model.MPeriod;
import model.database.model.MSchool;
import model.method.pso.hdpso.component.Setting;
import model.util.Dump;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import view.school.period.IPeriodEdit;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class CPeriodList implements Initializable {
    @NotNull
    private final DBMSchool schoolMetadata;
    @NotNull
    private final List<DBMPeriod> periodMetadata;
    @FXML
    public TableView<DBMPeriod> periodList;
    @FXML
    public TableColumn<DBMPeriod, String> columnName;
    @FXML
    public TableColumn<DBMPeriod, String> columnNickname;
    @FXML
    public TableColumn<DBMPeriod, String> columnStart;
    @FXML
    public TableColumn<DBMPeriod, String> columnEnd;
    @FXML
    public TableColumn<DBMPeriod, Integer> columnOrder;

    public CPeriodList(@NotNull final DBMSchool schoolMetadata, @NotNull final List<DBMPeriod> periodMetadata) {
        this.schoolMetadata = schoolMetadata;
        this.periodMetadata = periodMetadata;
    }

    public CPeriodList() throws UnsupportedEncodingException, SQLException {
        @NotNull final AbstractModel model = new MPeriod(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
        @NotNull final DBMSchool schoolMetadata = Dump.schoolMetadata();
        this.schoolMetadata = MSchool.getFromMetadata(model, schoolMetadata);
        this.periodMetadata = MPeriod.getAllMetadataFromSchool(model, this.schoolMetadata);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.columnName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        this.columnNickname.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getNickname()));
        this.columnStart.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStart().toString()));
        this.columnEnd.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getEnd().toString()));
        this.columnOrder.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getPosition()).asObject());
        this.periodList.setItems(FXCollections.observableList(this.periodMetadata));
    }

    public void onPeriodListEditPressed(ActionEvent actionEvent) {
        @Nullable final DBMPeriod periodMetadata = this.periodList.getSelectionModel().getSelectedItem();
        if (periodMetadata == null) {
            this.notifySelectFirst();
        } else {
            try {
                @NotNull final AbstractModel model = new MPeriod(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
                @NotNull final DBPeriod period = MPeriod.getFromMetadata(model, this.schoolMetadata, periodMetadata);
                @NotNull final Stage dialog = new Stage();
                dialog.setTitle("Edit Periode");

                try {
                    dialog.setScene(new Scene(IPeriodEdit.load(new CPeriodEdit(period) {
                        @Override
                        public void periodUpdated(@NotNull DBPeriod _period, String name, String nickname, String start, String end, int position) {
                            CPeriodList.this.periodMetadata.stream()
                                    .filter(metadata -> metadata.getId() == _period.getId())
                                    .forEach(metadata -> {
                                        metadata.setName(name);
                                        metadata.setNickname(nickname);
                                        metadata.setPosition(position);
                                        metadata.setStartLesson(start);
                                        metadata.setEndLesson(end);
                                    });
                            CPeriodList.this.periodList.refresh();
                            super.periodUpdated(period, name, nickname, start, end, position);
                        }
                    }).load()));
                } catch (IOException ignored) {
                }

                dialog.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.showAndWait();
            } catch (SQLException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }

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
