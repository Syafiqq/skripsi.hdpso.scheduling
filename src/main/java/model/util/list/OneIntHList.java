package model.util.list;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.dataset.component> created by : 
 * Name         : syafiq
 * Date / Time  : 17 November 2016, 9:34 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class OneIntHList extends IntHList
{
    public OneIntHList(int size)
    {
        super(size);
    }

    public boolean isExactlyOneEntry()
    {
        return super.counter == 0;
    }

    public boolean isSameEntry(int index, int lesson)
    {
        return super.list[index] == lesson;
    }
}
