package controller.menu;

import controller.school.CSchoolList;
import controller.school.CSchoolNew;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import view.school.ISchoolList;
import view.school.ISchoolNew;

/*
 * This <skripsi.hdpso.scheduling> project in package <controller> created by : 
 * Name         : syafiq
 * Date / Time  : 01 January 2017, 3:51 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class CMHome implements Initializable {

    public CMHome() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void onButtonExitPressed(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }

    public void onButtonCreateNewPressed(ActionEvent actionEvent) {
        @NotNull final Stage dialog = new Stage();
        dialog.setTitle("Buat Jadwal Baru");

        try {
            dialog.setScene(new Scene(ISchoolNew.load(new CSchoolNew()).load()));
        } catch (IOException ignored) {
        }

        dialog.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
    }

    public void onSchoolListPressed(ActionEvent actionEvent) {
        @NotNull final Stage dialog = new Stage();
        dialog.setTitle("Daftar Jadwal");

        try {
            dialog.setScene(new Scene(ISchoolList.load(new CSchoolList()).load()));
        } catch (IOException ignored) {
        }

        dialog.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
    }
}
