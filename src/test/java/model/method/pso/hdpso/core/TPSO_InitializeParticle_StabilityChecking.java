package model.method.pso.hdpso.core;

import java.util.Random;
import model.database.component.DBSchool;
import model.database.loader.DBProblemLoader;
import model.dataset.loader.DatasetGenerator;
import model.method.pso.hdpso.component.Particle;
import model.method.pso.hdpso.component.Setting;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.core> created by : 
 * Name         : syafiq
 * Date / Time  : 24 November 2016, 4:25 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("UnnecessaryLocalVariable") public class TPSO_InitializeParticle_StabilityChecking
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

        Setting setting = Setting.getInstance();
        setting.setbGlobMin(0.100);
        setting.setbGlobMax(0.400);
        setting.setbLocMin(0.600);
        setting.setbLocMax(0.900);
        setting.setbRandMin(0.001);
        setting.setbRandMax(0.100);
        setting.setMaxParticle(10);
        setting.setMaxEpoch(1);
        setting.setTotalCore(4);
    }

    @Test
    public void test_stability_check_once()
    {
        @NotNull final PSO pso = new PSO(this.dsLoader);
        Assert.assertNotNull(pso);
        pso.initialize();

        final Random random       = new Random();
        final int    max_particle = Setting.getInstance().getMaxParticle();
        int          c_particle   = random.nextInt(max_particle);

        Assert.assertTrue(TPSO_Particle_StabilityChecker.checkConflict(this.dsLoader, pso.getParticle(c_particle).getData().getPositions()));
        Assert.assertTrue(TPSO_Particle_StabilityChecker.checkAppearance(this.dsLoader, pso.getParticle(c_particle).getData().getPositions()));

        try
        {
            Thread.sleep(800);
        }
        catch(InterruptedException ignored)
        {
        }
    }

    @Test
    public void test_stability_check_full_repeat()
    {
        Setting setting = Setting.getInstance();
        setting.setbGlobMin(0.100);
        setting.setbGlobMax(0.400);
        setting.setbLocMin(0.600);
        setting.setbLocMax(0.900);
        setting.setbRandMin(0.001);
        setting.setbRandMax(0.100);
        setting.setMaxParticle(10);
        setting.setMaxEpoch(1);
        setting.setTotalCore(4);

        @NotNull final PSO pso = new PSO(this.dsLoader);
        Assert.assertNotNull(pso);
        pso.initialize();


        final Random random       = new Random();
        final int    max_particle = Setting.getInstance().getMaxParticle();
        final int    SIZE         = 10000;

        for(int i = -1, is = SIZE; ++i < is; )
        {
            int c_particle = random.nextInt(max_particle);
            Assert.assertTrue(TPSO_Particle_StabilityChecker.checkConflict(this.dsLoader, pso.getParticle(c_particle).getData().getPositions()));
            Assert.assertTrue(TPSO_Particle_StabilityChecker.checkAppearance(this.dsLoader, pso.getParticle(c_particle).getData().getPositions()));
            for(final Particle particle : pso.getParticles())
            {
                pso.random(particle);
            }
        }
    }
}
