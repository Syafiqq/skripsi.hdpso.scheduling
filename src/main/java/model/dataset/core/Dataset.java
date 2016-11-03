package model.dataset.core;

import model.database.loader.DBProblemLoader;
import model.dataset.component.DSLesson;
import model.dataset.component.DSTimeOff;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.dataset.core> created by : 
 * Name         : syafiq
 * Date / Time  : 02 November 2016, 8:58 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings({"FieldCanBeLocal", "unused"}) public class Dataset
{
    private final @NotNull int[]       days;
    private final @NotNull int[]       periods;
    private final @NotNull DSTimeOff[] classes;
    private final @NotNull DSTimeOff[] classrooms;
    private final @NotNull DSTimeOff[] lecturers;
    private final @NotNull DSTimeOff[] subjects;
    private final @NotNull DSLesson[]  lessons;

    public Dataset(final @NotNull DBProblemLoader loader)
    {
        this.days = new int[loader.getDays().size()];
        this.periods = new int[loader.getPeriods().size()];
        this.classes = new DSTimeOff[loader.getOperatingClass().size()];
        this.classrooms = new DSTimeOff[loader.getOperatingClassroom().size()];
        this.lecturers = new DSTimeOff[loader.getOperatingLecture().size()];
        this.subjects = new DSTimeOff[loader.getOperatingSubject().size()];
        this.lessons = new DSLesson[(this.days.length * this.periods.length * loader.getOperatingClassroom().size()) - (loader.getLessons().size() + loader.getComplexLessonSize())];
    }

    public @NotNull int[] getDays()
    {
        return this.days;
    }

    public @NotNull int[] getPeriods()
    {
        return this.periods;
    }

    @NotNull public DSTimeOff[] getClasses()
    {
        return this.classes;
    }

    @NotNull public DSTimeOff[] getClassrooms()
    {
        return this.classrooms;
    }

    @NotNull public DSTimeOff[] getLecturers()
    {
        return this.lecturers;
    }

    @NotNull public DSTimeOff[] getSubjects()
    {
        return this.subjects;
    }
}
