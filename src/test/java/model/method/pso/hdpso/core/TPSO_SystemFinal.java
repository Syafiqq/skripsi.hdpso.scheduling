package model.method.pso.hdpso.core;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 18 January 2017, 5:47 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import model.database.component.DBSchool;
import model.database.loader.DBProblemLoader;
import model.dataset.loader.DatasetGenerator;
import model.method.pso.hdpso.component.Setting;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("Duplicates")
public class TPSO_SystemFinal {

    private DatasetGenerator dsLoader;

    @SuppressWarnings("Duplicates")
    @Before
    public void initialize() {
        DBSchool school = new DBSchool(1, "Program Teknologi Informasi dan Ilmu Komputer Universitas Brawijaya", "PTIIK UB", "Jl. Veteran No.8, Ketawanggede, Kec. Lowokwaru, Kota Malang, Jawa Timur", "2013 - 2014", 0, 17, 5);
        Assert.assertNotNull(school);

        DBProblemLoader dbLoader = new DBProblemLoader(school);
        Assert.assertNotNull(dbLoader);

        dbLoader.loadData();

        this.dsLoader = new DatasetGenerator(dbLoader);
        Assert.assertNotNull(dsLoader);
    }

    @Test
    public void test() {
        Setting setting = Setting.getInstance();
        setting.setbGlobMin(0.6);
        setting.setbGlobMax(1);
        setting.setbLocMin(0.6);
        setting.setbLocMax(1);
        setting.setbRandMin(0.000);
        setting.setbRandMax(0.002);
        setting.setMaxParticle(2);
        setting.setMaxEpoch(10000000);
        setting.setTimeVariantWeight(1);
        setting.setTotalCore(Runtime.getRuntime().availableProcessors());
        setting.setCalculator(Setting.HDPSO_WR);
        setting.setMultiProcess(false);

        final long c1 = System.currentTimeMillis();
        @NotNull final PSO pso = new PSO(this.dsLoader);
        Assert.assertNotNull(pso);
        pso.initialize();
        while (!pso.isConditionSatisfied()) {
            pso.updatePBest();
            pso.assignGBest();
            pso.evaluateParticle();
            pso.updateStoppingCondition();
        }
        final long c2 = System.currentTimeMillis();
        System.out.printf("\t%f\t%d,\n", pso.getFitness(), c2 - c1);
    }
}
