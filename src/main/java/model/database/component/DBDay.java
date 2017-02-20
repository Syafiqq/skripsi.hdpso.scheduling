package model.database.component;

import model.database.component.metadata.DBMDay;
import model.database.component.metadata.DBMSchool;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.component> created by :
 * Name         : syafiq
 * Date / Time  : 23 October 2016, 10:08 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings(value = {"WeakerAccess", "unused"}) public class DBDay extends DBMDay
{
    @NotNull private final DBMSchool school;

    public DBDay(int id, int position, String name, String nickname, @NotNull DBMSchool school)
    {
        super(id, position, name, nickname);
        this.school = school;
    }

    @NotNull public DBMSchool getSchool()
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
