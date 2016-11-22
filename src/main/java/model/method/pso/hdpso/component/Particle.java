package model.method.pso.hdpso.component;

import java.util.Random;
import model.dataset.component.DSScheduleShufflingProperty;
import model.method.pso.hdpso.core.ScheduleRandomable;
import model.util.list.IntHList;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.component> created by : 
 * Name         : syafiq
 * Date / Time  : 22 November 2016, 10:10 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings({"FieldCanBeLocal", "unused"}) public class Particle
{
    @NotNull private final Data       data;
    @NotNull private final Data       pBest;
    @NotNull private final Velocity[] velocity;

    @NotNull private final Setting             setting;
    private final          Random              random;
    @NotNull private final VelocityProperty    velocity_properties;
    @NotNull private final PlacementProperties placement_properties;
    @NotNull private final RepairProperty[]    repair_properties;
    @NotNull private final IntHList[]          lesson_conflicts;

    public Particle(
            @NotNull final DSScheduleShufflingProperty builder,
            @NotNull final ScheduleRandomable<Position[], DSScheduleShufflingProperty> randomable,
            @NotNull final Setting setting,
            @NotNull final PlacementProperties placement_properties,
            @NotNull final RepairProperty[] repair_properties)
    {
        this.setting = setting;
        this.data = new Data(randomable.random(builder));
        this.pBest = Data.newInstance(this.data);

        this.velocity = new Velocity[this.data.getPositionSize()];
        this.lesson_conflicts = new IntHList[this.velocity.length];
        for(int c_position = -1, position_size = this.velocity.length; ++c_position < position_size; )
        {
            final int position_length = this.data.getPosition(c_position).getPositionSize();

            this.lesson_conflicts[c_position] = new IntHList(position_length);

            int velocity_bound = position_length * (int) Math.ceil(this.setting.brand_max);
            velocity_bound = velocity_bound == 0 ? position_length : velocity_bound;
            this.velocity[c_position] = new Velocity(velocity_bound);
        }

        this.velocity_properties = new VelocityProperty(builder, randomable, setting, this.data.getPositions());
        this.placement_properties = placement_properties;
        this.repair_properties = repair_properties;
        this.random = new Random();
    }

    public void assignPBest()
    {
        if(this.data.getFitness() > this.pBest.getFitness())
        {
            Data.replaceData(this.pBest, this.data);
        }
    }

    public void calculateVelocity(Data gBest, int cEpoch, int max_epoch)
    {
        @NotNull final VelocityProperty property        = this.velocity_properties;
        @NotNull final Velocity[]       velocity        = property.getVelocityTemporary();
        @NotNull final Velocity[]       v_temp          = property.getVelocityContainer();
        @NotNull final Position[]       p_mimic         = property.getPositionMimic();
        @NotNull final Position[]       p_cont          = property.getPositionContainer();
        final int                       position_length = this.data.getPositionSize();

        double random_coefficient;
        double constants_coefficient;

        property.initializePRand();
        property.initializeDLoc(this.data);
        property.initializeDGlob(this.data);

        random_coefficient = this.random.nextDouble();
        constants_coefficient = ((this.setting.bloc_max - this.setting.bloc_min) * (cEpoch * 1f / max_epoch)) + this.setting.bloc_min;
        for(int c_data = -1; ++c_data < position_length; )
        {
            Velocity.calculateDistance(velocity[c_data], this.pBest.getPosition(c_data), this.data.getPosition(c_data), p_mimic[c_data], p_cont[c_data]);
            Velocity.multiplication(random_coefficient * constants_coefficient, velocity[c_data], v_temp[c_data]);
            Position.update(property.getDLoc(c_data), velocity[c_data]);
        }

        random_coefficient = this.random.nextDouble();
        constants_coefficient = this.setting.bglob_max - ((this.setting.bglob_max - this.setting.bglob_min) * (cEpoch * 1f / max_epoch));
        for(int c_data = -1; ++c_data < position_length; )
        {
            Velocity.calculateDistance(velocity[c_data], gBest.getPosition(c_data), this.data.getPosition(c_data), p_mimic[c_data], p_cont[c_data]);
            Velocity.multiplication(random_coefficient * constants_coefficient, velocity[c_data], v_temp[c_data]);
            Position.update(property.getDGlob(c_data), velocity[c_data]);
        }

        random_coefficient = this.random.nextDouble();
        constants_coefficient = this.setting.brand_max - ((this.setting.brand_max - this.setting.brand_min) * (cEpoch * 1f / max_epoch));
        for(int c_data = -1; ++c_data < position_length; )
        {
            Velocity.calculateDistance(this.velocity[c_data], property.getPRand(c_data), this.data.getPosition(c_data), p_mimic[c_data], p_cont[c_data]);
            Velocity.multiplication(random_coefficient * constants_coefficient, this.velocity[c_data], v_temp[c_data]);
        }

        for(int c_data = -1; ++c_data < position_length; )
        {
            Velocity.calculateDistance(velocity[c_data], property.getDLoc(c_data), property.getDGlob(c_data), p_mimic[c_data], p_cont[c_data]);
            Velocity.multiplication(0.5, velocity[c_data], v_temp[c_data]);
            Position.update(property.getDGlob(c_data), velocity[c_data]);
            Position.update(property.getDGlob(c_data), this.velocity[c_data]);
        }
    }

    public void updateData()
    {
        Data.replacePosition(this.data.getPositions(), this.velocity_properties.getDGlob());
    }

    public String toString(int indent)
    {
        String storedIndent = "";
        for(int i = indent; --i >= 0; )
        {
            storedIndent += '\t';
        }

        return String.format("%s{\n", storedIndent)
                + String.format("%s\t%s : \n%s\n\n", storedIndent, "Data", this.data.toString(indent + 1))
                + String.format("%s\t%s : [%s = %b]\n%s\n\n", storedIndent, "P-Best", "Data : P-Best", this.data == this.pBest, this.pBest.toString(indent + 1))
                + String.format("%s\t%s : \n%s\t{\n", storedIndent, "Velocities", storedIndent) + String.format("%s", Velocity.toString(this.velocity, indent + 2))
                + String.format("%s\t}\n\n", storedIndent) + String.format("%s\t%s : \n%s\n", storedIndent, "Velocity Properties", this.velocity_properties.toString(indent + 1))
                + String.format("%s}", storedIndent);
    }
}
