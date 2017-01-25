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
import model.database.component.metadata.DBMClass;
import model.database.component.metadata.DBMLecture;
import model.database.component.metadata.DBMSchool;
import model.database.component.metadata.DBMSubject;
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
import java.util.List;

@SuppressWarnings("Duplicates")
public class THome {
    @BeforeClass
    public static void setUpClass() throws InterruptedException, UnsupportedEncodingException, SQLException {
        @NotNull final AbstractModel model = new MSchool(Setting.getDBUrl(Setting.defaultDB, DBType.DEFAULT));
        @NotNull final DBMSchool school = Dump.schoolMetadata();
        @NotNull final Session session = Session.getInstance();
        @NotNull final List<DBMSubject> subjectMetadata = MSubject.getAllMetadataFromSchool(model, school);
        @NotNull final List<DBMClass> classMetadata = MClass.getAllMetadataFromSchool(model, school);
        @NotNull final List<DBMLecture> lectureMetadata = MLecture.getAllMetadataFromSchool(model, school);
        session.put("school", school);
        session.put("day", MDay.getAllMetadataFromSchool(model, school));
        session.put("period", MPeriod.getAllMetadataFromSchool(model, school));
        session.put("availability", MAvailability.getAll(model));
        session.put("subject", subjectMetadata);
        session.put("klass", classMetadata);
        session.put("classroom", MClassroom.getAllMetadataFromSchool(model, school));
        session.put("lecture", lectureMetadata);
        session.put("lesson", MLesson.getAllMetadataFromSchool(model, school, subjectMetadata, classMetadata, lectureMetadata));

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
