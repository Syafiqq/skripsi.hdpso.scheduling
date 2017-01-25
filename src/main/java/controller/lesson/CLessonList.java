package controller.lesson;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 25 January 2017, 1:36 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.AbstractModel;
import model.database.component.metadata.*;
import model.database.core.DBType;
import model.database.model.*;
import model.method.pso.hdpso.component.Setting;
import model.util.Dump;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class CLessonList implements Initializable {
    @NotNull private final DBMSchool schoolMetadata;
    @NotNull private final List<DBMSubject> subjectMetadata;
    @NotNull private final List<DBMClass> classMetadata;
    @NotNull private final List<DBMLecture> lectureMetadata;
    @NotNull private final List<DBMClassroom> classroomMetadata;
    @NotNull private final List<DBMLesson> lessonMetadata;
    @FXML public TableView<DBMLesson> lectureList;
    @FXML public TableColumn<DBMLesson, String> columnSubject;
    @FXML public TableColumn<DBMLesson, String> columnLecture;
    @FXML public TableColumn<DBMLesson, String> columnClass;
    @FXML public TableColumn<DBMLesson, Integer> columnSKS;
    @FXML public TableColumn<DBMLesson, Integer> columnTotal;

    public CLessonList(@NotNull final DBMSchool schoolMetadata, @NotNull final List<DBMSubject> subjectMetadata, @NotNull final List<DBMClass> classMetadata, @NotNull final List<DBMLecture> lectureMetadata, @NotNull final List<DBMClassroom> classroomMetadata, @NotNull final List<DBMLesson> lessonMetadata) {
        this.schoolMetadata = schoolMetadata;
        this.subjectMetadata = subjectMetadata;
        this.classMetadata = classMetadata;
        this.lectureMetadata = lectureMetadata;
        this.classroomMetadata = classroomMetadata;
        this.lessonMetadata = lessonMetadata;
    }

    public CLessonList() throws UnsupportedEncodingException, SQLException {
        @NotNull final AbstractModel model = new MSchool(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
        this.schoolMetadata = Dump.schoolMetadata();
        this.subjectMetadata = MSubject.getAllMetadataFromSchool(model, this.schoolMetadata);
        this.lectureMetadata = MLecture.getAllMetadataFromSchool(model, this.schoolMetadata);
        this.classMetadata = MClass.getAllMetadataFromSchool(model, this.schoolMetadata);
        this.classroomMetadata = MClassroom.getAllMetadataFromSchool(model, this.schoolMetadata);
        this.lessonMetadata = MLesson.getAllMetadataFromSchool(model, this.schoolMetadata, this.subjectMetadata, this.classMetadata, this.lectureMetadata);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.columnSubject.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getSubject().getName()));
        this.columnSKS.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getSks()).asObject());
        this.columnTotal.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getCount()).asObject());
        this.columnLecture.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLecture() == null ? "-" : param.getValue().getLecture().getName()));
        this.columnClass.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getClass().getName()));
        this.lectureList.setItems(FXCollections.observableList(this.lessonMetadata));
    }

    public void onRemoveLessonListPressed(ActionEvent actionEvent) {

    }

    public void onAddLessonListPressed(ActionEvent actionEvent) {

    }

    public void onDetailLessonListPressed(ActionEvent actionEvent) {

    }

    public void onCloseLessonListPressed(ActionEvent actionEvent) {

    }
}
