package model.dataset.component;

import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import model.database.component.DBTimeOff;
import model.database.component.DBTimeOffContainer;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.dataset.component> created by : 
 * Name         : syafiq
 * Date / Time  : 02 November 2016, 5:24 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings({"WeakerAccess", "unchecked", "unused"}) public class DSTimeOff
{
    @NotNull private final double[][] timeoff;

    public DSTimeOff(@NotNull double[][] timeoff)
    {
        this.timeoff = timeoff;
    }

    @NotNull public static DSTimeOff newInstance(@NotNull final DBTimeOffContainer db_timeoff)
    {
        final @NotNull ObjectList                       availabilities = db_timeoff.getAvailabilities();
        final @NotNull double                           timeoff[][]    = new double[availabilities.size()][];
        final ObjectListIterator<ObjectList<DBTimeOff>> dayIt          = availabilities.iterator();
        for(int c_day = 0; dayIt.hasNext(); ++c_day)
        {
            final ObjectList<DBTimeOff>         period   = dayIt.next();
            final ObjectListIterator<DBTimeOff> periodIt = period.iterator();

            final double[] periods = timeoff[c_day] = new double[period.size()];

            for(int c_period = 0; periodIt.hasNext(); ++c_period)
            {
                periods[c_period] = periodIt.next().getAvailability().getValue();
            }
        }

        return new DSTimeOff(timeoff);
    }

    public void set(int day, int period, double value)
    {
        this.timeoff[day][period] = value;
    }

    public double get(int day, int period)
    {
        return this.timeoff[day][period];
    }

    @NotNull public double[][] getTimeoff()
    {
        return this.timeoff;
    }
}
