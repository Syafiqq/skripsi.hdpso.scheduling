package model.database.component;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.intellij.lang.annotations.Flow;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.component> created by : 
 * Name         : syafiq
 * Date / Time  : 01 November 2016, 5:34 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class DBLessonSet
{
    private final ObjectLinkedOpenHashSet<DBClassroom> classrooms;
    private final ObjectList<DBLesson>                 lessons;

    public DBLessonSet(ObjectList<DBClassroom> classrooms, int expected)
    {
        this.classrooms = new ObjectLinkedOpenHashSet<>(classrooms);
        this.lessons = new ObjectArrayList<>(expected);
    }

    public boolean add(@Flow(targetIsContainer = true) DBLesson dbLesson)
    {
        return this.lessons.add(dbLesson);
    }

    public boolean isOnSet(DBClassroom next)
    {
        return classrooms.contains(next);
    }

    public ObjectLinkedOpenHashSet<DBClassroom> getClassrooms()
    {
        return this.classrooms;
    }

    public ObjectList<DBLesson> getLessons()
    {
        return this.lessons;
    }
}
