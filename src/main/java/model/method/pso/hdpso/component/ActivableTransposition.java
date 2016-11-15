package model.method.pso.hdpso.component;

import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.component> created by : 
 * Name         : syafiq
 * Date / Time  : 15 November 2016, 10:41 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings("WeakerAccess") public class ActivableTransposition extends Transposition
{
    private boolean is_active;

    public ActivableTransposition(int source, int destination)
    {
        super(source, destination);
        this.setActive(false);
    }

    public ActivableTransposition()
    {
        super();
        this.setActive(false);
    }

    public void set(int source, int destination, boolean status)
    {
        this.setActive(status);
        super.set(source, destination);
    }

    @Override public void set(int source, int destination)
    {
        this.set(source, destination, true);
    }

    public void set(@NotNull Transposition that, boolean status)
    {
        this.setActive(status);
        super.set(that);
    }

    @Override public void set(@NotNull Transposition that)
    {
        this.set(that, true);
    }

    public boolean isActive()
    {
        return this.is_active;
    }

    public void setActive(boolean status)
    {
        this.is_active = status;
    }
}
