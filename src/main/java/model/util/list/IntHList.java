package model.util.list;

import it.unimi.dsi.fastutil.ints.IntIterator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.util.list> created by : 
 * Name         : syafiq
 * Date / Time  : 17 November 2016, 9:11 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("unused") public class IntHList
{
    @NotNull protected final int[] list;
    protected                int   counter;

    public IntHList(int size)
    {
        this.list = new int[size];
        this.counter = -1;
    }

    @NotNull public static IntHList newInstance(@NotNull final IntHList list)
    {
        return new IntHList(list.list.length);
    }

    public void add(int value)
    {
        this.list[++this.counter] = value;
    }

    public int get()
    {
        return this.get(this.counter);
    }

    public int get(int index)
    {
        return this.list[index];
    }

    public void reset()
    {
        this.counter = -1;
    }

    public int size()
    {
        return counter + 1;
    }

    @Override public String toString()
    {
        StringBuilder sb = new StringBuilder();
        if(this.counter == -1)
        {
            return "-";
        }
        else
        {
            sb.append('{');
            sb.append(this.list[0]);
            for(int i = 0, is = this.counter + 1; ++i < is; )
            {
                sb.append(',');
                sb.append(' ');
                sb.append(this.list[i]);
            }
            sb.append('}');
            return sb.toString();
        }
    }

    @NotNull public IntIterator iterator()
    {
        @NotNull final IntIterator iterator = new IntIterator()
        {
            @NotNull final int[] data = IntHList.this.list;
            int counter = -1;

            @Override public int nextInt()
            {
                return this.data[++this.counter];
            }

            @Contract("_ -> fail") @Deprecated @Override public int skip(int n)
            {
                throw new UnsupportedOperationException();
            }

            @Contract(pure = true) @Override public boolean hasNext()
            {
                return this.counter != IntHList.this.counter;
            }

            @Contract(" -> fail") @Deprecated @Override public Integer next()
            {
                throw new UnsupportedOperationException();
            }
        };

        return iterator;
    }

    public void addAll(@NotNull IntHList container)
    {
        System.arraycopy(container.list, 0, this.list, this.size(), container.size());
        this.counter += container.size();
    }

    public int[] toArray()
    {
        return this.list;
    }
}
