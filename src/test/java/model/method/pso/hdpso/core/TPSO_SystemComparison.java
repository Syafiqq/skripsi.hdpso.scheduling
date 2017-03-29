package model.method.pso.hdpso.core;

import model.database.component.DBTimetable;
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
 * Date / Time  : 20 December 2016, 8:51 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("Duplicates") public class TPSO_SystemComparison
{
    private DatasetGenerator dsLoader;

    @SuppressWarnings("Duplicates") @Before public void initialize()
    {
        DBTimetable school = new DBTimetable(1, "Program Teknologi Informasi dan Ilmu Komputer Universitas Brawijaya", "PTIIK UB", "Jl. Veteran No.8, Ketawanggede, Kec. Lowokwaru, Kota Malang, Jawa Timur", "2013 - 2014", 0, 17, 5);
        Assert.assertNotNull(school);

        DBProblemLoader dbLoader = new DBProblemLoader(school);
        Assert.assertNotNull(dbLoader);

        dbLoader.loadData();

        this.dsLoader = new DatasetGenerator(dbLoader);
        Assert.assertNotNull(dsLoader);
    }

    @Test public void testPurePSO_WO_Multi()
    {
        Setting setting = Setting.getInstance();
        setting.setbGlobMin(0.400);
        setting.setbGlobMax(0.600);
        setting.setbLocMin(0.700);
        setting.setbLocMax(0.900);
        setting.setbRandMin(0.001);
        setting.setbRandMax(0.010);
        setting.setMaxParticle(10);
        setting.setMaxEpoch(10000);
        setting.setTotalCore(Runtime.getRuntime().availableProcessors());
        setting.setCalculator(Setting.HDPSO_WTV);
        setting.setMultiProcess(false);

        @NotNull final PSO pso = new PSO(this.dsLoader);
        Assert.assertNotNull(pso);
        pso.initialize();
        while(!pso.isConditionSatisfied())
        {
            pso.updatePBest();
            pso.assignGBest();
            pso.evaluateParticle();
            pso.updateStoppingCondition();
        }
        System.out.printf("GBest %f\n", pso.getFitness());
    }

    @Test public void testPurePSO_W_Multi()
    {
        Setting setting = Setting.getInstance();
        setting.setbGlobMin(0.400);
        setting.setbGlobMax(0.600);
        setting.setbLocMin(0.700);
        setting.setbLocMax(0.900);
        setting.setbRandMin(0.001);
        setting.setbRandMax(0.010);
        setting.setMaxParticle(10);
        setting.setMaxEpoch(10000);
        setting.setTotalCore(Runtime.getRuntime().availableProcessors());
        setting.setCalculator(Setting.HDPSO_WTV);
        setting.setMultiProcess(true);

        @NotNull final PSO pso = new PSO(this.dsLoader);
        Assert.assertNotNull(pso);
        pso.initialize();
        while(!pso.isConditionSatisfied())
        {
            pso.updatePBest();
            pso.assignGBest();
            pso.evaluateParticle();
            pso.updateStoppingCondition();
        }
        System.out.printf("GBest %f\n", pso.getFitness());
    }

    @Test public void testPTVPSO_WO_Multi()
    {
        Setting setting = Setting.getInstance();
        setting.setbGlobMin(0.400);
        setting.setbGlobMax(0.600);
        setting.setbLocMin(0.700);
        setting.setbLocMax(0.900);
        setting.setbRandMin(0.001);
        setting.setbRandMax(0.010);
        setting.setMaxParticle(10);
        setting.setMaxEpoch(10000);
        setting.setTotalCore(Runtime.getRuntime().availableProcessors());
        setting.setCalculator(Setting.HDPSO);
        setting.setMultiProcess(false);

        @NotNull final PSO pso = new PSO(this.dsLoader);
        Assert.assertNotNull(pso);
        pso.initialize();
        while(!pso.isConditionSatisfied())
        {
            pso.updatePBest();
            pso.assignGBest();
            pso.evaluateParticle();
            pso.updateStoppingCondition();
        }
        System.out.printf("GBest %f\n", pso.getFitness());
    }

    @Test public void testPTVPSO_W_Multi()
    {
        Setting setting = Setting.getInstance();
        setting.setbGlobMin(0.400);
        setting.setbGlobMax(0.600);
        setting.setbLocMin(0.700);
        setting.setbLocMax(0.900);
        setting.setbRandMin(0.001);
        setting.setbRandMax(0.010);
        setting.setMaxParticle(10);
        setting.setMaxEpoch(10000);
        setting.setTotalCore(Runtime.getRuntime().availableProcessors());
        setting.setCalculator(Setting.HDPSO);
        setting.setMultiProcess(true);

        @NotNull final PSO pso = new PSO(this.dsLoader);
        Assert.assertNotNull(pso);
        pso.initialize();
        while(!pso.isConditionSatisfied())
        {
            pso.updatePBest();
            pso.assignGBest();
            pso.evaluateParticle();
            pso.updateStoppingCondition();
        }
        System.out.printf("GBest %f\n", pso.getFitness());
    }

    @Test public void testPurePTVPSO_WO_Multi()
    {
        Setting setting = Setting.getInstance();
        setting.setbGlobMin(0.400);
        setting.setbGlobMax(0.600);
        setting.setbLocMin(0.700);
        setting.setbLocMax(0.900);
        setting.setbRandMin(0.001);
        setting.setbRandMax(0.010);
        setting.setMaxParticle(10);
        setting.setMaxEpoch(10000);
        setting.setTimeVariantWeight(1);
        setting.setTotalCore(Runtime.getRuntime().availableProcessors());
        setting.setCalculator(Setting.HDPSO_WR);
        setting.setMultiProcess(false);

        @NotNull final PSO pso = new PSO(this.dsLoader);
        Assert.assertNotNull(pso);
        pso.initialize();
        while(!pso.isConditionSatisfied())
        {
            pso.updatePBest();
            pso.assignGBest();
            pso.evaluateParticle();
            pso.updateStoppingCondition();
        }
        System.out.printf("GBest %f\n", pso.getFitness());
    }

    @Test public void testPurePTVPSO_W_Multi()
    {
        Setting setting = Setting.getInstance();
        setting.setbGlobMin(0.400);
        setting.setbGlobMax(0.600);
        setting.setbLocMin(0.700);
        setting.setbLocMax(0.900);
        setting.setbRandMin(0.001);
        setting.setbRandMax(0.010);
        setting.setMaxParticle(10);
        setting.setMaxEpoch(10000);
        setting.setTimeVariantWeight(1);
        setting.setTotalCore(Runtime.getRuntime().availableProcessors());
        setting.setCalculator(Setting.HDPSO_WR);
        setting.setMultiProcess(true);

        @NotNull final PSO pso = new PSO(this.dsLoader);
        Assert.assertNotNull(pso);
        pso.initialize();
        while(!pso.isConditionSatisfied())
        {
            pso.updatePBest();
            pso.assignGBest();
            pso.evaluateParticle();
            pso.updateStoppingCondition();
        }
        System.out.printf("GBest %f\n", pso.getFitness());
    }

    @Test public void testPurePTVPSO_WO_Multi_05()
    {
        Setting setting = Setting.getInstance();
        setting.setbGlobMin(0.400);
        setting.setbGlobMax(0.600);
        setting.setbLocMin(0.700);
        setting.setbLocMax(0.900);
        setting.setbRandMin(0.001);
        setting.setbRandMax(0.010);
        setting.setMaxParticle(10);
        setting.setMaxEpoch(10000);
        setting.setTimeVariantWeight(0.5);
        setting.setTotalCore(Runtime.getRuntime().availableProcessors());
        setting.setCalculator(Setting.HDPSO_WR);
        setting.setMultiProcess(false);

        @NotNull final PSO pso = new PSO(this.dsLoader);
        Assert.assertNotNull(pso);
        pso.initialize();
        while(!pso.isConditionSatisfied())
        {
            pso.updatePBest();
            pso.assignGBest();
            pso.evaluateParticle();
            pso.updateStoppingCondition();
        }
        System.out.printf("GBest %f\n", pso.getFitness());
    }

    @Test public void testPurePTVPSO_W_Multi05()
    {
        Setting setting = Setting.getInstance();
        setting.setbGlobMin(0.400);
        setting.setbGlobMax(0.600);
        setting.setbLocMin(0.700);
        setting.setbLocMax(0.900);
        setting.setbRandMin(0.001);
        setting.setbRandMax(0.010);
        setting.setMaxParticle(10);
        setting.setMaxEpoch(10000);
        setting.setTimeVariantWeight(0.5);
        setting.setTotalCore(Runtime.getRuntime().availableProcessors());
        setting.setCalculator(Setting.HDPSO_WR);
        setting.setMultiProcess(true);

        @NotNull final PSO pso = new PSO(this.dsLoader);
        Assert.assertNotNull(pso);
        pso.initialize();
        while(!pso.isConditionSatisfied())
        {
            pso.updatePBest();
            pso.assignGBest();
            pso.evaluateParticle();
            pso.updateStoppingCondition();
        }
        System.out.printf("GBest %f\n", pso.getFitness());
    }
}
