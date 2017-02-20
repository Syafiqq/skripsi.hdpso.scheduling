package model.util;

import org.jetbrains.annotations.Contract;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.util> created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 28 January 2017, 9:11 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class Converter
{
    @Contract(pure = true) public static boolean booleanIntegerToBoolean(final int val)
    {
        final boolean bVal;
        switch(val)
        {
            case 0:
            {
                bVal = false;
            }
            break;
            default:
            {
                bVal = true;
            }
        }
        return bVal;
    }

    @Contract(pure = true) public static int integerToBooleanInteger(final boolean val)
    {
        final int iVal;
        if(val)
        {
            iVal = 1;
        }
        else
        {
            iVal = 0;
        }
        return iVal;
    }
}
