package model.method.pso.hdpso.component;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.component> created by : 
 * Name         : syafiq
 * Date / Time  : 15 November 2016, 10:48 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class TTransposition
{
    @Test
    public void test_transposition()
    {
        @NotNull Transposition transposition = new Transposition(1, 2);
        System.out.println(transposition);
        transposition.set(2, 3);
        System.out.println(transposition);
        transposition.setSource(8);
        transposition.setDestination(9);
        System.out.println(transposition);
    }
}
