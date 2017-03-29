package model.dataset.loader;

import java.util.Arrays;
import model.database.component.DBTimetable;
import model.database.loader.DBProblemLoader;
import model.dataset.core.Dataset;
import model.dataset.core.DatasetConverter;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.dataset.loader> created by : 
 * Name         : syafiq
 * Date / Time  : 02 November 2016, 10:47 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class TDatasetGenerator_Periods
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

    @Test public void periodTest_001()
    {
        final @NotNull Dataset dataset = this.dsLoader.getDataset();
        System.out.printf("[%3d] %s\n", dataset.getPeriods().length, Arrays.toString(dataset.getPeriods()));
    }

    @Test public void periodEncoderTest_001()
    {
        final @NotNull DatasetConverter encoder = this.dsLoader.getEncoder();
        System.out.println(encoder.getActivePeriods());
    }

    @Test public void periodDecoderTest_001()
    {
        final @NotNull DatasetConverter decoder = this.dsLoader.getDecoder();
        System.out.println(decoder.getActivePeriods());
    }
}
