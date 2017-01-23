package controller.school.period;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 21 January 2017, 1:57 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import model.AbstractModel;
import model.database.component.DBPeriod;
import model.database.component.metadata.DBMSchool;
import model.database.core.DBType;
import model.database.model.MPeriod;
import model.method.pso.hdpso.component.Setting;
import model.util.Dump;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

@SuppressWarnings("WeakerAccess")
public class CPeriodEdit implements Initializable {
    @NotNull
    private final DBPeriod period;
    @FXML
    public TextField tfName;
    @FXML
    public TextField tfNickname;
    @FXML
    public Slider slStartHour;
    @FXML
    public Slider slStartMinute;
    @FXML
    public Label lStart;
    @FXML
    public Label lEnd;
    @FXML
    public Slider slEndHour;
    @FXML
    public Slider slEndMinute;

    public CPeriodEdit(@NotNull final DBPeriod period) {
        this.period = period;
    }

    public CPeriodEdit() throws UnsupportedEncodingException, SQLException {
        @NotNull final AbstractModel model = new MPeriod(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
        @NotNull final DBMSchool schoolMetadata = Dump.schoolMetadata();
        this.period = MPeriod.getFromMetadata(model, schoolMetadata, MPeriod.getAllMetadataFromSchool(model, schoolMetadata).get(0));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tfName.setText(this.period.getName());
        this.tfNickname.setText(this.period.getNickname());
        this.slStartHour.valueProperty().addListener((observable, oldValue, newValue) -> this.changeTime(this.lStart, newValue.doubleValue(), this.slStartMinute.getValue()));
        this.slStartMinute.valueProperty().addListener((observable, oldValue, newValue) -> this.changeTime(this.lStart, this.slStartHour.getValue(), newValue.doubleValue()));
        this.slEndHour.valueProperty().addListener((observable, oldValue, newValue) -> this.changeTime(this.lEnd, newValue.doubleValue(), this.slEndMinute.getValue()));
        this.slEndMinute.valueProperty().addListener((observable, oldValue, newValue) -> this.changeTime(this.lEnd, this.slEndHour.getValue(), newValue.doubleValue()));
        this.slStartHour.setValue(this.period.getStart().getHour());
        this.slStartMinute.setValue(this.period.getStart().getMinute());
        this.slEndHour.setValue(this.period.getEnd().getHour());
        this.slEndMinute.setValue(this.period.getEnd().getMinute());
        this.lStart.setText(this.period.getStart().format(DateTimeFormatter.ofPattern("HH:mm")));
        this.lEnd.setText(this.period.getEnd().format(DateTimeFormatter.ofPattern("HH:mm")));
    }

    private void changeTime(@NotNull final Label container, double hour, double minute) {
        container.setText(String.format(Locale.getDefault(), "%02d:%02d", (int) hour, (int) minute));
    }

    public void onPeriodEditSavePressed(ActionEvent actionEvent) {
        try {
            @NotNull final AbstractModel model = new MPeriod(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
            MPeriod.update(model,
                    this.period,
                    this.tfName.getText(),
                    this.tfNickname.getText(),
                    this.lStart.getText(),
                    this.lEnd.getText(),
                    this.period.getPosition());
            @NotNull final Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Berhasil");
            alert.setHeaderText(null);
            alert.setContentText("Data Berhasil Dirubah !");

            @NotNull final Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    this.periodUpdated(this.period, this.tfName.getText(), this.tfNickname.getText(), this.lStart.getText(), this.lEnd.getText(), this.period.getPosition());
                    this.onPeriodEditClosePressed(actionEvent);
                }
            }
        } catch (SQLException | UnsupportedEncodingException ignored) {
            System.err.println("Error Activating Database");
            System.exit(-1);
        }
    }

    public void onPeriodEditClosePressed(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }

    public void periodUpdated(@NotNull DBPeriod period, String name, String nickname, String start, String end, int position) {
    }
}
