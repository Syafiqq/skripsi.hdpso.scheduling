package controller.classroom;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 25 January 2017, 8:06 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import it.unimi.dsi.fastutil.ints.Int2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import model.AbstractModel;
import model.custom.javafx.scene.control.CoordinatedLabel;
import model.database.component.DBAvailability;
import model.database.component.DBClassroom;
import model.database.component.DBTimeOff;
import model.database.component.DBTimeOffContainer;
import model.database.component.metadata.DBMClassroom;
import model.database.component.metadata.DBMDay;
import model.database.component.metadata.DBMPeriod;
import model.database.component.metadata.DBMSchool;
import model.database.core.DBType;
import model.database.model.MAvailability;
import model.database.model.MClassroom;
import model.database.model.MDay;
import model.database.model.MPeriod;
import model.database.model.MTimetable;
import model.method.pso.hdpso.component.Setting;
import model.util.Dump;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("WeakerAccess")
public class CClassroomEditTimeOff implements Initializable {
    @NotNull
    private final DBClassroom classroom;
    @NotNull
    private final List<DBMDay> dayMetadata;
    @NotNull
    private final List<DBMPeriod> periodMetadata;
    @NotNull
    private final List<DBAvailability> availabilities;
    @NotNull
    private final Int2IntMap availabilitiesLookup;
    @FXML
    public GridPane timeOffContainer;

    public CClassroomEditTimeOff(@NotNull final DBClassroom classroom, @NotNull final List<DBMDay> dayMetadata, @NotNull final List<DBMPeriod> periodMetadata, @NotNull final List<DBAvailability> availabilities) {
        this.classroom = classroom;
        this.dayMetadata = dayMetadata;
        this.periodMetadata = periodMetadata;
        this.availabilities = availabilities;
        this.availabilitiesLookup = new Int2IntLinkedOpenHashMap(this.availabilities.size());
        this.setAvailabilityLookup();
    }

    public CClassroomEditTimeOff() throws UnsupportedEncodingException, SQLException {
        @NotNull final AbstractModel model          = new MTimetable(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
        @NotNull final DBMSchool     schoolMetadata = Dump.schoolMetadata();
        this.dayMetadata = MDay.getAllMetadataFromSchool(model, schoolMetadata);
        this.periodMetadata = MPeriod.getAllMetadataFromSchool(model, schoolMetadata);
        this.availabilities = MAvailability.getAll(model);
        @NotNull final List<DBMClassroom> classroomMetadata = MClassroom.getAllMetadataFromSchool(model, schoolMetadata);
        if (classroomMetadata.size() > 0) {
            this.classroom = MClassroom.getFromMetadata(model, schoolMetadata, classroomMetadata.get(0));
            MClassroom.getTimeOff(model, classroom, this.dayMetadata, this.periodMetadata, this.availabilities);
        } else {
            this.classroom = new DBClassroom(-1, "A", schoolMetadata);
            System.exit(0);
        }
        this.availabilitiesLookup = new Int2IntLinkedOpenHashMap(this.availabilities.size());
        this.setAvailabilityLookup();
    }

    private void setAvailabilityLookup() {
        for (int c_availability = -1, cs_availability = this.availabilities.size(); ++c_availability < cs_availability; ) {
            @NotNull final DBAvailability availability = this.availabilities.get(c_availability);
            this.availabilitiesLookup.put(availability.getId(), c_availability);
        }
    }


    @SuppressWarnings({"Duplicates", "unchecked"})
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        @NotNull final Label[][] organizedLabel = new Label[this.dayMetadata.size()][this.periodMetadata.size()];

        @NotNull final EventHandler<MouseEvent> tileClicked = event -> {
            if (event.getSource() instanceof CoordinatedLabel) {
                @NotNull final CoordinatedLabel label = (CoordinatedLabel) event.getSource();
                controller.classroom.CClassroomEditTimeOff.this.processChanged(organizedLabel, label.getRow(), label.getColumn());
            }
        };

        @NotNull final Label lbl = new CoordinatedLabel("H\\P", 0, 0);
        lbl.setOnMouseClicked(tileClicked);
        this.timeOffContainer.add(lbl, 0, 0);
        int tileCounter = 0;
        for (DBMPeriod aPeriodMetadata : this.periodMetadata) {
            ++tileCounter;
            @NotNull final Label label = new CoordinatedLabel(aPeriodMetadata.getNickname(), 0, tileCounter);
            label.setPrefWidth(20);
            label.setStyle("-fx-rotate: -90");
            label.setOnMouseClicked(tileClicked);
            this.timeOffContainer.add(label, tileCounter, 0);
        }
        tileCounter = 0;
        for (DBMDay aDayMetadata : this.dayMetadata) {
            ++tileCounter;
            @NotNull final Label label = new CoordinatedLabel(aDayMetadata.getNickname(), tileCounter, 0);
            label.setPrefHeight(20);
            label.setOnMouseClicked(tileClicked);
            this.timeOffContainer.add(label, 0, tileCounter);
        }
        int c_day = 0;
        for (@NotNull final ObjectList<DBTimeOff> day : (ObjectList<ObjectList<DBTimeOff>>) classroom.getTimeoff().getAvailabilities()) {
            ++c_day;
            int c_period = 0;
            for (@NotNull final DBTimeOff period : day) {
                ++c_period;
                @NotNull final Label tile = new CoordinatedLabel(c_day, c_period);
                this.setTileColor(tile, period.getAvailability());
                tile.setPrefSize(20, 20);
                tile.setOnMouseClicked(tileClicked);
                this.timeOffContainer.add(tile, c_period, c_day);
                organizedLabel[c_day - 1][c_period - 1] = tile;
            }
        }
    }

