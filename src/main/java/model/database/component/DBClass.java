package model.database.component;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import model.database.component.metadata.DBMClass;
import model.database.component.metadata.DBMSchool;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.component> created by : 
 * Name         : syafiq
 * Date / Time  : 23 October 2016, 6:59 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings(value = {"WeakerAccess", "unused"}) public class DBClass extends DBMClass
{
    @NotNull private final DBMSchool school;
    @NotNull private       DBTimeOffContainer timeoff;

    public DBClass(int id, String name, @NotNull DBMSchool school)
    {
        super(id, name);
        this.school = school;
        this.timeoff = DBClass.TimeOffContainer.generateNew(this, this.school);
    }

    @NotNull public DBMSchool getSchool()
    {
        return this.school;
    }

    @NotNull public DBTimeOffContainer getTimeoff()
    {
        return this.timeoff;
    }

    public void setTimeoff(@NotNull DBClass.TimeOffContainer timeoff)
    {
        this.timeoff = timeoff;
    }

    @Override public String toString()
    {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("school", school.getId())
                .append("name", name)
                .toString();
    }

    @SuppressWarnings(value = {"WeakerAccess", "unused"}) public static class TimeOffContainer extends DBTimeOffContainer<DBMClass>
    {
        protected TimeOffContainer(@NotNull DBMClass domain, @NotNull ObjectList<ObjectList<DBTimeOff>> availabilities)
        {
            super(domain, availabilities);
        }

        public static TimeOffContainer generateNew(@NotNull DBMClass domain, @NotNull DBMSchool school)
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
