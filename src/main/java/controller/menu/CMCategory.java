package controller.menu;

import controller.classroom.CClassroomList;
import controller.klass.CClassList;
import controller.lecture.CLectureList;
import controller.lesson.CLessonList;
import controller.school.CSchoolDetail;
import controller.subject.CSubjectList;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.AbstractModel;
import model.database.component.DBAvailability;
import model.database.component.DBSchool;
import model.database.component.metadata.*;
import model.database.core.DBType;
import model.database.model.MSchool;
import model.method.pso.hdpso.component.Setting;
import model.util.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import view.classroom.IClassroomList;
import view.klass.IClassList;
import view.lecture.ILectureList;
import view.lesson.ILessonList;
import view.school.ISchoolDetail;
import view.subject.ISubjectList;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Observer;
import java.util.ResourceBundle;

/*
 * This <skripsi.hdpso.scheduling> project in package <controller.menu> created by : 
 * Name         : syafiq
 * Date / Time  : 01 January 2017, 4:29 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class CMCategory implements Initializable {
    @Nullable
    private final Observer content;
    @FXML
    public Button mc_button_generate;
    @FXML
    public Button bSchool;
    @FXML
    public Button bSubject;
    @FXML
    public Button bClass;
    @FXML
    public Button bClassroom;
    @FXML
    public Button bLecture;
    @FXML
    public Button bLesson;

    @Nullable
    private DBMSchool schoolMetadata;

    @Nullable
    private List<DBMDay> dayMetadata;

    @Nullable
    private List<DBMPeriod> periodMetadata;

    @Nullable
    private List<DBAvailability> availability;

    @Nullable
    private List<DBMSubject> subjectMetadata;

    @Nullable
    private List<DBMClass> classMetadata;

    @Nullable
    private List<DBMClassroom> classroomMetadata;

    @Nullable
    private List<DBMLecture> lectureMetadata;

    @Nullable
    private List<DBMLesson> lessonMetadata;


    public CMCategory() {
        this(null);
    }


    @SuppressWarnings("unchecked")
    public CMCategory(@Nullable Observer rootContentCallback) {
        this.content = rootContentCallback;
        this.setData();
    }

    @SuppressWarnings("unchecked")
    private void setData() {
        final Session session = Session.getInstance();
        this.schoolMetadata = (DBMSchool) session.get("school");
        this.dayMetadata = (List<DBMDay>) session.get("day");
        this.periodMetadata = (List<DBMPeriod>) session.get("period");
        this.availability = (List<DBAvailability>) session.get("availability");
        this.subjectMetadata = (List<DBMSubject>) session.get("subject");
        this.classMetadata = (List<DBMClass>) session.get("klass");
        this.classroomMetadata = (List<DBMClassroom>) session.get("classroom");
        this.lectureMetadata = (List<DBMLecture>) session.get("lecture");
        this.lectureMetadata = (List<DBMLecture>) session.get("lesson");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.bindSchoolData();

    }

    private void bindSchoolData() {
        @NotNull final BooleanProperty schoolExistenceListener = new SimpleBooleanProperty(true);
        @NotNull final Observer schoolExistenceObserver = (o, arg) -> {
            schoolExistenceListener.setValue(!Session.getInstance().containsKey("school"));
            if (arg != null) {
                if (arg instanceof String) {
                    if (((String) arg).contentEquals("school")) {
                        CMCategory.this.setData();
                    }
                }
            }
        };
        Session.getInstance().addObserver(schoolExistenceObserver);
        this.mc_button_generate.disableProperty().bind(schoolExistenceListener);
        this.bSchool.disableProperty().bind(schoolExistenceListener);
        this.bSubject.disableProperty().bind(schoolExistenceListener);
        this.bClass.disableProperty().bind(schoolExistenceListener);
        this.bClassroom.disableProperty().bind(schoolExistenceListener);
        this.bLecture.disableProperty().bind(schoolExistenceListener);
        this.bLesson.disableProperty().bind(schoolExistenceListener);
    }

    public void onSchoolButtonPressed(ActionEvent actionEvent) {
        if ((this.schoolMetadata != null) && (this.dayMetadata != null) && (this.periodMetadata != null)) {
            @NotNull final Stage dialog = new Stage();
            dialog.setTitle("Detail Jadwal");

            try {

                @NotNull final AbstractModel model = new MSchool(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
                @NotNull final DBSchool school = MSchool.getFromMetadata(model, this.schoolMetadata);
                dialog.setScene(new Scene(ISchoolDetail.load(new CSchoolDetail(school, this.dayMetadata, this.periodMetadata) {
                    @Override
                    public void schoolUpdated(String name, String nickname, String address, String academicYear, int semester, int activeDay, int activePeriod) {
                        if (CMCategory.this.schoolMetadata != null) {
                            CMCategory.this.schoolMetadata.setName(name);
                            CMCategory.this.schoolMetadata.setAcademicYear(academicYear);
                            CMCategory.this.schoolMetadata.setSemester(semester);
                            CMCategory.this.schoolMetadata.setActiveDay(activeDay);
                            CMCategory.this.schoolMetadata.setActivePeriod(activePeriod);
                        }
                        super.schoolUpdated(name, nickname, address, academicYear, semester, activeDay, activePeriod);
                    }
                }).load()));
            } catch (IOException | SQLException ignored) {
                ignored.printStackTrace();
            }

            dialog.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();
        }
    }

    public void onMenuCategoryGenerateClick(ActionEvent actionEvent) {

    }

    public void onSubjectButtonPressed(ActionEvent actionEvent) {
        if ((this.schoolMetadata != null) && (this.dayMetadata != null) && (this.periodMetadata != null) && (this.subjectMetadata != null) && (this.availability != null)) {
            @NotNull final Stage dialog = new Stage();
            dialog.setTitle("Daftar Mata Kuliah");
            try {
                dialog.setScene(new Scene(ISubjectList.load(new CSubjectList(this.schoolMetadata, this.dayMetadata, this.periodMetadata, this.availability, this.subjectMetadata)).load()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            dialog.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();
        }
    }

    public void onClassButtonPressed(ActionEvent actionEvent) {
        if ((this.schoolMetadata != null) && (this.dayMetadata != null) && (this.periodMetadata != null) && (this.classMetadata != null) && (this.availability != null)) {
            @NotNull final Stage dialog = new Stage();
            dialog.setTitle("Daftar Kelas");
            try {
                dialog.setScene(new Scene(IClassList.load(new CClassList(this.schoolMetadata, this.dayMetadata, this.periodMetadata, this.availability, this.classMetadata)).load()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            dialog.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();
        }
    }

    public void onClassroomButtonPressed(ActionEvent actionEvent) {
        if ((this.schoolMetadata != null) && (this.dayMetadata != null) && (this.periodMetadata != null) && (this.classroomMetadata != null) && (this.availability != null)) {
            @NotNull final Stage dialog = new Stage();
            dialog.setTitle("Daftar Ruangan");
            try {
                dialog.setScene(new Scene(IClassroomList.load(new CClassroomList(this.schoolMetadata, this.dayMetadata, this.periodMetadata, this.availability, this.classroomMetadata)).load()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            dialog.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();
        }
    }

    public void onLectureButtonPressed(ActionEvent actionEvent) {
        if ((this.schoolMetadata != null) && (this.dayMetadata != null) && (this.periodMetadata != null) && (this.lectureMetadata != null) && (this.availability != null)) {
            @NotNull final Stage dialog = new Stage();
            dialog.setTitle("Daftar Dosen");
            try {
                dialog.setScene(new Scene(ILectureList.load(new CLectureList(this.schoolMetadata, this.dayMetadata, this.periodMetadata, this.availability, this.lectureMetadata)).load()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            dialog.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();
        }
    }

    public void onLessonButtonPressed(ActionEvent actionEvent) {
        if ((this.schoolMetadata != null) && (this.subjectMetadata != null) && (this.classMetadata != null) && (this.lectureMetadata != null) && (this.classroomMetadata != null) && (this.lessonMetadata != null)) {
            @NotNull final Stage dialog = new Stage();
            dialog.setTitle("Daftar Pelajaran");
            try {
                dialog.setScene(new Scene(ILessonList.load(new CLessonList(this.schoolMetadata, this.subjectMetadata, this.classMetadata, this.lectureMetadata, this.classroomMetadata, this.lessonMetadata)).load()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            dialog.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();
        }
    }
}