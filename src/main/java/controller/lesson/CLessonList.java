package controller.lesson;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 25 January 2017, 1:36 PM.
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
import javafx.beans.property.SimpleIntegerProperty;
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
import model.database.component.DBLesson;
import model.database.component.metadata.DBMClass;
import model.database.component.metadata.DBMClassroom;
import model.database.component.metadata.DBMLecture;
import model.database.component.metadata.DBMLesson;
import model.database.component.metadata.DBMSchool;
import model.database.component.metadata.DBMSubject;
import model.database.core.DBType;
import model.database.model.MClass;
import model.database.model.MClassroom;
import model.database.model.MLecture;
import model.database.model.MLesson;
import model.database.model.MSubject;
import model.database.model.MTimetable;
import model.method.pso.hdpso.component.Setting;
import model.util.Dump;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import view.lesson.ILessonCreate;
import view.lesson.ILessonDetail;

@SuppressWarnings({"unused", "WeakerAccess"}) public class CLessonList implements Initializable
{
    @NotNull private final DBMSchool                       schoolMetadata;
    @NotNull private final List<DBMSubject>                subjectMetadata;
    @NotNull private final List<DBMClass>                  classMetadata;
    @NotNull private final List<DBMLecture>                lectureMetadata;
    @NotNull private final List<DBMClassroom>              classroomMetadata;
    @NotNull private final List<DBMLesson>                 lessonMetadata;
    @FXML public           TableView<DBMLesson>            lessonList;
    @FXML public           TableColumn<DBMLesson, String>  columnSubject;
    @FXML public           TableColumn<DBMLesson, String>  columnLecture;
    @FXML public           TableColumn<DBMLesson, String>  columnClass;
    @FXML public           TableColumn<DBMLesson, Integer> columnSKS;
    @FXML public           TableColumn<DBMLesson, Integer> columnTotal;

    public CLessonList(@NotNull final DBMSchool schoolMetadata, @NotNull final List<DBMSubject> subjectMetadata, @NotNull final List<DBMClass> classMetadata, @NotNull final List<DBMLecture> lectureMetadata, @NotNull final List<DBMClassroom> classroomMetadata, @NotNull final List<DBMLesson> lessonMetadata)
    {
        this.schoolMetadata = schoolMetadata;
        this.subjectMetadata = subjectMetadata;
        this.classMetadata = classMetadata;
        this.lectureMetadata = lectureMetadata;
        this.classroomMetadata = classroomMetadata;
        this.lessonMetadata = lessonMetadata;
    }

