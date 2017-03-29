package controller.lesson;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
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
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * This <skripsi.hdpso.scheduling> project in package <controller.lesson> created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 26 January 2017, 7:57 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings({"WeakerAccess", "unused", "Duplicates"})
public class CLessonEdit implements Initializable
{
    @NotNull private final DBMSchool                         schoolMetadata;
    @NotNull private final List<DBMSubject>                  subjectMetadata;
    @NotNull private final List<DBMClass>                    classMetadata;
    @NotNull private final List<DBMLecture>                  lectureMetadata;
    @NotNull private final List<DBMClassroom>                classroomMetadata;
    @NotNull private final List<DBMClassroom>                listStock;
    @NotNull private final List<DBMClassroom>                listUsage;
    @NotNull private final DBLesson                          lesson;
    @FXML public           TableView<DBMClassroom>           tUsage;
    @FXML public           TableColumn<DBMClassroom, String> tcUsage;
    @FXML public           TableView<DBMClassroom>           tStock;
    @FXML public           TableColumn<DBMClassroom, String> tcStock;
    @FXML public           ComboBox<DBMSubject>              cbSubject;
    @FXML public           ComboBox<DBMLecture>              cbLecture;
    @FXML public           ComboBox<DBMClass>                cbClass;
    @FXML public           TextField                         tfSKS;
    @FXML public           TextField                         tfTotal;

    public CLessonEdit(@NotNull final DBLesson lesson, @NotNull final DBMSchool schoolMetadata, @NotNull final List<DBMSubject> subjectMetadata, @NotNull final List<DBMClass> classMetadata, @NotNull final List<DBMLecture> lectureMetadata, @NotNull final List<DBMClassroom> classroomMetadata)
    {
        this.schoolMetadata = schoolMetadata;
        this.subjectMetadata = subjectMetadata;
        this.classMetadata = classMetadata;
        this.lectureMetadata = lectureMetadata;
        this.classroomMetadata = classroomMetadata;
        this.lesson = lesson;
        this.listStock = new ArrayList<>(this.classroomMetadata.size());
        this.listUsage = new ArrayList<>(this.classroomMetadata.size());
    }

