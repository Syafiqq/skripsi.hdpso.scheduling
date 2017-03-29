package controller.lesson;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
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
import view.lesson.ILessonEdit;

/*
 * This <skripsi.hdpso.scheduling> project in package <controller.lesson> created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 26 January 2017, 7:14 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings({"WeakerAccess", "unused"}) public class CLessonDetail implements Initializable
{
    @NotNull private final DBMSchool              schoolMetadata;
    @NotNull private final List<DBMSubject>       subjectMetadata;
    @NotNull private final List<DBMClass>         classMetadata;
    @NotNull private final List<DBMLecture>       lectureMetadata;
    @NotNull private final List<DBMClassroom>     classroomMetadata;
    @NotNull private final DBLesson               lesson;
    @FXML public           ListView<DBMClassroom> lvClassroom;
    @FXML public           Label                  lSubject;
    @FXML public           Label                  lLecture;
    @FXML public           Label                  lClass;
    @FXML public           Label                  lSKS;
    @FXML public           Label                  lTotal;

    public CLessonDetail(@NotNull final DBLesson lesson, @NotNull final DBMSchool schoolMetadata, @NotNull final List<DBMClassroom> classroomMetadata, @NotNull final List<DBMClass> classMetadata, @NotNull final List<DBMLecture> lectureMetadata, @NotNull final List<DBMSubject> subjectMetadata)
    {
        this.schoolMetadata = schoolMetadata;
        this.subjectMetadata = subjectMetadata;
        this.classMetadata = classMetadata;
        this.lectureMetadata = lectureMetadata;
        this.classroomMetadata = classroomMetadata;
        this.lesson = lesson;
    }

    public CLessonDetail() throws UnsupportedEncodingException, SQLException
    {
        @Nullable DBLesson           lesson = null;
        @NotNull final AbstractModel model  = new MTimetable(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
        this.schoolMetadata = Dump.schoolMetadata();
        this.subjectMetadata = MSubject.getAllMetadataFromSchool(model, this.schoolMetadata);
        this.lectureMetadata = MLecture.getAllMetadataFromSchool(model, this.schoolMetadata);
        this.classMetadata = MClass.getAllMetadataFromSchool(model, this.schoolMetadata);
        this.classroomMetadata = MClassroom.getAllMetadataFromSchool(model, this.schoolMetadata);
        @NotNull final List<DBMLesson> lessonMetadata = MLesson.getAllMetadataFromSchool(model, this.schoolMetadata, this.subjectMetadata, this.classMetadata, this.lectureMetadata);
        if(lessonMetadata.size() > 0)
        {
            lesson = MLesson.getFromMetadata(model, lessonMetadata.get(0), this.subjectMetadata, this.classMetadata, this.lectureMetadata);
            MLesson.getAvailableClassroom(model, lesson, this.classroomMetadata);
        }
        else
        {
            System.exit(0);
        }
        this.lesson = lesson;
    }

    @Override public void initialize(URL location, ResourceBundle resources)
    {

        this.lvClassroom.setCellFactory(new Callback<ListView<DBMClassroom>, ListCell<DBMClassroom>>()
        {
            @Override public ListCell<DBMClassroom> call(ListView<DBMClassroom> param)
            {
                return new ListCell<DBMClassroom>()
                {
                    @Override protected void updateItem(DBMClassroom item, boolean empty)
                    {
                        super.updateItem(item, empty);
                        if(empty || item == null)
                        {
                            super.setText(null);
                            super.setGraphic(null);
                        }
                        else
                        {
                            super.setText(item.getName());
                        }
                    }
                };
            }
        });
        this.setProperties(this.lesson.getSubject(), this.lesson.getKlass(), this.lesson.getLecture(), this.lesson.getSks(), this.lesson.getCount(), this.lesson.getClassrooms());
    }

    private void setProperties(@NotNull DBMSubject subject, @NotNull DBMClass klass, @Nullable DBMLecture lecture, int sks, int count, @NotNull List<DBMClassroom> classrooms)
    {
        this.lSubject.setText(subject.getName());
        this.lClass.setText(klass.getName());
        this.lLecture.setText(lecture == null ? "-" : lecture.getName());
        this.lSKS.setText(String.valueOf(sks));
        this.lTotal.setText(String.valueOf(count));
        this.lvClassroom.setItems(FXCollections.observableArrayList(classrooms));
        this.lvClassroom.refresh();
    }

    public void onCloseLessonDetailPressed(ActionEvent actionEvent)
    {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }

    public void onEditLessonDetailPressed(ActionEvent actionEvent)
    {
        @NotNull final Stage dialog = new Stage();
        dialog.setTitle("Edit Pelajaran");

        try
        {
            dialog.setScene(new Scene(ILessonEdit.load(new CLessonEdit(this.lesson, this.schoolMetadata, this.subjectMetadata, this.classMetadata, this.lectureMetadata, this.classroomMetadata)
            {
                @Override public void lessonEdited(@NotNull DBLesson lesson, @NotNull DBMSubject subject, int sks, int total, @Nullable DBMLecture lecture, @NotNull DBMClass klass, List<DBMClassroom> classrooms)
                {
                    CLessonDetail.this.setProperties(subject, klass, lecture, sks, total, classrooms);
                    CLessonDetail.this.lessonEdited(lesson, subject, sks, total, lecture, klass);
                    super.lessonEdited(lesson, subject, sks, total, lecture, klass, classrooms);
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

    public void lessonEdited(@NotNull final DBMLesson lessonMetadata, @NotNull final DBMSubject subject, int sks, int total, @Nullable final DBMLecture lecture, @NotNull final DBMClass klass)
    {
    }
}
