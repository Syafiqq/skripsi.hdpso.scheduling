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
 * Date / Time  : 27 November 2016, 11:25 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class TPSO_CalculateParticleFitness
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
    public void test_calculateParticle()
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
        pso.calculate(pso.getParticle(0));
        System.out.println(pso.getParticle(0).getData().getFitness());
    }


    @Test public void test_calculateManyParticle()
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
        for(@NotNull final Particle particle : pso.getParticles())
        {
            pso.calculate(particle);
            System.out.println(particle.getData().getFitness());
        }
    }

    @Test public void test_calculateManyParticlePBest()
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
        for(@NotNull final Particle particle : pso.getParticles())
        {
            pso.calculate(particle);
            System.out.println(particle.getPBest().getFitness());
        }
    }
}
