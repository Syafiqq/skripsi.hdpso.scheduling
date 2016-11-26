package model.method.pso.hdpso.component;

import model.util.list.IntHList;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.component> created by : 
 * Name         : syafiq
 * Date / Time  : 26 November 2016, 7:41 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("WeakerAccess") public class ScheduleContainer extends IntHList
{
    private int timeSize;

    public ScheduleContainer(int initialCapacity)
    {
        super(initialCapacity);
        this.timeSize = 0;
    }

    @Contract("_ -> !null") public static ScheduleContainer newInstance(@NotNull final ScheduleContainer container)
    {
        return new ScheduleContainer(container.list.length);
    }

    public void addSchedule(final int k, final int sks)
    {
        super.add(k);
        this.timeSize += sks;
    }

    public int getSizeSKS()
    {
        return this.timeSize;
    }

    @Override public void reset()
    {
        super.reset();
        this.timeSize = 0;
    }
}

