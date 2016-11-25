package model.method.pso.hdpso.core;

import model.database.component.DBSchool;
import model.database.loader.DBProblemLoader;
import model.dataset.component.DSLesson;
import model.dataset.component.DSLessonCluster;
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
 * Date / Time  : 24 November 2016, 4:25 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class TPSO_Particle_Stability_Checking
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
        setting.max_particle = 1;
        setting.max_epoch = 1;
        setting.bloc_min = 0.600;
        setting.bloc_max = 0.900;
        setting.bglob_min = 0.100;
        setting.bglob_max = 0.400;
        setting.brand_min = 0.001;
        setting.brand_max = 0.100;
        setting.total_core = 4;
    }

    @Test
    public void test_()
    {
        @NotNull final PSO pso = new PSO(this.dsLoader);
        Assert.assertNotNull(pso);
        pso.initialize();

        Assert.assertTrue(this.check(pso.getParticle(0)));
        try
        {
            Thread.sleep(800);
        }
        catch(InterruptedException ignored)
        {
        }
    }

    @SuppressWarnings("ConstantConditions") private boolean check(@NotNull final Particle particle)
    {
        boolean                    isValid         = true;
        @NotNull Position[]        positions       = particle.getData().getPositions();
        @NotNull DSLesson[]        lessons         = this.dsLoader.getDataset().getLessons();
        @NotNull DSLessonCluster[] lesson_clusters = this.dsLoader.getDataset().getLessonClusters();
        @NotNull int[]             days            = this.dsLoader.getDataset().getDays();
        cluster:
        for(int c_cluster = -1, cs_cluster = lesson_clusters.length; ++c_cluster < cs_cluster; )
        {
            @NotNull DSLessonCluster lesson_cluster = lesson_clusters[c_cluster];
            @NotNull int[]           classrooms     = lesson_cluster.getClassrooms();
            @NotNull int[][][]       clustered_time = lesson_cluster.getClassroomClusteredTime();
            @NotNull int[]           position       = positions[c_cluster].getPosition();
            int                      c_position     = -1;
            for(int c_classroom = -1, cs_classroom = classrooms.length; ++c_classroom < cs_classroom; )
            {
                for(int c_day = -1, cs_day = days.length; ++c_day < cs_day; )
                {
                    int c_clustered  = 0;
                    int clustered    = clustered_time[c_classroom][c_day][++c_clustered];
                    int current_time = 0;
                    int current_sks;
                    while(true)
                    {
                        final DSLesson lesson = lessons[position[++c_position]];
                        current_sks = lesson == null ? 1 : lesson.getSks();
                        if(current_time + current_sks < clustered)
                        {
                            current_time += current_sks;
                        }
                        else if(current_time + current_sks == clustered)
                        {
                            current_time += current_sks;
                            try
                            {
                                clustered += clustered_time[c_classroom][c_day][++c_clustered];
                            }
                            catch(ArrayIndexOutOfBoundsException ignored)
                            {
                                break;
                            }
                        }
                        else
                        {
                            isValid = false;
                            continue cluster;
                        }
                    }
                }
            }
        }
        return isValid;
    }
}
