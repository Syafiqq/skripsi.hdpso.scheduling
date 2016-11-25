package model.method.pso.hdpso.core;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.core> created by : 
 * Name         : syafiq
 * Date / Time  : 22 November 2016, 4:21 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public interface FitnessCalculator<T>
{
    void calculate(T data);
}
