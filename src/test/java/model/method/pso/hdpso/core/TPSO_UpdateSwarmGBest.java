package model.method.pso.hdpso.core;

import java.util.Arrays;
import model.database.component.DBSchool;
import model.database.loader.DBProblemLoader;
import model.dataset.loader.DatasetGenerator;
import model.method.pso.hdpso.component.Particle;
import model.method.pso.hdpso.component.Position;
import model.method.pso.hdpso.component.Setting;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.core> created by : 
 * Name         : syafiq
 * Date / Time  : 29 November 2016, 11:35 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("Duplicates") public class TPSO_UpdateSwarmGBest
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
    public void test_UpdateGBestParticleEarly()
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
        final @NotNull Position[] gBest_position = pso.getGBest().getPositions();
        for(@NotNull final Particle particle : pso.getParticles())
        {
            final @NotNull Position[] current_position = particle.getData().getPositions();
            final @NotNull Position[] pBest_position   = particle.getPBest().getPositions();
            for(int i_c = -1, is_c = particle.getData().getPositionSize(); ++i_c < is_c; )
            {
                Assert.assertFalse(particle.getFitness() == particle.getPBest().getFitness());
                Assert.assertFalse(current_position[i_c] == pBest_position[i_c]);
                Assert.assertFalse(current_position[i_c].getPosition() == pBest_position[i_c].getPosition());
                Assert.assertFalse(Arrays.equals(current_position[i_c].getPosition(), pBest_position[i_c].getPosition()));
                Assert.assertTrue(Integer.compare(current_position[i_c].getPositionSize(), pBest_position[i_c].getPositionSize()) == 0);

                Assert.assertFalse(particle.getFitness() == pso.getFitness());
                Assert.assertFalse(current_position[i_c] == gBest_position[i_c]);
                Assert.assertFalse(current_position[i_c].getPosition() == gBest_position[i_c].getPosition());
                Assert.assertFalse(Arrays.equals(current_position[i_c].getPosition(), gBest_position[i_c].getPosition()));
                Assert.assertTrue(Integer.compare(current_position[i_c].getPositionSize(), gBest_position[i_c].getPositionSize()) == 0);

                Assert.assertTrue(pso.getFitness() == particle.getPBest().getFitness());
                Assert.assertFalse(gBest_position[i_c] == pBest_position[i_c]);
                Assert.assertFalse(gBest_position[i_c].getPosition() == pBest_position[i_c].getPosition());
                Assert.assertTrue(Arrays.equals(gBest_position[i_c].getPosition(), pBest_position[i_c].getPosition()));
                Assert.assertTrue(Integer.compare(gBest_position[i_c].getPositionSize(), pBest_position[i_c].getPositionSize()) == 0);
            }
            particle.assignPBest();
            for(int i_c = -1, is_c = particle.getData().getPositionSize(); ++i_c < is_c; )
            {
                Assert.assertTrue(particle.getFitness() == particle.getPBest().getFitness());
                Assert.assertFalse(current_position[i_c] == pBest_position[i_c]);
                Assert.assertFalse(current_position[i_c].getPosition() == pBest_position[i_c].getPosition());
                Assert.assertTrue(Arrays.equals(current_position[i_c].getPosition(), pBest_position[i_c].getPosition()));
                Assert.assertTrue(Integer.compare(current_position[i_c].getPositionSize(), pBest_position[i_c].getPositionSize()) == 0);

                Assert.assertFalse(particle.getFitness() == pso.getFitness());
                Assert.assertFalse(current_position[i_c] == gBest_position[i_c]);
                Assert.assertFalse(current_position[i_c].getPosition() == gBest_position[i_c].getPosition());
                Assert.assertFalse(Arrays.equals(current_position[i_c].getPosition(), gBest_position[i_c].getPosition()));
                Assert.assertTrue(Integer.compare(current_position[i_c].getPositionSize(), gBest_position[i_c].getPositionSize()) == 0);

                Assert.assertFalse(pso.getFitness() == particle.getPBest().getFitness());
                Assert.assertFalse(gBest_position[i_c] == pBest_position[i_c]);
                Assert.assertFalse(gBest_position[i_c].getPosition() == pBest_position[i_c].getPosition());
                Assert.assertFalse(Arrays.equals(gBest_position[i_c].getPosition(), pBest_position[i_c].getPosition()));
                Assert.assertTrue(Integer.compare(gBest_position[i_c].getPositionSize(), pBest_position[i_c].getPositionSize()) == 0);
            }
        }
        pso.assignGBest();
        final @NotNull Particle   particle         = pso.getParticle(0);
        final @NotNull Position[] current_position = particle.getData().getPositions();
        final @NotNull Position[] pBest_position   = particle.getPBest().getPositions();
        for(int i_c = -1, is_c = particle.getData().getPositionSize(); ++i_c < is_c; )
        {
            Assert.assertTrue(particle.getFitness() == particle.getPBest().getFitness());
            Assert.assertFalse(current_position[i_c] == pBest_position[i_c]);
            Assert.assertFalse(current_position[i_c].getPosition() == pBest_position[i_c].getPosition());
            Assert.assertTrue(Arrays.equals(current_position[i_c].getPosition(), pBest_position[i_c].getPosition()));
            Assert.assertTrue(Integer.compare(current_position[i_c].getPositionSize(), pBest_position[i_c].getPositionSize()) == 0);

            Assert.assertTrue(particle.getFitness() == pso.getFitness());
            Assert.assertFalse(current_position[i_c] == gBest_position[i_c]);
            Assert.assertFalse(current_position[i_c].getPosition() == gBest_position[i_c].getPosition());
            Assert.assertTrue(Arrays.equals(current_position[i_c].getPosition(), gBest_position[i_c].getPosition()));
            Assert.assertTrue(Integer.compare(current_position[i_c].getPositionSize(), gBest_position[i_c].getPositionSize()) == 0);

            Assert.assertTrue(pso.getFitness() == particle.getPBest().getFitness());
            Assert.assertFalse(gBest_position[i_c] == pBest_position[i_c]);
            Assert.assertFalse(gBest_position[i_c].getPosition() == pBest_position[i_c].getPosition());
            Assert.assertTrue(Arrays.equals(gBest_position[i_c].getPosition(), pBest_position[i_c].getPosition()));
            Assert.assertTrue(Integer.compare(gBest_position[i_c].getPositionSize(), pBest_position[i_c].getPositionSize()) == 0);
        }
    }

    @Test
    public void test_UpdateGBestLowerGBest()
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
        final @NotNull Position[] gBest_position    = pso.getGBest().getPositions();
        double                    max_fitness       = Double.MIN_VALUE;
        int                       max_fitness_index = 0;
        int                       c                 = -1;
        for(@NotNull final Particle particle : pso.getParticles())
        {
            ++c;
            if(max_fitness < particle.getFitness())
            {
                max_fitness = particle.getFitness();
                max_fitness_index = c;
            }
            final @NotNull Position[] current_position = particle.getData().getPositions();
            final @NotNull Position[] pBest_position   = particle.getPBest().getPositions();
            for(int i_c = -1, is_c = particle.getData().getPositionSize(); ++i_c < is_c; )
            {
                Assert.assertFalse(particle.getFitness() == particle.getPBest().getFitness());
                Assert.assertFalse(current_position[i_c] == pBest_position[i_c]);
                Assert.assertFalse(current_position[i_c].getPosition() == pBest_position[i_c].getPosition());
                Assert.assertFalse(Arrays.equals(current_position[i_c].getPosition(), pBest_position[i_c].getPosition()));
                Assert.assertTrue(Integer.compare(current_position[i_c].getPositionSize(), pBest_position[i_c].getPositionSize()) == 0);

                Assert.assertFalse(particle.getFitness() == pso.getFitness());
                Assert.assertFalse(current_position[i_c] == gBest_position[i_c]);
                Assert.assertFalse(current_position[i_c].getPosition() == gBest_position[i_c].getPosition());
                Assert.assertFalse(Arrays.equals(current_position[i_c].getPosition(), gBest_position[i_c].getPosition()));
                Assert.assertTrue(Integer.compare(current_position[i_c].getPositionSize(), gBest_position[i_c].getPositionSize()) == 0);

                Assert.assertTrue(pso.getFitness() == particle.getPBest().getFitness());
                Assert.assertFalse(gBest_position[i_c] == pBest_position[i_c]);
                Assert.assertFalse(gBest_position[i_c].getPosition() == pBest_position[i_c].getPosition());
                Assert.assertTrue(Arrays.equals(gBest_position[i_c].getPosition(), pBest_position[i_c].getPosition()));
                Assert.assertTrue(Integer.compare(gBest_position[i_c].getPositionSize(), pBest_position[i_c].getPositionSize()) == 0);
            }
            particle.assignPBest();
            for(int i_c = -1, is_c = particle.getData().getPositionSize(); ++i_c < is_c; )
            {
                Assert.assertTrue(particle.getFitness() == particle.getPBest().getFitness());
                Assert.assertFalse(current_position[i_c] == pBest_position[i_c]);
                Assert.assertFalse(current_position[i_c].getPosition() == pBest_position[i_c].getPosition());
                Assert.assertTrue(Arrays.equals(current_position[i_c].getPosition(), pBest_position[i_c].getPosition()));
                Assert.assertTrue(Integer.compare(current_position[i_c].getPositionSize(), pBest_position[i_c].getPositionSize()) == 0);

                Assert.assertFalse(particle.getFitness() == pso.getFitness());
                Assert.assertFalse(current_position[i_c] == gBest_position[i_c]);
                Assert.assertFalse(current_position[i_c].getPosition() == gBest_position[i_c].getPosition());
                Assert.assertFalse(Arrays.equals(current_position[i_c].getPosition(), gBest_position[i_c].getPosition()));
                Assert.assertTrue(Integer.compare(current_position[i_c].getPositionSize(), gBest_position[i_c].getPositionSize()) == 0);

                Assert.assertFalse(pso.getFitness() == particle.getPBest().getFitness());
                Assert.assertFalse(gBest_position[i_c] == pBest_position[i_c]);
                Assert.assertFalse(gBest_position[i_c].getPosition() == pBest_position[i_c].getPosition());
                Assert.assertFalse(Arrays.equals(gBest_position[i_c].getPosition(), pBest_position[i_c].getPosition()));
                Assert.assertTrue(Integer.compare(gBest_position[i_c].getPositionSize(), pBest_position[i_c].getPositionSize()) == 0);
            }
        }
        pso.getGBest().setFitness(max_fitness - 1);
        Assert.assertTrue((pso.getParticle(max_fitness_index).getFitness() - 1) == pso.getFitness());
        pso.assignGBest();
        Assert.assertTrue((pso.getParticle(0).getFitness()) == pso.getFitness());
        final @NotNull Particle   particle         = pso.getParticle(0);
        final @NotNull Position[] current_position = particle.getData().getPositions();
        final @NotNull Position[] pBest_position   = particle.getPBest().getPositions();
        for(int i_c = -1, is_c = particle.getData().getPositionSize(); ++i_c < is_c; )
        {
            Assert.assertTrue(particle.getFitness() == particle.getPBest().getFitness());
            Assert.assertFalse(current_position[i_c] == pBest_position[i_c]);
            Assert.assertFalse(current_position[i_c].getPosition() == pBest_position[i_c].getPosition());
            Assert.assertTrue(Arrays.equals(current_position[i_c].getPosition(), pBest_position[i_c].getPosition()));
            Assert.assertTrue(Integer.compare(current_position[i_c].getPositionSize(), pBest_position[i_c].getPositionSize()) == 0);

            Assert.assertTrue(particle.getFitness() == pso.getFitness());
            Assert.assertFalse(current_position[i_c] == gBest_position[i_c]);
            Assert.assertFalse(current_position[i_c].getPosition() == gBest_position[i_c].getPosition());
            Assert.assertTrue(Arrays.equals(current_position[i_c].getPosition(), gBest_position[i_c].getPosition()));
            Assert.assertTrue(Integer.compare(current_position[i_c].getPositionSize(), gBest_position[i_c].getPositionSize()) == 0);

            Assert.assertTrue(pso.getFitness() == particle.getPBest().getFitness());
            Assert.assertFalse(gBest_position[i_c] == pBest_position[i_c]);
            Assert.assertFalse(gBest_position[i_c].getPosition() == pBest_position[i_c].getPosition());
            Assert.assertTrue(Arrays.equals(gBest_position[i_c].getPosition(), pBest_position[i_c].getPosition()));
            Assert.assertTrue(Integer.compare(gBest_position[i_c].getPositionSize(), pBest_position[i_c].getPositionSize()) == 0);
        }
    }

    @Test
    public void test_UpdateGBestHigherGBest()
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
        final @NotNull Position[] gBest_position    = pso.getGBest().getPositions();
        double                    max_fitness       = Double.MIN_VALUE;
        int                       max_fitness_index = 0;
        int                       c                 = -1;
        for(@NotNull final Particle particle : pso.getParticles())
        {
            ++c;
            if(max_fitness < particle.getFitness())
            {
                max_fitness = particle.getFitness();
                max_fitness_index = c;
            }
            final @NotNull Position[] current_position = particle.getData().getPositions();
            final @NotNull Position[] pBest_position   = particle.getPBest().getPositions();
            for(int i_c = -1, is_c = particle.getData().getPositionSize(); ++i_c < is_c; )
            {
                Assert.assertFalse(particle.getFitness() == particle.getPBest().getFitness());
                Assert.assertFalse(current_position[i_c] == pBest_position[i_c]);
                Assert.assertFalse(current_position[i_c].getPosition() == pBest_position[i_c].getPosition());
                Assert.assertFalse(Arrays.equals(current_position[i_c].getPosition(), pBest_position[i_c].getPosition()));
                Assert.assertTrue(Integer.compare(current_position[i_c].getPositionSize(), pBest_position[i_c].getPositionSize()) == 0);

                Assert.assertFalse(particle.getFitness() == pso.getFitness());
                Assert.assertFalse(current_position[i_c] == gBest_position[i_c]);
                Assert.assertFalse(current_position[i_c].getPosition() == gBest_position[i_c].getPosition());
                Assert.assertFalse(Arrays.equals(current_position[i_c].getPosition(), gBest_position[i_c].getPosition()));
                Assert.assertTrue(Integer.compare(current_position[i_c].getPositionSize(), gBest_position[i_c].getPositionSize()) == 0);

                Assert.assertTrue(pso.getFitness() == particle.getPBest().getFitness());
                Assert.assertFalse(gBest_position[i_c] == pBest_position[i_c]);
                Assert.assertFalse(gBest_position[i_c].getPosition() == pBest_position[i_c].getPosition());
                Assert.assertTrue(Arrays.equals(gBest_position[i_c].getPosition(), pBest_position[i_c].getPosition()));
                Assert.assertTrue(Integer.compare(gBest_position[i_c].getPositionSize(), pBest_position[i_c].getPositionSize()) == 0);
            }
            particle.assignPBest();
            for(int i_c = -1, is_c = particle.getData().getPositionSize(); ++i_c < is_c; )
            {
                Assert.assertTrue(particle.getFitness() == particle.getPBest().getFitness());
                Assert.assertFalse(current_position[i_c] == pBest_position[i_c]);
                Assert.assertFalse(current_position[i_c].getPosition() == pBest_position[i_c].getPosition());
                Assert.assertTrue(Arrays.equals(current_position[i_c].getPosition(), pBest_position[i_c].getPosition()));
                Assert.assertTrue(Integer.compare(current_position[i_c].getPositionSize(), pBest_position[i_c].getPositionSize()) == 0);

                Assert.assertFalse(particle.getFitness() == pso.getFitness());
                Assert.assertFalse(current_position[i_c] == gBest_position[i_c]);
                Assert.assertFalse(current_position[i_c].getPosition() == gBest_position[i_c].getPosition());
                Assert.assertFalse(Arrays.equals(current_position[i_c].getPosition(), gBest_position[i_c].getPosition()));
                Assert.assertTrue(Integer.compare(current_position[i_c].getPositionSize(), gBest_position[i_c].getPositionSize()) == 0);

                Assert.assertFalse(pso.getFitness() == particle.getPBest().getFitness());
                Assert.assertFalse(gBest_position[i_c] == pBest_position[i_c]);
                Assert.assertFalse(gBest_position[i_c].getPosition() == pBest_position[i_c].getPosition());
                Assert.assertFalse(Arrays.equals(gBest_position[i_c].getPosition(), pBest_position[i_c].getPosition()));
                Assert.assertTrue(Integer.compare(gBest_position[i_c].getPositionSize(), pBest_position[i_c].getPositionSize()) == 0);
            }
        }
        pso.getGBest().setFitness(max_fitness + 1);
        Assert.assertTrue((pso.getParticle(max_fitness_index).getFitness() + 1) == pso.getFitness());
        pso.assignGBest();
        Assert.assertFalse((pso.getParticle(0).getFitness()) == pso.getFitness());
        Assert.assertTrue((pso.getParticle(0).getFitness() + 1) == pso.getFitness());
        final @NotNull Particle   particle         = pso.getParticle(0);
        final @NotNull Position[] current_position = particle.getData().getPositions();
        final @NotNull Position[] pBest_position   = particle.getPBest().getPositions();
        for(int i_c = -1, is_c = particle.getData().getPositionSize(); ++i_c < is_c; )
        {
            Assert.assertTrue(particle.getFitness() == particle.getPBest().getFitness());
            Assert.assertFalse(current_position[i_c] == pBest_position[i_c]);
            Assert.assertFalse(current_position[i_c].getPosition() == pBest_position[i_c].getPosition());
            Assert.assertTrue(Arrays.equals(current_position[i_c].getPosition(), pBest_position[i_c].getPosition()));
            Assert.assertTrue(Integer.compare(current_position[i_c].getPositionSize(), pBest_position[i_c].getPositionSize()) == 0);

            Assert.assertFalse(particle.getFitness() == pso.getFitness());
            Assert.assertFalse(current_position[i_c] == gBest_position[i_c]);
            Assert.assertFalse(current_position[i_c].getPosition() == gBest_position[i_c].getPosition());
            Assert.assertFalse(Arrays.equals(current_position[i_c].getPosition(), gBest_position[i_c].getPosition()));
            Assert.assertTrue(Integer.compare(current_position[i_c].getPositionSize(), gBest_position[i_c].getPositionSize()) == 0);

            Assert.assertFalse(pso.getFitness() == particle.getPBest().getFitness());
            Assert.assertFalse(gBest_position[i_c] == pBest_position[i_c]);
            Assert.assertFalse(gBest_position[i_c].getPosition() == pBest_position[i_c].getPosition());
            Assert.assertFalse(Arrays.equals(gBest_position[i_c].getPosition(), pBest_position[i_c].getPosition()));
            Assert.assertTrue(Integer.compare(gBest_position[i_c].getPositionSize(), pBest_position[i_c].getPositionSize()) == 0);
        }
    }
}
