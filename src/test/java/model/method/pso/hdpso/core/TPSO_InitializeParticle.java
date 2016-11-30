package model.method.pso.hdpso.core;

import java.util.Random;
import model.database.component.DBSchool;
import model.database.loader.DBProblemLoader;
import model.dataset.loader.DatasetGenerator;
import model.method.pso.hdpso.component.Setting;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.core> created by : 
 * Name         : syafiq
 * Date / Time  : 24 November 2016, 12:53 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class TPSO_InitializeParticle
{
    private DatasetGenerator dsLoader;

    @SuppressWarnings("Duplicates") @Before public void initialize()
    {
        DBSchool school = new DBSchool(1, "Program Teknologi Informasi dan Ilmu Komputer Universitas Brawijaya", "PTIIK UB", "Jl. Veteran No.8, Ketawanggede, Kec. Lowokwaru, Kota Malang, Jawa Timur", "2013 - 2014", 0, 17, 5);
        Assert.assertNotNull(school);

        DBProblemLoader dbLoader = new DBProblemLoader(school);
        Assert.assertNotNull(dbLoader);

        dbLoader.loadData();

        this.dsLoader = new DatasetGenerator(dbLoader);
        Assert.assertNotNull(dsLoader);
    }

    @Test
    public void test_InitializeParticle()
    {
        Setting setting = Setting.getInstance();
        setting.max_particle = 10;
        setting.max_epoch = 1;
        setting.bLoc_min = 0.600;
        setting.bLoc_max = 0.900;
        setting.bGlob_min = 0.100;
        setting.bGlob_max = 0.400;
        setting.bRand_min = 0.001;
        setting.bRand_max = 0.100;
        setting.total_core = 4;

        @NotNull final PSO pso = new PSO(this.dsLoader);
        Assert.assertNotNull(pso);
        pso.initialize();
    }

    @Test
    public void test_InitializeParticleWithHugeSize()
    {
        Setting setting = Setting.getInstance();
        setting.max_particle = 500;
        setting.max_epoch = 1;
        setting.bLoc_min = 0.600;
        setting.bLoc_max = 0.900;
        setting.bGlob_min = 0.100;
        setting.bGlob_max = 0.400;
        setting.bRand_min = 0.001;
        setting.bRand_max = 0.100;
        setting.total_core = 4;

        @NotNull final PSO pso = new PSO(this.dsLoader);
        Assert.assertNotNull(pso);
        pso.initialize();
    }

    @Test
    public void test_InitializeParticle_AndCheck()
    {
        Setting setting = Setting.getInstance();
        setting.max_particle = 1;
        setting.max_epoch = 1;
        setting.bLoc_min = 0.600;
        setting.bLoc_max = 0.900;
        setting.bGlob_min = 0.100;
        setting.bGlob_max = 0.400;
        setting.bRand_min = 0.001;
        setting.bRand_max = 0.100;
        setting.total_core = 4;

        @NotNull final PSO pso = new PSO(this.dsLoader);
        Assert.assertNotNull(pso);
        pso.initialize();

        final Random random       = new Random();
        final int    max_particle = Setting.getInstance().max_particle;
        int          c_particle   = random.nextInt(max_particle);

        Assert.assertTrue(TPSO_Particle_StabilityChecker.checkConflict(this.dsLoader, pso.getParticle(c_particle)));
        Assert.assertTrue(TPSO_Particle_StabilityChecker.checkAppearance(this.dsLoader, pso.getParticle(c_particle)));
    }

}
