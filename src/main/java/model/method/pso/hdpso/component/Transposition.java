package model.method.pso.hdpso.component;

import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.component> created by :
 * Name         : syafiq
 * Date / Time  : 15 November 2016, 10:32 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings({"WeakerAccess", "unused"}) public class Transposition
{
    private int source;
    private int destination;

    public Transposition(int source, int destination)
    {
        this.set(source, destination);
    }

    public Transposition()
    {
        this(-1, -1);
    }

    public void set(int source, int destination)
    {
        this.setSource(source);
        this.setDestination(destination);
    }

    public void set(@NotNull final Transposition that)
    {
        this.set(that.source, that.destination);
    }

    public boolean equalsTransposition(final Transposition that)
    {
        return (this.source == that.source) && (this.destination == that.destination);
    }

    public int getSource()
    {
        return this.source;
    }

    public void setSource(int source)
    {
        this.source = source;
    }

    public int getDestination()
    {
        return this.destination;
    }

    public void setDestination(int destination)
    {
        this.destination = destination;
    }

    @Override public String toString()
    {
        return "{" + source + ", " + destination + "}";
    }
}