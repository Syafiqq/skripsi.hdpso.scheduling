package model.database.component.metadata;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 23 January 2017, 10:25 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import org.apache.commons.lang3.builder.ToStringBuilder;

public class DBMSubject {
    protected final int id;
    protected String name;
    protected String subject_id;

    public DBMSubject(int id, String name, String subject_id) {
        this.id = id;
        this.name = name;
        this.subject_id = subject_id;
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

    public String getSubjectId() {
        return this.subject_id;
    }

    public void setSubjectId(String subject_id) {
        this.subject_id = subject_id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("subject_id", subject_id)
                .toString();
    }
}
