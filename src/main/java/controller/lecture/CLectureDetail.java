package controller.lecture;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 25 January 2017, 9:07 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectList;
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
import model.database.component.DBLecture;
import model.database.component.DBTimeOff;
import model.database.component.DBTimeOffContainer;
import model.database.component.metadata.DBMDay;
import model.database.component.metadata.DBMLecture;
import model.database.component.metadata.DBMPeriod;
import model.database.component.metadata.DBMSchool;
import model.database.core.DBType;
import model.database.model.*;
import model.method.pso.hdpso.component.Setting;
import model.util.Dump;
import org.jetbrains.annotations.NotNull;
import view.lecture.ILectureEdit;
import view.lecture.ILectureEditTimeOff;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

@SuppressWarnings({"WeakerAccess", "unused"})
public class CLectureDetail implements Initializable {
    @NotNull
    private final DBLecture lecture;
    @NotNull
    private final List<DBMDay> dayMetadata;
    @NotNull
    private final List<DBMPeriod> periodMetadata;
    @NotNull
    private final List<DBAvailability> availabilities;
    @FXML
    public Label lName;
    @FXML
    public GridPane timeOffContainer;
    @NotNull
    final private Label[][] organizedLabel;

    public CLectureDetail(@NotNull final DBLecture lecture, @NotNull final List<DBMDay> dayMetadata, @NotNull final List<DBMPeriod> periodMetadata, @NotNull final List<DBAvailability> availabilities) {
        this.lecture = lecture;
        this.dayMetadata = dayMetadata;
        this.periodMetadata = periodMetadata;
        this.availabilities = availabilities;
        this.organizedLabel = new Label[this.dayMetadata.size()][this.periodMetadata.size()];
    }

    public CLectureDetail() throws UnsupportedEncodingException, SQLException {
        @NotNull final AbstractModel model = new MSchool(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
        @NotNull final DBMSchool schoolMetadata = Dump.schoolMetadata();
        this.dayMetadata = MDay.getAllMetadataFromSchool(model, schoolMetadata);
        this.periodMetadata = MPeriod.getAllMetadataFromSchool(model, schoolMetadata);
        this.availabilities = MAvailability.getAll(model);
        @NotNull final List<DBMLecture> lectureMetadata = MLecture.getAllMetadataFromSchool(model, schoolMetadata);
        if (lectureMetadata.size() > 0) {
            this.lecture = MLecture.getFromMetadata(model, schoolMetadata, lectureMetadata.get(0));
            @NotNull final Int2ObjectMap<DBMDay> mapDay = new Int2ObjectLinkedOpenHashMap<>(this.dayMetadata.size());
            @NotNull final Int2ObjectMap<DBMPeriod> mapPeriod = new Int2ObjectLinkedOpenHashMap<>(this.periodMetadata.size());
            @NotNull final Int2ObjectMap<DBAvailability> mapAvailability = new Int2ObjectLinkedOpenHashMap<>(this.availabilities.size());
            for (@NotNull final DBMDay _day : this.dayMetadata) {
                mapDay.put(_day.getId(), _day);
            }
            for (@NotNull final DBMPeriod _period : this.periodMetadata) {
                mapPeriod.put(_period.getId(), _period);
            }
            for (@NotNull final DBAvailability _availability : this.availabilities) {
                mapAvailability.put(_availability.getId(), _availability);
            }
            MLecture.getTimeOff(model, lecture, mapDay, mapPeriod, mapAvailability);
        } else {
            this.lecture = new DBLecture(-1, "A", schoolMetadata);
            System.exit(0);
        }
        this.organizedLabel = new Label[this.dayMetadata.size()][this.periodMetadata.size()];

    }

    @SuppressWarnings({"Duplicates", "unchecked"})
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.lName.setText(this.lecture.getName());

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
        for (@NotNull final ObjectList<DBTimeOff> day : (ObjectList<ObjectList<DBTimeOff>>) lecture.getTimeoff().getAvailabilities()) {
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

    public void onCloseLectureDetailPressed(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }

    public void onTimeOffEditLectureDetailPressed(ActionEvent actionEvent) {
        @NotNull final Stage dialog = new Stage();
        dialog.setTitle("Edit TimeOff Dosen");

        try {
            dialog.setScene(new Scene(ILectureEditTimeOff.load(new CLectureEditTimeOff(this.lecture, this.dayMetadata, this.periodMetadata, this.availabilities) {
                @Override
                public void timeOffUpdated(@NotNull DBLecture lecture, @NotNull DBTimeOffContainer<DBLecture> timeOff) {
                    @NotNull ObjectList<ObjectList<DBTimeOff>> timeOffs = timeOff.getAvailabilities();
                    for (int c_day = -1, cs_day = timeOffs.size(); ++c_day < cs_day; ) {
                        for (int c_period = -1, cs_period = timeOffs.get(c_day).size(); ++c_period < cs_period; ) {
                            CLectureDetail.this.setTileColor(CLectureDetail.this.organizedLabel[c_day][c_period], timeOffs.get(c_day).get(c_period).getAvailability());
                        }
                    }
                    super.timeOffUpdated(lecture, timeOff);
                }
            }).load()));
        } catch (IOException ignored) {
        }

        dialog.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
    }

    public void onEditLectureDetailPressed(ActionEvent actionEvent) {
        @NotNull final Stage dialog = new Stage();
        dialog.setTitle("Edit Dosen");

        try {
            dialog.setScene(new Scene(ILectureEdit.load(new CLectureEdit(this.lecture) {
                @Override
                public void lectureUpdated(@NotNull DBMLecture lecture, @NotNull String name) {
                    CLectureDetail.this.lName.setText(name);
                    CLectureDetail.this.lectureUpdated(lecture, name);
                    super.lectureUpdated(lecture, name);
                }
            }).load()));
        } catch (IOException ignored) {
        }

        dialog.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
    }

    public void lectureUpdated(@NotNull final DBMLecture lecture, @NotNull final String name) {

    }
}
