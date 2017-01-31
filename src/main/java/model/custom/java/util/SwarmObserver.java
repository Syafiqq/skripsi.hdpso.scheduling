package model.custom.java.util;

import model.method.pso.hdpso.component.Particle;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.custom.java.util> created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 28 January 2017, 10:45 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public interface SwarmObserver
{
    void update(@NotNull final Particle[] particles);
}
