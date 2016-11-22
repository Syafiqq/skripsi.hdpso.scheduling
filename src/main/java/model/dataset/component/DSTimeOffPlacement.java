package model.dataset.component;

import java.util.Arrays;
import model.util.list.IntHList;
import model.util.list.OneIntHList;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.dataset.component> created by : 
 * Name         : syafiq
 * Date / Time  : 17 November 2016, 9:09 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("unused") public class DSTimeOffPlacement
{
    /*
     * Global Important Variable
     *
     * @param placement = Time-off
     */
    private OneIntHList[][] placement;


    /**
     * @param active_day_size    = Active Day Size
     * @param active_period_size = Active Period Size
     * @param classroom_size     = Classroom Size
     */
    public DSTimeOffPlacement(int active_day_size, int active_period_size, int classroom_size)
    {
        this.placement = new OneIntHList[active_day_size][active_period_size];
        for(int counter_day = -1; ++counter_day < active_day_size; )
        {
            for(int counter_period = -1; ++counter_period < active_period_size; )
            {
                this.placement[counter_day][counter_period] = new OneIntHList(classroom_size);
            }
        }
    }

    /**
     * Print Time-off
     */
    public void printTimeoff()
    {
        for(int i = -1, is = this.placement.length; ++i < is; )
        {
            System.out.println(Arrays.toString(this.placement[i]));
        }
    }

    public void resetPlacement()
    {
        for(@NotNull final IntHList[] day : this.placement)
        {
            for(@NotNull final IntHList period : day)
            {
                period.reset();
            }
        }
    }

    @Contract(pure = true) @NotNull public IntHList get(int day_index, int period_index)
    {
        return this.placement[day_index][period_index];
    }

    public void reset(int day_index, int period_index)
    {
        this.placement[day_index][period_index].reset();
    }

    public boolean putPlacementIfAbsent(int day_index, int period_index, int lesson)
    {
        this.placement[day_index][period_index].add(lesson);
        return this.placement[day_index][period_index].isExactlyOneEntry();
    }

    public boolean isNotTheSameDay(int day, int[] link)
    {
        for(@NotNull final OneIntHList period : this.placement[day])
        {
            for(final int lesson : link)
            {
                for(int c_lesson = -1, c_lesson_s = period.size(); ++c_lesson <= c_lesson_s; )
                {
                    if(period.isSameEntry(c_lesson, lesson))
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
