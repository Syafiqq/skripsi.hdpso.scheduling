package model.dataset.loader;

import java.util.Arrays;
import model.database.component.DBTimetable;
import model.database.loader.DBProblemLoader;
import model.dataset.component.DSTimeOff;
import model.dataset.core.Dataset;
import model.dataset.core.DatasetConverter;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.dataset.loader> created by : 
 * Name         : syafiq
 * Date / Time  : 03 November 2016, 8:50 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class TDatasetGenerator_Classrooms
{
    private DBTimetable      school;
    private DBProblemLoader  dbLoader;
    private DatasetGenerator dsLoader;

    @SuppressWarnings("Duplicates") @Before public void initialize()
    {
        this.school = new DBTimetable(1, "Program Teknologi Informasi dan Ilmu Komputer Universitas Brawijaya", "PTIIK UB", "Jl. Veteran No.8, Ketawanggede, Kec. Lowokwaru, Kota Malang, Jawa Timur", "2013 - 2014", 0, 17, 5);
        Assert.assertNotNull(this.school);

        this.dbLoader = new DBProblemLoader(school);
        Assert.assertNotNull(this.dbLoader);

        this.dbLoader.loadData();

        this.dsLoader = new DatasetGenerator(this.dbLoader);
        Assert.assertNotNull(this.dsLoader);
    }

    @Test public void classTest_A_001()
    {
        final @NotNull Dataset dataset = this.dsLoader.getDataset();
        System.out.printf("[%3d] %s\n", dataset.getClassrooms().length, Arrays.toString(Arrays.stream(dataset.getClassrooms()).mapToInt(value -> value.getTimeoff().length).toArray()));
    }

    @Test public void classTest_B_001()
    {
        final @NotNull DatasetConverter encoder = this.dsLoader.getEncoder();
        System.out.println(encoder.getClassrooms());
    }

    @Test public void classTest_C_001()
    {
        final @NotNull DatasetConverter decoder = this.dsLoader.getDecoder();
        System.out.println(decoder.getClassrooms());
    }

    @Test public void classTest_D_001()
    {
        final @NotNull Dataset dataset = this.dsLoader.getDataset();
        int                    counter = -1;
        for(final DSTimeOff timeOff : dataset.getClassrooms())
        {
            System.out.println(++counter);
            Arrays.stream(timeOff.getTimeoff()).forEach(day -> System.out.println(Arrays.toString(day)));
            System.out.println();
            System.out.println();
        }
    }

    @Test public void activeClassroom()
    {
        System.out.println(this.dsLoader.getEncoder().getClassrooms().keySet());
    }

    @Test public void nonactiveClassroomTimeoff()
    {
        final @NotNull Dataset dataset = this.dsLoader.getDataset();
        long size = 0;
        for(final DSTimeOff timeOff : dataset.getClassrooms())
        {
            for(double [] tmpTimeOff : timeOff.getTimeoff())
            {
                size += Arrays.stream(tmpTimeOff).filter(value -> value == 0.2).count();
            }
        }
        System.out.println(size);
    }
}
