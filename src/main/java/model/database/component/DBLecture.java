package model.database.component;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import model.database.component.metadata.DBMLecture;
import model.database.component.metadata.DBMSchool;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.component> created by : 
 * Name         : syafiq
 * Date / Time  : 23 October 2016, 7:55 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings(value = {"WeakerAccess", "unused"}) public class DBLecture extends DBMLecture
{
    @NotNull private final DBMSchool          school;
    @NotNull private       DBTimeOffContainer timeoff;

    public DBLecture(int id, String name, @NotNull DBMSchool school)
    {
        super(id, name);
        this.school = school;
        this.timeoff = DBLecture.TimeOffContainer.generateNew(this, this.school);
    }

    @NotNull public DBMSchool getSchool()
    {
        return this.school;
    }

    @NotNull public DBTimeOffContainer getTimeoff()
    {
        return this.timeoff;
    }

    public void setTimeoff(@NotNull DBLecture.TimeOffContainer timeoff)
    {
        this.timeoff = timeoff;
    }

    @Override public String toString()
    {
        return new ToStringBuilder(this)
                .append("id", super.id)
                .append("school", school.getId())
                .append("name", super.name)
                .toString();
    }

    @SuppressWarnings({"unused", "WeakerAccess"}) public static class TimeOffContainer extends DBTimeOffContainer<DBMLecture>
    {
        protected TimeOffContainer(@NotNull DBMLecture domain, @NotNull ObjectList<ObjectList<DBTimeOff>> availabilities)
        {
            super(domain, availabilities);
        }

        public static TimeOffContainer generateNew(@NotNull DBMLecture domain, @NotNull DBMSchool school)
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
