package model.database.component;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import model.database.component.metadata.*;
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
@SuppressWarnings({"unused", "WeakerAccess"}) public class DBLesson extends DBMLesson
{
    @NotNull private  ObjectList<DBMClassroom> classrooms;

    public DBLesson(int id, @NotNull DBMSubject subject, int sks, int count, @Nullable DBMLecture lecture, @NotNull DBMClass klass, int available_classroom_size)
    {
        super(id, subject, sks, count, lecture, klass);
        this.classrooms = new ObjectArrayList<>(available_classroom_size);
    }

    @NotNull public ObjectList<DBMClassroom> getClassrooms()
    {
        return this.classrooms;
    }

    @Override public String toString()
    {
        return new ToStringBuilder(this)
                .append("id", super.id)
                .append("subject", super.subject.getId())
                .append("sks", super.sks)
                .append("count", super.count)
                .append("lecture", super.lecture == null ? null : super.lecture.getId())
                .append("klass", super.klass.getId())
                .append("classrooms", this.classrooms.stream().map(DBMClassroom::getId).toArray())
                .toString();
    }
}