    @SuppressWarnings("Duplicates")
    private void processChanged(@NotNull final Label[][] organizedLabel, int row, int column) {
        if ((row == 0) && (column == 0)) {
            if ((this.dayMetadata.size() > 0) && (this.periodMetadata.size() > 0)) {
                this.changeAll(organizedLabel);
            }
        } else if (column == 0) {
            if (this.periodMetadata.size() > 0) {
                this.changeRow(organizedLabel, row - 1);
            }
        } else if (row == 0) {
            if (this.dayMetadata.size() > 0) {
                this.changeColumn(organizedLabel, column - 1);
            }
        } else {
            this.changeTile(organizedLabel, row - 1, column - 1);
        }
    }

    @SuppressWarnings("unchecked")
    private void changeTile(@NotNull final Label[][] labels, int c_day, int c_period) {
        @NotNull ObjectList<ObjectList<DBTimeOff>> timeOffs = this.classroom.getTimeoff().getAvailabilities();
        @NotNull final DBAvailability availability = this.availabilities.get((this.availabilitiesLookup.get(timeOffs.get(c_day).get(c_period).getAvailability().getId()) + 1) % this.availabilities.size());
        @NotNull final DBTimeOff timeOff = timeOffs.get(c_day).get(c_period);
        timeOff.setAvailability(availability);
        this.setTileColor(labels[c_day][c_period], timeOff.getAvailability());
    }

    @SuppressWarnings("unchecked")
    private void changeColumn(@NotNull final Label[][] labels, int c_period) {
        @NotNull ObjectList<ObjectList<DBTimeOff>> timeOffs = this.classroom.getTimeoff().getAvailabilities();
        @NotNull final DBAvailability availability = this.availabilities.get((this.availabilitiesLookup.get(timeOffs.get(0).get(c_period).getAvailability().getId()) + 1) % this.availabilities.size());
        for (int c_day = -1, cs_day = timeOffs.size(); ++c_day < cs_day; ) {
            @NotNull final DBTimeOff timeOff = timeOffs.get(c_day).get(c_period);
            timeOff.setAvailability(availability);
            this.setTileColor(labels[c_day][c_period], timeOff.getAvailability());
        }
    }

    @SuppressWarnings("unchecked")
    private void changeRow(@NotNull final Label[][] labels, int c_day) {
        @NotNull ObjectList<ObjectList<DBTimeOff>> timeOffs = this.classroom.getTimeoff().getAvailabilities();
        @NotNull final DBAvailability availability = this.availabilities.get((this.availabilitiesLookup.get(timeOffs.get(c_day).get(0).getAvailability().getId()) + 1) % this.availabilities.size());
        for (int c_period = -1, cs_period = timeOffs.get(c_day).size(); ++c_period < cs_period; ) {
            @NotNull final DBTimeOff timeOff = timeOffs.get(c_day).get(c_period);
            timeOff.setAvailability(availability);
            this.setTileColor(labels[c_day][c_period], timeOff.getAvailability());
        }
    }

    @SuppressWarnings("unchecked")
    private void changeAll(@NotNull final Label[][] labels) {
        @NotNull ObjectList<ObjectList<DBTimeOff>> timeOffs = this.classroom.getTimeoff().getAvailabilities();
        @NotNull final DBAvailability availability = this.availabilities.get((this.availabilitiesLookup.get(timeOffs.get(0).get(0).getAvailability().getId()) + 1) % this.availabilities.size());
        for (int c_day = -1, cs_day = timeOffs.size(); ++c_day < cs_day; ) {
            for (int c_period = -1, cs_period = timeOffs.get(c_day).size(); ++c_period < cs_period; ) {
                @NotNull final DBTimeOff timeOff = timeOffs.get(c_day).get(c_period);
                timeOff.setAvailability(availability);
                this.setTileColor(labels[c_day][c_period], timeOff.getAvailability());
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

    public void onCloseClassroomEditTimeOffPressed(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }

    @SuppressWarnings("unchecked")
    public void onSaveClassroomEditTimeOffPressed(ActionEvent actionEvent) {
        try {
            @NotNull final AbstractModel model = new MClassroom(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
            MClassroom.updateTimeOff(model, this.classroom.getTimeoff());
            @NotNull final Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Berhasil");
            alert.setHeaderText(null);
            alert.setContentText("Data Berhasil Dirubah !");

            @NotNull final Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    this.timeOffUpdated(this.classroom, this.classroom.getTimeoff());
                    this.onCloseClassroomEditTimeOffPressed(actionEvent);
                }
            }
        } catch (SQLException | UnsupportedEncodingException ignored) {
            System.err.println("Error Activating Database");
            System.exit(-1);
        }
    }

    public void timeOffUpdated(@NotNull final DBClassroom classroom, @NotNull DBTimeOffContainer<DBClassroom> timeOff) {

    }
}
