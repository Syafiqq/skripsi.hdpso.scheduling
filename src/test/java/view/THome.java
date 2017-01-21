package view;

/*
 * This <skripsi.hdpso.scheduling> project in package <view> created by : 
 * Name         : syafiq
 * Date / Time  : 01 January 2017, 2:29 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import javafx.application.Application;
import model.database.component.DBSchool;
import model.database.core.DBType;
import model.database.model.MSchool;
import model.method.pso.hdpso.component.Setting;
import model.util.Session;
import model.util.pattern.observer.ObservableDBSchool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

@SuppressWarnings("Duplicates") public class THome
{
    @BeforeClass
    public static void setUpClass() throws InterruptedException
    {
        try {
            @NotNull final MSchool mSchool = new MSchool(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));

            @Nullable DBSchool school = mSchool.select(2);
            if (school != null) {
                if (!Session.getInstance().containsKey("school")) {
                    Session.getInstance().put("school", new ObservableDBSchool(school));
                } else {
                    ((ObservableDBSchool) Session.getInstance().get("school")).setSchool(school);
                }
            }
        } catch (SQLException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

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
