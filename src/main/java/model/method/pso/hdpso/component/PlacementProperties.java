package model.method.pso.hdpso.component;

import model.dataset.component.DSTimeOffPlacement;
import model.util.list.IntHList;
import org.apache.commons.math3.util.FastMath;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.component> created by : 
 * Name         : syafiq
 * Date / Time  : 17 November 2016, 11:20 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"}) public class PlacementProperties
{
    /*
    * @param lecture_placement
    *   list of all lecture placement
    * @param lecture_placement
    *   list of all class placement
    * @param lecture fill
    *   record all accessed lecture placement to reduce reset operator of lecture placement
    * @param class fill
    *   record all accessed class placement to reduce reset operator of class placement
    * */

    private final DSTimeOffPlacement[] lecture_placement;
    private final DSTimeOffPlacement[] class_placement;
    private final IntHList             lecture_fill;
    private final IntHList             class_fill;
    private final int                  classroom;
    private final int                  day;
    private final int                  period;

    public PlacementProperties(int lecture_total, int class_total, int classroom_total, int day_total, int period_total, int active_classroom)
    {
        this.lecture_placement = new DSTimeOffPlacement[lecture_total];
        this.class_placement = new DSTimeOffPlacement[class_total];
        for(int counter_lecture_placement = -1, lecture_placement_size = this.lecture_placement.length; ++counter_lecture_placement < lecture_placement_size; )
        {
            this.lecture_placement[counter_lecture_placement] = new DSTimeOffPlacement(day_total, period_total, classroom_total);
        }
        for(int counter_class_placement = -1, class_placement_size = this.class_placement.length; ++counter_class_placement < class_placement_size; )
        {
            this.class_placement[counter_class_placement] = new DSTimeOffPlacement(day_total, period_total, classroom_total);
        }
        this.lecture_fill = new IntHList(active_classroom * day_total * period_total);
        this.class_fill = new IntHList(active_classroom * day_total * period_total);
        this.classroom = active_classroom;
        this.period = period_total;
        this.day = day_total;
    }

    public void resetPlacement()
    {
        final int lecture_fill_size = this.lecture_fill.size();
        final int class_fill_size   = this.class_fill.size();
        for(int c_fill = -1, c_fill_s = FastMath.min(lecture_fill_size, class_fill_size); ++c_fill < c_fill_s; )
        {
            for(int counter_day = -1; ++counter_day < this.day; )
            {
                for(int counter_period = -1; ++counter_period < this.period; )
                {
                    this.lecture_placement[this.lecture_fill.get(c_fill)].reset(counter_day, counter_period);
                    this.class_placement[this.class_fill.get(c_fill)].reset(counter_day, counter_period);
                }
            }
        }

        if(lecture_fill_size < class_fill_size)
        {
            for(int c_fill = lecture_fill_size; ++c_fill < class_fill_size; )
            {
                for(int counter_day = -1; ++counter_day < this.day; )
                {
                    for(int counter_period = -1; ++counter_period < this.period; )
                    {
                        this.class_placement[this.class_fill.get(c_fill)].reset(counter_day, counter_period);
                    }
                }
            }
        }
        else
        {
            for(int c_fill = class_fill_size; ++c_fill < lecture_fill_size; )
            {
                for(int counter_day = -1; ++counter_day < this.day; )
                {
                    for(int counter_period = -1; ++counter_period < this.period; )
                    {
                        this.lecture_placement[this.lecture_fill.get(c_fill)].reset(counter_day, counter_period);
                    }
                }
            }
        }
        this.lecture_fill.reset();
        this.class_fill.reset();
    }
}
