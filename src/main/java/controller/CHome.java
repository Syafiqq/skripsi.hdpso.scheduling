package controller;

import controller.menu.CMCategory;
import controller.menu.CMDeveloper;
import controller.menu.CMHome;
import java.io.IOException;
import java.net.URL;
import java.util.Observer;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
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
public class CHome implements Initializable
{
    @FXML public BorderPane home_ribbon;
    @FXML public Button     home_menu_home;
    @FXML public Button     home_menu_configuration;
    @FXML public Button     home_menu_development;
    @FXML public BorderPane home_container;

    @Override public void initialize(URL location, ResourceBundle resources)
    {
        try
        {
            this.setRibbon(IMHome.load(new CMHome()).load());
        }
        catch(IOException ignored)
        {

        }
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
        Observer content = (o, arg) ->
        {
            if(arg instanceof Node)
            {
                this.home_container.setCenter(null);
                this.home_container.setCenter((Node) arg);
            }
        };
        try
        {
            this.setRibbon(IMCategory.load(new CMCategory(content)).load());
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
