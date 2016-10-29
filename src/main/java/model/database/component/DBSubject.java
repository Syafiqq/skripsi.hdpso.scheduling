package model.database.component;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.component> created by : 
 * Name         : syafiq
 * Date / Time  : 23 October 2016, 8:02 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings(value = {"unused", "WeakerAccess"}) public class DBSubject
{
    private final          int                id;
    @NotNull private final DBSchool           school;
    private                String             name;
    private                String             subject_id;
    @NotNull private       DBTimeOffContainer timeoff;

    public DBSubject(int id, String name, String subject_id, @NotNull DBSchool school)
    {
        this.id = id;
        this.name = name;
        this.subject_id = subject_id;
        this.school = school;
        this.timeoff = DBSubject.TimeOffContainer.generateNew(this, this.school);
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

    public String getSubjectId()
    {
        return this.subject_id;
    }

    public void setSubjectId(String subject_id)
    {
        this.subject_id = subject_id;
    }

    @NotNull public DBSchool getSchool()
    {
        return this.school;
    }

    @NotNull public DBTimeOffContainer getTimeoff()
    {
        return this.timeoff;
    }

    public void setTimeoff(@NotNull DBSubject.TimeOffContainer timeoff)
    {
        this.timeoff = timeoff;
    }

    @Override public String toString()
    {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("school", school.getId())
                .append("name", name)
                .append("subject_id", subject_id)
                .toString();
    }

    @SuppressWarnings({"unused", "WeakerAccess"}) public static class TimeOffContainer extends DBTimeOffContainer<DBSubject>
    {
        protected TimeOffContainer(@NotNull DBSubject domain, @NotNull ObjectList<ObjectList<DBTimeOff>> availabilities)
        {
            super(domain, availabilities);
        }

        public static TimeOffContainer generateNew(@NotNull DBSubject domain, @NotNull DBSchool school)
        {
            final ObjectList<ObjectList<DBTimeOff>> availabilities = new ObjectArrayList<>(school.getActiveDay());
            for(int day_index = -1, day_size = school.getActiveDay(), period_size = school.getActivePeriod(); ++day_index < day_size; )
            {
                availabilities.add(new ObjectArrayList<>(period_size));
                final ObjectList<DBTimeOff> tmp_timeOff = availabilities.get(day_index);
                for(int period_index = -1; ++period_index < period_size; )
                {
                    tmp_timeOff.add(null);
                }
            }
            return new TimeOffContainer(domain, availabilities);
        }
    }
}
