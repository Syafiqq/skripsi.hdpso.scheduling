package model.method.pso.hdpso.component;

import java.util.Arrays;
import model.util.list.IntHList;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.component> created by : 
 * Name         : syafiq
 * Date / Time  : 26 November 2016, 9:07 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class TScheduleContainer
{
    @Test public void test_addAll()
    {
        IntHList a = new IntHList(10);
        a.add(10);
        a.add(20);
        a.add(30);

        IntHList b = new IntHList(10);
        b.add(40);
        b.add(50);
        b.add(60);

        ScheduleContainer sc = new ScheduleContainer(20);
        sc.addSchedule(4, 6);
        sc.addSchedule(5, 6);
        ScheduleContainer sc1 = new ScheduleContainer(20);
        sc1.addSchedule(7, 6);
        sc1.addSchedule(8, 6);
        System.out.println(sc);
        System.out.println(Arrays.toString(sc.toArray()));
        sc.addAll(a);
        System.out.println(sc);
        System.out.println(Arrays.toString(sc.toArray()));
        sc.addAll(b);
        System.out.println(sc);
        System.out.println(Arrays.toString(sc.toArray()));
        sc.addAll(sc1);
        System.out.println(sc);
        System.out.println(Arrays.toString(sc.toArray()));
    }
}
