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
@SuppressWarnings({"unused", "ConstantConditions", "WeakerAccess"}) public class DSLesson
{
    final private int       klass;
    final private int       subject;
    final private int       lecture;
    final private int       sks;
    final private int       lesson_parent;
    final private int[]     lesson_link;
    final private int[]     available_classroom;
    final private boolean[] allowed_classroom;

    public DSLesson(int klass, int subject, int lecture, int sks, int lesson_parent, int[] lesson_link, int[] available_classroom, boolean[] allowed_classroom)
    {
        this.klass = klass;
        this.subject = subject;
        this.lecture = lecture;
        this.sks = sks;
        this.lesson_parent = lesson_parent;
        this.lesson_link = lesson_link;
        this.available_classroom = available_classroom;
        this.allowed_classroom = allowed_classroom;
    }

    public static DSLesson newInstance(final @NotNull DatasetConverter encoder, final @NotNull DBLesson lesson, int[] lesson_link)
    {
        return DSLesson.newLinkInstance(encoder, lesson, -1, lesson_link);
    }

    public static DSLesson newLinkInstance(final @NotNull DatasetConverter encoder, final @NotNull DBLesson lesson, int parent, int[] lesson_link)
    {
        int subject = encoder.getSubject(lesson.getSubject().getId());

        int lecture;
        try
        {
            lecture = encoder.getLecture(lesson.getLecture().getId());
        }
        catch(NullPointerException ignored)
        {
            lecture = -1;
        }

        int       klass               = encoder.getKlass(lesson.getKlass().getId());
        int       sks                 = lesson.getSks();
        int[]     available_classroom = new int[lesson.getClassrooms().size()];
        boolean[] allowed_classroom   = new boolean[encoder.getClassrooms().size()];

        final @NotNull Int2IntLinkedOpenHashMap classroom_encoder = encoder.getClassrooms();
        int                                     counter           = -1;
        for(final @NotNull DBClassroom classroom : lesson.getClassrooms())
        {
            available_classroom[++counter] = classroom_encoder.get(classroom.getId());
            allowed_classroom[available_classroom[counter]] = true;
        }

        int lesson_parent = -1;

        return new DSLesson(klass, subject, lecture, sks, parent, lesson_link, available_classroom, allowed_classroom);
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

    public int getLessonParent()
    {
        return this.lesson_parent;
    }

    public int[] getLessonLink()
    {
        return this.lesson_link;
    }

    public int[] getAvailableClassroom()
    {
        return this.available_classroom;
    }

    public boolean[] getAllowedClassroom()
    {
        return this.allowed_classroom;
    }
}
