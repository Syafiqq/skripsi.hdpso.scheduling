package controller.classroom;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 25 January 2017, 8:06 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.AbstractModel;
import model.database.component.DBAvailability;
import model.database.component.DBClassroom;
import model.database.component.metadata.DBMClassroom;
import model.database.component.metadata.DBMDay;
import model.database.component.metadata.DBMPeriod;
import model.database.component.metadata.DBMSchool;
import model.database.core.DBType;
import model.database.model.*;
import model.method.pso.hdpso.component.Setting;
import model.util.Dump;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import view.classroom.IClassroomCreate;
import view.classroom.IClassroomDetail;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@SuppressWarnings({"unused", "WeakerAccess"})
public class CClassroomList implements Initializable {
    @FXML
    public TableView<DBMClassroom> classroomList;
    @FXML
    public TableColumn<DBMClassroom, String> columnName;
    @NotNull
    private final DBMSchool schoolMetadata;
    @NotNull
    private final List<DBMDay> dayMetadata;
    @NotNull
    private final List<DBMPeriod> periodMetadata;
    @NotNull
    private final List<DBMClassroom> classroomMetadata;
    @NotNull
    private final List<DBAvailability> availabilities;


    public CClassroomList(@NotNull DBMSchool schoolMetadata, @NotNull final List<DBMDay> dayMetadata, @NotNull final List<DBMPeriod> periodMetadata, @NotNull final List<DBAvailability> availabilities, @NotNull final List<DBMClassroom> classroomMetadata) {
        this.schoolMetadata = schoolMetadata;
        this.dayMetadata = dayMetadata;
        this.periodMetadata = periodMetadata;
        this.availabilities = availabilities;
        this.classroomMetadata = classroomMetadata;
    }

    public CClassroomList() throws UnsupportedEncodingException, SQLException {
        @NotNull final AbstractModel model = new MSchool(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
        this.schoolMetadata = Dump.schoolMetadata();
        this.dayMetadata = MDay.getAllMetadataFromSchool(model, this.schoolMetadata);
        this.periodMetadata = MPeriod.getAllMetadataFromSchool(model, this.schoolMetadata);
        this.classroomMetadata = MClassroom.getAllMetadataFromSchool(model, this.schoolMetadata);
        this.availabilities = MAvailability.getAll(model);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.columnName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        this.classroomList.setItems(FXCollections.observableList(this.classroomMetadata));
    }

    public void onRemoveClassroomListPressed() {
        @Nullable final DBMClassroom classroom = this.classroomList.getSelectionModel().getSelectedItem();
        if (classroom == null) {
            this.notifySelectFirst();
        } else {
            @NotNull Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Konfirmasi");
            alert.setHeaderText(null);
            alert.setContentText("Apakah Anda yakin untuk menghapus ruangan ini ? ");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    try {
                        @NotNull final AbstractModel model = new MClassroom(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
                        MClassroom.deleteTimeOff(model, classroom);
                        MClassroom.delete(model, classroom);
                        this.classroomMetadata.clear();
                        this.classroomMetadata.addAll(MClassroom.getAllMetadataFromSchool(model, this.schoolMetadata));
                        this.classroomList.refresh();
                    } catch (SQLException | UnsupportedEncodingException ignored) {
                        System.err.println("Error Activating Database");
                        System.exit(-1);
                    }
                }
            }
        }
    }

    @SuppressWarnings("Duplicates")
    private void notifySelectFirst() {
        @NotNull final Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText("Pilih salah satu data ruangan terlebih dahulu!");
        alert.showAndWait();
    }

    public void onAddClassroomListPressed(ActionEvent actionEvent) {
        @NotNull final Stage dialog = new Stage();
        dialog.setTitle("Tambah Ruangan");

        try {
            dialog.setScene(new Scene(IClassroomCreate.load(new CClassroomCreate(this.schoolMetadata, this.dayMetadata, this.periodMetadata, this.availabilities) {
                @Override
                public void classroomCreated(@NotNull DBMClassroom classroom) {
                    try {
                        @NotNull final AbstractModel model = new MClassroom(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
                        CClassroomList.this.classroomMetadata.clear();
                        CClassroomList.this.classroomMetadata.addAll(MClassroom.getAllMetadataFromSchool(model, controller.classroom.CClassroomList.this.schoolMetadata));
                        CClassroomList.this.classroomList.refresh();
                    } catch (SQLException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    super.classroomCreated(classroom);
                }
            }).load()));
        } catch (IOException ignored) {
        }

        dialog.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
    }

    public void onDetailClassroomListPressed(ActionEvent actionEvent) {
        @Nullable final DBMClassroom classroomMetadata = this.classroomList.getSelectionModel().getSelectedItem();
        if (classroomMetadata == null) {
            this.notifySelectFirst();
        } else {
            try {
                @NotNull final AbstractModel model = new MClassroom(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
                @NotNull final DBClassroom classroom = MClassroom.getFromMetadata(model, this.schoolMetadata, classroomMetadata);
                MClassroom.getTimeOff(model, classroom, this.dayMetadata, this.periodMetadata, this.availabilities);
                @NotNull final Stage dialog = new Stage();
                dialog.setTitle("Detail Ruangan");

                try {
                    dialog.setScene(new Scene(IClassroomDetail.load(new CClassroomDetail(classroom, this.dayMetadata, this.periodMetadata, this.availabilities) {
                        @Override
                        public void classroomUpdated(@NotNull final DBMClassroom _classroom, @NotNull final String name) {
                            CClassroomList.this.classroomMetadata.stream().filter(vClassroom -> vClassroom.getId() == _classroom.getId()).forEach(vClassroom -> vClassroom.setName(name));
                            CClassroomList.this.classroomList.refresh();
                            super.classroomUpdated(_classroom, name);
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

    public void onCloseClassroomListPressed(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }
}
