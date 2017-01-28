package controller;

import controller.menu.CMCategory;
import controller.menu.CMDeveloper;
import controller.menu.CMHome;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Observer;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
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
    public BorderPane home_ribbon;
    @FXML
    public Button home_menu_home;
    @FXML
    public Button home_menu_configuration;
    @FXML
    public Button home_menu_development;
    @FXML
    public BorderPane home_container;
    @FXML
    public Label home_content_label_fitness_value;
    @FXML
    public Slider home_content_slider_web_zoom;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.setRibbon(IMHome.load(new CMHome()).load());
        } catch (IOException ignored) {

        }
    }

    private void setRibbon(Node node) {
        this.home_ribbon.setBottom(null);
        this.home_ribbon.setBottom(node);
    }

    public void onHomeClick() {
        try {
            this.setRibbon(IMHome.load(new CMHome()).load());
        } catch (IOException ignored) {

        }
    }

    public void onConfigurationClick() {
        @NotNull final Observer content = (o, arg) ->
        {
            if (arg instanceof Object[]) {
                @NotNull final Object[] _arg = (Object[]) arg;
                if (_arg[0] instanceof Node) {
                    this.home_container.setCenter(null);
                    this.home_container.setCenter((Node) _arg[0]);
                    ((WebView) _arg[0]).zoomProperty().bind(CHome.this.home_content_slider_web_zoom.valueProperty());
                }
                if (_arg[1] instanceof Double) {
                    this.home_content_label_fitness_value.setText(String.format(Locale.getDefault(), "%f", (Double) (_arg[1])));
                }
            }
        };
        try {
            this.setRibbon(IMCategory.load(new CMCategory(content)).load());
        } catch (IOException ignored) {

        }
    }

    public void onDevelopmentClick() {
        try {
            this.setRibbon(IMDeveloper.load(new CMDeveloper()).load());
        } catch (IOException ignored) {

        }
    }
}
