package controller.subject;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 23 January 2017, 3:38 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import it.unimi.dsi.fastutil.objects.ObjectList;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.AbstractModel;
import model.database.component.DBAvailability;
import model.database.component.DBSubject;
import model.database.component.DBTimeOff;
import model.database.component.DBTimeOffContainer;
import model.database.component.metadata.DBMDay;
import model.database.component.metadata.DBMPeriod;
import model.database.component.metadata.DBMSchool;
import model.database.component.metadata.DBMSubject;
import model.database.core.DBType;
import model.database.model.MAvailability;
import model.database.model.MDay;
import model.database.model.MPeriod;
import model.database.model.MSubject;
import model.database.model.MTimetable;
import model.method.pso.hdpso.component.Setting;
import model.util.Dump;
import org.jetbrains.annotations.NotNull;
import view.subject.ISubjectEdit;
import view.subject.ISubjectEditTimeOff;

@SuppressWarnings({"UnusedParameters", "WeakerAccess"})
public class CSubjectDetail implements Initializable {
    @NotNull
    private final DBSubject            subject;
    @NotNull
    private final List<DBMDay>         dayMetadata;
    @NotNull
    private final List<DBMPeriod>      periodMetadata;
    @NotNull
    private final List<DBAvailability> availabilities;
    @NotNull
    final private Label[][]            organizedLabel;
    @FXML
    public        Label                lName;
    @FXML
    public        Label                lCode;
    @FXML
    public        GridPane             timeOffContainer;

    public CSubjectDetail(@NotNull final DBSubject subject, @NotNull final List<DBMDay> dayMetadata, @NotNull final List<DBMPeriod> periodMetadata, @NotNull final List<DBAvailability> availabilities) {
        this.subject = subject;
        this.dayMetadata = dayMetadata;
        this.periodMetadata = periodMetadata;
        this.availabilities = availabilities;
        this.organizedLabel = new Label[this.dayMetadata.size()][this.periodMetadata.size()];
    }

    public CSubjectDetail() throws UnsupportedEncodingException, SQLException {
        @NotNull final AbstractModel model          = new MTimetable(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
        @NotNull final DBMSchool     schoolMetadata = Dump.schoolMetadata();
        this.dayMetadata = MDay.getAllMetadataFromSchool(model, schoolMetadata);
        this.periodMetadata = MPeriod.getAllMetadataFromSchool(model, schoolMetadata);
        this.availabilities = MAvailability.getAll(model);
        @NotNull final List<DBMSubject> subjectMetadata = MSubject.getAllMetadataFromSchool(model, schoolMetadata);
        if (subjectMetadata.size() > 0) {
            this.subject = MSubject.getFromMetadata(model, schoolMetadata, subjectMetadata.get(0));
            MSubject.getTimeOff(model, subject, this.dayMetadata, this.periodMetadata, this.availabilities);
        } else {
            this.subject = new DBSubject(-1, "A", "B", schoolMetadata);
            System.exit(0);
        }
        this.organizedLabel = new Label[this.dayMetadata.size()][this.periodMetadata.size()];

    }

    @SuppressWarnings({"Duplicates", "unchecked"})
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.lName.setText(this.subject.getName());
        this.lCode.setText(this.subject.getSubjectId());

        @NotNull final Label lb = new Label("H\\P");
        lb.setPrefHeight(50);
        lb.setMinHeight(50);
        this.timeOffContainer.add(lb, 0, 0);
        int tileCounter = 0;
        for (DBMPeriod aPeriodMetadata : this.periodMetadata) {
            @NotNull final Label label = new Label(aPeriodMetadata.getNickname());
            label.setStyle("-fx-rotate: -90");
            label.setPrefSize(20, 50);
            this.timeOffContainer.add(label, ++tileCounter, 0);
        }
        tileCounter = 0;
        for (DBMDay aDayMetadata : this.dayMetadata) {
            @NotNull final Label label = new Label(aDayMetadata.getNickname());
            label.setPrefHeight(20);
            this.timeOffContainer.add(label, 0, ++tileCounter);
        }
        int c_day = 0;
        for (@NotNull final ObjectList<DBTimeOff> day : (ObjectList<ObjectList<DBTimeOff>>) subject.getTimeoff().getAvailabilities()) {
            ++c_day;
            int c_period = 0;
            for (@NotNull final DBTimeOff period : day) {
                @NotNull final Label tile = new Label();
                this.setTileColor(tile, period.getAvailability());
                tile.setPrefSize(20, 20);
                this.timeOffContainer.add(tile, ++c_period, c_day);
                this.organizedLabel[c_day - 1][c_period - 1] = tile;
            }
        }
    }

    @SuppressWarnings("Duplicates")
    private void setTileColor(@NotNull final Label tile, @NotNull final DBAvailability availability) {
        switch (availability.getId()) {
            case 1: {
                tile.setStyle("-fx-background-color: #F44336");
            }
            break;
            case 2: {
                tile.setStyle("-fx-background-color: #4CAF50");
            }
            break;
            default: {
                tile.setStyle("-fx-background-color: #FFEB3B");
            }
            break;
        }
    }

    public void onCloseSubjectDetailPressed(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }

    public void onTimeOffEditSubjectDetailPressed(ActionEvent actionEvent) {
        @NotNull final Stage dialog = new Stage();
        dialog.setTitle("Edit TimeOff Mata Kuliah");

        try {
            dialog.setScene(new Scene(ISubjectEditTimeOff.load(new CSubjectEditTimeOff(this.subject, this.dayMetadata, this.periodMetadata, this.availabilities) {
                @Override
                public void timeOffUpdated(@NotNull DBSubject subject, @NotNull DBTimeOffContainer<DBSubject> timeOff) {
                    @NotNull ObjectList<ObjectList<DBTimeOff>> timeOffs = timeOff.getAvailabilities();
                    for (int c_day = -1, cs_day = timeOffs.size(); ++c_day < cs_day; ) {
                        for (int c_period = -1, cs_period = timeOffs.get(c_day).size(); ++c_period < cs_period; ) {
                            CSubjectDetail.this.setTileColor(CSubjectDetail.this.organizedLabel[c_day][c_period], timeOffs.get(c_day).get(c_period).getAvailability());
                        }
                    }
                    super.timeOffUpdated(subject, timeOff);
                }
            }).load()));
        } catch (IOException ignored) {
        }

        dialog.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
    }

    public void onEditSubjectDetailPressed(ActionEvent actionEvent) {
        @NotNull final Stage dialog = new Stage();
        dialog.setTitle("Edit Mata Kuliah");

        try {
            dialog.setScene(new Scene(ISubjectEdit.load(new CSubjectEdit(this.subject) {
                @Override
                public void subjectUpdated(@NotNull DBMSubject subject, @NotNull String name, @NotNull String code) {
                    CSubjectDetail.this.lName.setText(name);
                    CSubjectDetail.this.lCode.setText(code);
                    CSubjectDetail.this.subjectUpdated(subject, name, code);
                    super.subjectUpdated(subject, name, code);
                }
            }).load()));
        } catch (IOException ignored) {
        }

        dialog.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
    }

    public void subjectUpdated(@NotNull final DBMSubject subject, @NotNull final String name, @NotNull final String code) {

    }
}
