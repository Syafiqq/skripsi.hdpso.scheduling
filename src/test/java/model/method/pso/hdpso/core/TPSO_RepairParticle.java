package model.method.pso.hdpso.core;

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
 * Date / Time  : 01 December 2016, 9:53 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("Duplicates") public class TPSO_RepairParticle
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

    @Test public void testRepair()
    {
        Setting setting = Setting.getInstance();
        this.setting501000();

        @NotNull final PSO pso = new PSO(this.dsLoader);
        Assert.assertNotNull(pso);
        pso.initialize();
        while(!pso.isConditionSatisfied())
        {
            for(@NotNull final Particle particle : pso.getParticles())
            {
                particle.assignPBest();
            }
            pso.assignGBest();
            for(@NotNull final Particle particle : pso.getParticles())
            {
                particle.calculateVelocity(pso.getGBest(), pso.getEpoch(), setting.getMaxEpoch());
                particle.updateData();
                pso.repair(particle);
                Assert.assertTrue(TPSO_Particle_StabilityChecker.checkConflict(this.dsLoader, particle.getData().getPositions()));
                Assert.assertTrue(TPSO_Particle_StabilityChecker.checkAppearance(this.dsLoader, particle.getData().getPositions()));
                Assert.assertTrue(TPSO_Particle_StabilityChecker.checkAllowed(this.dsLoader, particle.getData().getPositions()));

                pso.calculate(particle);
            }
            pso.updateStoppingCondition();
        }
    }

    private void setting501000()
    {
        Setting setting = Setting.getInstance();
        setting.setbGlobMin(0.100);
        setting.setbGlobMax(0.400);
        setting.setbLocMin(0.600);
        setting.setbLocMax(0.900);
        setting.setbRandMin(0.001);
        setting.setbRandMax(0.100);
        setting.setMaxParticle(50);
        setting.setMaxEpoch(1000);
        setting.setTotalCore(4);
    }
}
