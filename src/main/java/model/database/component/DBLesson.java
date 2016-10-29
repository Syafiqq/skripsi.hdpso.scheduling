package model.database.component;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.component> created by : 
 * Name         : syafiq
 * Date / Time  : 26 October 2016, 6:31 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings({"unused", "WeakerAccess"}) public class DBLesson
{
    private final     int                     id;
    @NotNull private  DBSubject               subject;
    private           int                     sks;
    private           int                     count;
    @Nullable private DBLecture               lecture;
    @NotNull private  DBClass                 klass;
    @NotNull private  ObjectList<DBClassroom> classrooms;

    public DBLesson(int id, @NotNull DBSubject subject, int sks, int count, @Nullable DBLecture lecture, @NotNull DBClass klass, int available_classroom_size)
    {
        this.id = id;
        this.subject = subject;
        this.sks = sks;
        this.count = count;
        this.lecture = lecture;
        this.klass = klass;
        this.classrooms = new ObjectArrayList<>(available_classroom_size);
    }

    public int getId()
    {
        return this.id;
    }

    @NotNull public DBSubject getSubject()
    {
        return this.subject;
    }

    public int getSks()
    {
        return this.sks;
    }

    public int getCount()
    {
        return this.count;
    }

    @Nullable public DBLecture getLecture()
    {
        return this.lecture;
    }

    @NotNull public DBClass getKlass()
    {
        return this.klass;
    }

    @NotNull public ObjectList<DBClassroom> getClassrooms()
    {
        return this.classrooms;
    }

    @Override public String toString()
    {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("subject", subject.getId())
                .append("sks", sks)
                .append("count", count)
                .append("lecture", lecture == null ? null : lecture.getId())
                .append("klass", klass.getId())
                .append("classrooms", classrooms.stream().map(DBClassroom::getId).toArray())
                .toString();
    }
}
