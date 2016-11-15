package model.method.pso.hdpso.component;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.component> created by : 
 * Name         : syafiq
 * Date / Time  : 15 November 2016, 3:22 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("unused") public class TVelocity_AdditionVelocity_Benchmark
{
    private Velocity destination;
    private Velocity source;

    @Before
    public void initialize()
    {
        final int SIZE   = 30000000;
        final int SEED   = 100;
        Random    random = ThreadLocalRandom.current();
        this.destination = new Velocity(SIZE);
        for(int ci = -1, cis = SIZE / 2; ++ci < cis; )
        {
            destination.set(ci, ci);
        }
        System.out.println(destination.size());
        this.source = new Velocity(SIZE);
        Velocity.cloneVelocity(this.destination, source);
        System.out.println(source.size());
    }

    @Test
    public void destination_lt_source_no_reduction()
    {

        Long time = System.currentTimeMillis();
        Velocity.addition(this.destination, this.source);
        System.out.println(System.currentTimeMillis() - time);
    }

    @After
    public void finish()
    {
        System.out.println(this.destination.size());
    }
}
