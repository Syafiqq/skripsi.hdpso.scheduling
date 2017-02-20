package model.database.component.metadata;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 23 January 2017, 6:56 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import java.time.LocalTime;

@SuppressWarnings(value = {"WeakerAccess", "unused"})
public class DBMPeriod {
    protected final int id;
    protected int position;
    protected String name;
    protected String nickname;
    protected LocalTime start_lesson;
    protected LocalTime end_lesson;

    public DBMPeriod(int id, int position, String name, String nickname, String start_lesson, String end_lesson) {
        this.id = id;
        this.position = position;
        this.name = name;
        this.nickname = nickname;
        this.start_lesson = LocalTime.parse(start_lesson);
        this.end_lesson = LocalTime.parse(end_lesson);
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
                '}';
    }

    public int getId() {
        return this.id;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public LocalTime getStart() {
        return this.start_lesson;
    }

    public void setStartLesson(String start) {
        this.start_lesson = LocalTime.parse(start);
    }

    public LocalTime getEnd() {
        return this.end_lesson;
    }

    public void setEndLesson(String end) {
        this.end_lesson = LocalTime.parse(end);
    }
}
