package model.database.component.metadata;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 23 January 2017, 6:48 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

@SuppressWarnings(value = {"WeakerAccess", "unused"})
public class DBMDay {
    protected final int id;
    protected int position;
    protected String name;
    protected String nickname;

    public DBMDay(int id, int position, String name, String nickname) {
        this.id = id;
        this.position = position;
        this.name = name;
        this.nickname = nickname;
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

    @Override
    public String toString() {
        return "DBDay{" +
                "id=" + id +
                ", position=" + position +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
