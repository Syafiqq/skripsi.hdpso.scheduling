package model.database.component.metadata;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 25 January 2017, 8:53 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import org.apache.commons.lang3.builder.ToStringBuilder;

@SuppressWarnings("unused")
public class DBMLecture {
    protected final int id;
    protected String name;

    public DBMLecture(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .toString();
    }
}