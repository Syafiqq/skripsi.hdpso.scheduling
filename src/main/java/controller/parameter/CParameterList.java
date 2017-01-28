package controller.parameter;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import model.AbstractModel;
import model.database.component.DBParameter;
import model.database.component.metadata.DBMSchool;
import model.database.core.DBType;
import model.database.model.MParameter;
import model.database.model.MSchool;
import model.method.pso.hdpso.component.Setting;
import model.method.pso.hdpso.core.VelocityCalculator;
import model.util.Dump;
import org.apache.commons.lang3.math.NumberUtils;
import org.controlsfx.control.RangeSlider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * This <skripsi.hdpso.scheduling> project in package <controller.parameter> created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 26 January 2017, 6:49 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings({"WeakerAccess", "unused"}) public class CParameterList implements Initializable
{
    private final DBParameter       parameter;
    @FXML public  TextField         tfBLocMin;
    @FXML public  TextField         tfBLocMax;
    @FXML public  TextField         tfBGlobMin;
    @FXML public  TextField         tfBGlobMax;
    @FXML public  TextField         tfBRandMin;
    @FXML public  TextField         tfBRandMax;
    @FXML public  RangeSlider       rsBLoc;
    @FXML public  RangeSlider       rsBGlob;
    @FXML public  RangeSlider       rsBRand;
    @FXML public  ComboBox<Integer> cbProcessor;
    @FXML public  ComboBox<Integer> cbMethod;
    @FXML public  CheckBox          cbIsMultiThread;
    @FXML public  TextField         tfParticle;
    @FXML public  TextField         tfIteration;

    public CParameterList(@NotNull DBParameter parameter)
    {
        this.parameter = parameter;
    }

    public CParameterList() throws UnsupportedEncodingException, SQLException
    {
        @NotNull final AbstractModel model          = new MSchool(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
        @NotNull final DBMSchool     schoolMetadata = Dump.schoolMetadata();
        this.parameter = MParameter.getFromSchool(model, schoolMetadata);
    }

    @Override public void initialize(URL location, ResourceBundle resources)
    {
        @NotNull final DecimalFormat df = new DecimalFormat("#.#####");
        df.setRoundingMode(RoundingMode.CEILING);
        @NotNull final NumberStringConverter nsc = new NumberStringConverter()
        {
            @NotNull @Override public Number fromString(@NotNull final String value)
            {
                try
                {
                    return NumberUtils.createNumber(value);
                }
                catch(NumberFormatException ignored)
                {
                    return 0;
                }
            }

            @NotNull @Override public String toString(@NotNull final Number value)
            {
                return df.format(value.doubleValue());
            }
        };
        Bindings.bindBidirectional(this.tfBLocMin.textProperty(), this.rsBLoc.lowValueProperty(), nsc);
        Bindings.bindBidirectional(this.tfBLocMax.textProperty(), this.rsBLoc.highValueProperty(), nsc);
        Bindings.bindBidirectional(this.tfBGlobMin.textProperty(), this.rsBGlob.lowValueProperty(), nsc);
        Bindings.bindBidirectional(this.tfBGlobMax.textProperty(), this.rsBGlob.highValueProperty(), nsc);
        Bindings.bindBidirectional(this.tfBRandMin.textProperty(), this.rsBRand.lowValueProperty(), nsc);
        Bindings.bindBidirectional(this.tfBRandMax.textProperty(), this.rsBRand.highValueProperty(), nsc);
        @NotNull final IntList listProc = new IntArrayList(Runtime.getRuntime().availableProcessors());
        for(int c_list = -1, cs_list = Runtime.getRuntime().availableProcessors(); ++c_list < cs_list; )
        {
            listProc.add(c_list + 1);
        }
        this.cbProcessor.setItems(FXCollections.observableList(listProc));
        this.cbMethod.setItems(FXCollections.observableArrayList(0, 1, 2));
        this.cbMethod.setConverter(new StringConverter<Integer>()
        {
            @Override public String toString(Integer object)
            {
                switch(object)
                {
                    case 0:
                    {
                        return "HDPSO Tanpa Time Variant";
                    }
                    case 1:
                    {
                        return "HDPSO";
                    }
                    case 2:
                    {
                        return "HDPSO Tanpa Random";
                    }
                }
                return null;
            }

            @Override public Integer fromString(String string)
            {
                return null;
            }
        });

        this.rsBLoc.lowValueProperty().setValue(this.parameter.getbLocMin());
        this.rsBLoc.highValueProperty().setValue(this.parameter.getbLocMax());
        this.rsBGlob.lowValueProperty().setValue(this.parameter.getgLobMin());
        this.rsBGlob.highValueProperty().setValue(this.parameter.getgLobMax());
        this.rsBRand.lowValueProperty().setValue(this.parameter.getbRandMin());
        this.rsBRand.highValueProperty().setValue(this.parameter.getbRandMax());
        this.tfParticle.setText(String.valueOf(this.parameter.getParticle()));
        this.tfIteration.setText(String.valueOf(this.parameter.getIteration()));
        this.cbMethod.getSelectionModel().select(Integer.valueOf(Setting.getVelocity(this.parameter.getMethod())));
        this.cbProcessor.getSelectionModel().select(Integer.valueOf(this.parameter.getProcessor()));
        this.cbIsMultiThread.selectedProperty().setValue(this.parameter.isMultiThread());
    }

    public void onCloseParameterListPressed(ActionEvent actionEvent)
    {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }

    public void onSaveParameterListPressed(ActionEvent actionEvent)
    {
        try
        {
            final double                       bLocMin       = NumberUtils.createDouble(this.tfBLocMin.getText());
            final double                       bLocMax       = NumberUtils.createDouble(this.tfBLocMax.getText());
            final double                       bGlobMin      = NumberUtils.createDouble(this.tfBGlobMin.getText());
            final double                       bGlobMax      = NumberUtils.createDouble(this.tfBGlobMax.getText());
            final double                       bRandMin      = NumberUtils.createDouble(this.tfBRandMin.getText());
            final double                       bRandMax      = NumberUtils.createDouble(this.tfBRandMax.getText());
            final int                          iteration     = NumberUtils.createInteger(this.tfIteration.getText());
            final int                          particle      = NumberUtils.createInteger(this.tfParticle.getText());
            final int                          processor     = this.cbProcessor.getSelectionModel().getSelectedItem();
            @Nullable final VelocityCalculator method        = Setting.getVelocity(this.cbMethod.getSelectionModel().getSelectedItem());
            final boolean                      isMultiThread = this.cbIsMultiThread.selectedProperty().get();
            @NotNull Alert                     alert         = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Konfirmasi");
            alert.setHeaderText(null);
            alert.setContentText("Apakah Anda yakin untuk merubah data parameter? ");

            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent())
            {
                if(result.get() == ButtonType.OK)
                {
                    this.parameter.setbLocMin(bLocMin);
                    this.parameter.setbLocMax(bLocMax);
                    this.parameter.setgLobMin(bGlobMin);
                    this.parameter.setgLobMax(bGlobMax);
                    this.parameter.setbRandMin(bRandMin);
                    this.parameter.setbRandMax(bRandMax);
                    this.parameter.setIteration(iteration);
                    this.parameter.setParticle(particle);
                    this.parameter.setProcessor(processor);
                    this.parameter.setMethod(method);
                    this.parameter.setMultiThread(isMultiThread);
                    try
                    {
                        @NotNull final AbstractModel model = new MSchool(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
                        MParameter.update(model, this.parameter);
                        @NotNull final Alert success = new Alert(Alert.AlertType.INFORMATION);
                        success.setTitle("Berhasil");
                        success.setHeaderText(null);
                        success.setContentText("Data Berhasil Dirubah !");
                        success.showAndWait();
                        return;
                    }
                    catch(SQLException | UnsupportedEncodingException e)
                    {
                        e.printStackTrace();
                    }
                }
                else if((result.get() == ButtonType.CANCEL) || (result.get() == ButtonType.CLOSE))
                {
                    return;
                }
            }
        }
        catch(NumberFormatException ignored)
        {

        }
        @NotNull Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Kesalahan");
        alert.setHeaderText(null);
        alert.setContentText("Terdapat Kesalahan, Silahakan isi data dengan benar");
        alert.showAndWait();
    }
}
