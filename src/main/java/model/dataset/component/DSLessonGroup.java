package model.dataset.component;

import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.dataset.component> created by : 
 * Name         : syafiq
 * Date / Time  : 08 November 2016, 9:40 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("unused") public class DSLessonGroup
{
    @NotNull private final int[] lessons;
    @NotNull private final int[] classrooms;
    @NotNull private final int[] time_distribution;

    public DSLessonGroup(@NotNull int[] lessons, @NotNull int[] classrooms, @NotNull int[] time_distribution)
    {
        this.lessons = lessons;
        this.classrooms = classrooms;
        this.time_distribution = time_distribution;
    }

    @NotNull public int[] getLessons()
    {
        return this.lessons;
    }

    @NotNull public int[] getClassrooms()
    {
        return this.classrooms;
    }

    @NotNull public int[] getTimeDistributions()
    {
        return this.time_distribution;
    }

    public int getClassroom(final int index)
    {
        return this.classrooms[index];
    }

    public int getLesson(final int index)
    {
        return this.classrooms[index];
    }

    public int getTimeDistribution(final int index)
    {
        return this.time_distribution[index];
    }

    public int getLessonsLength()
    {
        return this.lessons.length;
    }

    public int getClassroomTotal()
    {
        return this.classrooms.length;
    }

    public int getTimeDistributionLength()
    {
        return this.time_distribution.length;
    }
}
