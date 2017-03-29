package model.dataset.component;

import java.util.Arrays;
import model.database.component.DBTimetable;
import model.database.loader.DBProblemLoader;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.dataset.component> created by : 
 * Name         : syafiq
 * Date / Time  : 02 November 2016, 6:35 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("FieldCanBeLocal") public class TDSTimeOff
{
    private DBTimetable     school;
    private DBProblemLoader loader;

    @SuppressWarnings("Duplicates") @Before public void initialize()
    {
        this.school = new DBTimetable(1, "Program Teknologi Informasi dan Ilmu Komputer Universitas Brawijaya", "PTIIK UB", "Jl. Veteran No.8, Ketawanggede, Kec. Lowokwaru, Kota Malang, Jawa Timur", "2013 - 2014", 0, 17, 5);
        Assert.assertNotNull(this.school);

        this.loader = new DBProblemLoader(school);
        Assert.assertNotNull(this.loader);

        this.loader.loadData();
    }

    @SuppressWarnings("Duplicates") @Test public void lecture_timeoffTest()
    {
        final @NotNull DSTimeOff timeOff = DSTimeOff.newInstance(this.loader.getLecturers().get(1).getTimeoff());
        Assert.assertNotNull(timeOff);
        print_Timeoff(timeOff);
    }

    @SuppressWarnings("Duplicates") @Test public void class_timeoffTest()
    {
        final @NotNull DSTimeOff timeOff = DSTimeOff.newInstance(this.loader.getClasses().get(1).getTimeoff());
        Assert.assertNotNull(timeOff);
        print_Timeoff(timeOff);
    }

    @SuppressWarnings("Duplicates") @Test public void classroom_timeoffTest()
    {
        final @NotNull DSTimeOff timeOff = DSTimeOff.newInstance(this.loader.getClassrooms().get(1).getTimeoff());
        Assert.assertNotNull(timeOff);
        print_Timeoff(timeOff);
    }

    @SuppressWarnings("Duplicates") @Test public void subject_timeoffTest()
    {
        final @NotNull DSTimeOff timeOff = DSTimeOff.newInstance(this.loader.getSubjects().get(1).getTimeoff());
        Assert.assertNotNull(timeOff);
        print_Timeoff(timeOff);
    }

    private void print_Timeoff(final @NotNull DSTimeOff timeOff)
    {
        for(final @NotNull double[] day : timeOff.getTimeoff())
        {
            System.out.println(Arrays.toString(day));
        }
    }
}
