package model.custom.it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.ints.IntArrayList;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.custom.it.unimi.dsi.fastutil.ints> created by : 
 * Name         : syafiq
 * Date / Time  : 23 November 2016, 4:50 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class ScheduleContainer extends IntArrayList
{
    private int timeSize;

    public ScheduleContainer(int initialCapacity)
    {
        super(initialCapacity);
        this.timeSize = 0;
    }

    public boolean addSchedule(final int k, final int sks)
    {
        final boolean bool = super.add(k);
        if(bool)
        {
            this.timeSize += sks;
            return true;
        }
        else
        {
            return false;
        }
    }

    public int getSizeSKS()
    {
        return this.timeSize;
    }
}
