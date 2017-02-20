package controller.lecture;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 25 January 2017, 9:07 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import it.unimi.dsi.fastutil.objects.ObjectList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import model.AbstractModel;
import model.database.component.DBAvailability;
import model.database.component.DBLecture;
import model.database.component.DBTimeOff;
import model.database.component.metadata.DBMDay;
import model.database.component.metadata.DBMLecture;
import model.database.component.metadata.DBMPeriod;
import model.database.component.metadata.DBMSchool;
import model.database.core.DBType;
import model.database.model.*;
import model.method.pso.hdpso.component.Setting;
import model.util.Dump;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@SuppressWarnings({"WeakerAccess", "unused"})
public class CLectureCreate  implements Initializable {
    @NotNull
    private final DBMSchool schoolMetadata;
    @NotNull
    private final List<DBMDay> dayMetadata;
    @NotNull
    private final List<DBMPeriod> periodMetadata;
    @NotNull
    private final List<DBAvailability> availabilities;
    @FXML
    public TextField tfName;

    public CLectureCreate(@NotNull final DBMSchool schoolMetadata, @NotNull final List<DBMDay> dayMetadata, @NotNull final List<DBMPeriod> periodMetadata, @NotNull final List<DBAvailability> availabilities) {
        this.schoolMetadata = schoolMetadata;
        this.dayMetadata = dayMetadata;
        this.periodMetadata = periodMetadata;
        this.availabilities = availabilities;
    }

    public CLectureCreate() throws UnsupportedEncodingException, SQLException {
        @NotNull final AbstractModel model = new MSchool(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
        this.schoolMetadata = Dump.schoolMetadata();
        this.dayMetadata = MDay.getAllMetadataFromSchool(model, this.schoolMetadata);
        this.periodMetadata = MPeriod.getAllMetadataFromSchool(model, this.schoolMetadata);
        this.availabilities = MAvailability.getAll(model);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void onCloseLectureCreatePressed(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }

    public void onSaveLectureCreatePressed(ActionEvent actionEvent) {
        try {
            @NotNull final AbstractModel model = new MLecture(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
            @Nullable DBMLecture lecture = MLecture.insert(model,
                    this.schoolMetadata,
                    this.tfName.getText()
            );

            if (lecture != null) {
                this.generateLectureData(model, lecture, this.schoolMetadata, this.dayMetadata, this.periodMetadata);
                this.lectureCreated(lecture);
            }

            @NotNull final Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Berhasil");
            alert.setHeaderText(null);
            alert.setContentText("Data Dosen Berhasil Dibuat !");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    this.onCloseLectureCreatePressed(actionEvent);
                }
            }
        } catch (SQLException | UnsupportedEncodingException ignored) {
            System.err.println("Error Activating Database");
            System.exit(-1);
        }
    }

    public void lectureCreated(@NotNull final  DBMLecture lecture) {

    }

    @SuppressWarnings("Duplicates")
    private void generateLectureData(@NotNull final AbstractModel model, @NotNull DBMLecture lecture, @NotNull final DBMSchool schoolMetadata, @NotNull final List<DBMDay> dayMetadata, @NotNull final List<DBMPeriod> periodMetadata) {
        @NotNull final DBLecture.TimeOffContainer container = DBLecture.TimeOffContainer.generateNew(lecture, schoolMetadata);

        @NotNull final DBAvailability availability = availabilities.get(0);
        @NotNull final Iterator<DBMDay> dayIterator = dayMetadata.iterator();

        for(@NotNull final ObjectList<DBTimeOff> timeOff : container.getAvailabilities())
        {
            @NotNull final DBMDay day = dayIterator.next();
            @NotNull final Iterator<DBMPeriod> periodIterator = periodMetadata.iterator();
            for(int c_period = 0; periodIterator.hasNext(); ++c_period)
            {
                timeOff.set(c_period, new DBTimeOff(-1, day, periodIterator.next(), availability));
            }
        }

        MLecture.insertTimeOff(model, lecture, container);
    }
}

