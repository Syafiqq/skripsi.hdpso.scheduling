package model.util.list;

import java.util.Iterator;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.util.list> created by : 
 * Name         : syafiq
 * Date / Time  : 15 November 2016, 10:26 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class THList
{
    private HList<Integer> list;

    @Before
    public void initialize()
    {
        this.list = new HList<Integer>(new Integer[]
                {
                        new Integer(1),
                        new Integer(2),
                        new Integer(3),
                        new Integer(4),
                        new Integer(5),
                        new Integer(6),
                        new Integer(7),
                        new Integer(8),
                        new Integer(9),
                        new Integer(10)
                })
        {
            @Override public void set(Integer data)
            {
                super.list[++super.counter] = data;
            }
        };
    }

    @Test
    public void test_integer_toString()
    {
        list.set(10);
        list.set(10);
        list.set(10);
        list.set(10);
        list.set(10);
        list.set(10);
        list.set(10);

        System.out.println(list);
    }

    @Test
    public void test_integer_iterator()
    {
        list.set(10);
        list.set(10);
        list.set(10);
        list.set(10);
        list.set(10);
        list.set(10);
        list.set(10);

        for(@NotNull final Iterator<Integer> iter = list.iterator(); iter.hasNext(); )
        {
            System.out.print(iter.next());
            System.out.print('\t');
        }
        System.out.println();
    }
}
