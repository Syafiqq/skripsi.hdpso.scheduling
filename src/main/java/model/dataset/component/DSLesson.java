package model.dataset.component;

import it.unimi.dsi.fastutil.ints.Int2IntLinkedOpenHashMap;
import model.database.component.DBClassroom;
import model.database.component.DBLesson;
import model.dataset.core.DatasetConverter;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.dataset.component> created by : 
 * Name         : syafiq
 * Date / Time  : 02 November 2016, 7:49 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("unused") public class DSLesson
{
    final private int       klass;
    final private int       subject;
    final private int       lecture;
    final private int       sks;
    final private int       lesson_parent;
    final private int[]     lesson_link;
    final private int[]     available_classroom;
    final private boolean[] allowed_classroom;

    public DSLesson(final @NotNull DatasetConverter encoder, final @NotNull DBLesson lesson)
    {
        this.subject = encoder.getSubject(lesson.getSubject().getId());

        int lecture = -1;
        try
        {
            lecture = encoder.getLecture(lesson.getLecture().getId());
        }
        catch(NullPointerException ignored)
        {
            lecture = -1;
        }
        finally
        {
            this.lecture = lecture;

        }
        this.klass = encoder.getKlass(lesson.getKlass().getId());
        this.sks = lesson.getSks();
        this.lesson_link = new int[lesson.getCount() - 1];
        this.available_classroom = new int[lesson.getClassrooms().size()];
        final @NotNull Int2IntLinkedOpenHashMap classroom_encoder = encoder.getClassrooms();

        int counter = -1;
        for(final @NotNull DBClassroom classroom : lesson.getClassrooms())
        {
            this.available_classroom[++counter] = classroom_encoder.get(classroom.getId());
        }

        this.lesson_parent = -1;
        this.allowed_classroom = new boolean[encoder.getClassrooms().size()];
        for(final int classroom : available_classroom)
        {
            this.allowed_classroom[classroom] = true;
        }
    }

    public int getKlass()
    {
        return this.klass;
    }

    public int getSubject()
    {
        return this.subject;
    }

    public int getLecture()
    {
        return this.lecture;
    }

    public int getSks()
    {
        return this.sks;
    }

    public int getLesson_parent()
    {
        return this.lesson_parent;
    }

    public int[] getLesson_link()
    {
        return this.lesson_link;
    }

    public int[] getAvailable_classroom()
    {
        return this.available_classroom;
    }

    public boolean[] getAllowed_classroom()
    {
        return this.allowed_classroom;
    }
}
