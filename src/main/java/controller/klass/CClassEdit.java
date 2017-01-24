package controller.klass;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 24 January 2017, 9:07 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import model.AbstractModel;
import model.database.component.DBClass;
import model.database.component.metadata.DBMClass;
import model.database.component.metadata.DBMSchool;
import model.database.core.DBType;
import model.database.model.MClass;
import model.database.model.MSchool;
import model.method.pso.hdpso.component.Setting;
import model.util.Dump;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@SuppressWarnings("WeakerAccess")
public class CClassEdit implements Initializable {
    @NotNull
    private final DBMClass klass;
    public TextField tfName;

    public CClassEdit(@NotNull final DBMClass klass) {
        this.klass = klass;
    }

    public CClassEdit() throws UnsupportedEncodingException, SQLException {
        @NotNull final AbstractModel model = new MSchool(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
        @NotNull final DBMSchool schoolMetadata = Dump.schoolMetadata();
        @NotNull final List<DBMClass> klassMetadata = MClass.getAllMetadataFromSchool(model, schoolMetadata);
        if (klassMetadata.size() > 0) {
            this.klass = MClass.getFromMetadata(model, schoolMetadata, klassMetadata.get(0));
        } else {
            this.klass = new DBClass(-1, "A", schoolMetadata);
            System.exit(0);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tfName.setText(this.klass.getName());
    }

    public void onCloseClassEditPressed(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }

    public void onSaveClassEditPressed(ActionEvent actionEvent) {
        try {
            @NotNull final AbstractModel model = new MClass(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
            MClass.update(model,
                    this.klass,
                    this.tfName.getText()
            );
            @NotNull final Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Berhasil");
            alert.setHeaderText(null);
            alert.setContentText("Data Berhasil Dirubah !");

            @NotNull final Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    this.klassUpdated(this.klass, this.tfName.getText());
                    this.onCloseClassEditPressed(actionEvent);
                }
            }
        } catch (SQLException | UnsupportedEncodingException ignored) {
            System.err.println("Error Activating Database");
            System.exit(-1);
        }
    }

    public void klassUpdated(@NotNull final DBMClass klass, @NotNull final String name) {

    }
}
