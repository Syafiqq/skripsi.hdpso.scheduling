package model.method.pso.hdpso.component;

import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.component> created by : 
 * Name         : syafiq
 * Date / Time  : 22 November 2016, 3:42 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("WeakerAccess") public abstract class ParticleBuilder<T extends Data, V extends Velocity>
{
    @NotNull protected final T data;
    @NotNull protected final T pBest;
    @NotNull protected final V velocity[];

    @SuppressWarnings("unchecked") public ParticleBuilder(@NotNull T data)
    {
        this.data = data;
        this.pBest = (T) Data.newInstance(data);
        this.velocity = (V[]) new Velocity[data.getPositionSize()];
    }

    public abstract void assignPBest();

    public abstract void calculateVelocity(final T gBest, int cEpoch, int MAX_EPOCHS);

    public abstract void updateData();

    @NotNull public T getData()
    {
        return data;
    }

    @NotNull public T getPBest()
    {
        return pBest;
    }

    @NotNull public V[] getVelocity()
    {
        return velocity;
    }
}
