package main;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 01 January 2017, 9:13 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */


import javafx.application.Application;
import org.junit.BeforeClass;
import org.junit.Test;

public class TMain
{
    @BeforeClass
    public static void setUpClass() throws InterruptedException
    {
        // Initialise Java FX

        System.out.printf("About to launch FX App\n");
        Thread t = new Thread("JavaFX Init Thread")
        {
            public void run()
            {
                Application.launch(Main.class);
            }
        };
        t.setDaemon(true);
        t.start();
        t.join();
        System.out.printf("FX App thread started\n");
        Thread.sleep(500);
    }

    @Test public void TesT()
    {
        System.out.println("abc");
    }
}