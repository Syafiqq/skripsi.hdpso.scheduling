package model.database.component;

import it.unimi.dsi.fastutil.objects.ObjectList;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.component> created by : 
 * Name         : syafiq
 * Date / Time  : 23 October 2016, 6:59 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings(value = {"WeakerAccess", "unused"}) public abstract class DBTimeOffContainer<Domain>
{
    @NotNull private final Domain                            domain;
    @NotNull private final ObjectList<ObjectList<DBTimeOff>> availabilities;

    protected DBTimeOffContainer(@NotNull Domain domain, @NotNull ObjectList<ObjectList<DBTimeOff>> availabilities)
    {
        this.domain = domain;
        this.availabilities = availabilities;
    }

    @NotNull public Domain getDomain()
    {
        return this.domain;
    }

    @NotNull public ObjectList<ObjectList<DBTimeOff>> getAvailabilities()
    {
        return this.availabilities;
    }

    @Override public String toString()
    {
        final StringBuilder sb = new StringBuilder();
        for(final ObjectList<DBTimeOff> timeOffDay : this.availabilities)
        {
            for(final DBTimeOff timeOff : timeOffDay)
            {
                try
                {
                    sb.append(timeOff.getAvailability().getId());
                }
                catch(NullPointerException ignored)
                {
                    sb.append((String) null);
                }
                sb.append('\t');
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
