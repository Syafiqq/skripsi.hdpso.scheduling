package model.dataset.loader;

import java.util.Arrays;
import model.database.component.DBSchool;
import model.database.loader.DBProblemLoader;
import model.dataset.core.Dataset;
import model.dataset.core.DatasetConverter;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.dataset.dbLoader> created by :
 * Name         : syafiq
 * Date / Time  : 02 November 2016, 10:31 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class TDatasetGenerator_Days
{
    private DBSchool         school;
    private DBProblemLoader  dbLoader;
    private DatasetGenerator dsLoader;

    @SuppressWarnings("Duplicates") @Before public void initialize()
    {
        this.school = new DBSchool(1, "Program Teknologi Informasi dan Ilmu Komputer Universitas Brawijaya", "PTIIK UB", "Jl. Veteran No.8, Ketawanggede, Kec. Lowokwaru, Kota Malang, Jawa Timur", "2013 - 2014", 0, 17, 5);
        Assert.assertNotNull(this.school);

        this.dbLoader = new DBProblemLoader(school);
        Assert.assertNotNull(this.dbLoader);

        this.dbLoader.loadData();

        this.dsLoader = new DatasetGenerator(this.dbLoader);
        Assert.assertNotNull(this.dsLoader);
    }

    @Test public void dayTest_001()
    {
        final @NotNull Dataset dataset = this.dsLoader.getDataset();
        System.out.printf("[%3d] %s\n", dataset.getDays().length, Arrays.toString(dataset.getDays()));
    }

    @Test public void dayEncoderTest_001()
    {
        final @NotNull DatasetConverter encoder = this.dsLoader.getEncoder();
        System.out.println(encoder.getActiveDays());
    }

    @Test public void dayDecoderTest_001()
    {
        final @NotNull DatasetConverter decoder = this.dsLoader.getDecoder();
        System.out.println(decoder.getActiveDays());
    }
}
