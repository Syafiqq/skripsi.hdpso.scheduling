package model.method.pso.hdpso.component;

import it.unimi.dsi.fastutil.ints.IntArrays;
import java.util.Random;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.component> created by : 
 * Name         : syafiq
 * Date / Time  : 22 November 2016, 9:22 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class TPosition
{
    @Test public void test_position()
    {
        final Position p1    = new Position(new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        final Position p2    = new Position(new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        final Position mimic = Position.newInstance(p1.getPosition());
        final Position temp  = Position.newInstance(p1.getPosition());
        IntArrays.shuffle(p1.getPosition(), new Random());
        IntArrays.shuffle(p2.getPosition(), new Random());
        System.out.println(p1);
        System.out.println(p2);
        System.out.println(mimic);
        System.out.println(temp);
        Velocity velocity = new Velocity(p1.getPosition().length * 2);
        Velocity.calculateDistance(velocity, p1, p2, mimic, temp);
        System.out.println(velocity);
        Position.update(p2, velocity);
        System.out.println(p1);
        System.out.println(p2);
    }
}
