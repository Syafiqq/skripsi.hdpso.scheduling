package model.method.pso.hdpso.component;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.component> created by : 
 * Name         : syafiq
 * Date / Time  : 15 November 2016, 3:22 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class TVelocity_AdditionVelocity_Benchmark
{
    @Test
    public void destination_lt_source_no_reduction()
    {
        final int SIZE        = 10000000;
        final int SEED        = 100;
        Random    random      = ThreadLocalRandom.current();
        Velocity  destination = new Velocity(SIZE);
        for(int ci = -1, cis = SIZE / 2; ++ci < cis; )
        {
            destination.set(random.nextInt(SEED), random.nextInt(SEED));
        }
        Velocity source = new Velocity(SIZE);
        for(int ci = -1, cis = SIZE / 2; ++ci < cis; )
        {
            source.set(random.nextInt(SEED), random.nextInt(SEED));
        }
        System.out.println(source.size());
        System.out.println(destination.size());
        Velocity.additionVelocity(destination, source);
        System.out.println(destination.size());
    }
}
