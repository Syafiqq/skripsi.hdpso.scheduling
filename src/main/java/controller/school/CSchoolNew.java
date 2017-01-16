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
import model.database.component.DBSemester;
import model.database.core.DBType;
import model.method.pso.hdpso.component.Setting;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

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
            @NotNull final Connection connection = DriverManager.getConnection(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
            @NotNull final PreparedStatement statement = connection.prepareStatement("INSERT INTO `school` (`id`, `name`, `nick`, `address`, `academic_year`, `semester`, `active_period`, `active_day`) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, name.getText());
            statement.setString(2, nick.getText());
            statement.setString(3, address.getText());
            statement.setString(4, academicYear.getText());
            statement.setInt(5, ((RadioButton) semesterGroup.getSelectedToggle()).getText().toUpperCase().contentEquals(DBSemester.ODD.name()) ? DBSemester.ODD.ordinal() : DBSemester.EVEN.ordinal());
            statement.setInt(6, Integer.parseInt(activePeriod.getText()));
            statement.setInt(7, Integer.parseInt(activeDay.getText()));
            statement.execute();
            statement.close();
            connection.close();
            @NotNull final Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Successfully Created");

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

    public void onCancelCreatePressed(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }
}
