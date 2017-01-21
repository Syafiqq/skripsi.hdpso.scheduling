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
import model.database.component.DBPeriod;
import model.database.core.DBType;
import model.database.model.MPeriod;
import model.method.pso.hdpso.component.Setting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

@SuppressWarnings("WeakerAccess")
public class CPeriodEdit implements Initializable {
    @Nullable
    private final DBPeriod period;
    @FXML
    public TextField tfName;
    @FXML
    public TextField tfNickname;
    @FXML
    public TextField tfOrder;
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

    public CPeriodEdit() {
        this.period = null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (this.period != null) {
            this.tfName.setText(this.period.getName());
            this.tfNickname.setText(this.period.getNickname());
            this.tfOrder.setText(String.valueOf(this.period.getPosition()));
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
    }

    private void changeTime(@NotNull final Label container, double hour, double minute) {
        container.setText(String.format(Locale.getDefault(), "%02d:%02d", (int) hour, (int) minute));
    }

    public void onPeriodEditSavePressed(ActionEvent actionEvent) {
        if (this.period != null) {
            try {
                @NotNull final MPeriod mDay = new MPeriod(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
                mDay.update(
                        this.period,
                        this.tfName.getText(),
                        this.tfNickname.getText(),
                        this.lStart.getText(),
                        this.lEnd.getText(),
                        Integer.parseInt(this.tfOrder.getText()));
                @NotNull final Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Berhasil");
                alert.setHeaderText(null);
                alert.setContentText("Data Berhasil Dirubah !");

                @NotNull final Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent()) {
                    if (result.get() == ButtonType.OK) {
                        this.periodChanged();
                        this.onPeriodEditClosePressed(actionEvent);
                    }
                }
            } catch (SQLException | UnsupportedEncodingException ignored) {
                System.err.println("Error Activating Database");
                System.exit(-1);
            }
        }
    }

    public void onPeriodEditClosePressed(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }

    public void periodChanged() {
    }
}
