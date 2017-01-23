package controller.school;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 16 January 2017, 4:56 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import model.AbstractModel;
import model.database.component.DBDay;
import model.database.component.DBPeriod;
import model.database.component.DBSchool;
import model.database.component.DBSemester;
import model.database.core.DBType;
import model.database.model.MDay;
import model.database.model.MPeriod;
import model.database.model.MSchool;
import model.method.pso.hdpso.component.Setting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class CSchoolNew implements Initializable {
    @FXML
    public TextField name;
    @FXML
    public TextField nick;
    @FXML
    public TextField address;
    @FXML
    public TextField academicYear;
    @FXML
    public ToggleGroup semesterGroup;
    @FXML
    public TextField activeDay;
    @FXML
    public TextField activePeriod;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void onCreateTimetablePressed(ActionEvent actionEvent) {
        try {
            @NotNull final AbstractModel model = new MSchool(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
            @Nullable DBSchool school = MSchool.insert(model,
                    name.getText(),
                    nick.getText(),
                    address.getText(),
                    academicYear.getText(),
                    ((RadioButton) semesterGroup.getSelectedToggle()).getText().toLowerCase().contentEquals(DBSemester.ODD.describe()) ? DBSemester.ODD.ordinal() : DBSemester.EVEN.ordinal(),
                    Integer.parseInt(activePeriod.getText()),
                    Integer.parseInt(activeDay.getText()));

            if(school != null)
            {
                this.generateSchoolData(model, school);
            }

            @NotNull final Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Berhasil");
            alert.setHeaderText(null);
            alert.setContentText("Data Berhasil Dibuat !");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    onCancelCreatePressed(actionEvent);
                }
            }
        } catch (SQLException | UnsupportedEncodingException ignored) {
            System.err.println("Error Activating Database");
            System.exit(-1);
        }
    }

    private void generateSchoolData(@NotNull final AbstractModel model, @NotNull final DBSchool school) {
        /*
        * Generate Day
        * */
        List<DBDay> days = new LinkedList<>();
        for(int c_day = -1, cs_day = school.getActiveDay(); ++c_day < cs_day;)
        {
            final int day = c_day + 1;
            days.add(new DBDay(-1, c_day+1, "Hari-"+day, "H-"+day, school));
        }
        MDay.insertOnlyBulk(model, days);

        /*
        * Generate Day
        * */
        List<DBPeriod> periods = new LinkedList<>();
        for(int c_period = -1, cs_period = school.getActivePeriod(); ++c_period < cs_period;)
        {
            final int period = c_period + 1;
            periods.add(new DBPeriod(
                    -1,
                    period,
                    "Period-"+period,
                    "P-"+period,
                    String.format(Locale.getDefault(), "00:%02d", c_period),
                    String.format(Locale.getDefault(), "00:%02d", period),
                    school));
        }
        MPeriod.insertOnlyBulk(model, periods);
    }

    public void onCancelCreatePressed(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }
}
