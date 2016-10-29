package model.database.component;

import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.component> created by : 
 * Name         : syafiq
 * Date / Time  : 25 October 2016, 4:18 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class DBTimeOff
{
    private final          int            id;
    @NotNull private final DBDay          day;
    @NotNull private final DBPeriod       period;
    @NotNull private final DBAvailability availability;

    public DBTimeOff(int id, @NotNull DBDay day, @NotNull DBPeriod period, @NotNull DBAvailability availability)
    {
        this.id = id;
        this.day = day;
        this.period = period;
        this.availability = availability;
    }

    public int getId()
    {
        return this.id;
    }

    public DBDay getDay()
    {
        return this.day;
    }

    public DBPeriod getPeriod()
    {
        return this.period;
    }

    public DBAvailability getAvailability()
    {
        return this.availability;
    }
}
