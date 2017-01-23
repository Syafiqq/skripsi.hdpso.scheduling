package model.database.component;

import model.database.component.metadata.DBMSchool;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.component> created by :
 * Name         : syafiq
 * Date / Time  : 23 October 2016, 10:06 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings(value = {"WeakerAccess", "unused"}) public class DBSchool extends DBMSchool
{
    private       String     nickname;
    private       String     address;

    public DBSchool(int id, String name, String nickname, String address, String academic_year, int semester, int active_period, int active_day)
    {
        super(id, name, academic_year, semester, active_period, active_day);
        this.nickname = nickname;
        this.address = address;
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
