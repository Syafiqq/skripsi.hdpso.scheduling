package model.method.pso.hdpso.component;

import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.component> created by : 
 * Name         : syafiq
 * Date / Time  : 15 November 2016, 3:04 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class TVelocity_AdditionVelocity
{
    @Test
    public void destination_lt_source_no_reduction()
    {
        /*
        * @param vel_destination = ABC_______
        * @param vel_source      = ACDEF_____
        * */
        Velocity destination = new Velocity(10);
        destination.set(0, 1);
        destination.set(2, 3);
        destination.set(4, 5);
        Velocity source = new Velocity(10);
        source.set(6, 7);
        source.set(8, 9);
        source.set(10, 11);
        source.set(12, 13);
        source.set(14, 15);
        System.out.println(source);
        System.out.println(destination);
        Velocity.additionVelocity(destination, source);
        System.out.println(destination);
    }

    @Test
    public void destination_lt_source_reduction_if()
    {
        /*
        * @param vel_destination = ABC_______
        * @param vel_source      = ACDEF_____
        * */
        Velocity destination = new Velocity(10);
        destination.set(0, 1);
        destination.set(2, 3);
        destination.set(4, 5);
        Velocity source = new Velocity(10);
        source.set(0, 1);
        source.set(12, 13);
        source.set(6, 7);
        source.set(8, 9);
        source.set(10, 11);
        System.out.println(source);
        System.out.println(destination);
        Velocity.additionVelocity(destination, source);
        System.out.println(destination);
    }

    @Test
    public void destination_lt_source_reduction_else_if()
    {
        /*
        * @param vel_destination = ABC_______
        * @param vel_source      = ACDEF_____
        * */
        Velocity destination = new Velocity(10);
        destination.set(0, 1);
        destination.set(2, 3);
        destination.set(4, 5);
        Velocity source = new Velocity(10);
        source.set(10, 1);
        source.set(4, 5);
        source.set(6, 7);
        source.set(8, 9);
        source.set(10, 11);
        System.out.println(source);
        System.out.println(destination);
        Velocity.additionVelocity(destination, source);
        System.out.println(destination);
    }

    @Test
    public void destination_lt_source_reduction_else_all()
    {
        /*
        * @param vel_destination = ABC_______
        * @param vel_source      = ACDEF_____
        * */
        Velocity destination = new Velocity(10);
        destination.set(0, 1);
        destination.set(2, 3);
        destination.set(4, 5);
        Velocity source = new Velocity(10);
        source.set(0, 1);
        source.set(4, 5);
        source.set(6, 7);
        source.set(8, 9);
        source.set(10, 11);
        System.out.println(source);
        System.out.println(destination);
        Velocity.additionVelocity(destination, source);
        System.out.println(destination);
    }
}
