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
import model.database.component.DBDay;
import model.database.core.DBType;
import model.database.model.MDay;
import model.method.pso.hdpso.component.Setting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

@SuppressWarnings("WeakerAccess")
public class CDayEdit implements Initializable {

    @Nullable private final DBDay day;
    @FXML public TextField tfName;
    @FXML public TextField tfNickname;
    @FXML public TextField tfOrder;

    public CDayEdit()
    {
        this(null);
    }

    public CDayEdit(@Nullable DBDay day) {
        this.day = day;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(this.day != null)
        {
            this.tfName.setText(this.day.getName());
            this.tfNickname.setText(this.day.getNickname());
            this.tfOrder.setText(String.valueOf(this.day.getPosition()));
        }
    }

    public void onDayEditSavePressed(ActionEvent actionEvent) {
        if(this.day != null)
        {
            try {
                @NotNull final MDay mDay = new MDay(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
                mDay.update(
                        this.day,
                        this.tfName.getText(),
                        this.tfNickname.getText(),
                        Integer.parseInt(this.tfOrder.getText()));
                @NotNull final Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Berhasil");
                alert.setHeaderText(null);
                alert.setContentText("Data Berhasil Dirubah !");

                @NotNull final Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent()) {
                    if (result.get() == ButtonType.OK) {
                        this.dayUpdated();
                        this.onDayEditClosePressed(actionEvent);
                    }
                }
            } catch (SQLException | UnsupportedEncodingException ignored) {
                System.err.println("Error Activating Database");
                System.exit(-1);
            }
        }
    }

    public void dayUpdated() {
        if(this.day != null) {
            this.day.setName(this.tfName.getText());
            this.day.setNickname(this.tfNickname.getText());
            this.day.setPosition(Integer.parseInt(this.tfOrder.getText()));
        }
    }

    public void onDayEditClosePressed(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }
}