    public CLessonList() throws UnsupportedEncodingException, SQLException
    {
        @NotNull final AbstractModel model = new MTimetable(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
        this.schoolMetadata = Dump.schoolMetadata();
        this.subjectMetadata = MSubject.getAllMetadataFromSchool(model, this.schoolMetadata);
        this.lectureMetadata = MLecture.getAllMetadataFromSchool(model, this.schoolMetadata);
        this.classMetadata = MClass.getAllMetadataFromSchool(model, this.schoolMetadata);
        this.classroomMetadata = MClassroom.getAllMetadataFromSchool(model, this.schoolMetadata);
        this.lessonMetadata = MLesson.getAllMetadataFromSchool(model, this.schoolMetadata, this.subjectMetadata, this.classMetadata, this.lectureMetadata);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        this.columnSubject.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getSubject().getName()));
        this.columnSKS.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getSks()).asObject());
        this.columnTotal.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getCount()).asObject());
        this.columnLecture.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLecture() == null ? "-" : param.getValue().getLecture().getName()));
        this.columnClass.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getKlass().getName()));
        this.lessonList.setItems(FXCollections.observableList(this.lessonMetadata));
    }

    public void onRemoveLessonListPressed(ActionEvent actionEvent)
    {
        @Nullable final DBMLesson lesson = this.lessonList.getSelectionModel().getSelectedItem();
        if(lesson == null)
        {
            this.notifySelectFirst();
        }
        else
        {
            @NotNull Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Konfirmasi");
            alert.setHeaderText(null);
            alert.setContentText("Apakah Anda yakin untuk menghapus pelajaran ini ? ");

            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent())
            {
                if(result.get() == ButtonType.OK)
                {
                    try
                    {
                        @NotNull final AbstractModel model = new MLesson(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
                        MLesson.deleteAvailableClassroom(model, lesson);
                        MLesson.delete(model, lesson);
                        this.lessonMetadata.clear();
                        this.lessonMetadata.addAll(MLesson.getAllMetadataFromSchool(model, this.schoolMetadata, this.subjectMetadata, this.classMetadata, this.lectureMetadata));
                        this.lessonList.setItems(FXCollections.observableList(this.lessonMetadata));
                        this.lessonList.refresh();
                    }
                    catch(SQLException | UnsupportedEncodingException ignored)
                    {
                        System.err.println("Error Activating Database");
                        System.exit(-1);
                    }
                }
            }
        }
    }

    public void onAddLessonListPressed(ActionEvent actionEvent)
    {
        @NotNull final Stage dialog = new Stage();
        dialog.setTitle("Tambah Pelajaran");

        try
        {
            dialog.setScene(new Scene(ILessonCreate.load(new CLessonCreate(this.schoolMetadata, this.subjectMetadata, this.classMetadata, this.lectureMetadata, this.classroomMetadata)
            {
                @Override public void lessonCreated(@NotNull DBMLesson lessonMetadata, @NotNull DBMSubject subject, int sks, int total, @Nullable DBMLecture lecture, @NotNull DBMClass klass)
                {
                    try
                    {
                        @NotNull final AbstractModel model = new MLecture(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
                        CLessonList.this.lessonMetadata.clear();
                        CLessonList.this.lessonMetadata.addAll(MLesson.getAllMetadataFromSchool(model, CLessonList.this.schoolMetadata, CLessonList.this.subjectMetadata, CLessonList.this.classMetadata, CLessonList.this.lectureMetadata));
                        CLessonList.this.lessonList.setItems(FXCollections.observableArrayList(CLessonList.this.lessonMetadata));
                        CLessonList.this.lessonList.refresh();
                    }
                    catch(SQLException | UnsupportedEncodingException e)
                    {
                        e.printStackTrace();
                    }
                    super.lessonCreated(lessonMetadata, subject, sks, total, lecture, klass);
                }
            }).load()));
        }
        catch(IOException ignored)
        {
        }

        dialog.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
    }

    public void onDetailLessonListPressed(ActionEvent actionEvent)
    {
        @Nullable final DBMLesson lessonMetadata = this.lessonList.getSelectionModel().getSelectedItem();
        if(lessonMetadata == null)
        {
            this.notifySelectFirst();
        }
        else
        {
            try
            {
                @NotNull final AbstractModel model  = new MLesson(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
                @NotNull final DBLesson      lesson = MLesson.getFromMetadata(model, lessonMetadata, this.subjectMetadata, this.classMetadata, this.lectureMetadata);
                MLesson.getAvailableClassroom(model, lesson, this.classroomMetadata);
                @NotNull final Stage dialog = new Stage();
                dialog.setTitle("Detail Pelajaran");

                try
                {
                    dialog.setScene(new Scene(ILessonDetail.load(new CLessonDetail(lesson, this.schoolMetadata, this.classroomMetadata, this.classMetadata, this.lectureMetadata, this.subjectMetadata)
                    {
                        @Override public void lessonEdited(@NotNull final DBMLesson lessonMetadata, @NotNull final DBMSubject _subject, final int _sks, final int _total, @Nullable final DBMLecture _lecture, @NotNull final DBMClass _klass)
                        {
                            CLessonList.this.lessonMetadata.stream().filter(dbmLesson -> dbmLesson.getId() == lessonMetadata.getId()).forEach(dbmLesson ->
                            {
                                dbmLesson.setCount(_total);
                                dbmLesson.setSks(_sks);
                                dbmLesson.setLecture(_lecture);
                                dbmLesson.setSubject(_subject);
                                dbmLesson.setKlass(_klass);
                            });
                            CLessonList.this.lessonList.setItems(FXCollections.observableArrayList(CLessonList.this.lessonMetadata));
                            CLessonList.this.lessonList.refresh();
                            super.lessonEdited(lessonMetadata, _subject, _sks, _total, _lecture, _klass);
                        }
                    }).load()));
                }
                catch(IOException ignored)
                {
                }

                dialog.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.showAndWait();
            }
            catch(SQLException | UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void onCloseLessonListPressed(ActionEvent actionEvent)
    {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }

    @SuppressWarnings("Duplicates")
    private void notifySelectFirst()
    {
        @NotNull final Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText("Pilih salah satu data pelajaran terlebih dahulu!");
        alert.showAndWait();
    }
}
