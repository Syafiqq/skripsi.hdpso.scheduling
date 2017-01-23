package view;

/*
 * This <skripsi.hdpso.scheduling> project in package <view> created by : 
 * Name         : syafiq
 * Date / Time  : 01 January 2017, 2:29 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import javafx.application.Application;
import model.AbstractModel;
import model.database.component.metadata.DBMSchool;
import model.database.core.DBType;
import model.database.model.*;
import model.method.pso.hdpso.component.Setting;
import model.util.Dump;
import model.util.Session;
import org.jetbrains.annotations.NotNull;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

@SuppressWarnings("Duplicates")
public class THome {
    @BeforeClass
    public static void setUpClass() throws InterruptedException, UnsupportedEncodingException, SQLException {
        @NotNull final AbstractModel model = new MSchool(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
        @NotNull final Session session = Session.getInstance();
        @NotNull final DBMSchool school = Dump.schoolMetadata();
        session.put("school", Dump.schoolMetadata());
        session.put("day", MDay.getAllMetadataFromSchool(model, school));
        session.put("period", MPeriod.getAllMetadataFromSchool(model, school));
        session.put("subject", MSubject.getAllMetadataFromSchool(model, school));
        session.put("availability", MAvailability.getAll(model));

        Thread t = new Thread() {
            public void run() {
                Application.launch(IHome.class);
            }
        };
        t.setDaemon(true);
        t.start();
        t.join();
    }

    @Test
    public void Test() {

    }
}
