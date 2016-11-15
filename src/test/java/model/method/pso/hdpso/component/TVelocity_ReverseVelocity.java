package model.method.pso.hdpso.component;

import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.component> created by : 
 * Name         : syafiq
 * Date / Time  : 15 November 2016, 4:22 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class TVelocity_ReverseVelocity
{
    @Test public void test_reverse_odd()
    {
        final int SIZE   = 5;
        Velocity  source = new Velocity(SIZE);
        for(int ci = -1; ++ci < SIZE; )
        {
            source.set(ci, ci);
        }
        System.out.println(source);
        System.out.println(source.size());
        Velocity.reverseVelocity(source);
        System.out.println(source);
        System.out.println(source.size());
    }

    @Test public void test_reverse_even()
    {
        final int SIZE   = 6;
        Velocity  source = new Velocity(SIZE);
        for(int ci = -1; ++ci < SIZE; )
        {
            source.set(ci, ci);
        }
        System.out.println(source);
        System.out.println(source.size());
        Velocity.reverseVelocity(source);
        System.out.println(source);
        System.out.println(source.size());
    }
}
