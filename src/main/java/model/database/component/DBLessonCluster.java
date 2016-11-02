package model.database.component;

import it.unimi.dsi.fastutil.objects.ObjectAVLTreeSet;
import org.intellij.lang.annotations.Flow;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.component> created by : 
 * Name         : syafiq
 * Date / Time  : 01 November 2016, 5:34 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("unused") public class DBLessonCluster
{
    private final ObjectAVLTreeSet<DBClassroom> classrooms;
    private final ObjectAVLTreeSet<DBLessonSet> lesson_cluster;
    private final ObjectAVLTreeSet<DBLesson>    lessons;

    public DBLessonCluster(final DBLessonSet lesson_set)
    {
        this.classrooms = new ObjectAVLTreeSet<>((class_1, class_2) -> (int) Math.signum(class_1.getId() - class_2.getId()));
        this.lesson_cluster = new ObjectAVLTreeSet<>((lesson_set_1, lesson_set_2) -> (int) Math.signum(lesson_set_1.getLessons().size() - lesson_set_2.getLessons().size()));
        this.lessons = new ObjectAVLTreeSet<>((lesson_1, lesson_2) -> (int) Math.signum(lesson_1.getId() - lesson_2.getId()));

        this.add(lesson_set);
    }

    public boolean isOnCluster(DBLessonSet lesson_set)
    {
        boolean isOnCluster = false;
        for(DBClassroom classroom : this.classrooms)
        {
            if(lesson_set.isOnSet(classroom))
            {
                isOnCluster = true;
                break;
            }
        }
        return isOnCluster;
    }

    public boolean add(@Flow(targetIsContainer = true) DBLessonSet dbLessonSet)
    {
        this.classrooms.addAll(dbLessonSet.getClassrooms());
        this.lessons.addAll(dbLessonSet.getLessons());
        return lesson_cluster.add(dbLessonSet);
    }

    public ObjectAVLTreeSet<DBClassroom> getClassrooms()
    {
        return this.classrooms;
    }

    public ObjectAVLTreeSet<DBLessonSet> getLessonCluster()
    {
        return this.lesson_cluster;
    }

    public ObjectAVLTreeSet<DBLesson> getLessons()
    {
        return this.lessons;
    }
}
