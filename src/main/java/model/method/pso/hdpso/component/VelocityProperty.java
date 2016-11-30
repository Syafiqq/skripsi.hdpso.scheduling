package model.method.pso.hdpso.component;

import model.dataset.component.DSScheduleShufflingProperty;
import model.method.pso.hdpso.core.ScheduleRandomable;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.component> created by : 
 * Name         : syafiq
 * Date / Time  : 22 November 2016, 10:39 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings({"WeakerAccess", "unused"}) public class VelocityProperty
{
    @NotNull private final Position[]                                                  dLoc;
    @NotNull private final Position[]                                                  dGlob;
    @NotNull private final Position[]                                                  pRand;
    @NotNull private final Position[]                                                  position_mimic;
    @NotNull private final Position[]                                                  position_container;
    @NotNull private final Velocity[]                                                  velocity_container;
    @NotNull private final Velocity[]                                                  velocity_temporary;
    @NotNull private final ScheduleRandomable<Position[], DSScheduleShufflingProperty> random_generator;
    @NotNull private final DSScheduleShufflingProperty                                 rand_properties;

    public VelocityProperty(@NotNull final DSScheduleShufflingProperty builder,
                            @NotNull final ScheduleRandomable<Position[], DSScheduleShufflingProperty> randomable,
                            @NotNull final Setting setting,
                            @NotNull final Position[] sample_position)
    {
        final int sample_size = sample_position.length;
        this.random_generator = randomable;
        this.dLoc = new Position[sample_size];
        this.dGlob = new Position[sample_size];
        this.pRand = new Position[sample_size];
        this.position_mimic = new Position[sample_size];
        this.position_container = new Position[sample_size];
        this.velocity_temporary = new Velocity[sample_size];
        this.velocity_container = new Velocity[sample_size];
        this.rand_properties = DSScheduleShufflingProperty.newInstance(builder);

        for(int counter_sample = -1; ++counter_sample < sample_size; )
        {
            int max = 0;
            for(int current_max : sample_position[counter_sample].getPosition())
            {
                max = current_max > max ? current_max : max;
            }

            int position_length = sample_position[counter_sample].getPositionSize();
            this.dLoc[counter_sample] = Position.newInstance(position_length);
            this.dGlob[counter_sample] = Position.newInstance(position_length);
            this.pRand[counter_sample] = Position.newInstance(position_length);
            this.position_mimic[counter_sample] = Position.newInstance(position_length);
            this.position_container[counter_sample] = Position.newInstance(max + 1);
            int velocity_bound = (position_length * (int) Math.ceil(Math.max(setting.bLoc_max, setting.bGlob_max)));
            velocity_bound = velocity_bound == 0 ? position_length * 2 : velocity_bound;
            this.velocity_temporary[counter_sample] = new Velocity(velocity_bound);
            this.velocity_container[counter_sample] = new Velocity(velocity_bound);
        }
    }

    public void initializeDLoc(final Data data)
    {
        this.initializePositionFromData(this.dLoc, data.getPositions());
    }

    public void initializeDGlob(final Data data)
    {
        this.initializePositionFromData(this.dGlob, data.getPositions());
    }

    public void initializePRand()
    {
        this.random_generator.random(this.rand_properties, this.pRand);
    }

    private void initializePositionFromData(final Position[] destination, final Position[] source)
    {
        for(int counter_position = -1, position_size = source.length; ++counter_position < position_size; )
        {
            Position.replace(destination[counter_position], source[counter_position]);
        }
    }

    public String toString(int indent)
    {
        String storedIndent = "";
        for(int i = indent; --i >= 0; )
        {
            storedIndent += '\t';
        }

        return String.format("%s{\n", storedIndent)
                + String.format("%s\t%s : \n%s\t{\n%s%s\t}\n\n", storedIndent, "D-Loc", storedIndent, Position.toString(this.dLoc, indent + 2), storedIndent)
                + String.format("%s\t%s : \n%s\t{\n%s%s\t}\n\n", storedIndent, "D-Glob", storedIndent, Position.toString(this.dGlob, indent + 2), storedIndent)
                + String.format("%s\t%s : \n%s\t{\n%s%s\t}\n\n", storedIndent, "D-Rand", storedIndent, Position.toString(this.pRand, indent + 2), storedIndent)
                + String.format("%s\t%s : \n%s\t{\n%s%s\t}\n\n", storedIndent, "Position Mimic", storedIndent, Position.toString(this.position_mimic, indent + 2), storedIndent)
                + String.format("%s\t%s : \n%s\t{\n%s%s\t}\n\n", storedIndent, "Position Container", storedIndent, Position.toString(this.position_container, indent + 2), storedIndent)
                + String.format("%s\t%s : \n%s\t{\n%s%s\t}\n\n", storedIndent, "Velocity Container", storedIndent, Velocity.toString(this.velocity_container, indent + 2), storedIndent)
                + String.format("%s\t%s : \n%s\t{\n%s%s\t}\n", storedIndent, "Velocity Temporary", storedIndent, Velocity.toString(this.velocity_temporary, indent + 2), storedIndent)
                + String.format("%s}", storedIndent);
    }

    @NotNull public Velocity[] getVelocityTemporary()
    {
        return this.velocity_temporary;
    }

    @NotNull public Position[] getPositionMimic()
    {
        return this.position_mimic;
    }

    @NotNull public Position[] getPositionContainer()
    {
        return this.position_container;
    }

    @NotNull public Velocity[] getVelocityContainer()
    {
        return this.velocity_container;
    }

    @NotNull public Position getDLoc(int index)
    {
        return this.dLoc[index];
    }

    @NotNull public Position getDGlob(int index)
    {
        return this.dGlob[index];
    }

    @NotNull public Position getPRand(int index)
    {
        return this.pRand[index];
    }

    @NotNull public Position[] getDGlob()
    {
        return this.dGlob;
    }

    public @NotNull DSScheduleShufflingProperty getPRandProperty()
    {
        return this.rand_properties;
    }
}
