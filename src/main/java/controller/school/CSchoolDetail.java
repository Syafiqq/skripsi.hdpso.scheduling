package controller.school;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 20 January 2017, 7:02 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import controller.school.day.CDayList;
import controller.school.period.CPeriodList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.database.component.DBSchool;
import model.util.Session;
import model.util.pattern.observer.ObservableDBSchool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import view.school.ISchoolEdit;
import view.school.day.IDayList;
import view.school.period.IPeriodList;

import java.io.IOException;
import java.net.URL;
import java.util.Observer;
import java.util.ResourceBundle;

public class CSchoolDetail implements Initializable {

    @FXML
    public Label lName;
    @FXML
    public Label lNickname;
    @FXML
    public Label lAddress;
    @FXML
    public Label lAcademicYear;
    @FXML
    public Label lSemester;
    @FXML
    public Label lActiveDay;
    @FXML
    public Label lActivePeriod;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        @NotNull final Session session = Session.getInstance();
        if (session.containsKey("school")) {
            @NotNull final Observer schoolExistenceListener = (o, arg) -> {
                @Nullable final DBSchool school = ((ObservableDBSchool) Session.getInstance().get("school")).getSchool();
                if (school != null) {
                    CSchoolDetail.this.lName.setText(school.getName());
                    CSchoolDetail.this.lNickname.setText(school.getNickname());
                    CSchoolDetail.this.lAddress.setText(school.getAddress());
                    CSchoolDetail.this.lAcademicYear.setText(school.getAcademicYear());
                    CSchoolDetail.this.lSemester.setText(school.getSemester().describe());
                    CSchoolDetail.this.lActiveDay.setText(String.valueOf(school.getActiveDay()));
                    CSchoolDetail.this.lActivePeriod.setText(String.valueOf(school.getActivePeriod()));
                } else {
                    CSchoolDetail.this.lName.setText("-");
                    CSchoolDetail.this.lNickname.setText("-");
                    CSchoolDetail.this.lAddress.setText("-");
                    CSchoolDetail.this.lAcademicYear.setText("-");
                    CSchoolDetail.this.lSemester.setText("-");
                    CSchoolDetail.this.lActiveDay.setText("-");
                    CSchoolDetail.this.lActivePeriod.setText("-");
                }
            };

            ((ObservableDBSchool) Session.getInstance().get("school")).addObserver(schoolExistenceListener);
        }
    }

    public void onSchoolDetailEditPressed(ActionEvent actionEvent) {
        @NotNull final Stage dialog = new Stage();
        dialog.setTitle("Edit Jadwal");

        try {
            dialog.setScene(new Scene(ISchoolEdit.load(new CSchoolEdit()).load()));
        } catch (IOException ignored) {
        }

        dialog.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
    }

    public void onSchoolDetailClosePressed(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }

    public void onSchoolDayEditPressed(ActionEvent actionEvent) {
        @NotNull final Stage dialog = new Stage();
        dialog.setTitle("Edit Hari");

        try {
            dialog.setScene(new Scene(IDayList.load(new CDayList()).load()));
        } catch (IOException ignored) {
        }

        dialog.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
    }

    public void onSchoolPeriodEditPressed(ActionEvent actionEvent) {
        @NotNull final Stage dialog = new Stage();
        dialog.setTitle("Edit Periode");

        try {
            dialog.setScene(new Scene(IPeriodList.load(new CPeriodList()).load()));
        } catch (IOException ignored) {
        }

        dialog.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
    }
}
