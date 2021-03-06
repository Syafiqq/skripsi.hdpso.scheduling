package model.database.component.metadata;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 23 January 2017, 6:04 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import model.database.component.DBSemester;

@SuppressWarnings(value = {"WeakerAccess", "unused"}) public class DBMSchool
{
    protected final int        id;
    protected        String     name;
    protected        String     academic_year;
    protected DBSemester semester;
    protected        int        active_period;
    protected        int        active_day;

    public DBMSchool(int id, String name, String academic_year, int semester, int active_period, int active_day)
    {
        this.id = id;
        this.name = name;
        this.academic_year = academic_year;
        this.semester = DBSemester.getSemester(semester);
        this.active_day = active_day;
        this.active_period = active_period;
    }

    public int getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAcademicYear()
    {
        return this.academic_year;
    }

    public void setAcademicYear(String academic_year)
    {
        this.academic_year = academic_year;
    }

    public DBSemester getSemester()
    {
        return this.semester;
    }

    public void setSemester(int semester)
    {
        this.semester = DBSemester.getSemester(semester);
    }

    public int getActivePeriod()
    {
        return this.active_period;
    }

    public void setActivePeriod(int active_period)
    {
        this.active_period = active_period;
    }

    public int getActiveDay()
    {
        return this.active_day;
    }

    public void setActiveDay(int active_day)
    {
        this.active_day = active_day;
    }

    @Override public String toString()
    {
        return "DBSchool{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", academic_year='" + academic_year + '\'' +
                ", semester=" + semester.describe() +
                ", active_period=" + active_period +
                ", active_day=" + active_day +
                '}';
    }
}
