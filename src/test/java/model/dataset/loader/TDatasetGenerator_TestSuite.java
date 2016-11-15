package model.dataset.loader;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.dataset.loader> created by : 
 * Name         : syafiq
 * Date / Time  : 03 November 2016, 9:51 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@RunWith(Suite.class)

@Suite.SuiteClasses({
                            TDatasetGenerator_Days.class,
                            TDatasetGenerator_Periods.class,
                            TDatasetGenerator_Classes.class,
                            TDatasetGenerator_Classrooms.class,
                            TDatasetGenerator_Lecturers.class,
                            TDatasetGenerator_Subjects.class,
                            TDatasetGenerator_Lesson.class,
                            TDatasetGenerator_ClusteredClassroomTime.class,
                            TDatasetGenerator_TimeDistribution.class,
                            TDatasetGenerator_LessonGroup.class,
                            TDatasetGenerator_LessonCluster.class
                    })


public class TDatasetGenerator_TestSuite
{
}
