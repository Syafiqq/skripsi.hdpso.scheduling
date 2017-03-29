package controller.constraint;

import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import model.AbstractModel;
import model.database.component.DBConstraint;
import model.database.component.metadata.DBMSchool;
import model.database.core.DBType;
import model.database.model.MConstraint;
import model.database.model.MTimetable;
import model.method.pso.hdpso.component.Setting;
import model.util.Converter;
import model.util.Dump;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <controller.constraint> created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 28 January 2017, 9:51 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings({"WeakerAccess", "unused", "FieldCanBeLocal"}) public class CConstraintList implements Initializable
{
    private final int CONSTRAINT_LENGTH = 8;
    @NotNull private final DBConstraint constraint;
    @FXML public           CheckBox     cbc1;
    @FXML public           CheckBox     cbc2;
    @FXML public           CheckBox     cbc3;
    @FXML public           CheckBox     cbc4;
    @FXML public           CheckBox     cbc5;
    @FXML public           CheckBox     cbc6;
    @FXML public           CheckBox     cbc7;
    @FXML public           CheckBox     cbc8;
    @FXML public           PieChart     pcConstraint;
    @FXML public           TextField    tfc1;
    @FXML public           TextField    tfc2;
    @FXML public           TextField    tfc3;
    @FXML public           TextField    tfc4;
    @FXML public           TextField    tfc5;
    @FXML public           TextField    tfc6;
    @FXML public           TextField    tfc7;
    @FXML public           TextField    tfc8;
    @FXML public           Pane         pc1;
    @FXML public           Pane         pc2;
    @FXML public           Pane         pc3;
    @FXML public           Pane         pc4;
    @FXML public           Pane         pc5;
    @FXML public           Pane         pc6;
    @FXML public           Pane         pc7;
    @FXML public           Pane         pc8;

    public CConstraintList(@NotNull final DBConstraint constraint)
    {
        this.constraint = constraint;
    }

    public CConstraintList() throws UnsupportedEncodingException, SQLException
    {
        @NotNull final AbstractModel model          = new MTimetable(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
        @NotNull final DBMSchool     schoolMetadata = Dump.schoolMetadata();
        this.constraint = MConstraint.getFromSchool(model, schoolMetadata);
    }

    @Override public void initialize(URL location, ResourceBundle resources)
    {
        @NotNull final DecimalFormat df = new DecimalFormat("#.#####");
        df.setRoundingMode(RoundingMode.CEILING);

        @NotNull final List<DoubleProperty> valProp = new ArrayList<>(CONSTRAINT_LENGTH);
        @NotNull final List<PieChart.Data>  bindVal = new ArrayList<>(CONSTRAINT_LENGTH);

        for(int c_c = -1; ++c_c < CONSTRAINT_LENGTH; )
        {
            valProp.add(new SimpleDoubleProperty(0));
            bindVal.add(new PieChart.Data("Constraint " + (c_c + 1), 0));
            bindVal.get(c_c).pieValueProperty().bind(valProp.get(c_c));
        }

        this.cbc1.selectedProperty().addListener((observable, oldValue, newValue) -> this.updateData(valProp.get(0), Converter.integerToBooleanInteger(newValue), this.tfc1.textProperty().get()));
        this.cbc2.selectedProperty().addListener((observable, oldValue, newValue) -> this.updateData(valProp.get(1), Converter.integerToBooleanInteger(newValue), this.tfc2.textProperty().get()));
        this.cbc3.selectedProperty().addListener((observable, oldValue, newValue) -> this.updateData(valProp.get(2), Converter.integerToBooleanInteger(newValue), this.tfc3.textProperty().get()));
        this.cbc4.selectedProperty().addListener((observable, oldValue, newValue) -> this.updateData(valProp.get(3), Converter.integerToBooleanInteger(newValue), this.tfc4.textProperty().get()));
        this.cbc5.selectedProperty().addListener((observable, oldValue, newValue) -> this.updateData(valProp.get(4), Converter.integerToBooleanInteger(newValue), this.tfc5.textProperty().get()));
        this.cbc6.selectedProperty().addListener((observable, oldValue, newValue) -> this.updateData(valProp.get(5), Converter.integerToBooleanInteger(newValue), this.tfc6.textProperty().get()));
        this.cbc7.selectedProperty().addListener((observable, oldValue, newValue) -> this.updateData(valProp.get(6), Converter.integerToBooleanInteger(newValue), this.tfc7.textProperty().get()));
        this.cbc8.selectedProperty().addListener((observable, oldValue, newValue) -> this.updateData(valProp.get(7), Converter.integerToBooleanInteger(newValue), this.tfc8.textProperty().get()));
        this.tfc1.textProperty().addListener((observable, oldValue, newValue) -> this.updateData(valProp.get(0), Converter.integerToBooleanInteger(this.cbc1.selectedProperty().get()), newValue));
        this.tfc2.textProperty().addListener((observable, oldValue, newValue) -> this.updateData(valProp.get(1), Converter.integerToBooleanInteger(this.cbc2.selectedProperty().get()), newValue));
        this.tfc3.textProperty().addListener((observable, oldValue, newValue) -> this.updateData(valProp.get(2), Converter.integerToBooleanInteger(this.cbc3.selectedProperty().get()), newValue));
        this.tfc4.textProperty().addListener((observable, oldValue, newValue) -> this.updateData(valProp.get(3), Converter.integerToBooleanInteger(this.cbc4.selectedProperty().get()), newValue));
        this.tfc5.textProperty().addListener((observable, oldValue, newValue) -> this.updateData(valProp.get(4), Converter.integerToBooleanInteger(this.cbc5.selectedProperty().get()), newValue));
        this.tfc6.textProperty().addListener((observable, oldValue, newValue) -> this.updateData(valProp.get(5), Converter.integerToBooleanInteger(this.cbc6.selectedProperty().get()), newValue));
        this.tfc7.textProperty().addListener((observable, oldValue, newValue) -> this.updateData(valProp.get(6), Converter.integerToBooleanInteger(this.cbc7.selectedProperty().get()), newValue));
        this.tfc8.textProperty().addListener((observable, oldValue, newValue) -> this.updateData(valProp.get(7), Converter.integerToBooleanInteger(this.cbc8.selectedProperty().get()), newValue));

        this.pcConstraint.setData(FXCollections.observableArrayList(bindVal));
        this.pcConstraint.legendVisibleProperty().setValue(false);

        this.cbc1.selectedProperty().set(this.constraint.isSubject());
        this.cbc2.selectedProperty().set(this.constraint.isLecture());
        this.cbc3.selectedProperty().set(this.constraint.isKlass());
        this.cbc4.selectedProperty().set(this.constraint.isClassroom());
        this.cbc5.selectedProperty().set(this.constraint.isLPlacement());
        this.cbc6.selectedProperty().set(this.constraint.isCPlacement());
        this.cbc7.selectedProperty().set(this.constraint.isLink());
        this.cbc8.selectedProperty().set(this.constraint.isAllow());
        this.tfc1.textProperty().setValue(df.format(this.constraint.getSubject()));
        this.tfc2.textProperty().setValue(df.format(this.constraint.getLecture()));
        this.tfc3.textProperty().setValue(df.format(this.constraint.getKlass()));
        this.tfc4.textProperty().setValue(df.format(this.constraint.getClassroom()));
        this.tfc5.textProperty().setValue(df.format(this.constraint.getLPlacement()));
        this.tfc6.textProperty().setValue(df.format(this.constraint.getCPlacement()));
        this.tfc7.textProperty().setValue(df.format(this.constraint.getLink()));
        this.tfc8.textProperty().setValue(df.format(this.constraint.getAllow()));

        bindVal.get(0).getNode().setStyle("-fx-pie-color: " + this.pc1.getStyle().split(";")[0].replace("-fx-background-color: ", ""));
        bindVal.get(1).getNode().setStyle("-fx-pie-color: " + this.pc2.getStyle().split(";")[0].replace("-fx-background-color: ", ""));
        bindVal.get(2).getNode().setStyle("-fx-pie-color: " + this.pc3.getStyle().split(";")[0].replace("-fx-background-color: ", ""));
        bindVal.get(3).getNode().setStyle("-fx-pie-color: " + this.pc4.getStyle().split(";")[0].replace("-fx-background-color: ", ""));
        bindVal.get(4).getNode().setStyle("-fx-pie-color: " + this.pc5.getStyle().split(";")[0].replace("-fx-background-color: ", ""));
        bindVal.get(5).getNode().setStyle("-fx-pie-color: " + this.pc6.getStyle().split(";")[0].replace("-fx-background-color: ", ""));
        bindVal.get(6).getNode().setStyle("-fx-pie-color: " + this.pc7.getStyle().split(";")[0].replace("-fx-background-color: ", ""));
        bindVal.get(7).getNode().setStyle("-fx-pie-color: " + this.pc8.getStyle().split(";")[0].replace("-fx-background-color: ", ""));
    }

    private void updateData(@NotNull final DoubleProperty property, int isEnable, @NotNull final String value)
    {
        try
        {
            this.updateData(property, isEnable, NumberUtils.createDouble(value));
        }
        catch(NumberFormatException ignored)
        {
            this.updateData(property, isEnable, 0);
        }
    }

    private void updateData(@NotNull final DoubleProperty property, int isEnable, final double value)
    {
        property.setValue(isEnable * value);
    }

    public void onCloseConstraintListPressed(ActionEvent actionEvent)
    {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }

    public void onSaveConstraintListPressed(ActionEvent actionEvent)
    {
        try
        {
            final double   subject      = NumberUtils.createDouble(this.tfc1.getText());
            final double   lecture      = NumberUtils.createDouble(this.tfc2.getText());
            final double   klass        = NumberUtils.createDouble(this.tfc3.getText());
            final double   classroom    = NumberUtils.createDouble(this.tfc4.getText());
            final double   lPlacement   = NumberUtils.createDouble(this.tfc5.getText());
            final double   cPlacement   = NumberUtils.createDouble(this.tfc6.getText());
            final double   link         = NumberUtils.createDouble(this.tfc7.getText());
            final double   allow        = NumberUtils.createDouble(this.tfc8.getText());
            final boolean  isSubject    = this.cbc1.selectedProperty().get();
            final boolean  isLecture    = this.cbc2.selectedProperty().get();
            final boolean  isKlass      = this.cbc3.selectedProperty().get();
            final boolean  isClassroom  = this.cbc4.selectedProperty().get();
            final boolean  isLPlacement = this.cbc5.selectedProperty().get();
            final boolean  isCPlacement = this.cbc6.selectedProperty().get();
            final boolean  isLink       = this.cbc7.selectedProperty().get();
            final boolean  isAllow      = this.cbc8.selectedProperty().get();
            @NotNull Alert alert        = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Konfirmasi");
            alert.setHeaderText(null);
            alert.setContentText("Apakah Anda yakin untuk merubah data constraint? ");

            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent())
            {
                if(result.get() == ButtonType.OK)
                {
                    this.constraint.setSubject(subject);
                    this.constraint.setSubject(isSubject);
                    this.constraint.setLecture(lecture);
                    this.constraint.setLecture(isLecture);
                    this.constraint.setKlass(klass);
                    this.constraint.setKlass(isKlass);
                    this.constraint.setClassroom(classroom);
                    this.constraint.setClassroom(isClassroom);
                    this.constraint.setLPlacement(lPlacement);
                    this.constraint.setLPlacement(isLPlacement);
                    this.constraint.setCPlacement(cPlacement);
                    this.constraint.setCPlacement(isCPlacement);
                    this.constraint.setLink(link);
                    this.constraint.setLink(isLink);
                    this.constraint.setAllow(allow);
                    this.constraint.setAllow(isAllow);
                    try
                    {
                        @NotNull final AbstractModel model = new MTimetable(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
                        MConstraint.update(model, this.constraint);
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
