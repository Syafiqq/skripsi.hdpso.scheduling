package model.database.component;

import model.database.component.metadata.DBMPeriod;
import model.database.component.metadata.DBMSchool;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.component> created by :
 * Name         : syafiq
 * Date / Time  : 23 October 2016, 10:09 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings(value = {"WeakerAccess", "unused"})
public class DBPeriod extends DBMPeriod {
    @NotNull
    private final DBMSchool school;

    public DBPeriod(int id, int position, String name, String nickname, String start_lesson, String end_lesson, @NotNull DBMSchool school) {
        super(id, position, name, nickname, start_lesson, end_lesson);
        this.school = school;
    }

    @Override
    public String toString() {
        return "DBPeriod{" +
                "id=" + id +
                ", position=" + position +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", start_lesson=" + start_lesson +
                ", end_lesson=" + end_lesson +
                ", school=" + school.getId() +
                '}';
    }


    @NotNull
    public DBMSchool getSchool() {
        return this.school;
    }
}
