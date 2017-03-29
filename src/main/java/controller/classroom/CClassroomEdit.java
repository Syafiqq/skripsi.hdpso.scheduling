package controller.classroom;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 25 January 2017, 8:06 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import model.AbstractModel;
import model.database.component.DBClassroom;
import model.database.component.metadata.DBMClassroom;
import model.database.component.metadata.DBMSchool;
import model.database.core.DBType;
import model.database.model.MClassroom;
import model.database.model.MTimetable;
import model.method.pso.hdpso.component.Setting;
import model.util.Dump;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("WeakerAccess")
public class CClassroomEdit implements Initializable {
    @NotNull
    private final DBMClassroom classroom;
    public TextField tfName;

    public CClassroomEdit(@NotNull final DBMClassroom classroom) {
        this.classroom = classroom;
    }

    public CClassroomEdit() throws UnsupportedEncodingException, SQLException {
        @NotNull final AbstractModel      model             = new MTimetable(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
        @NotNull final DBMSchool          schoolMetadata    = Dump.schoolMetadata();
        @NotNull final List<DBMClassroom> classroomMetadata = MClassroom.getAllMetadataFromSchool(model, schoolMetadata);
        if (classroomMetadata.size() > 0) {
            this.classroom = MClassroom.getFromMetadata(model, schoolMetadata, classroomMetadata.get(0));
        } else {
            this.classroom = new DBClassroom(-1, "A", schoolMetadata);
            System.exit(0);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tfName.setText(this.classroom.getName());
    }

    public void onCloseClassroomEditPressed(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }

    public void onSaveClassroomEditPressed(ActionEvent actionEvent) {
        try {
            @NotNull final AbstractModel model = new MClassroom(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
            MClassroom.update(model,
                    this.classroom,
                    this.tfName.getText()
            );
            @NotNull final Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Berhasil");
            alert.setHeaderText(null);
            alert.setContentText("Data Berhasil Dirubah !");

            @NotNull final Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    this.classroomUpdated(this.classroom, this.tfName.getText());
                    this.onCloseClassroomEditPressed(actionEvent);
                }
            }
        } catch (SQLException | UnsupportedEncodingException ignored) {
            System.err.println("Error Activating Database");
            System.exit(-1);
        }
    }

    public void classroomUpdated(@NotNull final DBMClassroom classroom, @NotNull final String name) {

    }
}

