package controller.subject;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 21 January 2017, 5:27 PM.
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
import model.database.component.DBSubject;
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
import model.database.model.MLesson;
import model.database.model.MPeriod;
import model.database.model.MSubject;
import model.database.model.MTimetable;
import model.method.pso.hdpso.component.Setting;
import model.util.Dump;
import model.util.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import view.subject.ISubjectCreate;
import view.subject.ISubjectDetail;

@SuppressWarnings({"unchecked", "WeakerAccess", "unused"}) public class CSubjectList implements Initializable
{
    @NotNull
    private final DBMSchool                       schoolMetadata;
    @NotNull
    private final List<DBMDay>                    dayMetadata;
    @NotNull
    private final List<DBMPeriod>                 periodMetadata;
    @NotNull
    private final List<DBMSubject>                subjectMetadata;
    @NotNull
    private final List<DBAvailability>            availabilities;
    @FXML
    public        TableView<DBMSubject>           subjectList;
    @FXML
    public        TableColumn<DBMSubject, String> columnName;
    @FXML
    public        TableColumn<DBMSubject, String> columnCode;


    public CSubjectList(@NotNull DBMSchool schoolMetadata, @NotNull final List<DBMDay> dayMetadata, @NotNull final List<DBMPeriod> periodMetadata, @NotNull final List<DBAvailability> availabilities, @NotNull final List<DBMSubject> subjectMetadata) {
        this.schoolMetadata = schoolMetadata;
        this.dayMetadata = dayMetadata;
        this.periodMetadata = periodMetadata;
        this.availabilities = availabilities;
        this.subjectMetadata = subjectMetadata;
    }

    public CSubjectList() throws UnsupportedEncodingException, SQLException {
        @NotNull final AbstractModel model = new MTimetable(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
        this.schoolMetadata = Dump.schoolMetadata();
        this.dayMetadata = MDay.getAllMetadataFromSchool(model, this.schoolMetadata);
        this.periodMetadata = MPeriod.getAllMetadataFromSchool(model, this.schoolMetadata);
        this.subjectMetadata = MSubject.getAllMetadataFromSchool(model, this.schoolMetadata);
        this.availabilities = MAvailability.getAll(model);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.columnName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        this.columnCode.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getSubjectId()));
        this.subjectList.setItems(FXCollections.observableList(this.subjectMetadata));
    }

    public void onSubjectListRemovePressed() {
        @Nullable final DBMSubject subject = this.subjectList.getSelectionModel().getSelectedItem();
        if (subject == null) {
            this.notifySelectFirst();
        } else {
            @NotNull Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Konfirmasi");
            alert.setHeaderText(null);
            alert.setContentText("Apakah Anda yakin untuk menghapus mata kuliah ini ? ");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    try {
                        @NotNull final AbstractModel model   = new MSubject(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
                        @NotNull final Session       session = Session.getInstance();
                        if(session.containsKey("klass") && session.containsKey("lecture") && session.containsKey("lesson"))
                        {
                            @NotNull final List<DBMClass>   classMetadata   = (List<DBMClass>) session.get("klass");
                            @NotNull final List<DBMLecture> lectureMetadata = (List<DBMLecture>) session.get("lecture");
                            @NotNull final List<DBMLesson>  lessons         = MLesson.getAllMetadataFromSubject(model, subject, classMetadata, lectureMetadata);
                            MLesson.deleteBunch(model, lessons);
                            @NotNull final List<DBMLesson> lessonMetadata = (List<DBMLesson>) session.get("lesson");
                            lessonMetadata.clear();
                            lessonMetadata.addAll(MLesson.getAllMetadataFromSchool(model, this.schoolMetadata, this.subjectMetadata, classMetadata, lectureMetadata));
                        }
                        MSubject.deleteTimeOff(model, subject);
                        MSubject.delete(model, subject);
                        this.subjectMetadata.clear();
                        this.subjectMetadata.addAll(MSubject.getAllMetadataFromSchool(model, this.schoolMetadata));
                        this.subjectList.setItems(FXCollections.observableList(this.subjectMetadata));
                        this.subjectList.refresh();
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
        alert.setContentText("Pilih salah satu data mata kuliah terlebih dahulu!");
        alert.showAndWait();
    }

    public void onSubjectListAddPressed(ActionEvent actionEvent) {
        @NotNull final Stage dialog = new Stage();
        dialog.setTitle("Tambah Mata Kuliah");

        try {
            dialog.setScene(new Scene(ISubjectCreate.load(new CSubjectCreate(this.schoolMetadata, this.dayMetadata, this.periodMetadata, this.availabilities) {
                @Override
                public void subjectCreated(@NotNull DBMSubject subject) {
                    try {
                        @NotNull final AbstractModel model = new MSubject(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
                        CSubjectList.this.subjectMetadata.clear();
                        CSubjectList.this.subjectMetadata.addAll(MSubject.getAllMetadataFromSchool(model, CSubjectList.this.schoolMetadata));
                        CSubjectList.this.subjectList.setItems(FXCollections.observableList(CSubjectList.this.subjectMetadata));
                        CSubjectList.this.subjectList.refresh();
                    } catch (SQLException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    super.subjectCreated(subject);
                }
            }).load()));
        } catch (IOException ignored) {
        }

        dialog.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
    }

    public void onSubjectListDetailPressed(ActionEvent actionEvent) {
        @Nullable final DBMSubject subjectMetadata = this.subjectList.getSelectionModel().getSelectedItem();
        if (subjectMetadata == null) {
            this.notifySelectFirst();
        } else {
            try {
                @NotNull final AbstractModel model = new MSubject(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
                @NotNull final DBSubject subject = MSubject.getFromMetadata(model, this.schoolMetadata, subjectMetadata);
                MSubject.getTimeOff(model, subject, this.dayMetadata, this.periodMetadata, this.availabilities);
                @NotNull final Stage dialog = new Stage();
                dialog.setTitle("Data Mata Kuliah");

                try {
                    dialog.setScene(new Scene(ISubjectDetail.load(new CSubjectDetail(subject, this.dayMetadata, this.periodMetadata, this.availabilities) {
                        @Override
                        public void subjectUpdated(@NotNull final DBMSubject _subject, @NotNull final String name, @NotNull final String code) {
                            CSubjectList.this.subjectMetadata.stream().filter(vSubject -> vSubject.getId() == _subject.getId()).forEach(vSubject -> {
                                vSubject.setName(name);
                                vSubject.setSubjectId(code);
                            });
                            CSubjectList.this.subjectList.refresh();
                            super.subjectUpdated(_subject, name, code);
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

    public void onSubjectListClosePressed(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }
}
