package model.database.component;

import model.database.component.metadata.DBMDay;
import model.database.component.metadata.DBMPeriod;
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
    @NotNull private final DBMDay         day;
    @NotNull private final DBMPeriod      period;
    @NotNull private final DBAvailability availability;

    public DBTimeOff(int id, @NotNull DBMDay day, @NotNull DBMPeriod period, @NotNull DBAvailability availability)
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

    @NotNull public DBMDay getDay()
    {
        return this.day;
    }

    @NotNull public DBMPeriod getPeriod()
    {
        return this.period;
    }

    @NotNull public DBAvailability getAvailability()
    {
        return this.availability;
    }
}
