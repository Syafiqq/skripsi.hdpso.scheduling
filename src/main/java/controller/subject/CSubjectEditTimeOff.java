package controller.subject;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 24 January 2017, 6:44 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import it.unimi.dsi.fastutil.ints.Int2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectList;
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
import model.database.component.DBSubject;
import model.database.component.DBTimeOff;
import model.database.component.DBTimeOffContainer;
import model.database.component.metadata.DBMDay;
import model.database.component.metadata.DBMPeriod;
import model.database.component.metadata.DBMSchool;
import model.database.component.metadata.DBMSubject;
import model.database.core.DBType;
import model.database.model.*;
import model.method.pso.hdpso.component.Setting;
import model.util.Dump;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@SuppressWarnings({"WeakerAccess", "unchecked"})
public class CSubjectEditTimeOff implements Initializable {
    @NotNull
    private final DBSubject subject;
    @NotNull
    private final List<DBMDay> dayMetadata;
    @NotNull
    private final List<DBMPeriod> periodMetadata;
    @NotNull
    private final List<DBAvailability> availabilities;
    @NotNull
    final Int2IntMap availabilitiesLookup;
    @FXML
    public GridPane timeOffContainer;

    public CSubjectEditTimeOff(@NotNull final DBSubject subject, @NotNull final List<DBMDay> dayMetadata, @NotNull final List<DBMPeriod> periodMetadata, @NotNull final List<DBAvailability> availabilities) {
        this.subject = subject;
        this.dayMetadata = dayMetadata;
        this.periodMetadata = periodMetadata;
        this.availabilities = availabilities;
        this.availabilitiesLookup = new Int2IntLinkedOpenHashMap(this.availabilities.size());
        this.setAvailabilityLookup();
    }

    public CSubjectEditTimeOff() throws UnsupportedEncodingException, SQLException {
        @NotNull final AbstractModel model = new MSchool(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
        @NotNull final DBMSchool schoolMetadata = Dump.schoolMetadata();
        this.dayMetadata = MDay.getAllMetadataFromSchool(model, schoolMetadata);
        this.periodMetadata = MPeriod.getAllMetadataFromSchool(model, schoolMetadata);
        this.availabilities = MAvailability.getAll(model);
        @NotNull final List<DBMSubject> subjectMetadata = MSubject.getAllMetadataFromSchool(model, schoolMetadata);
        if (subjectMetadata.size() > 0) {
            this.subject = MSubject.getFromMetadata(model, schoolMetadata, subjectMetadata.get(0));
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
            MSubject.getTimeOff(model, subject, mapDay, mapPeriod, mapAvailability);
        } else {
            this.subject = new DBSubject(-1, "A", "B", schoolMetadata);
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


    @SuppressWarnings("Duplicates")
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        @NotNull final Label[][] organizedLabel = new Label[this.dayMetadata.size()][this.periodMetadata.size()];

        @NotNull final EventHandler<MouseEvent> tileClicked = event -> {
            if (event.getSource() instanceof CoordinatedLabel) {
                @NotNull final CoordinatedLabel label = (CoordinatedLabel) event.getSource();
                CSubjectEditTimeOff.this.processChanged(organizedLabel, label.getRow(), label.getColumn());
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
        for (@NotNull final ObjectList<DBTimeOff> day : (ObjectList<ObjectList<DBTimeOff>>) subject.getTimeoff().getAvailabilities()) {
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

    private void changeTile(@NotNull final Label[][] labels, int c_day, int c_period) {
        @NotNull ObjectList<ObjectList<DBTimeOff>> timeOffs = this.subject.getTimeoff().getAvailabilities();
        @NotNull final DBAvailability availability = this.availabilities.get((this.availabilitiesLookup.get(timeOffs.get(c_day).get(c_period).getAvailability().getId()) + 1) % this.availabilities.size());
        @NotNull final DBTimeOff timeOff = timeOffs.get(c_day).get(c_period);
        timeOff.setAvailability(availability);
        this.setTileColor(labels[c_day][c_period], timeOff.getAvailability());
    }

    private void changeColumn(@NotNull final Label[][] labels, int c_period) {
        @NotNull ObjectList<ObjectList<DBTimeOff>> timeOffs = this.subject.getTimeoff().getAvailabilities();
        @NotNull final DBAvailability availability = this.availabilities.get((this.availabilitiesLookup.get(timeOffs.get(0).get(c_period).getAvailability().getId()) + 1) % this.availabilities.size());
        for (int c_day = -1, cs_day = timeOffs.size(); ++c_day < cs_day; ) {
            @NotNull final DBTimeOff timeOff = timeOffs.get(c_day).get(c_period);
            timeOff.setAvailability(availability);
            this.setTileColor(labels[c_day][c_period], timeOff.getAvailability());
        }
    }

    private void changeRow(@NotNull final Label[][] labels, int c_day) {
        @NotNull ObjectList<ObjectList<DBTimeOff>> timeOffs = this.subject.getTimeoff().getAvailabilities();
        @NotNull final DBAvailability availability = this.availabilities.get((this.availabilitiesLookup.get(timeOffs.get(c_day).get(0).getAvailability().getId()) + 1) % this.availabilities.size());
        for (int c_period = -1, cs_period = timeOffs.get(c_day).size(); ++c_period < cs_period; ) {
            @NotNull final DBTimeOff timeOff = timeOffs.get(c_day).get(c_period);
            timeOff.setAvailability(availability);
            this.setTileColor(labels[c_day][c_period], timeOff.getAvailability());
        }
    }

    @SuppressWarnings("unchecked")
    private void changeAll(@NotNull final Label[][] labels) {
        @NotNull ObjectList<ObjectList<DBTimeOff>> timeOffs = this.subject.getTimeoff().getAvailabilities();
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

    public void onCloseSchoolEditTimeOffPressed(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }

    @SuppressWarnings("unchecked")
    public void onSaveSchoolEditTimeOffPressed(ActionEvent actionEvent) {
        try {
            @NotNull final AbstractModel model = new MSubject(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
            MSubject.updateTimeOff(model, this.subject.getTimeoff());
            @NotNull final Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Berhasil");
            alert.setHeaderText(null);
            alert.setContentText("Data Berhasil Dirubah !");

            @NotNull final Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    this.timeOffUpdated(this.subject, this.subject.getTimeoff());
                    this.onCloseSchoolEditTimeOffPressed(actionEvent);
                }
            }
        } catch (SQLException | UnsupportedEncodingException ignored) {
            System.err.println("Error Activating Database");
            System.exit(-1);
        }
    }

    public void timeOffUpdated(@NotNull final DBSubject subject, @NotNull DBTimeOffContainer<DBSubject> timeOff) {

    }
}
