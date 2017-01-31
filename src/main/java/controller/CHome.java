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
import javafx.scene.chart.CategoryAxis;
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
import model.custom.java.util.ParticleObserver;
import model.custom.java.util.SwarmObserver;
import org.controlsfx.control.ToggleSwitch;
import org.jetbrains.annotations.NotNull;
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
@SuppressWarnings({"WeakerAccess", "unused"}) public class CHome implements Initializable
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
                    ((WebView) _arg[0]).zoomProperty().bind(CHome.this.home_content_slider_web_zoom.valueProperty());
                }
                if(_arg[1] instanceof Double)
                {
                    this.home_content_label_fitness_value.setText(String.format(Locale.getDefault(), "%f", (Double) (_arg[1])));
                }
            }
        };
        @NotNull final SwarmObserver swarmObserver = particles ->
                Platform.runLater(new Runnable()
                {
                    @Override public void run()
                    {
                        final CategoryAxis xAxis = new CategoryAxis();
                        final NumberAxis   yAxis = new NumberAxis();
                        for(int c_p = -1, cs_p = particles.length; ++c_p < cs_p; )
                        {
                            @NotNull final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
                            lineChart.setTitle("Particle " + (c_p + 1));
                            @NotNull final XYChart.Series<String, Number> sFitness      = new XYChart.Series<>();
                            @NotNull final XYChart.Series<String, Number> sPBestFitness = new XYChart.Series<>();
                            sFitness.setName("Fitness");
                            sPBestFitness.setName("PBest Fitness");
                            @NotNull final ObservableList<XYChart.Data<String, Number>> dFitness      = sFitness.getData();
                            @NotNull final ObservableList<XYChart.Data<String, Number>> dPBestFitness = sPBestFitness.getData();
                            @NotNull final ParticleObserver poFitness = new ParticleObserver()
                            {
                                private int iteration = 0;

                                @Override public void update(double val)
                                {
                                    if(++iteration % 1000 == 0)
                                    {
                                        Platform.runLater(() -> dFitness.add(new XYChart.Data<>(String.valueOf(this.iteration), val)));
                                    }
                                }
                            };
                            @NotNull final ParticleObserver poPBestFitness = new ParticleObserver()
                            {
                                private int iteration = 0;

                                @Override public void update(double val)
                                {
                                    if(++iteration % 1000 == 0)
                                    {
                                        Platform.runLater(() -> dPBestFitness.add(new XYChart.Data<>(String.valueOf(this.iteration), val)));
                                    }
                                }
                            };
                            particles[c_p].setParticleFitness(poFitness);
                            particles[c_p].setParticlePBestFitness(poPBestFitness);
                            lineChart.getData().add(sFitness);
                            lineChart.getData().add(sPBestFitness);
                            CHome.this.gpChart.add(lineChart, 0, (c_p + 1));
                        }
                    }
                });

        try
        {
            @NotNull final CMCategory category = new CMCategory(content);
            this.pbTimetable.visibleProperty().bind(category.getGenerateListener());
            //category.setSwarmObserver(swarmObserver);
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
