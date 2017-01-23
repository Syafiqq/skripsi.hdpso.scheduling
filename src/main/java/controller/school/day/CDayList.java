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
import model.AbstractModel;
import model.database.component.DBDay;
import model.database.component.metadata.DBMDay;
import model.database.component.metadata.DBMSchool;
import model.database.core.DBType;
import model.database.model.MDay;
import model.database.model.MSchool;
import model.method.pso.hdpso.component.Setting;
import model.util.Dump;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import view.school.day.IDayEdit;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class CDayList implements Initializable {
    @NotNull
    private final DBMSchool schoolMetadata;
    @NotNull
    private final List<DBMDay> dayMetadata;
    @FXML
    public TableView<DBMDay> dayList;
    @FXML
    public TableColumn<DBMDay, String> columnName;
    @FXML
    public TableColumn<DBMDay, String> columnNickname;
    @FXML
    public TableColumn<DBMDay, Integer> columnOrder;

    public CDayList(@NotNull final DBMSchool schoolMetadata, @NotNull final List<DBMDay> dayMetadata) {
        this.schoolMetadata = schoolMetadata;
        this.dayMetadata = dayMetadata;
    }

    public CDayList() throws UnsupportedEncodingException, SQLException {
        @NotNull final AbstractModel model = new MDay(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
        @NotNull final DBMSchool schoolMetadata = Dump.schoolMetadata();
        this.schoolMetadata = MSchool.getFromMetadata(model, schoolMetadata);
        this.dayMetadata = MDay.getAllMetadataFromSchool(model, this.schoolMetadata);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.columnName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        this.columnNickname.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getNickname()));
        this.columnOrder.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getPosition()).asObject());
        this.dayList.setItems(FXCollections.observableList(this.dayMetadata));
    }

    public void onDayListEditPressed(ActionEvent actionEvent) {
        @Nullable final DBMDay dayMetadata = this.dayList.getSelectionModel().getSelectedItem();
        if (dayMetadata == null) {
            this.notifySelectFirst();
        } else {
            try {
                @NotNull final AbstractModel model = new MDay(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
                @NotNull final DBDay day = MDay.getFromMetadata(model, this.schoolMetadata, dayMetadata);
                @NotNull final Stage dialog = new Stage();
                dialog.setTitle("Edit Hari");

                try {
                    dialog.setScene(new Scene(IDayEdit.load(new CDayEdit(day) {
                        @Override
                        public void dayUpdated(@NotNull final DBDay _day, String name, String nickname, int position) {
                            CDayList.this.dayMetadata.stream()
                                    .filter(metadata -> metadata.getId() == _day.getId())
                                    .forEach(metadata -> {
                                        metadata.setName(name);
                                        metadata.setNickname(nickname);
                                        metadata.setPosition(position);
                                    });
                            CDayList.this.dayList.refresh();
                            super.dayUpdated(_day, name, nickname, position);
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
        alert.setContentText("Pilih salah satu data hari terlebih dahulu");
        alert.showAndWait();
    }

    public void onDayListClosePressed(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }
}
