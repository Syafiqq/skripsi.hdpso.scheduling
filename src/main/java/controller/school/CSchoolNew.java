package controller.school;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 16 January 2017, 4:56 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import model.AbstractModel;
import model.database.component.DBConstraint;
import model.database.component.DBDay;
import model.database.component.DBParameter;
import model.database.component.DBPeriod;
import model.database.component.DBSchool;
import model.database.component.DBSemester;
import model.database.core.DBType;
import model.database.model.MConstraint;
import model.database.model.MDay;
import model.database.model.MParameter;
import model.database.model.MPeriod;
import model.database.model.MTimetable;
import model.method.pso.hdpso.component.Setting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"WeakerAccess", "unused"}) public class CSchoolNew implements Initializable
{
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
            @NotNull final AbstractModel model = new MTimetable(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
            @Nullable DBSchool school = MTimetable.insert(model,
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

        /*
        * Generate Parameter Setting
        * */
        @NotNull final DBParameter parameter = new DBParameter(
                -1,
                school,
                0,
                1,
                0,
                1,
                0,
                1,
                100000,
                2,
                4
                , Setting.HDPSO
                , false
        );
        MParameter.insert(model, school, parameter);

        /*
        * Generate Constraint Setting
        * */
        @NotNull final DBConstraint constraint = new DBConstraint(
                -1,
                school,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true
        );
        MConstraint.insert(model, school, constraint);
    }

    public void onCancelCreatePressed(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }
}
