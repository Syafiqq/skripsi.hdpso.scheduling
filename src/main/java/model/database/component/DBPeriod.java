package model.database.component;

import java.time.LocalTime;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.component> created by :
 * Name         : syafiq
 * Date / Time  : 23 October 2016, 10:09 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings(value = {"WeakerAccess", "unused"}) public class DBPeriod
{
    private final          int       id;
    @NotNull private final DBSchool  school;
    private                int       position;
    private                String    name;
    private                String    nickname;
    private                LocalTime start_lesson;
    private                LocalTime end_lesson;

    public DBPeriod(int id, int position, String name, String nickname, String start_lesson, String end_lesson, @NotNull DBSchool school)
    {
        this.id = id;
        this.position = position;
        this.name = name;
        this.nickname = nickname;
        this.start_lesson = LocalTime.parse(start_lesson);
        this.end_lesson = LocalTime.parse(end_lesson);
        this.school = school;
    }

    @Override public String toString()
    {
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

    public int getId()
    {
        return this.id;
    }

    @NotNull public DBSchool getSchool()
    {
        return this.school;
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

    public LocalTime getStart()
    {
        return this.start_lesson;
    }

    public void setStartLesson(String start)
    {
        this.start_lesson = LocalTime.parse(start);
    }

    public LocalTime getEnd()
    {
        return this.end_lesson;
    }

    public void setEndLesson(String end)
    {
        this.end_lesson = LocalTime.parse(end);
    }
}
