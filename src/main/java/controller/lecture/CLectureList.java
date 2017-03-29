package controller.lecture;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 25 January 2017, 9:08 AM.
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
import model.database.component.DBLecture;
import model.database.component.metadata.DBMClass;
import model.database.component.metadata.DBMDay;
import model.database.component.metadata.DBMLecture;
import model.database.component.metadata.DBMLesson;
import model.database.component.metadata.DBMPeriod;
import model.database.component.metadata.DBMSchool;
import model.database.component.metadata.DBMSubject;
import model.database.core.DBType;
import model.database.model.MAvailability;
import model.database.model.MDay;
import model.database.model.MLecture;
import model.database.model.MLesson;
import model.database.model.MPeriod;
import model.database.model.MTimetable;
import model.method.pso.hdpso.component.Setting;
import model.util.Dump;
import model.util.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import view.lecture.ILectureCreate;
import view.lecture.ILectureDetail;

@SuppressWarnings({"unchecked", "WeakerAccess", "unused"})
public class CLectureList implements Initializable {
    @NotNull
    private final DBMSchool                       schoolMetadata;
    @NotNull
    private final List<DBMDay>                    dayMetadata;
    @NotNull
    private final List<DBMPeriod>                 periodMetadata;
    @NotNull
    private final List<DBMLecture>                lectureMetadata;
    @NotNull
    private final List<DBAvailability>            availabilities;
    @FXML
    public        TableView<DBMLecture>           lectureList;
    @FXML
    public        TableColumn<DBMLecture, String> columnName;


    public CLectureList(@NotNull DBMSchool schoolMetadata, @NotNull final List<DBMDay> dayMetadata, @NotNull final List<DBMPeriod> periodMetadata, @NotNull final List<DBAvailability> availabilities, @NotNull final List<DBMLecture> lectureMetadata) {
        this.schoolMetadata = schoolMetadata;
        this.dayMetadata = dayMetadata;
        this.periodMetadata = periodMetadata;
        this.availabilities = availabilities;
        this.lectureMetadata = lectureMetadata;
    }

    public CLectureList() throws UnsupportedEncodingException, SQLException {
        @NotNull final AbstractModel model = new MTimetable(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
        this.schoolMetadata = Dump.schoolMetadata();
        this.dayMetadata = MDay.getAllMetadataFromSchool(model, this.schoolMetadata);
        this.periodMetadata = MPeriod.getAllMetadataFromSchool(model, this.schoolMetadata);
        this.lectureMetadata = MLecture.getAllMetadataFromSchool(model, this.schoolMetadata);
        this.availabilities = MAvailability.getAll(model);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.columnName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        this.lectureList.setItems(FXCollections.observableList(this.lectureMetadata));
    }

    public void onRemoveLectureListPressed() {
        @Nullable final DBMLecture lecture = this.lectureList.getSelectionModel().getSelectedItem();
        if (lecture == null) {
            this.notifySelectFirst();
        } else {
            @NotNull Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Konfirmasi");
            alert.setHeaderText(null);
            alert.setContentText("Apakah Anda yakin untuk menghapus dosen ini ? ");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    try {
                        @NotNull final AbstractModel model   = new MLecture(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
                        @NotNull final Session       session = Session.getInstance();
                        if(session.containsKey("subject") && session.containsKey("klass") && session.containsKey("lesson"))
                        {
                            @NotNull final List<DBMSubject> subjectMetadata = (List<DBMSubject>) session.get("subject");
                            @NotNull final List<DBMClass>   klassMetadata   = (List<DBMClass>) session.get("klass");
                            @NotNull final List<DBMLesson>  lessons         = MLesson.getAllMetadataFromLecture(model, lecture, subjectMetadata, klassMetadata);
                            MLesson.deleteBunch(model, lessons);
                            @NotNull final List<DBMLesson> lessonMetadata = (List<DBMLesson>) session.get("lesson");
                            lessonMetadata.clear();
                            lessonMetadata.addAll(MLesson.getAllMetadataFromSchool(model, this.schoolMetadata, subjectMetadata, klassMetadata, this.lectureMetadata));
                        }
                        MLecture.deleteTimeOff(model, lecture);
                        MLecture.delete(model, lecture);
                        this.lectureMetadata.clear();
                        this.lectureMetadata.addAll(MLecture.getAllMetadataFromSchool(model, this.schoolMetadata));
                        this.lectureList.setItems(FXCollections.observableList(this.lectureMetadata));
                        this.lectureList.refresh();
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
        alert.setContentText("Pilih salah satu data dosen terlebih dahulu!");
        alert.showAndWait();
    }

    public void onAddLectureListPressed(ActionEvent actionEvent) {
        @NotNull final Stage dialog = new Stage();
        dialog.setTitle("Tambah Dosen");

        try {
            dialog.setScene(new Scene(ILectureCreate.load(new CLectureCreate(this.schoolMetadata, this.dayMetadata, this.periodMetadata, this.availabilities) {
                @Override
                public void lectureCreated(@NotNull DBMLecture lecture) {
                    try {
                        @NotNull final AbstractModel model = new MLecture(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
                        CLectureList.this.lectureMetadata.clear();
                        CLectureList.this.lectureMetadata.addAll(MLecture.getAllMetadataFromSchool(model, controller.lecture.CLectureList.this.schoolMetadata));
                        CLectureList.this.lectureList.setItems(FXCollections.observableList(CLectureList.this.lectureMetadata));
                        CLectureList.this.lectureList.refresh();
                    } catch (SQLException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    super.lectureCreated(lecture);
                }
            }).load()));
        } catch (IOException ignored) {
        }

        dialog.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
    }

    public void onDetailLectureListPressed(ActionEvent actionEvent) {
        @Nullable final DBMLecture lectureMetadata = this.lectureList.getSelectionModel().getSelectedItem();
        if (lectureMetadata == null) {
            this.notifySelectFirst();
        } else {
            try {
                @NotNull final AbstractModel model = new MLecture(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
                @NotNull final DBLecture lecture = MLecture.getFromMetadata(model, this.schoolMetadata, lectureMetadata);
                MLecture.getTimeOff(model, lecture, this.dayMetadata, this.periodMetadata, this.availabilities);
                @NotNull final Stage dialog = new Stage();
                dialog.setTitle("Data Dosen");

                try {
                    dialog.setScene(new Scene(ILectureDetail.load(new CLectureDetail(lecture, this.dayMetadata, this.periodMetadata, this.availabilities) {
                        @Override
                        public void lectureUpdated(@NotNull final DBMLecture _lecture, @NotNull final String name) {
                            CLectureList.this.lectureMetadata.stream().filter(vLecture -> vLecture.getId() == _lecture.getId()).forEach(vLecture -> vLecture.setName(name));
                            CLectureList.this.lectureList.refresh();
                            super.lectureUpdated(_lecture, name);
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

    public void onCloseLectureListPressed(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }
}
