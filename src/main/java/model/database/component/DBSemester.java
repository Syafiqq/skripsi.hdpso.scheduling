package model.database.component;

import model.util.Discribable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.component> created by : 
 * Name         : syafiq
 * Date / Time  : 23 October 2016, 6:38 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings(value = {"unused", "WeakerAccess"}) public enum DBSemester implements Discribable
{
    ODD
            {
                @Override public String describe()
                {
                    return "Ganjil";
                }
            },
    EVEN
            {
                @Override public String describe()
                {
                    return "Genap";
                }
            };

    @Nullable @Contract(pure = true) public static DBSemester getSemester(final int semester)
    {
        switch(semester)
        {
            case 0:
            {
                return ODD;
            }
            case 1:
            {
                return EVEN;
            }
        }
        return null;
    }

    @Contract(pure = true) public static int getSemester(@NotNull final DBSemester semester)
    {
        return semester.ordinal();
    }
}
