package model.database.component;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.component> created by :
 * Name         : syafiq
 * Date / Time  : 23 October 2016, 10:06 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings(value = {"WeakerAccess", "unused"}) public class DBSchool
{
    private final int        id;
    private       String     name;
    private       String     nickname;
    private       String     address;
    private       String     academic_year;
    private       DBSemester semester;
    private       int        active_period;
    private       int        active_day;

    public DBSchool(int id, String name, String nickname, String address, String academic_year, int semester, int active_period, int active_day)
    {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.address = address;
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

    public String getNickname()
    {
        return this.nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public String getAddress()
    {
        return this.address;
    }

    public void setAddress(String address)
    {
        this.address = address;
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
                ", nickname='" + nickname + '\'' +
                ", address='" + address + '\'' +
                ", academic_year='" + academic_year + '\'' +
                ", semester=" + semester.describe() +
                ", active_period=" + active_period +
                ", active_day=" + active_day +
                '}';
    }
}
