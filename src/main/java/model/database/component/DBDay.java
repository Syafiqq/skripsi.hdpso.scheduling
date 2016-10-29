package model.database.component;

import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.component> created by :
 * Name         : syafiq
 * Date / Time  : 23 October 2016, 10:08 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings(value = {"WeakerAccess", "unused"}) public class DBDay
{
    private final          int      id;
    @NotNull private final DBSchool school;
    private                int      position;
    private                String   name;
    private                String   nickname;

    public DBDay(int id, int position, String name, String nickname, @NotNull DBSchool school)
    {
        this.id = id;
        this.position = position;
        this.name = name;
        this.nickname = nickname;
        this.school = school;
    }

    public int getId()
    {
        return this.id;
    }

    public int getPosition()
    {
        return this.position;
    }

    public void setPosition(int position)
    {
        this.position = position;
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

    @NotNull public DBSchool getSchool()
    {
        return this.school;
    }

    @Override public String toString()
    {
        return "DBDay{" +
                "id=" + id +
                ", position=" + position +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", school=" + school.getId() +
                '}';
    }
}
