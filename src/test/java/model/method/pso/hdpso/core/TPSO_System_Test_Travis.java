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
 * Date / Time  : 16 December 2016, 9:02 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("Duplicates") public class TPSO_System_Test_Travis
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

    @Test public void testSystem()
    {
        Setting setting = Setting.getInstance();
        setting.bGlob_min = 0.4;
        setting.bGlob_max = 0.6;
        setting.bLoc_min = 0.7;
        setting.bLoc_max = 0.9;
        setting.bRand_min = 0.001;
        setting.bLoc_max = 0.01;
        setting.total_core = 3;
        setting.max_particle = 10;
        setting.max_epoch = 100;
        setting.total_core = 4;

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
                particle.calculateVelocity(pso.getGBest(), pso.getEpoch(), setting.max_epoch);
                particle.updateData();
                pso.repair(particle);
                pso.calculate(particle);
            }
            pso.updateStoppingCondition();
        }
        System.out.printf("GBest %f\n", pso.getFitness());
    }
}
