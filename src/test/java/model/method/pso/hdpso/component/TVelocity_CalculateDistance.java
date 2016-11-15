package model.method.pso.hdpso.component;

import it.unimi.dsi.fastutil.ints.IntArrays;
import java.util.Arrays;
import java.util.Random;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.component> created by : 
 * Name         : syafiq
 * Date / Time  : 15 November 2016, 8:42 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class TVelocity_CalculateDistance
{
    @Test
    public void test_calculate_distance()
    {
        int   SIZE        = 4;
        int[] source      = new int[SIZE];
        int[] destination = new int[SIZE];
        int[] mimic       = new int[SIZE];
        int[] temp        = new int[SIZE];

        int i = -1;
        source[++i] = i;
        source[++i] = i;
        source[++i] = i;
        source[++i] = i;
/*        source[++i] = i;
        source[++i] = i;
        source[++i] = i;
        source[++i] = i;
        source[++i] = i;
        source[++i] = i;*/

        System.arraycopy(source, 0, destination, 0, SIZE);
        IntArrays.shuffle(source, new Random());
        Velocity vel           = new Velocity(SIZE * 2);
        Position p_source      = new Position(source);
        Position p_destination = new Position(destination);
        Position p_mimic       = new Position(mimic);
        Position p_temp        = new Position(temp);
        System.out.println(Arrays.toString(source));
        System.out.println(Arrays.toString(destination));
        System.out.println(vel);
        Velocity.calculateDistance(vel, p_destination, p_source, p_mimic, p_temp);
        System.out.println(vel);
    }

    @Test public void test_with_random_position()
    {
        int   SIZE        = 10;
        int[] source      = new int[SIZE];
        int[] destination = new int[SIZE];
        int[] mimic       = new int[SIZE];
        int[] temp        = new int[SIZE];

        for(int ci = -1; ++ci < SIZE; )
        {
            source[ci] = ci;
            destination[ci] = ci;
        }
        IntArrays.shuffle(source, new Random());
        IntArrays.shuffle(destination, new Random());
        Velocity vel           = new Velocity(SIZE * 2);
        Position p_source      = new Position(source);
        Position p_destination = new Position(destination);
        Position p_mimic       = new Position(mimic);
        Position p_temp        = new Position(temp);
        System.out.println(Arrays.toString(source));
        System.out.println(Arrays.toString(destination));
        System.out.println(vel);
        Velocity.calculateDistance(vel, p_destination, p_source, p_mimic, p_temp);
        System.out.println(vel);
    }

    @Test public void test_with_random_position_with_checking()
    {
        int   SIZE        = 1000;
        int[] source      = new int[SIZE];
        int[] destination = new int[SIZE];
        int[] mimic       = new int[SIZE];
        int[] temp        = new int[SIZE];

        for(int ci = -1; ++ci < SIZE; )
        {
            source[ci] = ci;
            destination[ci] = ci;
        }
        IntArrays.shuffle(source, new Random());
        IntArrays.shuffle(destination, new Random());
        Velocity vel           = new Velocity(SIZE * 2);
        Position p_source      = new Position(source);
        Position p_destination = new Position(destination);
        Position p_mimic       = new Position(mimic);
        Position p_temp        = new Position(temp);
        System.out.println(Arrays.toString(source));
        System.out.println(Arrays.toString(destination));
        System.out.println(vel);
        Velocity.calculateDistance(vel, p_destination, p_source, p_mimic, p_temp);
        System.out.println(vel);
        Position.update(p_source, vel);
        System.out.println(p_source);
        System.out.println(p_destination);
        System.out.println(p_source == p_destination);
    }
}
