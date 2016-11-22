package model.method.pso.hdpso.component;

import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.component> created by : 
 * Name         : syafiq
 * Date / Time  : 17 November 2016, 3:30 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings({"unused", "WeakerAccess"}) public class Data
{
    @NotNull private final Position[] positions;
    private                double     fitness;

    public Data(@NotNull final Position[] positions)
    {
        this.positions = positions;
        this.fitness = 0.0;
    }

    public static Data newInstance(@NotNull final Data data)
    {
        @NotNull final Position[] positions = new Position[data.positions.length];
        for(int c_position = -1, c_position_s = positions.length; ++c_position < c_position_s; )
        {
            positions[c_position] = Position.newInstance(data.positions[c_position].getPosition());
        }
        return new Data(positions);
    }

    public static void replaceData(final Data destination, final Data source)
    {
        Data.replacePosition(destination.positions, source.positions);
        destination.fitness = source.fitness;
    }

    public static void replacePosition(final Position[] destination, final Position[] source)
    {
        for(int counter_position = -1, position_size = source.length; ++counter_position < position_size; )
        {
            Position.replace(destination[counter_position], source[counter_position]);
        }
    }

    @NotNull public Position[] getPositions()
    {
        return this.positions;
    }

    public String toString(int indent)
    {
        String storedIndent = "";
        for(int i = indent; --i >= 0; )
        {
            storedIndent += '\t';
        }

        return String.format("%s{\n", storedIndent) +
                String.format("%s", Position.toString(this.positions, indent + 1)) +
                String.format("%s\t%12s : %g\n", storedIndent, "Fitness", this.fitness) +
                String.format("%s}", storedIndent);
    }

    @Override public String toString()
    {
        return this.toString(0);
    }

    public int getPositionSize()
    {
        return this.positions.length;
    }

    public @NotNull Position getPosition(int c_position)
    {
        return this.positions[c_position];
    }

    public double getFitness()
    {
        return fitness;
    }
}
