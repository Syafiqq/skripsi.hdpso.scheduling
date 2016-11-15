package model.dataset.core;

import it.unimi.dsi.fastutil.ints.Int2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import model.database.loader.DBProblemLoader;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.dataset.core> created by : 
 * Name         : syafiq
 * Date / Time  : 02 November 2016, 8:08 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings({"WeakerAccess", "unused"}) public class DatasetConverter
{
    final @NotNull Int2IntMap active_days;
    final @NotNull Int2IntMap active_periods;
    final @NotNull Int2IntMap classes;
    final @NotNull Int2IntMap classrooms;
    final @NotNull Int2IntMap lecturers;
    final @NotNull Int2IntMap subjects;
    final @NotNull Int2IntMap lessons;

    public DatasetConverter(@NotNull final DBProblemLoader loader)
    {
        this.active_days = new Int2IntLinkedOpenHashMap(loader.getDays().size());
        this.active_periods = new Int2IntLinkedOpenHashMap(loader.getPeriods().size());
        this.classes = new Int2IntLinkedOpenHashMap(loader.getOperatingClass().size());
        this.classrooms = new Int2IntLinkedOpenHashMap(loader.getOperatingClassroom().size());
        this.lecturers = new Int2IntLinkedOpenHashMap(loader.getOperatingLecture().size());
        this.subjects = new Int2IntLinkedOpenHashMap(loader.getOperatingSubject().size());
        this.lessons = new Int2IntLinkedOpenHashMap(loader.getLessons().size());
    }

    @NotNull public Int2IntMap getActiveDays()
    {
        return this.active_days;
    }

    @NotNull public Int2IntMap getActivePeriods()
    {
        return this.active_periods;
    }

    @NotNull public Int2IntMap getClasses()
    {
        return this.classes;
    }

    @NotNull public Int2IntMap getClassrooms()
    {
        return this.classrooms;
    }

    @NotNull public Int2IntMap getLecturers()
    {
        return this.lecturers;
    }

    @NotNull public Int2IntMap getSubjects()
    {
        return this.subjects;
    }

    @NotNull public Int2IntMap getLessons()
    {
        return this.lessons;
    }

    public int getSubject(int id)
    {
        return this.subjects.get(id);
    }

    public int getLecture(int id)
    {
        return this.lecturers.get(id);
    }

    public int getKlass(int id)
    {
        return this.classes.get(id);
    }

    public int getDay(int id)
    {
        return this.active_days.get(id);
    }

    public int getPeriod(int id)
    {
        return this.active_periods.get(id);
    }

    public int getClassroom(int id)
    {
        return this.classrooms.get(id);
    }

    public int getLesson(int id)
    {
        return this.lessons.get(id);
    }
}
