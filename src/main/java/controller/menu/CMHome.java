package controller.menu;

import controller.school.CSchoolList;
import controller.school.CSchoolNew;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.util.Session;
import model.util.pattern.observer.ObservableDBSchool;
import org.jetbrains.annotations.NotNull;
import view.school.ISchoolList;
import view.school.ISchoolNew;

import java.io.IOException;
import java.net.URL;
import java.util.Observer;
import java.util.ResourceBundle;

/*
 * This <skripsi.hdpso.scheduling> project in package <controller> created by : 
 * Name         : syafiq
 * Date / Time  : 01 January 2017, 3:51 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class CMHome implements Initializable
{
    public Button buttonNew;
    public Button buttonLoad;
    private BooleanProperty disableListener;

    public CMHome() {

    }

    @Override public void initialize(URL location, ResourceBundle resources)
    {
        if (!Session.getInstance().containsKey("school")) {
            Session.getInstance().put("school", new ObservableDBSchool(null));
        }
        this.disableListener = new SimpleBooleanProperty(true);
        this.buttonNew.disableProperty().bindBidirectional(new SimpleBooleanProperty());
        @NotNull final Observer observer = (o, arg) -> {
            if (o instanceof ObservableDBSchool) {
                this.disableListener.setValue(((ObservableDBSchool) o).getSchool() != null);
            }
        };

        this.buttonNew.disableProperty().bind(this.disableListener);
        this.buttonLoad.disableProperty().bind(this.disableListener);
        ((ObservableDBSchool) Session.getInstance().get("school")).addObserver(observer);
    }

    public void onButtonExitPressed(ActionEvent actionEvent)
    {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }

    public void onButtonCreateNewPressed(ActionEvent actionEvent) {
        @NotNull final Stage dialog = new Stage();
        dialog.setTitle("Create new Timetable");

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
        dialog.setTitle("Load Timetable");

        try {
            dialog.setScene(new Scene(ISchoolList.load(new CSchoolList()).load()));
        } catch (IOException ignored) {
        }

        dialog.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
    }
}
