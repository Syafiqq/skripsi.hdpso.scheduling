package model.database.component.metadata;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 25 January 2017, 11:28 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"unused", "WeakerAccess"})
public class DBMLesson {
    protected final int id;
    protected int sks;
    protected int count;
    @NotNull
    protected DBMSubject subject;
    @Nullable
    protected DBMLecture lecture;
    @NotNull
    protected DBMClass klass;

    public DBMLesson(int id, @NotNull DBMSubject subject, int sks, int count, @Nullable DBMLecture lecture, @NotNull DBMClass klass) {
        this.id = id;
        this.subject = subject;
        this.sks = sks;
        this.count = count;
        this.lecture = lecture;
        this.klass = klass;
    }

    public int getId() {
        return this.id;
    }

    public int getSks() {
        return this.sks;
    }

    public void setSks(int sks)
    {
        this.sks = sks;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    @Nullable
    public DBMLecture getLecture() {
        return this.lecture;
    }

    public void setLecture(@Nullable DBMLecture lecture)
    {
        this.lecture = lecture;
    }

    @NotNull
    public DBMClass getKlass() {
        return this.klass;
    }

    public void setKlass(@NotNull DBMClass klass)
    {
        this.klass = klass;
    }

    @NotNull
    public DBMSubject getSubject() {
        return this.subject;
    }

    public void setSubject(@NotNull DBMSubject subject)
    {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("subject", subject.getId())
                .append("sks", sks)
                .append("count", count)
                .append("lecture", lecture == null ? null : lecture.getId())
                .append("klass", klass.getId())
                .toString();
    }
}