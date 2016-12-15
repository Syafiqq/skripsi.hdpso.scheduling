package model.method.pso.hdpso.core;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.core> created by : 
 * Name         : syafiq
 * Date / Time  : 22 November 2016, 10:04 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public interface ScheduleRandomable<T, P>
{
    T random(P properties);

    void random(P properties, T data);
}
