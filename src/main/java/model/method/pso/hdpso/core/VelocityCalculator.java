package model.method.pso.hdpso.core;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.core> created by : 
 * Name         : syafiq
 * Date / Time  : 16 December 2016, 11:26 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public interface VelocityCalculator
{
    double calculateLoc(double... operator);

    double calculateGlob(double... operator);

    double calculateRand(double... operator);
}
