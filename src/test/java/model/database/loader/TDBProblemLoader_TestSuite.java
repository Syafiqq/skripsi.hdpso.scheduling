package model.database.loader;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.loader> created by : 
 * Name         : syafiq
 * Date / Time  : 25 October 2016, 2:53 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@RunWith(Suite.class)

@Suite.SuiteClasses({
                            TDBProblemLoader_Availability.class,
                            TDBProblemLoader_Class.class,
                            TDBProblemLoader_Classroom.class,
                            TDBProblemLoader_Day.class,
                            TDBProblemLoader_ExtraData.class,
                            TDBProblemLoader_Lecture.class,
                            TDBProblemLoader_Lesson.class,
                            TDBProblemLoader_Period.class,
                            TDBProblemLoader_Subject.class,
                            TDBProblemLoader_LessonSet.class,
                            TDBProblemLoader_LessonCluster.class})


public class TDBProblemLoader_TestSuite
{
}
