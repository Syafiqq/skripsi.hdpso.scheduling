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
 * Date / Time  : 29 November 2016, 11:17 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("Duplicates") public class TPSO_UpdateParticlePBest
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
    public void test_UpdatePBestEarly()
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
        for(@NotNull final Particle particle : pso.getParticles())
        {
            final @NotNull Position[] current_position = particle.getData().getPositions();
            final @NotNull Position[] pBest_position   = particle.getPBest().getPositions();
            for(int i_c = -1, is_c = particle.getData().getPositionSize(); ++i_c < is_c; )
            {
                Assert.assertFalse(particle.getData().getFitness() == particle.getPBest().getFitness());
                Assert.assertFalse(current_position[i_c] == pBest_position[i_c]);
                Assert.assertFalse(current_position[i_c].getPosition() == pBest_position[i_c].getPosition());
                Assert.assertFalse(Arrays.equals(current_position[i_c].getPosition(), pBest_position[i_c].getPosition()));
                Assert.assertTrue(Integer.compare(current_position[i_c].getPositionSize(), pBest_position[i_c].getPositionSize()) == 0);
            }
            particle.assignPBest();
            for(int i_c = -1, is_c = particle.getData().getPositionSize(); ++i_c < is_c; )
            {
                Assert.assertTrue(particle.getData().getFitness() == particle.getPBest().getFitness());
                Assert.assertFalse(current_position[i_c] == pBest_position[i_c]);
                Assert.assertFalse(current_position[i_c].getPosition() == pBest_position[i_c].getPosition());
                Assert.assertTrue(Arrays.equals(current_position[i_c].getPosition(), pBest_position[i_c].getPosition()));
                Assert.assertTrue(Integer.compare(current_position[i_c].getPositionSize(), pBest_position[i_c].getPositionSize()) == 0);
            }
        }
    }

    @Test
    public void test_UpdatePBestLowerPBest()
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
        for(@NotNull final Particle particle : pso.getParticles())
        {
            final @NotNull Position[] current_position = particle.getData().getPositions();
            final @NotNull Position[] pBest_position   = particle.getPBest().getPositions();
            for(int i_c = -1, is_c = particle.getData().getPositionSize(); ++i_c < is_c; )
            {
                Assert.assertFalse(particle.getData().getFitness() == particle.getPBest().getFitness());
                Assert.assertFalse(current_position[i_c] == pBest_position[i_c]);
                Assert.assertFalse(current_position[i_c].getPosition() == pBest_position[i_c].getPosition());
                Assert.assertFalse(Arrays.equals(current_position[i_c].getPosition(), pBest_position[i_c].getPosition()));
                Assert.assertTrue(Integer.compare(current_position[i_c].getPositionSize(), pBest_position[i_c].getPositionSize()) == 0);
            }
            particle.getPBest().setFitness(particle.getFitness() - 1);
            Assert.assertTrue((particle.getFitness() - 1) == particle.getPBest().getFitness());
            particle.assignPBest();
            Assert.assertTrue((particle.getFitness()) == particle.getPBest().getFitness());
            for(int i_c = -1, is_c = particle.getData().getPositionSize(); ++i_c < is_c; )
            {
                Assert.assertTrue(particle.getData().getFitness() == particle.getPBest().getFitness());
                Assert.assertFalse(current_position[i_c] == pBest_position[i_c]);
                Assert.assertFalse(current_position[i_c].getPosition() == pBest_position[i_c].getPosition());
                Assert.assertTrue(Arrays.equals(current_position[i_c].getPosition(), pBest_position[i_c].getPosition()));
                Assert.assertTrue(Integer.compare(current_position[i_c].getPositionSize(), pBest_position[i_c].getPositionSize()) == 0);
            }
        }
    }

    @Test
    public void test_UpdatePBestHigherPBest()
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
        for(@NotNull final Particle particle : pso.getParticles())
        {
            final @NotNull Position[] current_position = particle.getData().getPositions();
            final @NotNull Position[] pBest_position   = particle.getPBest().getPositions();
            for(int i_c = -1, is_c = particle.getData().getPositionSize(); ++i_c < is_c; )
            {
                Assert.assertFalse(particle.getData().getFitness() == particle.getPBest().getFitness());
                Assert.assertFalse(current_position[i_c] == pBest_position[i_c]);
                Assert.assertFalse(current_position[i_c].getPosition() == pBest_position[i_c].getPosition());
                Assert.assertFalse(Arrays.equals(current_position[i_c].getPosition(), pBest_position[i_c].getPosition()));
                Assert.assertTrue(Integer.compare(current_position[i_c].getPositionSize(), pBest_position[i_c].getPositionSize()) == 0);
            }
            particle.getPBest().setFitness(particle.getFitness() + 1);
            Assert.assertTrue((particle.getFitness() + 1) == particle.getPBest().getFitness());
            particle.assignPBest();
            Assert.assertFalse((particle.getFitness()) == particle.getPBest().getFitness());
            Assert.assertTrue((particle.getFitness() + 1) == particle.getPBest().getFitness());
            for(int i_c = -1, is_c = particle.getData().getPositionSize(); ++i_c < is_c; )
            {
                Assert.assertFalse(particle.getData().getFitness() == particle.getPBest().getFitness());
                Assert.assertFalse(current_position[i_c] == pBest_position[i_c]);
                Assert.assertFalse(current_position[i_c].getPosition() == pBest_position[i_c].getPosition());
                Assert.assertFalse(Arrays.equals(current_position[i_c].getPosition(), pBest_position[i_c].getPosition()));
                Assert.assertTrue(Integer.compare(current_position[i_c].getPositionSize(), pBest_position[i_c].getPositionSize()) == 0);
            }
        }
    }
}
