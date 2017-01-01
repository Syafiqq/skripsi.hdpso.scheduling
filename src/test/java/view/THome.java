package view;

/*
 * This <skripsi.hdpso.scheduling> project in package <view> created by : 
 * Name         : syafiq
 * Date / Time  : 01 January 2017, 2:29 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import javafx.application.Application;
import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings("Duplicates") public class THome
{
    @BeforeClass
    public static void setUpClass() throws InterruptedException
    {
        Thread t = new Thread()
        {
            public void run()
            {
                Application.launch(IHome.class);
            }
        };
        t.setDaemon(true);
        t.start();
        t.join();
    }

    @Test public void Test()
    {

    }
}
