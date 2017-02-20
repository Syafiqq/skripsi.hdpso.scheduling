package controller.school.day;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 21 January 2017, 11:52 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import model.AbstractModel;
import model.database.component.DBDay;
import model.database.component.metadata.DBMSchool;
import model.database.core.DBType;
import model.database.model.MDay;
import model.method.pso.hdpso.component.Setting;
import model.util.Dump;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

@SuppressWarnings("WeakerAccess")
public class CDayEdit implements Initializable {

    @NotNull
    private final DBDay day;
    @FXML
    public TextField tfName;
    @FXML
    public TextField tfNickname;

    public CDayEdit(@NotNull final DBDay day) {
        this.day = day;
    }

    public CDayEdit() throws UnsupportedEncodingException, SQLException {
        @NotNull final AbstractModel model = new MDay(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
        @NotNull final DBMSchool schoolMetadata = Dump.schoolMetadata();
        this.day = MDay.getFromMetadata(model, schoolMetadata, MDay.getAllMetadataFromSchool(model, schoolMetadata).get(0));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tfName.setText(this.day.getName());
        this.tfNickname.setText(this.day.getNickname());
    }

    public void onDayEditSavePressed(ActionEvent actionEvent) {
        try {
            @NotNull final AbstractModel model = new MDay(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
            MDay.update(model,
                    this.day,
                    this.tfName.getText(),
                    this.tfNickname.getText(),
                    this.day.getPosition());
            @NotNull final Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Berhasil");
            alert.setHeaderText(null);
            alert.setContentText("Data Berhasil Dirubah !");

            @NotNull final Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    this.dayUpdated(this.day, this.tfName.getText(), this.tfNickname.getText(), this.day.getPosition());
                    this.onDayEditClosePressed(actionEvent);
                }
            }
        } catch (SQLException | UnsupportedEncodingException ignored) {
            System.err.println("Error Activating Database");
            System.exit(-1);
        }
    }

    public void dayUpdated(@NotNull final DBDay id, String name, String nickname, int position) {

    }

    public void onDayEditClosePressed(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }
}
