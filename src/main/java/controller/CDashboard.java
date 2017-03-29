package controller;

import controller.menu.CMCategory;
import controller.menu.CMDeveloper;
import controller.menu.CMHome;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Observer;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebView;
import model.custom.java.util.SwarmObserver;
import model.custom.java.util.ValueObserver;
import model.database.component.DBParameter;
import model.util.Session;
import org.apache.commons.math3.util.FastMath;
import org.controlsfx.control.ToggleSwitch;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import view.menu.IMCategory;
import view.menu.IMDeveloper;
import view.menu.IMHome;

/*
 * This <skripsi.hdpso.scheduling> project in package <controller> created by : 
 * Name         : syafiq
 * Date / Time  : 01 January 2017, 2:18 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings({"WeakerAccess", "unused"}) public class CDashboard implements Initializable
{
    @FXML
    public       BorderPane   home_ribbon;
    @FXML
    public       Button       home_menu_home;
    @FXML
    public       Button       home_menu_configuration;
    @FXML
    public       Button       home_menu_development;
    @FXML
    public       BorderPane   home_container;
    @FXML
    public       Label        home_content_label_fitness_value;
    @FXML
    public       Slider       home_content_slider_web_zoom;
    @FXML
    public       ProgressBar  pbTimetable;
    @FXML
    public       ToggleSwitch tsResult;
    @FXML public BorderPane   bpResult;
    @FXML public BorderPane   bpChart;
    @FXML public GridPane     gpChart;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        try
        {
            this.setRibbon(IMHome.load(new CMHome()).load());
        }
        catch(IOException ignored)
        {

        }
        this.tsResult.selectedProperty().addListener((observable, oldValue, newValue) ->
        {
            this.bpResult.setVisible(newValue);
            this.bpChart.setVisible(!newValue);
        });
    }

    private void setRibbon(Node node)
    {
        this.home_ribbon.setBottom(null);
        this.home_ribbon.setBottom(node);
    }

    public void onHomeClick()
    {
        try
        {
            this.setRibbon(IMHome.load(new CMHome()).load());
        }
        catch(IOException ignored)
        {

        }
    }

    public void onConfigurationClick()
    {
        @NotNull final Observer content = (o, arg) ->
        {
            if(arg instanceof Object[])
            {
                @NotNull final Object[] _arg = (Object[]) arg;
                if(_arg[0] instanceof Node)
                {
                    this.bpResult.setCenter(null);
                    this.bpResult.setCenter((Node) _arg[0]);
                    ((WebView) _arg[0]).zoomProperty().bind(CDashboard.this.home_content_slider_web_zoom.valueProperty());
                }
                if(_arg[1] instanceof Double)
                {
                    this.home_content_label_fitness_value.setText(String.format(Locale.getDefault(), "%f", (Double) (_arg[1])));
                }
            }
        };
        @NotNull final SwarmObserver swarmObserver = particles -> Platform.runLater(() ->
        {
            @Nullable final DBParameter parameter = (DBParameter) Session.getInstance().get("parameter");

            for(int c_p = -1, cs_p = particles.length; ++c_p < cs_p; )
            {
                final NumberAxis                         xAxis     = new NumberAxis();
                final NumberAxis                         yAxis     = new NumberAxis();
                @NotNull final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
                lineChart.setTitle("Particle " + (c_p + 1));
                @NotNull final XYChart.Series<Number, Number> sFitness      = new XYChart.Series<>();
                @NotNull final XYChart.Series<Number, Number> sPBestFitness = new XYChart.Series<>();
                sFitness.setName("Fitness");
                sPBestFitness.setName("PBest Fitness");
                @NotNull final ObservableList<XYChart.Data<Number, Number>> dFitness      = sFitness.getData();
                @NotNull final ObservableList<XYChart.Data<Number, Number>> dPBestFitness = sPBestFitness.getData();
                if(parameter != null)
                {
                    xAxis.lowerBoundProperty().set(0);
                    xAxis.upperBoundProperty().set(FastMath.ceil(parameter.getIteration() / 100 * 110));
                    xAxis.setAutoRanging(false);
                    xAxis.tickUnitProperty().setValue(FastMath.ceil(parameter.getIteration() / 10));
                }
                lineChart.createSymbolsProperty().setValue(false);
                yAxis.setAutoRanging(false);
                yAxis.setLowerBound(Integer.MAX_VALUE);
                yAxis.setUpperBound(Integer.MIN_VALUE);
                yAxis.tickUnitProperty().setValue(FastMath.ceil((yAxis.getUpperBound() - yAxis.getLowerBound()) / 2));
                @NotNull final ValueObserver poFitness = val -> Platform.runLater(() ->
                {
                    if(val[0] % 10 == 0)
                    {

                        if(val[1] >= val[2])
                        {
                            if(val[1] > yAxis.getUpperBound())
                            {
                                yAxis.setUpperBound(val[1] + 1000);
                            }
                            if(val[2] < yAxis.getLowerBound())
                            {
                                yAxis.setLowerBound(val[2] - 1000);
                            }
                        }
                        else
                        {
                            if(val[2] > yAxis.getUpperBound())
                            {
                                yAxis.setUpperBound(val[2] + 1000);
                            }
                            if(val[1] < yAxis.getLowerBound())
                            {
                                yAxis.setLowerBound(val[1] - 1000);
                            }
                        }
                        yAxis.tickUnitProperty().setValue(FastMath.ceil((yAxis.getUpperBound() - yAxis.getLowerBound()) / 2));
                        dPBestFitness.add(new XYChart.Data<>(val[0], val[1]));
                        dFitness.add(new XYChart.Data<>(val[0], val[2]));
                    }
                });
                particles[c_p].setFitnessObserver(poFitness);
                lineChart.getData().add(sFitness);
                lineChart.getData().add(sPBestFitness);
                CDashboard.this.gpChart.add(lineChart, 0, (c_p + 1));
            }
        });

        try
        {
            @NotNull final CMCategory category = new CMCategory(content);
            this.pbTimetable.visibleProperty().bind(category.getGenerateListener());
            category.setSwarmObserver(swarmObserver);
            this.setRibbon(IMCategory.load(category).load());
        }
        catch(IOException ignored)
        {

        }
    }

    public void onDevelopmentClick()
    {
        try
        {
            this.setRibbon(IMDeveloper.load(new CMDeveloper()).load());
        }
        catch(IOException ignored)
        {

        }
    }
}
