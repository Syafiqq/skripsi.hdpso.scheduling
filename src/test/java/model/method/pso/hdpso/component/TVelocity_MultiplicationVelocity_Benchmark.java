package model.method.pso.hdpso.component;

import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.component> created by : 
 * Name         : syafiq
 * Date / Time  : 15 November 2016, 8:12 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class TVelocity_MultiplicationVelocity_Benchmark
{
    private Velocity temp;
    private Velocity source;
    private Velocity copycat;

    @Test
    public void test_empty_velocity()
    {
        final int SIZE = 5000000;
        this.source = new Velocity(SIZE * 2);
        this.copycat = new Velocity(SIZE * 2);
        this.temp = new Velocity(SIZE * 2);
        Velocity.cloneVelocity(source, copycat);
        this.doTest();
    }

    @Test
    public void test_nonempty_velocity()
    {
        final int SIZE = 5000000;
        this.source = new Velocity(SIZE * 2);
        this.copycat = new Velocity(SIZE * 2);
        this.temp = new Velocity(SIZE * 4);
        for(int ci = -1; ++ci < SIZE; )
        {
            this.source.set(ci, ci);
        }
        Velocity.cloneVelocity(source, copycat);
        this.doTest();
    }

    @SuppressWarnings("Duplicates") private void doTest()
    {
        this.test_multiplication_empty_lt__1();
        this.test_multiplication_empty_eq__1();
        this.test_multiplication_empty_bw__1_0();
        this.test_multiplication_empty_eq_0();
        this.test_multiplication_empty_bw_0_1();
        this.test_multiplication_empty_eq_1();
        this.test_multiplication_empty_ht_1();
    }

    private void test_multiplication_empty_bw__1_0()
    {
        double val = FastMath.random() * -1.0;
        System.out.printf("Less than -1 {%f} ", val);
        this.calculate(val);
    }

    private void test_multiplication_empty_eq__1()
    {
        double val = -1.0;
        System.out.printf("Equal -1 {%f} ", val);
        this.calculate(val);
    }

    private void test_multiplication_empty_lt__1()
    {
        double val = (FastMath.random() * 4 + 1) * -1.0;
        System.out.printf("Between -1 and 0 {%f} ", val);
        this.calculate(val);
    }

    private void test_multiplication_empty_eq_0()
    {
        double val = 0.0;
        System.out.printf("Equal 0 {%f} ", val);
        this.calculate(val);
    }

    private void test_multiplication_empty_bw_0_1()
    {
        double val = FastMath.random();
        System.out.printf("Between 0 and 1 {%f} ", val);
        this.calculate(val);
    }

    private void test_multiplication_empty_eq_1()
    {
        double val = 1.0;
        System.out.printf("Equal 1 {%f} ", val);
        this.calculate(val);
    }

    private void test_multiplication_empty_ht_1()
    {
        double val = FastMath.random() * 4 + 1;
        System.out.printf("Higher than 1 {%f} ", val);
        this.calculate(val);
    }


    private void calculate(double val)
    {
        Velocity.cloneVelocity(copycat, source);
        long time = System.currentTimeMillis();
        Velocity.multiplication(val, source, temp);
        System.out.println(System.currentTimeMillis() - time);
    }
}
