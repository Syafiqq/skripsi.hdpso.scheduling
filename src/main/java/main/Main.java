package main;/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 01 January 2017, 9:10 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application
{
    @Override
    public void start(Stage primaryStage) throws IOException
    {
        Parent root = FXMLLoader.load(this.getClass().getResource("/layout/category/dashboard.fxml"));
        primaryStage.setTitle("Developer Mode");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }
}