package controller.menu;

import controller.constraint.CConstraintList;
import controller.parameter.CParameterList;
import java.io.IOException;
import java.net.URL;
import java.util.Observer;
import java.util.ResourceBundle;
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
import model.database.component.DBConstraint;
import model.database.component.DBParameter;
import model.database.component.metadata.DBMSchool;
import model.util.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import view.constraint.IConstraintList;
import view.parameter.IParameterList;

/*
 * This <skripsi.hdpso.scheduling> project in package <controller.menu> created by : 
 * Name         : syafiq
 * Date / Time  : 01 January 2017, 4:45 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings({"WeakerAccess", "FieldCanBeLocal", "unused"}) public class CMDeveloper implements Initializable
{
    @FXML public Button       bParameter;
    @FXML public Button       bConstraint;
    @Nullable
    private      DBMSchool    schoolMetadata;
    @Nullable
    private      DBParameter  parameter;
    @Nullable
    private      DBConstraint constraint;

    public CMDeveloper()
    {
        this.setData();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        @NotNull final BooleanProperty schoolExistenceListener = new SimpleBooleanProperty(true);
        @NotNull final Observer schoolExistenceObserver = (o, arg) ->
        {
            schoolExistenceListener.setValue(!Session.getInstance().containsKey("school"));
            if(arg != null)
            {
                if(arg instanceof String)
                {
                    if(((String) arg).contentEquals("school"))
                    {
                        CMDeveloper.this.setData();
                    }
                }
            }
        };
        Session.getInstance().addObserver(schoolExistenceObserver);
        this.bConstraint.disableProperty().bind(schoolExistenceListener);
        this.bParameter.disableProperty().bind(schoolExistenceListener);
    }

    private void setData()
    {
        final Session session = Session.getInstance();
        this.schoolMetadata = (DBMSchool) session.get("school");
        this.parameter = (DBParameter) session.get("parameter");
        this.constraint = (DBConstraint) session.get("constraint");

    }

    public void onParameterDeveloperMenuPressed(ActionEvent actionEvent)
    {
        if((this.schoolMetadata != null) && (this.parameter != null))
        {
            @NotNull final Stage dialog = new Stage();
            dialog.setTitle("Parameter");

            try
            {
                dialog.setScene(new Scene(IParameterList.load(new CParameterList(this.parameter)).load()));
            }
            catch(IOException ignored)
            {
                ignored.printStackTrace();
            }

            dialog.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();
        }
    }

    public void onConstraintDeveloperMenuPressed(ActionEvent actionEvent)
    {
        if((this.schoolMetadata != null) && (this.constraint != null))
        {
            @NotNull final Stage dialog = new Stage();
            dialog.setTitle("Parameter");

            try
            {
                dialog.setScene(new Scene(IConstraintList.load(new CConstraintList(this.constraint)).load()));
            }
            catch(IOException ignored)
            {
                ignored.printStackTrace();
            }

            dialog.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();
        }
    }
}
