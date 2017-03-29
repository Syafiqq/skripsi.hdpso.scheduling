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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.AbstractModel;
import model.database.component.DBSchool;
import model.database.component.DBSemester;
import model.database.component.metadata.DBMDay;
import model.database.component.metadata.DBMPeriod;
import model.database.component.metadata.DBMSchool;
import model.database.core.DBType;
import model.database.model.MDay;
import model.database.model.MPeriod;
import model.database.model.MTimetable;
import model.method.pso.hdpso.component.Setting;
import model.util.Dump;
import org.jetbrains.annotations.NotNull;
import view.school.ISchoolEdit;
import view.school.day.IDayList;
import view.school.period.IPeriodList;

@SuppressWarnings({"unused", "WeakerAccess"})
public class CSchoolDetail implements Initializable {

    @NotNull
    private final DBSchool        school;
    @NotNull
    private final List<DBMDay>    dayMetadata;
    @NotNull
    private final List<DBMPeriod> periodMetadata;
    @FXML
    public        Label           lName;
    @FXML
    public        Label           lNickname;
    @FXML
    public        Label           lAddress;
    @FXML
    public        Label           lAcademicYear;
    @FXML
    public        Label           lSemester;
    @FXML
    public        Label           lActiveDay;
    @FXML
    public        Label           lActivePeriod;

    public CSchoolDetail(@NotNull final DBSchool school, @NotNull final List<DBMDay> dayMetadata, @NotNull final List<DBMPeriod> periodMetadata) throws UnsupportedEncodingException, SQLException {
        this.school = school;
        this.dayMetadata = dayMetadata;
        this.periodMetadata = periodMetadata;
    }

    public CSchoolDetail() throws UnsupportedEncodingException, SQLException {
        @NotNull final AbstractModel model          = new MTimetable(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
        @NotNull final DBMSchool     schoolMetadata = Dump.schoolMetadata();
        this.school = MTimetable.getFromMetadata(model, schoolMetadata);
        this.dayMetadata = MDay.getAllMetadataFromSchool(model, schoolMetadata);
        this.periodMetadata = MPeriod.getAllMetadataFromSchool(model, schoolMetadata);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.schoolUpdated(
                this.school.getName(),
                this.school.getNickname(),
                this.school.getAddress(),
                this.school.getAcademicYear(),
                this.school.getSemester().ordinal(),
                this.school.getActiveDay(),
                this.school.getActivePeriod());
    }

    public void schoolUpdated(String name, String nickname, String address, String academicYear, int semester, int activeDay, int activePeriod) {
        this.lName.setText(name);
        this.lNickname.setText(nickname);
        this.lAddress.setText(address);
        this.lAcademicYear.setText(academicYear);
        this.lSemester.setText(semester == DBSemester.EVEN.ordinal() ? DBSemester.EVEN.describe() : DBSemester.ODD.describe());
        this.lActiveDay.setText(String.valueOf(activeDay));
        this.lActivePeriod.setText(String.valueOf(activePeriod));

        this.school.setName(name);
        this.school.setAcademicYear(academicYear);
        this.school.setSemester(semester);
        this.school.setActiveDay(activeDay);
        this.school.setActivePeriod(activePeriod);
    }

    public void onSchoolDetailEditPressed(ActionEvent actionEvent) {
        @NotNull final Stage dialog = new Stage();
        dialog.setTitle("Edit Jadwal");

        try {
            dialog.setScene(new Scene(ISchoolEdit.load(new CSchoolEdit(this.school){
                @Override
                public void schoolUpdated(String name, String nickname, String address, String academicYear, int semester, int activeDay, int activePeriod) {
                    CSchoolDetail.this.schoolUpdated(name, nickname, address, academicYear, semester, activeDay, activePeriod);
                }
            }).load()));
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
            dialog.setScene(new Scene(IDayList.load(new CDayList(this.school, this.dayMetadata)).load()));
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
            dialog.setScene(new Scene(IPeriodList.load(new CPeriodList(this.school, this.periodMetadata)).load()));
        } catch (IOException ignored) {
        }

        dialog.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
    }
}
