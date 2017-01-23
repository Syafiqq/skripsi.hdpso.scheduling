package model.database.component;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import model.database.component.metadata.DBMSchool;
import model.database.component.metadata.DBMSubject;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.component> created by : 
 * Name         : syafiq
 * Date / Time  : 23 October 2016, 8:02 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings(value = {"unused", "WeakerAccess"}) public class DBSubject extends DBMSubject
{
    @NotNull private final DBMSchool           school;
    @NotNull private       DBTimeOffContainer timeoff;

    public DBSubject(int id, String name, String subject_id, @NotNull DBMSchool school)
    {
        super(id, name, subject_id);
        this.school = school;
        this.timeoff = DBSubject.TimeOffContainer.generateNew(this, this.school);
    }

    @NotNull public DBMSchool getSchool()
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

    @SuppressWarnings({"unused", "WeakerAccess"}) public static class TimeOffContainer extends DBTimeOffContainer<DBMSubject>
    {
        protected TimeOffContainer(@NotNull DBMSubject domain, @NotNull ObjectList<ObjectList<DBTimeOff>> availabilities)
        {
            super(domain, availabilities);
        }

        public static TimeOffContainer generateNew(@NotNull DBMSubject domain, @NotNull DBMSchool school)
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
