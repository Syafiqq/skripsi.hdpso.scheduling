package model.util.list;

import java.util.Iterator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.util.list> created by : 
 * Name         : syafiq
 * Date / Time  : 15 November 2016, 10:17 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings({"WeakerAccess", "unused"}) public class HList<Data_Type>
{
    protected final Data_Type[] list;
    protected       int         counter;

    protected HList()
    {
        this(null);
    }

    public HList(Data_Type[] transpositions)
    {
        this.list = transpositions;
        this.counter = -1;
    }

    public void set(Data_Type data)
    {
        this.list[++this.counter] = data;
    }

    public Data_Type get()
    {
        return this.get(this.counter);
    }

    public Data_Type get(int index)
    {
        return this.list[index];
    }

    public void reset()
    {
        this.counter = -1;
    }

    public void backward(int length)
    {
        this.counter -= length;
    }

    public void forward(int length)
    {
        this.counter += length;
    }

    public void moveTo(int length)
    {
        this.counter = length - 1;
    }

    public int size()
    {
        return this.counter + 1;
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

    @NotNull public Iterator<Data_Type> iterator()
    {
        @NotNull final Iterator<Data_Type> iterator = new Iterator<Data_Type>()
        {
            @NotNull final Data_Type[] data = HList.this.list;
            int counter = -1;

            @Contract(pure = true) @Override public boolean hasNext()
            {
                return this.counter != HList.this.counter;
            }

            @Override public Data_Type next()
            {
                return this.data[++this.counter];
            }
        };

        return iterator;
    }
}
