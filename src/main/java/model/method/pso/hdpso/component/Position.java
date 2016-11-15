package model.method.pso.hdpso.component;

import java.util.Arrays;
import java.util.Iterator;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.component> created by : 
 * Name         : syafiq
 * Date / Time  : 15 November 2016, 11:09 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings({"WeakerAccess", "unused"}) public class Position
{
    @NotNull private final int[] position;

    public Position(@NotNull final int[] positions)
    {
        this.position = positions;
    }

    public static Position newInstance(@NotNull final int[] that_position)
    {
        @NotNull final int[] this_position = new int[that_position.length];
        System.arraycopy(that_position, 0, this_position, 0, that_position.length);
        return new Position(this_position);
    }

    public static Position newInstance(int length)
    {
        @NotNull final int[] position = new int[length];
        return new Position(position);
    }

    public static void replace(final @NotNull Position destination, final @NotNull Position source)
    {
        System.arraycopy(source.position, 0, destination.position, 0, source.position.length);
    }

    public static void update(final @NotNull Position position, final @NotNull Velocity velocity)
    {
        for(final @NotNull Iterator<Transposition> iterator = velocity.iterator(); iterator.hasNext(); )
        {
            position.update(iterator.next());
        }
    }

    @NotNull public static String toString(final @NotNull Position[] positions, int indent)
    {
        String storedIndent = "";
        for(int i = indent; --i >= 0; )
        {
            storedIndent += '\t';
        }

        final StringBuilder sb = new StringBuilder();
        for(int i = -1, is = positions.length; ++i < is; )
        {
            sb.append(String.format("%s%12s : %s\n", storedIndent, String.format("%s [%d]", "Position", i), positions[i]));
        }
        return sb.toString();
    }

    private void update(final Transposition transposition)
    {
        final int source      = transposition.getSource();
        final int destination = transposition.getDestination();
        final int temp        = this.position[source];
        this.position[source] = this.position[destination];
        this.position[destination] = temp;
    }

    public @NotNull int[] getPosition()
    {
        return this.position;
    }

    public int getPositionSize()
    {
        return this.position.length;
    }

    @Override public String toString()
    {
        return Arrays.toString(this.position);
    }

}
