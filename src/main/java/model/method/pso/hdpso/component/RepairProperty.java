package model.method.pso.hdpso.component;

import java.util.Arrays;
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

    public RepairProperty(int classroom_length, int day_length)
    {
        this.absent = new boolean[classroom_length][day_length];
        this.index = new int[classroom_length][day_length];
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
        --index[classroom][day];
    }
}
