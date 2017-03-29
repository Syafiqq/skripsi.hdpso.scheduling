package misc.it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import model.database.component.DBSubject;
import model.database.component.DBTimetable;
import org.junit.Assert;
import org.junit.Test;

/*
 * This <skripsi.hdpso.scheduling> project in package <misc.it.unimi.dsi.fastutil.ints> created by : 
 * Name         : syafiq
 * Date / Time  : 26 October 2016, 7:28 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class TInt2ObjectLinkedOpenHashMap
{
    @Test public void testDefaultReturnValue()
    {
        Int2ObjectLinkedOpenHashMap<DBSubject> subjects = new Int2ObjectLinkedOpenHashMap<>(1);
        subjects.put(1, new DBSubject(1, "Abc", "BCA", new DBTimetable(-1, null, null, null, null, -1, 1, 1)));

        Assert.assertNull(subjects.defaultReturnValue());
    }
}
