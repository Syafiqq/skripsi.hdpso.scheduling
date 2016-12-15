package model.method.pso.hdpso.component;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.component> created by : 
 * Name         : syafiq
 * Date / Time  : 15 November 2016, 4:27 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class TVelocity_ReverseVelocity_Benchmark
{
    private Velocity destination;

    @Before
    public void initialize()
    {
        final int SIZE = 30000000;
        this.destination = new Velocity(SIZE);
        for(int ci = -1; ++ci < SIZE; )
        {
            destination.set(ci, ci);
        }
        System.out.println(destination.size());
    }

    @Test
    public void destination_lt_source_no_reduction()
    {

        Long time = System.currentTimeMillis();
        Velocity.reverse(this.destination);
        System.out.println(System.currentTimeMillis() - time);
    }

    @After
    public void finish()
    {
        System.out.println(this.destination.size());
    }
}
