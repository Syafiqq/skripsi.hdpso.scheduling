package model.method.pso.hdpso.component;

import it.unimi.dsi.fastutil.ints.IntArrays;
import java.util.Arrays;
import java.util.Random;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.component> created by : 
 * Name         : syafiq
 * Date / Time  : 22 November 2016, 10:19 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings({"unused", "FieldCanBeLocal", "WeakerAccess"}) public class RepairProperty
{
    @NotNull private final boolean[][] absent;
    @NotNull private final int[][]     index;
    @NotNull private final int[]       shuffled_day;
    private                int         shuffle_cycle;

    public RepairProperty(int classroom_length, @NotNull int[] day)
    {
        this.absent = new boolean[classroom_length][day.length];
        this.index = new int[classroom_length][day.length];
        this.shuffled_day = new int[day.length];
        System.arraycopy(day, 0, this.shuffled_day, 0, day.length);
        this.shuffle_cycle = -1;
    }

    public void resetAbsent()
    {
        for(boolean[] absent : this.absent)
        {
            Arrays.fill(absent, false);
        }
    }

    public void set(int classroom, int day, int c_lesson)
    {
        this.absent[classroom][day] = true;
        this.index[classroom][day] = c_lesson;
    }

    public @NotNull boolean[][] getAbsent()
    {
        return this.absent;
    }

    public int getPosition(final int classroom, final int day)
    {
        return this.index[classroom][day];
    }

    public void decrementIndex(final int classroom, final int day) throws ArrayIndexOutOfBoundsException
    {
        --this.index[classroom][day];
    }

    public @NotNull int[] getShuffledDay(@NotNull final Random random)
    {
        if(++this.shuffle_cycle > 5)
        {
            IntArrays.shuffle(this.shuffled_day, random);
            this.shuffle_cycle = -1;
        }
        return this.shuffled_day;
    }
}
