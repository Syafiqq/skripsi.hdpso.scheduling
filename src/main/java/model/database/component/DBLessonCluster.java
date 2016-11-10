package model.database.component;

import it.unimi.dsi.fastutil.objects.ObjectAVLTreeSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.math3.util.FastMath;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.component> created by : 
 * Name         : syafiq
 * Date / Time  : 01 November 2016, 5:34 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("unused") public class DBLessonCluster
{
    private final LinkedList<DBLessonGroup>     lesson_group;
    private final ObjectAVLTreeSet<DBClassroom> classrooms;
    private final ObjectAVLTreeSet<DBLesson>    lessons;

    public DBLessonCluster(final DBLessonGroup lesson_group)
    {
        this.lesson_group = new LinkedList<>();
        this.classrooms = new ObjectAVLTreeSet<>((class_1, class_2) -> (int) Math.signum(class_1.getId() - class_2.getId()));
        this.lessons = new ObjectAVLTreeSet<>((lesson_1, lesson_2) -> (int) Math.signum(lesson_1.getId() - lesson_2.getId()));

        this.add(lesson_group);
    }

    private void addLessonGroup(@NotNull final DBLessonGroup lesson_group)
    {
        this.lesson_group.addLast(lesson_group);
        Collections.sort(this.lesson_group, (lesson_group_1, lesson_group_2) -> (int) FastMath.signum(lesson_group_1.getClassroomSize() - lesson_group_2.getClassroomSize()));
    }

    public boolean isOnCluster(DBLessonGroup lesson_group)
    {
        boolean isOnCluster = false;
        for(DBClassroom classroom : this.classrooms)
        {
            if(lesson_group.isOnSet(classroom))
            {
                isOnCluster = true;
                break;
            }
        }
        return isOnCluster;
    }

    public void add(@Flow(targetIsContainer = true) DBLessonGroup lesson_group)
    {
        this.classrooms.addAll(lesson_group.getClassrooms());
        this.lessons.addAll(lesson_group.getLessons());
        this.addLessonGroup(lesson_group);
    }

    @NotNull public List<DBLessonGroup> getLessonGroup()
    {
        return this.lesson_group;
    }

    @NotNull public ObjectSet<DBClassroom> getClassrooms()
    {
        return this.classrooms;
    }

    @NotNull public ObjectSet<DBLesson> getLessons()
    {
        return this.lessons;
    }

    public int getLessonGroupSize()
    {
        return this.lesson_group.size();
    }

    public int getClassroomSize()
    {
        return this.classrooms.size();
    }

    public int getLessonSize()
    {
        return this.lessons.size();
    }
}
