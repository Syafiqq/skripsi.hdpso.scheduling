package model.database.component;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import model.database.component.metadata.DBMClassroom;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.component> created by : 
 * Name         : syafiq
 * Date / Time  : 01 November 2016, 5:34 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings({"WeakerAccess", "unused"}) public class DBLessonGroup
{
    private final ObjectSet<DBMClassroom> classrooms;
    private final ObjectList<DBLesson>   lessons;

    public DBLessonGroup(ObjectList<DBMClassroom> classrooms, int expected)
    {
        this.classrooms = new ObjectLinkedOpenHashSet<>(classrooms);
        this.lessons = new ObjectArrayList<>(expected);
    }

    public boolean add(@Flow(targetIsContainer = true) DBLesson dbLesson)
    {
        return this.lessons.add(dbLesson);
    }

    public boolean isOnSet(DBMClassroom next)
    {
        return classrooms.contains(next);
    }

    @NotNull public ObjectSet<DBMClassroom> getClassrooms()
    {
        return this.classrooms;
    }

    @NotNull public ObjectList<DBLesson> getLessons()
    {
        return this.lessons;
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
