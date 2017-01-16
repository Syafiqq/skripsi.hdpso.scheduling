package model.method.pso.hdpso.core;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 15 January 2017, 12:46 AM.
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
public class TPSO_SystemTest_ForReporting {
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
    public void testParameter() {

        for (double brand_max = -0.002, brand_max_size = 0.010, brand_min = 0.0; (brand_max += 0.002) <= brand_max_size; brand_min = (brand_max + 0.002 - 0.002 < 0.0 ? 0.0 : brand_max + 0.002 - 0.002)) {
            for (double bloc_max = -0.2, bloc_max_size = 1.0, bloc_min = 0.0; (bloc_max += 0.2) <= bloc_max_size; bloc_min = (bloc_max + 0.2 - 0.4 < 0.0 ? 0.0 : bloc_max + 0.2 - 0.4)) {
                for (double bglob_max = -0.2, bglob_max_size = 1.0, bglob_min = 0.0; (bglob_max += 0.2) <= bglob_max_size; bglob_min = (bglob_max + 0.2 - 0.4 < 0.0 ? 0.0 : bglob_max + 0.2 - 0.4)) {
                    for (int c = -1, cs = 2; ++c < cs; ) {
                        //System.out.printf("[%.3f\t%.3f]\t[%.3f\t%.3f]\t[%.3f\t%.3f]\n", brand_min, brand_max, bloc_min, bloc_max, bglob_min, bglob_max);
                        System.out.printf("{%.3f, %.3f, %.3f, %.3f, %.3f, %.3f},\n", brand_min, brand_max, bloc_min, bloc_max, bglob_min, bglob_max);
                    }
                }
            }
        }
    }

