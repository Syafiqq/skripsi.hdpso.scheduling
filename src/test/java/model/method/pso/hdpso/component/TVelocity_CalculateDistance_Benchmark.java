package model.method.pso.hdpso.component;

import it.unimi.dsi.fastutil.ints.IntArrays;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import java.util.Arrays;
import java.util.Random;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.component> created by : 
 * Name         : syafiq
 * Date / Time  : 15 November 2016, 9:20 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class TVelocity_CalculateDistance_Benchmark
{
    private static final int COMPONENT_SIZE = 1000000;
    private static final int ITERATION_SIZE = 1000;
    private int[]    source;
    private int[]    destination;
    private Velocity vel;
    private Position p_source;
    private Position p_destination;
    private Position p_mimic;
    private Position p_temp;

    @Before
    public void initialize()
    {
        this.source = new int[COMPONENT_SIZE];
        this.destination = new int[COMPONENT_SIZE];
        int[] mimic = new int[COMPONENT_SIZE];
        int[] temp  = new int[COMPONENT_SIZE];

        for(int ci = -1; ++ci < COMPONENT_SIZE; )
        {
            source[ci] = ci;
            destination[ci] = ci;
        }
        this.vel = new Velocity(COMPONENT_SIZE * 2);
        this.p_source = new Position(source);
        this.p_destination = new Position(destination);
        this.p_mimic = new Position(mimic);
        this.p_temp = new Position(temp);
    }


    @Test public void test_with_random_position()
    {
        final LongList elapsed    = new LongArrayList(ITERATION_SIZE);
        final Random   random_des = new Random();
        final Random   random_src = new Random();
        for(int ci = -1; ++ci < ITERATION_SIZE; )
        {
            IntArrays.shuffle(destination, random_des);
            IntArrays.shuffle(source, random_src);
            final long no_see = System.currentTimeMillis();
            Velocity.calculateDistance(vel, p_destination, p_source, p_mimic, p_temp);
            final long long_time = System.currentTimeMillis();
            elapsed.add(long_time - no_see);
        }
        System.out.println();
        System.out.println(elapsed.stream().mapToLong(value -> value).average());
    }

    @Test
    public void test_with_stability_checking()
    {
        int[]        pooling    = new int[2];
        final Random random_des = new Random();
        final Random random_src = new Random();
        for(int ci = -1; ++ci < ITERATION_SIZE; )
        {
            IntArrays.shuffle(destination, random_des);
            IntArrays.shuffle(source, random_src);
            Velocity.calculateDistance(vel, p_destination, p_source, p_mimic, p_temp);
            Position.update(p_source, vel);
            Assert.assertTrue(Arrays.equals(source, destination));
            ++pooling[Arrays.equals(source, destination) ? 1 : 0];
        }
        System.out.println(Arrays.toString(pooling));
    }
}
