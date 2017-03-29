package controller.klass;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 24 January 2017, 9:08 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
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
import model.database.component.DBClass;
import model.database.component.metadata.DBMClass;
import model.database.component.metadata.DBMDay;
import model.database.component.metadata.DBMLecture;
import model.database.component.metadata.DBMLesson;
import model.database.component.metadata.DBMPeriod;
import model.database.component.metadata.DBMSchool;
import model.database.component.metadata.DBMSubject;
import model.database.core.DBType;
import model.database.model.MAvailability;
import model.database.model.MClass;
import model.database.model.MDay;
import model.database.model.MLesson;
import model.database.model.MPeriod;
import model.database.model.MTimetable;
import model.method.pso.hdpso.component.Setting;
import model.util.Dump;
import model.util.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import view.klass.IClassCreate;
import view.klass.IClassDetail;

@SuppressWarnings({"unchecked", "WeakerAccess", "unused"})
public class CClassList implements Initializable {
    @NotNull
    private final DBMSchool                     schoolMetadata;
    @NotNull
    private final List<DBMDay>                  dayMetadata;
    @NotNull
    private final List<DBMPeriod>               periodMetadata;
    @NotNull
    private final List<DBMClass>                klassMetadata;
    @NotNull
    private final List<DBAvailability>          availabilities;
    @FXML
    public        TableView<DBMClass>           klassList;
    @FXML
    public        TableColumn<DBMClass, String> columnName;


    public CClassList(@NotNull DBMSchool schoolMetadata, @NotNull final List<DBMDay> dayMetadata, @NotNull final List<DBMPeriod> periodMetadata, @NotNull final List<DBAvailability> availabilities, @NotNull final List<DBMClass> klassMetadata) {
        this.schoolMetadata = schoolMetadata;
        this.dayMetadata = dayMetadata;
        this.periodMetadata = periodMetadata;
        this.availabilities = availabilities;
        this.klassMetadata = klassMetadata;
    }

    public CClassList() throws UnsupportedEncodingException, SQLException {
        @NotNull final AbstractModel model = new MTimetable(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
        this.schoolMetadata = Dump.schoolMetadata();
        this.dayMetadata = MDay.getAllMetadataFromSchool(model, this.schoolMetadata);
        this.periodMetadata = MPeriod.getAllMetadataFromSchool(model, this.schoolMetadata);
        this.klassMetadata = MClass.getAllMetadataFromSchool(model, this.schoolMetadata);
        this.availabilities = MAvailability.getAll(model);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.columnName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        this.klassList.setItems(FXCollections.observableList(this.klassMetadata));
    }

    public void onRemoveClassListPressed() {
        @Nullable final DBMClass klass = this.klassList.getSelectionModel().getSelectedItem();
        if (klass == null) {
            this.notifySelectFirst();
        } else {
            @NotNull Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Konfirmasi");
            alert.setHeaderText(null);
            alert.setContentText("Apakah Anda yakin untuk menghapus kelas ini ? ");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    try {
                        @NotNull final AbstractModel model   = new MClass(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
                        @NotNull final Session       session = Session.getInstance();
                        if(session.containsKey("subject") && session.containsKey("lecture") && session.containsKey("lesson"))
                        {
                            @NotNull final List<DBMSubject> subjectMetadata = (List<DBMSubject>) session.get("subject");
                            @NotNull final List<DBMLecture> lectureMetadata = (List<DBMLecture>) session.get("lecture");
                            @NotNull final List<DBMLesson>  lessons         = MLesson.getAllMetadataFromClass(model, klass, subjectMetadata, lectureMetadata);
                            MLesson.deleteBunch(model, lessons);
                            @NotNull final List<DBMLesson> lessonMetadata = (List<DBMLesson>) session.get("lesson");
                            lessonMetadata.clear();
                            lessonMetadata.addAll(MLesson.getAllMetadataFromSchool(model, this.schoolMetadata, subjectMetadata, this.klassMetadata, lectureMetadata));
                        }
                        MClass.deleteTimeOff(model, klass);
                        MClass.delete(model, klass);
                        this.klassMetadata.clear();
                        this.klassMetadata.addAll(MClass.getAllMetadataFromSchool(model, this.schoolMetadata));
                        this.klassList.setItems(FXCollections.observableList(this.klassMetadata));
                        this.klassList.refresh();
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
        alert.setContentText("Pilih salah satu data kelas terlebih dahulu!");
        alert.showAndWait();
    }

    public void onAddClassListPressed(ActionEvent actionEvent) {
        @NotNull final Stage dialog = new Stage();
        dialog.setTitle("Tambah Kelas");

        try {
            dialog.setScene(new Scene(IClassCreate.load(new CClassCreate(this.schoolMetadata, this.dayMetadata, this.periodMetadata, this.availabilities) {
                @Override
                public void klassCreated(@NotNull DBMClass klass) {
                    try {
                        @NotNull final AbstractModel model = new MClass(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
                        CClassList.this.klassMetadata.clear();
                        CClassList.this.klassMetadata.addAll(MClass.getAllMetadataFromSchool(model, CClassList.this.schoolMetadata));
                        CClassList.this.klassList.setItems(FXCollections.observableList(CClassList.this.klassMetadata));
                        CClassList.this.klassList.refresh();
                    } catch (SQLException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    super.klassCreated(klass);
                }
            }).load()));
        } catch (IOException ignored) {
        }

        dialog.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
    }

    public void onDetailClassListPressed(ActionEvent actionEvent) {
        @Nullable final DBMClass klassMetadata = this.klassList.getSelectionModel().getSelectedItem();
        if (klassMetadata == null) {
            this.notifySelectFirst();
        } else {
            try {
                @NotNull final AbstractModel model = new MClass(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
                @NotNull final DBClass klass = MClass.getFromMetadata(model, this.schoolMetadata, klassMetadata);
                MClass.getTimeOff(model, klass, this.dayMetadata, this.periodMetadata, this.availabilities);
                @NotNull final Stage dialog = new Stage();
                dialog.setTitle("Data Kelas");

                try {
                    dialog.setScene(new Scene(IClassDetail.load(new CClassDetail(klass, this.dayMetadata, this.periodMetadata, this.availabilities) {
                        @Override
                        public void klassUpdated(@NotNull final DBMClass _klass, @NotNull final String name) {
                            CClassList.this.klassMetadata.stream().filter(vClass -> vClass.getId() == _klass.getId()).forEach(vClass -> vClass.setName(name));
                            CClassList.this.klassList.refresh();
                            super.klassUpdated(_klass, name);
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

    public void onCloseClassListPressed(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }
}