    public CLessonEdit() throws UnsupportedEncodingException, SQLException
    {
        @Nullable DBLesson           lesson = null;
        @NotNull final AbstractModel model  = new MTimetable(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
        this.schoolMetadata = Dump.schoolMetadata();
        this.subjectMetadata = MSubject.getAllMetadataFromSchool(model, this.schoolMetadata);
        this.lectureMetadata = MLecture.getAllMetadataFromSchool(model, this.schoolMetadata);
        this.classMetadata = MClass.getAllMetadataFromSchool(model, this.schoolMetadata);
        this.classroomMetadata = MClassroom.getAllMetadataFromSchool(model, this.schoolMetadata);
        this.listStock = new ArrayList<>(this.classroomMetadata.size());
        this.listUsage = new ArrayList<>(this.classroomMetadata.size());
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
        this.cbSubject.setConverter(new StringConverter<DBMSubject>()
        {
            @Override public String toString(DBMSubject object)
            {
                return object.getName();
            }

            @Override public DBMSubject fromString(String string)
            {
                return null;
            }
        });
        this.cbClass.setConverter(new StringConverter<DBMClass>()
        {
            @Override public String toString(DBMClass object)
            {
                return object.getName();
            }

            @Override public DBMClass fromString(String string)
            {
                return null;
            }
        });
        this.cbLecture.setConverter(new StringConverter<DBMLecture>()
        {
            @Override public String toString(DBMLecture object)
            {
                return object.getName();
            }

            @Override public DBMLecture fromString(String string)
            {
                return null;
            }
        });
        this.cbSubject.setItems(FXCollections.observableArrayList(this.subjectMetadata));
        this.cbClass.setItems(FXCollections.observableArrayList(this.classMetadata));
        this.cbLecture.setItems(FXCollections.observableArrayList(this.lectureMetadata));
        this.cbSubject.getSelectionModel().select(this.lesson.getSubject());
        this.cbClass.getSelectionModel().select(this.lesson.getKlass());
        this.cbLecture.getSelectionModel().select(this.lesson.getLecture());
        this.tfSKS.setText(String.valueOf(this.lesson.getSks()));
        this.tfTotal.setText(String.valueOf(this.lesson.getCount()));
        this.tcUsage.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        this.tcStock.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        this.listStock.addAll(this.classroomMetadata);
        this.listStock.removeAll(this.lesson.getClassrooms());
        this.listUsage.addAll(this.lesson.getClassrooms());
        this.tUsage.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.tStock.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.refreshTable();
    }

    private void refreshTable()
    {
        this.tUsage.setItems(FXCollections.observableArrayList(this.listUsage));
        this.tStock.setItems(FXCollections.observableArrayList(this.listStock));
        this.tStock.refresh();
        this.tUsage.refresh();
    }

    public void onCloseLessonEditPressed(ActionEvent actionEvent)
    {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }

    public void onSaveLessonEditPressed(ActionEvent actionEvent)
    {
        try
        {
            @Nullable final DBMSubject subject = this.cbSubject.getSelectionModel().getSelectedItem();
            @Nullable final DBMClass   klass   = this.cbClass.getSelectionModel().getSelectedItem();
            @Nullable final DBMLecture lecture = this.cbLecture.getSelectionModel().getSelectedItem();
            @Nullable final Number     sks     = NumberUtils.createNumber(this.tfSKS.getText());
            @Nullable final Number     total   = NumberUtils.createNumber(this.tfTotal.getText());
            if((subject != null) && (klass != null) && (sks != null) && (total != null))
            {
                this.lesson.setSubject(subject);
                this.lesson.setKlass(klass);
                this.lesson.setLecture(lecture);
                this.lesson.setSks(sks.intValue());
                this.lesson.setCount(total.intValue());
                this.lesson.getClassrooms().clear();
                this.lesson.getClassrooms().addAll(this.listUsage);
                try
                {
                    @NotNull final AbstractModel model = new MLesson(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
                    MLesson.deleteAvailableClassroom(model, this.lesson);
                    MLesson.updateMetadata(model, this.lesson);
                    MLesson.insertAvailableClassroom(model, this.lesson);
                    this.lessonEdited(this.lesson, subject, sks.intValue(), total.intValue(), lecture, klass, this.listUsage);
                    this.onCloseLessonEditPressed(actionEvent);
                }
                catch(SQLException | UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                this.notifyEmptyEntry();
            }
        }
        catch(NumberFormatException ignored)
        {
            this.notifyEmptyEntry();
        }
    }

    private void notifyEmptyEntry()
    {
        @NotNull final Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Perhatian!");
        alert.setHeaderText(null);
        alert.setContentText("Silahkan Isi data yang masih kosong");
        alert.showAndWait();
    }

    public void lessonEdited(@NotNull final DBLesson lesson, @NotNull final DBMSubject subject, int sks, int total, @Nullable final DBMLecture lecture, @NotNull final DBMClass klass, List<DBMClassroom> listUsage)
    {

    }

    public void onUsageRemove()
    {
        final ObservableList<DBMClassroom> selected = this.tUsage.getSelectionModel().getSelectedItems();
        this.listStock.addAll(selected);
        this.listUsage.removeAll(selected);
        this.refreshTable();
    }

    public void onUsageRemoveAll()
    {
        this.listStock.addAll(this.listUsage);
        this.listUsage.clear();
        this.refreshTable();
    }

    public void onStockRemove()
    {
        final ObservableList<DBMClassroom> selected = this.tStock.getSelectionModel().getSelectedItems();
        this.listUsage.addAll(selected);
        this.listStock.removeAll(selected);
        this.refreshTable();
    }

    public void onStockRemoveAll()
    {
        this.listUsage.addAll(this.listStock);
        this.listStock.clear();
        this.refreshTable();
    }

    public void onLectureClearSelection()
    {
        this.cbLecture.getSelectionModel().clearSelection();
    }
}
