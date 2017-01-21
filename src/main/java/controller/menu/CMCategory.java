package controller.menu;

import controller.school.CSchoolDetail;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.util.Session;
import model.util.pattern.observer.ObservableDBSchool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import view.school.ISchoolDetail;

import java.io.IOException;
import java.net.URL;
import java.util.Observer;
import java.util.ResourceBundle;

/*
 * This <skripsi.hdpso.scheduling> project in package <controller.menu> created by : 
 * Name         : syafiq
 * Date / Time  : 01 January 2017, 4:29 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("unused")
public class CMCategory implements Initializable {
    @Nullable
    private final Observer content;
    @FXML
    public Button mc_button_generate;
    @FXML
    public Button bSchool;
    @FXML
    public Button bSubject;
    @FXML
    public Button bClass;
    @FXML
    public Button bClassroom;
    @FXML
    public Button bLecture;
    @FXML
    public Button bLesson;

    public CMCategory() {
        this.content = null;
    }


    public CMCategory(@Nullable Observer rootContentCallback) {
        this.content = rootContentCallback;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.bindSchoolData();

    }

    private void bindSchoolData() {
        @NotNull final BooleanProperty schoolExistenceListener = new SimpleBooleanProperty(true);
        @NotNull final Observer schoolExistenceObserver = (o, arg) -> {
            if (o instanceof ObservableDBSchool) {
                schoolExistenceListener.setValue(((ObservableDBSchool) o).getSchool() == null);
            }
        };
        ((ObservableDBSchool) Session.getInstance().get("school")).addObserver(schoolExistenceObserver);
        this.mc_button_generate.disableProperty().bind(schoolExistenceListener);
        this.bSchool.disableProperty().bind(schoolExistenceListener);
        this.bSubject.disableProperty().bind(schoolExistenceListener);
        this.bClass.disableProperty().bind(schoolExistenceListener);
        this.bClassroom.disableProperty().bind(schoolExistenceListener);
        this.bLecture.disableProperty().bind(schoolExistenceListener);
        this.bLesson.disableProperty().bind(schoolExistenceListener);
    }


    public void onSchoolButtonPressed(ActionEvent actionEvent) {
        @NotNull final Stage dialog = new Stage();
        dialog.setTitle("Detail Jadwal");

        try {
            dialog.setScene(new Scene(ISchoolDetail.load(new CSchoolDetail()).load()));
        } catch (IOException ignored) {
        }

        dialog.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
    }

    public void onMenuCategoryGenerateClick(ActionEvent actionEvent) {

    }
}