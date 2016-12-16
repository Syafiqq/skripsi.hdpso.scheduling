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
 * Date / Time  : 29 November 2016, 7:23 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("Duplicates") public class TPSO_ParticleMovement
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

    @Test public void testParticleMovement()
    {
        Setting setting = Setting.getInstance();
        setting.setbGlobMin(0.100);
        setting.setbGlobMax(0.400);
        setting.setbLocMin(0.600);
        setting.setbLocMax(0.900);
        setting.setbRandMin(0.001);
        setting.setbRandMax(0.100);
        setting.setMaxParticle(2);
        setting.setMaxEpoch(2);
        setting.setTotalCore(4);

        @NotNull final PSO pso = new PSO(this.dsLoader);
        Assert.assertNotNull(pso);
        pso.initialize();
        int a = 0;
        while(!pso.isConditionSatisfied())
        {
            System.out.println("Iteration : " + ++a);
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
                pso.calculate(particle);
            }
            pso.updateStoppingCondition();
        }
    }
}