    @Test
    public void testParameterWithReport() {
        @NotNull final double[][] params = new double[][]
                {
                        {0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
                        {0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
                        {0.000, 0.000, 0.000, 0.000, 0.000, 0.200},
                        {0.000, 0.000, 0.000, 0.000, 0.000, 0.200},
                        {0.000, 0.000, 0.000, 0.000, 0.000, 0.400},
                        {0.000, 0.000, 0.000, 0.000, 0.000, 0.400},
                        {0.000, 0.000, 0.000, 0.000, 0.200, 0.600},
                        {0.000, 0.000, 0.000, 0.000, 0.200, 0.600},
                        {0.000, 0.000, 0.000, 0.000, 0.400, 0.800},
                        {0.000, 0.000, 0.000, 0.000, 0.400, 0.800},
                        {0.000, 0.000, 0.000, 0.000, 0.600, 1.000},
                        {0.000, 0.000, 0.000, 0.000, 0.600, 1.000},
                        {0.000, 0.000, 0.000, 0.200, 0.000, 0.000},
                        {0.000, 0.000, 0.000, 0.200, 0.000, 0.000},
                        {0.000, 0.000, 0.000, 0.200, 0.000, 0.200},
                        {0.000, 0.000, 0.000, 0.200, 0.000, 0.200},
                        {0.000, 0.000, 0.000, 0.200, 0.000, 0.400},
                        {0.000, 0.000, 0.000, 0.200, 0.000, 0.400},
                        {0.000, 0.000, 0.000, 0.200, 0.200, 0.600},
                        {0.000, 0.000, 0.000, 0.200, 0.200, 0.600},
                        {0.000, 0.000, 0.000, 0.200, 0.400, 0.800},
                        {0.000, 0.000, 0.000, 0.200, 0.400, 0.800},
                        {0.000, 0.000, 0.000, 0.200, 0.600, 1.000},
                        {0.000, 0.000, 0.000, 0.200, 0.600, 1.000},
                        {0.000, 0.000, 0.000, 0.400, 0.000, 0.000},
                        {0.000, 0.000, 0.000, 0.400, 0.000, 0.000},
                        {0.000, 0.000, 0.000, 0.400, 0.000, 0.200},
                        {0.000, 0.000, 0.000, 0.400, 0.000, 0.200},
                        {0.000, 0.000, 0.000, 0.400, 0.000, 0.400},
                        {0.000, 0.000, 0.000, 0.400, 0.000, 0.400},
                        {0.000, 0.000, 0.000, 0.400, 0.200, 0.600},
                        {0.000, 0.000, 0.000, 0.400, 0.200, 0.600},
                        {0.000, 0.000, 0.000, 0.400, 0.400, 0.800},
                        {0.000, 0.000, 0.000, 0.400, 0.400, 0.800},
                        {0.000, 0.000, 0.000, 0.400, 0.600, 1.000},
                        {0.000, 0.000, 0.000, 0.400, 0.600, 1.000},
                        {0.000, 0.000, 0.200, 0.600, 0.000, 0.000},
                        {0.000, 0.000, 0.200, 0.600, 0.000, 0.000},
                        {0.000, 0.000, 0.200, 0.600, 0.000, 0.200},
                        {0.000, 0.000, 0.200, 0.600, 0.000, 0.200},
                        {0.000, 0.000, 0.200, 0.600, 0.000, 0.400},
                        {0.000, 0.000, 0.200, 0.600, 0.000, 0.400},
                        {0.000, 0.000, 0.200, 0.600, 0.200, 0.600},
                        {0.000, 0.000, 0.200, 0.600, 0.200, 0.600},
                        {0.000, 0.000, 0.200, 0.600, 0.400, 0.800},
                        {0.000, 0.000, 0.200, 0.600, 0.400, 0.800},
                        {0.000, 0.000, 0.200, 0.600, 0.600, 1.000},
                        {0.000, 0.000, 0.200, 0.600, 0.600, 1.000},
                        {0.000, 0.000, 0.400, 0.800, 0.000, 0.000},
                        {0.000, 0.000, 0.400, 0.800, 0.000, 0.000},
                        {0.000, 0.000, 0.400, 0.800, 0.000, 0.200},
                        {0.000, 0.000, 0.400, 0.800, 0.000, 0.200},
                        {0.000, 0.000, 0.400, 0.800, 0.000, 0.400},
                        {0.000, 0.000, 0.400, 0.800, 0.000, 0.400},
                        {0.000, 0.000, 0.400, 0.800, 0.200, 0.600},
                        {0.000, 0.000, 0.400, 0.800, 0.200, 0.600},
                        {0.000, 0.000, 0.400, 0.800, 0.400, 0.800},
                        {0.000, 0.000, 0.400, 0.800, 0.400, 0.800},
                        {0.000, 0.000, 0.400, 0.800, 0.600, 1.000},
                        {0.000, 0.000, 0.400, 0.800, 0.600, 1.000},
                        {0.000, 0.000, 0.600, 1.000, 0.000, 0.000},
                        {0.000, 0.000, 0.600, 1.000, 0.000, 0.000},
                        {0.000, 0.000, 0.600, 1.000, 0.000, 0.200},
                        {0.000, 0.000, 0.600, 1.000, 0.000, 0.200},
                        {0.000, 0.000, 0.600, 1.000, 0.000, 0.400},
                        {0.000, 0.000, 0.600, 1.000, 0.000, 0.400},
                        {0.000, 0.000, 0.600, 1.000, 0.200, 0.600},
                        {0.000, 0.000, 0.600, 1.000, 0.200, 0.600},
                        {0.000, 0.000, 0.600, 1.000, 0.400, 0.800},
                        {0.000, 0.000, 0.600, 1.000, 0.400, 0.800},
                        {0.000, 0.000, 0.600, 1.000, 0.600, 1.000},
                        {0.000, 0.000, 0.600, 1.000, 0.600, 1.000},
                        {0.000, 0.002, 0.000, 0.000, 0.000, 0.000},
                        {0.000, 0.002, 0.000, 0.000, 0.000, 0.000},
                        {0.000, 0.002, 0.000, 0.000, 0.000, 0.200},
                        {0.000, 0.002, 0.000, 0.000, 0.000, 0.200},
                        {0.000, 0.002, 0.000, 0.000, 0.000, 0.400},
                        {0.000, 0.002, 0.000, 0.000, 0.000, 0.400},
                        {0.000, 0.002, 0.000, 0.000, 0.200, 0.600},
                        {0.000, 0.002, 0.000, 0.000, 0.200, 0.600},
                        {0.000, 0.002, 0.000, 0.000, 0.400, 0.800},
                        {0.000, 0.002, 0.000, 0.000, 0.400, 0.800},
                        {0.000, 0.002, 0.000, 0.000, 0.600, 1.000},
                        {0.000, 0.002, 0.000, 0.000, 0.600, 1.000},
                        {0.000, 0.002, 0.000, 0.200, 0.000, 0.000},
                        {0.000, 0.002, 0.000, 0.200, 0.000, 0.000},
                        {0.000, 0.002, 0.000, 0.200, 0.000, 0.200},
                        {0.000, 0.002, 0.000, 0.200, 0.000, 0.200},
                        {0.000, 0.002, 0.000, 0.200, 0.000, 0.400},
                        {0.000, 0.002, 0.000, 0.200, 0.000, 0.400},
                        {0.000, 0.002, 0.000, 0.200, 0.200, 0.600},
                        {0.000, 0.002, 0.000, 0.200, 0.200, 0.600},
                        {0.000, 0.002, 0.000, 0.200, 0.400, 0.800},
                        {0.000, 0.002, 0.000, 0.200, 0.400, 0.800},
                        {0.000, 0.002, 0.000, 0.200, 0.600, 1.000},
                        {0.000, 0.002, 0.000, 0.200, 0.600, 1.000},
                        {0.000, 0.002, 0.000, 0.400, 0.000, 0.000},
                        {0.000, 0.002, 0.000, 0.400, 0.000, 0.000},
                        {0.000, 0.002, 0.000, 0.400, 0.000, 0.200},
                        {0.000, 0.002, 0.000, 0.400, 0.000, 0.200},
                        {0.000, 0.002, 0.000, 0.400, 0.000, 0.400},
                        {0.000, 0.002, 0.000, 0.400, 0.000, 0.400},
                        {0.000, 0.002, 0.000, 0.400, 0.200, 0.600},
                        {0.000, 0.002, 0.000, 0.400, 0.200, 0.600},
                        {0.000, 0.002, 0.000, 0.400, 0.400, 0.800},
                        {0.000, 0.002, 0.000, 0.400, 0.400, 0.800},
                        {0.000, 0.002, 0.000, 0.400, 0.600, 1.000},
                        {0.000, 0.002, 0.000, 0.400, 0.600, 1.000},
                        {0.000, 0.002, 0.200, 0.600, 0.000, 0.000},
                        {0.000, 0.002, 0.200, 0.600, 0.000, 0.000},
                        {0.000, 0.002, 0.200, 0.600, 0.000, 0.200},
                        {0.000, 0.002, 0.200, 0.600, 0.000, 0.200},
                        {0.000, 0.002, 0.200, 0.600, 0.000, 0.400},
                        {0.000, 0.002, 0.200, 0.600, 0.000, 0.400},
                        {0.000, 0.002, 0.200, 0.600, 0.200, 0.600},
                        {0.000, 0.002, 0.200, 0.600, 0.200, 0.600},
                        {0.000, 0.002, 0.200, 0.600, 0.400, 0.800},
                        {0.000, 0.002, 0.200, 0.600, 0.400, 0.800},
                        {0.000, 0.002, 0.200, 0.600, 0.600, 1.000},
                        {0.000, 0.002, 0.200, 0.600, 0.600, 1.000},
                        {0.000, 0.002, 0.400, 0.800, 0.000, 0.000},
                        {0.000, 0.002, 0.400, 0.800, 0.000, 0.000},
                        {0.000, 0.002, 0.400, 0.800, 0.000, 0.200},
                        {0.000, 0.002, 0.400, 0.800, 0.000, 0.200},
                        {0.000, 0.002, 0.400, 0.800, 0.000, 0.400},
                        {0.000, 0.002, 0.400, 0.800, 0.000, 0.400},
                        {0.000, 0.002, 0.400, 0.800, 0.200, 0.600},
                        {0.000, 0.002, 0.400, 0.800, 0.200, 0.600},
                        {0.000, 0.002, 0.400, 0.800, 0.400, 0.800},
                        {0.000, 0.002, 0.400, 0.800, 0.400, 0.800},
                        {0.000, 0.002, 0.400, 0.800, 0.600, 1.000},
                        {0.000, 0.002, 0.400, 0.800, 0.600, 1.000},
                        {0.000, 0.002, 0.600, 1.000, 0.000, 0.000},
                        {0.000, 0.002, 0.600, 1.000, 0.000, 0.000},
                        {0.000, 0.002, 0.600, 1.000, 0.000, 0.200},
                        {0.000, 0.002, 0.600, 1.000, 0.000, 0.200},
                        {0.000, 0.002, 0.600, 1.000, 0.000, 0.400},
                        {0.000, 0.002, 0.600, 1.000, 0.000, 0.400},
                        {0.000, 0.002, 0.600, 1.000, 0.200, 0.600},
                        {0.000, 0.002, 0.600, 1.000, 0.200, 0.600},
                        {0.000, 0.002, 0.600, 1.000, 0.400, 0.800},
                        {0.000, 0.002, 0.600, 1.000, 0.400, 0.800},
                        {0.000, 0.002, 0.600, 1.000, 0.600, 1.000},
                        {0.000, 0.002, 0.600, 1.000, 0.600, 1.000},
                        {0.002, 0.004, 0.000, 0.000, 0.000, 0.000},
                        {0.002, 0.004, 0.000, 0.000, 0.000, 0.000},
                        {0.002, 0.004, 0.000, 0.000, 0.000, 0.200},
                        {0.002, 0.004, 0.000, 0.000, 0.000, 0.200},
                        {0.002, 0.004, 0.000, 0.000, 0.000, 0.400},
                        {0.002, 0.004, 0.000, 0.000, 0.000, 0.400},
                        {0.002, 0.004, 0.000, 0.000, 0.200, 0.600},
                        {0.002, 0.004, 0.000, 0.000, 0.200, 0.600},
                        {0.002, 0.004, 0.000, 0.000, 0.400, 0.800},
                        {0.002, 0.004, 0.000, 0.000, 0.400, 0.800},
                        {0.002, 0.004, 0.000, 0.000, 0.600, 1.000},
                        {0.002, 0.004, 0.000, 0.000, 0.600, 1.000},
                        {0.002, 0.004, 0.000, 0.200, 0.000, 0.000},
                        {0.002, 0.004, 0.000, 0.200, 0.000, 0.000},
                        {0.002, 0.004, 0.000, 0.200, 0.000, 0.200},
                        {0.002, 0.004, 0.000, 0.200, 0.000, 0.200},
                        {0.002, 0.004, 0.000, 0.200, 0.000, 0.400},
                        {0.002, 0.004, 0.000, 0.200, 0.000, 0.400},
                        {0.002, 0.004, 0.000, 0.200, 0.200, 0.600},
                        {0.002, 0.004, 0.000, 0.200, 0.200, 0.600},
                        {0.002, 0.004, 0.000, 0.200, 0.400, 0.800},
                        {0.002, 0.004, 0.000, 0.200, 0.400, 0.800},
                        {0.002, 0.004, 0.000, 0.200, 0.600, 1.000},
                        {0.002, 0.004, 0.000, 0.200, 0.600, 1.000},
                        {0.002, 0.004, 0.000, 0.400, 0.000, 0.000},
                        {0.002, 0.004, 0.000, 0.400, 0.000, 0.000},
                        {0.002, 0.004, 0.000, 0.400, 0.000, 0.200},
                        {0.002, 0.004, 0.000, 0.400, 0.000, 0.200},
                        {0.002, 0.004, 0.000, 0.400, 0.000, 0.400},
                        {0.002, 0.004, 0.000, 0.400, 0.000, 0.400},
                        {0.002, 0.004, 0.000, 0.400, 0.200, 0.600},
                        {0.002, 0.004, 0.000, 0.400, 0.200, 0.600},
                        {0.002, 0.004, 0.000, 0.400, 0.400, 0.800},
                        {0.002, 0.004, 0.000, 0.400, 0.400, 0.800},
                        {0.002, 0.004, 0.000, 0.400, 0.600, 1.000},
                        {0.002, 0.004, 0.000, 0.400, 0.600, 1.000},
                        {0.002, 0.004, 0.200, 0.600, 0.000, 0.000},
                        {0.002, 0.004, 0.200, 0.600, 0.000, 0.000},
                        {0.002, 0.004, 0.200, 0.600, 0.000, 0.200},
                        {0.002, 0.004, 0.200, 0.600, 0.000, 0.200},
                        {0.002, 0.004, 0.200, 0.600, 0.000, 0.400},
                        {0.002, 0.004, 0.200, 0.600, 0.000, 0.400},
                        {0.002, 0.004, 0.200, 0.600, 0.200, 0.600},
                        {0.002, 0.004, 0.200, 0.600, 0.200, 0.600},
                        {0.002, 0.004, 0.200, 0.600, 0.400, 0.800},
                        {0.002, 0.004, 0.200, 0.600, 0.400, 0.800},
                        {0.002, 0.004, 0.200, 0.600, 0.600, 1.000},
                        {0.002, 0.004, 0.200, 0.600, 0.600, 1.000},
                        {0.002, 0.004, 0.400, 0.800, 0.000, 0.000},
                        {0.002, 0.004, 0.400, 0.800, 0.000, 0.000},
                        {0.002, 0.004, 0.400, 0.800, 0.000, 0.200},
                        {0.002, 0.004, 0.400, 0.800, 0.000, 0.200},
                        {0.002, 0.004, 0.400, 0.800, 0.000, 0.400},
                        {0.002, 0.004, 0.400, 0.800, 0.000, 0.400},
                        {0.002, 0.004, 0.400, 0.800, 0.200, 0.600},
                        {0.002, 0.004, 0.400, 0.800, 0.200, 0.600},
                        {0.002, 0.004, 0.400, 0.800, 0.400, 0.800},
                        {0.002, 0.004, 0.400, 0.800, 0.400, 0.800},
                        {0.002, 0.004, 0.400, 0.800, 0.600, 1.000},
                        {0.002, 0.004, 0.400, 0.800, 0.600, 1.000},
                        {0.002, 0.004, 0.600, 1.000, 0.000, 0.000},
                        {0.002, 0.004, 0.600, 1.000, 0.000, 0.000},
                        {0.002, 0.004, 0.600, 1.000, 0.000, 0.200},
                        {0.002, 0.004, 0.600, 1.000, 0.000, 0.200},
                        {0.002, 0.004, 0.600, 1.000, 0.000, 0.400},
                        {0.002, 0.004, 0.600, 1.000, 0.000, 0.400},
                        {0.002, 0.004, 0.600, 1.000, 0.200, 0.600},
                        {0.002, 0.004, 0.600, 1.000, 0.200, 0.600},
                        {0.002, 0.004, 0.600, 1.000, 0.400, 0.800},
                        {0.002, 0.004, 0.600, 1.000, 0.400, 0.800},
                        {0.002, 0.004, 0.600, 1.000, 0.600, 1.000},
                        {0.002, 0.004, 0.600, 1.000, 0.600, 1.000},
                        {0.004, 0.006, 0.000, 0.000, 0.000, 0.000},
                        {0.004, 0.006, 0.000, 0.000, 0.000, 0.000},
                        {0.004, 0.006, 0.000, 0.000, 0.000, 0.200},
                        {0.004, 0.006, 0.000, 0.000, 0.000, 0.200},
                        {0.004, 0.006, 0.000, 0.000, 0.000, 0.400},
                        {0.004, 0.006, 0.000, 0.000, 0.000, 0.400},
                        {0.004, 0.006, 0.000, 0.000, 0.200, 0.600},
                        {0.004, 0.006, 0.000, 0.000, 0.200, 0.600},
                        {0.004, 0.006, 0.000, 0.000, 0.400, 0.800},
                        {0.004, 0.006, 0.000, 0.000, 0.400, 0.800},
                        {0.004, 0.006, 0.000, 0.000, 0.600, 1.000},
                        {0.004, 0.006, 0.000, 0.000, 0.600, 1.000},
                        {0.004, 0.006, 0.000, 0.200, 0.000, 0.000},
                        {0.004, 0.006, 0.000, 0.200, 0.000, 0.000},
                        {0.004, 0.006, 0.000, 0.200, 0.000, 0.200},
                        {0.004, 0.006, 0.000, 0.200, 0.000, 0.200},
                        {0.004, 0.006, 0.000, 0.200, 0.000, 0.400},
                        {0.004, 0.006, 0.000, 0.200, 0.000, 0.400},
                        {0.004, 0.006, 0.000, 0.200, 0.200, 0.600},
                        {0.004, 0.006, 0.000, 0.200, 0.200, 0.600},
                        {0.004, 0.006, 0.000, 0.200, 0.400, 0.800},
                        {0.004, 0.006, 0.000, 0.200, 0.400, 0.800},
                        {0.004, 0.006, 0.000, 0.200, 0.600, 1.000},
                        {0.004, 0.006, 0.000, 0.200, 0.600, 1.000},
                        {0.004, 0.006, 0.000, 0.400, 0.000, 0.000},
                        {0.004, 0.006, 0.000, 0.400, 0.000, 0.000},
                        {0.004, 0.006, 0.000, 0.400, 0.000, 0.200},
                        {0.004, 0.006, 0.000, 0.400, 0.000, 0.200},
                        {0.004, 0.006, 0.000, 0.400, 0.000, 0.400},
                        {0.004, 0.006, 0.000, 0.400, 0.000, 0.400},
                        {0.004, 0.006, 0.000, 0.400, 0.200, 0.600},
                        {0.004, 0.006, 0.000, 0.400, 0.200, 0.600},
                        {0.004, 0.006, 0.000, 0.400, 0.400, 0.800},
                        {0.004, 0.006, 0.000, 0.400, 0.400, 0.800},
                        {0.004, 0.006, 0.000, 0.400, 0.600, 1.000},
                        {0.004, 0.006, 0.000, 0.400, 0.600, 1.000},
                        {0.004, 0.006, 0.200, 0.600, 0.000, 0.000},
                        {0.004, 0.006, 0.200, 0.600, 0.000, 0.000},
                        {0.004, 0.006, 0.200, 0.600, 0.000, 0.200},
                        {0.004, 0.006, 0.200, 0.600, 0.000, 0.200},
                        {0.004, 0.006, 0.200, 0.600, 0.000, 0.400},
                        {0.004, 0.006, 0.200, 0.600, 0.000, 0.400},
                        {0.004, 0.006, 0.200, 0.600, 0.200, 0.600},
                        {0.004, 0.006, 0.200, 0.600, 0.200, 0.600},
                        {0.004, 0.006, 0.200, 0.600, 0.400, 0.800},
                        {0.004, 0.006, 0.200, 0.600, 0.400, 0.800},
                        {0.004, 0.006, 0.200, 0.600, 0.600, 1.000},
                        {0.004, 0.006, 0.200, 0.600, 0.600, 1.000},
                        {0.004, 0.006, 0.400, 0.800, 0.000, 0.000},
                        {0.004, 0.006, 0.400, 0.800, 0.000, 0.000},
                        {0.004, 0.006, 0.400, 0.800, 0.000, 0.200},
                        {0.004, 0.006, 0.400, 0.800, 0.000, 0.200},
                        {0.004, 0.006, 0.400, 0.800, 0.000, 0.400},
                        {0.004, 0.006, 0.400, 0.800, 0.000, 0.400},
                        {0.004, 0.006, 0.400, 0.800, 0.200, 0.600},
                        {0.004, 0.006, 0.400, 0.800, 0.200, 0.600},
                        {0.004, 0.006, 0.400, 0.800, 0.400, 0.800},
                        {0.004, 0.006, 0.400, 0.800, 0.400, 0.800},
                        {0.004, 0.006, 0.400, 0.800, 0.600, 1.000},
                        {0.004, 0.006, 0.400, 0.800, 0.600, 1.000},
                        {0.004, 0.006, 0.600, 1.000, 0.000, 0.000},
                        {0.004, 0.006, 0.600, 1.000, 0.000, 0.000},
                        {0.004, 0.006, 0.600, 1.000, 0.000, 0.200},
                        {0.004, 0.006, 0.600, 1.000, 0.000, 0.200},
                        {0.004, 0.006, 0.600, 1.000, 0.000, 0.400},
                        {0.004, 0.006, 0.600, 1.000, 0.000, 0.400},
                        {0.004, 0.006, 0.600, 1.000, 0.200, 0.600},
                        {0.004, 0.006, 0.600, 1.000, 0.200, 0.600},
                        {0.004, 0.006, 0.600, 1.000, 0.400, 0.800},
                        {0.004, 0.006, 0.600, 1.000, 0.400, 0.800},
                        {0.004, 0.006, 0.600, 1.000, 0.600, 1.000},
                        {0.004, 0.006, 0.600, 1.000, 0.600, 1.000},
                        {0.006, 0.008, 0.000, 0.000, 0.000, 0.000},
                        {0.006, 0.008, 0.000, 0.000, 0.000, 0.000},
                        {0.006, 0.008, 0.000, 0.000, 0.000, 0.200},
                        {0.006, 0.008, 0.000, 0.000, 0.000, 0.200},
                        {0.006, 0.008, 0.000, 0.000, 0.000, 0.400},
                        {0.006, 0.008, 0.000, 0.000, 0.000, 0.400},
                        {0.006, 0.008, 0.000, 0.000, 0.200, 0.600},
                        {0.006, 0.008, 0.000, 0.000, 0.200, 0.600},
                        {0.006, 0.008, 0.000, 0.000, 0.400, 0.800},
                        {0.006, 0.008, 0.000, 0.000, 0.400, 0.800},
                        {0.006, 0.008, 0.000, 0.000, 0.600, 1.000},
                        {0.006, 0.008, 0.000, 0.000, 0.600, 1.000},
                        {0.006, 0.008, 0.000, 0.200, 0.000, 0.000},
                        {0.006, 0.008, 0.000, 0.200, 0.000, 0.000},
                        {0.006, 0.008, 0.000, 0.200, 0.000, 0.200},
                        {0.006, 0.008, 0.000, 0.200, 0.000, 0.200},
                        {0.006, 0.008, 0.000, 0.200, 0.000, 0.400},
                        {0.006, 0.008, 0.000, 0.200, 0.000, 0.400},
                        {0.006, 0.008, 0.000, 0.200, 0.200, 0.600},
                        {0.006, 0.008, 0.000, 0.200, 0.200, 0.600},
                        {0.006, 0.008, 0.000, 0.200, 0.400, 0.800},
                        {0.006, 0.008, 0.000, 0.200, 0.400, 0.800},
                        {0.006, 0.008, 0.000, 0.200, 0.600, 1.000},
                        {0.006, 0.008, 0.000, 0.200, 0.600, 1.000},
                        {0.006, 0.008, 0.000, 0.400, 0.000, 0.000},
                        {0.006, 0.008, 0.000, 0.400, 0.000, 0.000},
                        {0.006, 0.008, 0.000, 0.400, 0.000, 0.200},
                        {0.006, 0.008, 0.000, 0.400, 0.000, 0.200},
                        {0.006, 0.008, 0.000, 0.400, 0.000, 0.400},
                        {0.006, 0.008, 0.000, 0.400, 0.000, 0.400},
                        {0.006, 0.008, 0.000, 0.400, 0.200, 0.600},
                        {0.006, 0.008, 0.000, 0.400, 0.200, 0.600},
                        {0.006, 0.008, 0.000, 0.400, 0.400, 0.800},
                        {0.006, 0.008, 0.000, 0.400, 0.400, 0.800},
                        {0.006, 0.008, 0.000, 0.400, 0.600, 1.000},
                        {0.006, 0.008, 0.000, 0.400, 0.600, 1.000},
                        {0.006, 0.008, 0.200, 0.600, 0.000, 0.000},
                        {0.006, 0.008, 0.200, 0.600, 0.000, 0.000},
                        {0.006, 0.008, 0.200, 0.600, 0.000, 0.200},
                        {0.006, 0.008, 0.200, 0.600, 0.000, 0.200},
                        {0.006, 0.008, 0.200, 0.600, 0.000, 0.400},
                        {0.006, 0.008, 0.200, 0.600, 0.000, 0.400},
                        {0.006, 0.008, 0.200, 0.600, 0.200, 0.600},
                        {0.006, 0.008, 0.200, 0.600, 0.200, 0.600},
                        {0.006, 0.008, 0.200, 0.600, 0.400, 0.800},
                        {0.006, 0.008, 0.200, 0.600, 0.400, 0.800},
                        {0.006, 0.008, 0.200, 0.600, 0.600, 1.000},
                        {0.006, 0.008, 0.200, 0.600, 0.600, 1.000},
                        {0.006, 0.008, 0.400, 0.800, 0.000, 0.000},
                        {0.006, 0.008, 0.400, 0.800, 0.000, 0.000},
                        {0.006, 0.008, 0.400, 0.800, 0.000, 0.200},
                        {0.006, 0.008, 0.400, 0.800, 0.000, 0.200},
                        {0.006, 0.008, 0.400, 0.800, 0.000, 0.400},
                        {0.006, 0.008, 0.400, 0.800, 0.000, 0.400},
                        {0.006, 0.008, 0.400, 0.800, 0.200, 0.600},
                        {0.006, 0.008, 0.400, 0.800, 0.200, 0.600},
                        {0.006, 0.008, 0.400, 0.800, 0.400, 0.800},
                        {0.006, 0.008, 0.400, 0.800, 0.400, 0.800},
                        {0.006, 0.008, 0.400, 0.800, 0.600, 1.000},
                        {0.006, 0.008, 0.400, 0.800, 0.600, 1.000},
                        {0.006, 0.008, 0.600, 1.000, 0.000, 0.000},
                        {0.006, 0.008, 0.600, 1.000, 0.000, 0.000},
                        {0.006, 0.008, 0.600, 1.000, 0.000, 0.200},
                        {0.006, 0.008, 0.600, 1.000, 0.000, 0.200},
                        {0.006, 0.008, 0.600, 1.000, 0.000, 0.400},
                        {0.006, 0.008, 0.600, 1.000, 0.000, 0.400},
                        {0.006, 0.008, 0.600, 1.000, 0.200, 0.600},
                        {0.006, 0.008, 0.600, 1.000, 0.200, 0.600},
                        {0.006, 0.008, 0.600, 1.000, 0.400, 0.800},
                        {0.006, 0.008, 0.600, 1.000, 0.400, 0.800},
                        {0.006, 0.008, 0.600, 1.000, 0.600, 1.000},
                        {0.006, 0.008, 0.600, 1.000, 0.600, 1.000},
                        {0.008, 0.010, 0.000, 0.000, 0.000, 0.000},
                        {0.008, 0.010, 0.000, 0.000, 0.000, 0.000},
                        {0.008, 0.010, 0.000, 0.000, 0.000, 0.200},
                        {0.008, 0.010, 0.000, 0.000, 0.000, 0.200},
                        {0.008, 0.010, 0.000, 0.000, 0.000, 0.400},
                        {0.008, 0.010, 0.000, 0.000, 0.000, 0.400},
                        {0.008, 0.010, 0.000, 0.000, 0.200, 0.600},
                        {0.008, 0.010, 0.000, 0.000, 0.200, 0.600},
                        {0.008, 0.010, 0.000, 0.000, 0.400, 0.800},
                        {0.008, 0.010, 0.000, 0.000, 0.400, 0.800},
                        {0.008, 0.010, 0.000, 0.000, 0.600, 1.000},
                        {0.008, 0.010, 0.000, 0.000, 0.600, 1.000},
                        {0.008, 0.010, 0.000, 0.200, 0.000, 0.000},
                        {0.008, 0.010, 0.000, 0.200, 0.000, 0.000},
                        {0.008, 0.010, 0.000, 0.200, 0.000, 0.200},
                        {0.008, 0.010, 0.000, 0.200, 0.000, 0.200},
                        {0.008, 0.010, 0.000, 0.200, 0.000, 0.400},
                        {0.008, 0.010, 0.000, 0.200, 0.000, 0.400},
                        {0.008, 0.010, 0.000, 0.200, 0.200, 0.600},
                        {0.008, 0.010, 0.000, 0.200, 0.200, 0.600},
                        {0.008, 0.010, 0.000, 0.200, 0.400, 0.800},
                        {0.008, 0.010, 0.000, 0.200, 0.400, 0.800},
                        {0.008, 0.010, 0.000, 0.200, 0.600, 1.000},
                        {0.008, 0.010, 0.000, 0.200, 0.600, 1.000},
                        {0.008, 0.010, 0.000, 0.400, 0.000, 0.000},
                        {0.008, 0.010, 0.000, 0.400, 0.000, 0.000},
                        {0.008, 0.010, 0.000, 0.400, 0.000, 0.200},
                        {0.008, 0.010, 0.000, 0.400, 0.000, 0.200},
                        {0.008, 0.010, 0.000, 0.400, 0.000, 0.400},
                        {0.008, 0.010, 0.000, 0.400, 0.000, 0.400},
                        {0.008, 0.010, 0.000, 0.400, 0.200, 0.600},
                        {0.008, 0.010, 0.000, 0.400, 0.200, 0.600},
                        {0.008, 0.010, 0.000, 0.400, 0.400, 0.800},
                        {0.008, 0.010, 0.000, 0.400, 0.400, 0.800},
                        {0.008, 0.010, 0.000, 0.400, 0.600, 1.000},
                        {0.008, 0.010, 0.000, 0.400, 0.600, 1.000},
                        {0.008, 0.010, 0.200, 0.600, 0.000, 0.000},
                        {0.008, 0.010, 0.200, 0.600, 0.000, 0.000},
                        {0.008, 0.010, 0.200, 0.600, 0.000, 0.200},
                        {0.008, 0.010, 0.200, 0.600, 0.000, 0.200},
                        {0.008, 0.010, 0.200, 0.600, 0.000, 0.400},
                        {0.008, 0.010, 0.200, 0.600, 0.000, 0.400},
                        {0.008, 0.010, 0.200, 0.600, 0.200, 0.600},
                        {0.008, 0.010, 0.200, 0.600, 0.200, 0.600},
                        {0.008, 0.010, 0.200, 0.600, 0.400, 0.800},
                        {0.008, 0.010, 0.200, 0.600, 0.400, 0.800},
                        {0.008, 0.010, 0.200, 0.600, 0.600, 1.000},
                        {0.008, 0.010, 0.200, 0.600, 0.600, 1.000},
                        {0.008, 0.010, 0.400, 0.800, 0.000, 0.000},
                        {0.008, 0.010, 0.400, 0.800, 0.000, 0.000},
                        {0.008, 0.010, 0.400, 0.800, 0.000, 0.200},
                        {0.008, 0.010, 0.400, 0.800, 0.000, 0.200},
                        {0.008, 0.010, 0.400, 0.800, 0.000, 0.400},
                        {0.008, 0.010, 0.400, 0.800, 0.000, 0.400},
                        {0.008, 0.010, 0.400, 0.800, 0.200, 0.600},
                        {0.008, 0.010, 0.400, 0.800, 0.200, 0.600},
                        {0.008, 0.010, 0.400, 0.800, 0.400, 0.800},
                        {0.008, 0.010, 0.400, 0.800, 0.400, 0.800},
                        {0.008, 0.010, 0.400, 0.800, 0.600, 1.000},
                        {0.008, 0.010, 0.400, 0.800, 0.600, 1.000},
                        {0.008, 0.010, 0.600, 1.000, 0.000, 0.000},
                        {0.008, 0.010, 0.600, 1.000, 0.000, 0.000},
                        {0.008, 0.010, 0.600, 1.000, 0.000, 0.200},
                        {0.008, 0.010, 0.600, 1.000, 0.000, 0.200},
                        {0.008, 0.010, 0.600, 1.000, 0.000, 0.400},
                        {0.008, 0.010, 0.600, 1.000, 0.000, 0.400},
                        {0.008, 0.010, 0.600, 1.000, 0.200, 0.600},
                        {0.008, 0.010, 0.600, 1.000, 0.200, 0.600},
                        {0.008, 0.010, 0.600, 1.000, 0.400, 0.800},
                        {0.008, 0.010, 0.600, 1.000, 0.400, 0.800},
                        {0.008, 0.010, 0.600, 1.000, 0.600, 1.000},
                        {0.008, 0.010, 0.600, 1.000, 0.600, 1.000},
                };
        for (@NotNull final double[] param : params) {
            System.runFinalization();
            System.gc();
            Setting setting = Setting.getInstance();
            setting.setbGlobMin(param[4]);
            setting.setbGlobMax(param[5]);
            setting.setbLocMin(param[2]);
            setting.setbLocMax(param[3]);
            setting.setbRandMin(param[0]);
            setting.setbRandMax(param[1]);
            setting.setMaxParticle(20);
            setting.setMaxEpoch(10000);
            setting.setTimeVariantWeight(1);
            setting.setTotalCore(Runtime.getRuntime().availableProcessors());
            setting.setCalculator(Setting.PURE_PTVPSO);
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
            System.out.printf("%.3f\t%.3f\t%.3f\t%.3f\t%.3f\t%.3f\t%f\t%d,\n", param[0], param[1], param[2], param[3], param[4], param[5], pso.getFitness(), c2 - c1);
            System.runFinalization();
            System.gc();
        }
    }

    @Test
    public void testIterationWithReport() {
        @NotNull final int[] params = new int[]
                {
                        50,
                        50,
                        100,
                        100,
                        200,
                        200,
                        500,
                        500,
                        1000,
                        1000,
                        2000,
                        2000,
                        5000,
                        5000,
                        10000,
                        10000,
                        20000,
                        20000,
                        50000,
                        50000,
                        100000,
                        100000,
                        500000,
                        500000
                };

        for (final int param : params) {
            System.runFinalization();
            System.gc();
            Setting setting = Setting.getInstance();
            setting.setbGlobMin(0.6);
            setting.setbGlobMax(1);
            setting.setbLocMin(0.6);
            setting.setbLocMax(1);
            setting.setbRandMin(0.000);
            setting.setbRandMax(0.002);
            setting.setMaxParticle(20);
            setting.setMaxEpoch(param);
            setting.setTimeVariantWeight(1);
            setting.setTotalCore(Runtime.getRuntime().availableProcessors());
            setting.setCalculator(Setting.PURE_PTVPSO);
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
            System.out.printf("%6d\t%f\t%d,\n", param, pso.getFitness(), c2 - c1);
            System.runFinalization();
            System.gc();
        }
    }
}