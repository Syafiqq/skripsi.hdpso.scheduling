package controller.menu;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;

/*
 * This <skripsi.hdpso.scheduling> project in package <controller> created by : 
 * Name         : syafiq
 * Date / Time  : 01 January 2017, 3:51 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class CMHome implements Initializable
{
    @Override public void initialize(URL location, ResourceBundle resources)
    {

    }

    public void onButtonExitPressed(ActionEvent event)
    {
        ((Node) event.getSource()).getScene().getWindow().hide();
    }
}
