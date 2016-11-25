package model.method.pso.hdpso.component;

import java.util.Comparator;
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
@SuppressWarnings({"FieldCanBeLocal", "unused"}) public class Particle extends ParticleBuilder<Data, Velocity>
{
    public static final Comparator<Particle> particlePBestFitnessDescComparator = (p1, p2) -> (int) (p2.pBest.getFitness() - p1.pBest.getFitness());

    @NotNull private final Setting           setting;
    @NotNull private final Random            random;
    @NotNull private final VelocityProperty  velocity_properties;
    @NotNull private final PlacementProperty placement_properties;
    @NotNull private final RepairProperty[]  repair_properties;
    @NotNull private final IntHList[]        lesson_conflicts;

    public Particle(
            @NotNull final DSScheduleShufflingProperty builder,
            @NotNull final ScheduleRandomable<Position[], DSScheduleShufflingProperty> randomable,
            @NotNull final Setting setting,
            @NotNull final PlacementProperty placement_properties,
            @NotNull final RepairProperty[] repair_properties)
    {
        super(new Data(randomable.random(builder)));
        this.setting = setting;
        this.lesson_conflicts = new IntHList[super.velocity.length];
        for(int c_position = -1, position_size = super.velocity.length; ++c_position < position_size; )
        {
            final int position_length = super.data.getPosition(c_position).getPositionSize();

            this.lesson_conflicts[c_position] = new IntHList(position_length);

            int velocity_bound = position_length * (int) Math.ceil(this.setting.brand_max);
            velocity_bound = velocity_bound == 0 ? position_length : velocity_bound;
            this.velocity[c_position] = new Velocity(velocity_bound);
        }

        this.velocity_properties = new VelocityProperty(builder, randomable, setting, super.data.getPositions());
        this.placement_properties = placement_properties;
        this.repair_properties = repair_properties;
        this.random = new Random();
    }

    public void assignPBest()
    {
        if(super.data.getFitness() > super.pBest.getFitness())
        {
            Data.replaceData(super.pBest, super.data);
        }
    }

    public void calculateVelocity(Data gBest, int cEpoch, int max_epoch)
    {
        @NotNull final VelocityProperty property        = this.velocity_properties;
        @NotNull final Velocity[]       velocity        = property.getVelocityTemporary();
        @NotNull final Velocity[]       v_temp          = property.getVelocityContainer();
        @NotNull final Position[]       p_mimic         = property.getPositionMimic();
        @NotNull final Position[]       p_cont          = property.getPositionContainer();
        final int                       position_length = super.data.getPositionSize();

        double random_coefficient;
        double constants_coefficient;

        property.initializePRand();
        property.initializeDLoc(super.data);
        property.initializeDGlob(super.data);

        random_coefficient = this.random.nextDouble();
        constants_coefficient = ((this.setting.bloc_max - this.setting.bloc_min) * (cEpoch * 1f / max_epoch)) + this.setting.bloc_min;
        for(int c_data = -1; ++c_data < position_length; )
        {
            Velocity.calculateDistance(velocity[c_data], super.pBest.getPosition(c_data), super.data.getPosition(c_data), p_mimic[c_data], p_cont[c_data]);
            Velocity.multiplication(random_coefficient * constants_coefficient, velocity[c_data], v_temp[c_data]);
            Position.update(property.getDLoc(c_data), velocity[c_data]);
        }

        random_coefficient = this.random.nextDouble();
        constants_coefficient = this.setting.bglob_max - ((this.setting.bglob_max - this.setting.bglob_min) * (cEpoch * 1f / max_epoch));
        for(int c_data = -1; ++c_data < position_length; )
        {
            Velocity.calculateDistance(velocity[c_data], gBest.getPosition(c_data), super.data.getPosition(c_data), p_mimic[c_data], p_cont[c_data]);
            Velocity.multiplication(random_coefficient * constants_coefficient, velocity[c_data], v_temp[c_data]);
            Position.update(property.getDGlob(c_data), velocity[c_data]);
        }

        random_coefficient = this.random.nextDouble();
        constants_coefficient = this.setting.brand_max - ((this.setting.brand_max - this.setting.brand_min) * (cEpoch * 1f / max_epoch));
        for(int c_data = -1; ++c_data < position_length; )
        {
            Velocity.calculateDistance(this.velocity[c_data], property.getPRand(c_data), super.data.getPosition(c_data), p_mimic[c_data], p_cont[c_data]);
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
        Data.replacePosition(super.data.getPositions(), this.velocity_properties.getDGlob());
    }

    public String toString(int indent)
    {
        String storedIndent = "";
        for(int i = indent; --i >= 0; )
        {
            storedIndent += '\t';
        }

        return String.format("%s{\n", storedIndent)
                + String.format("%s\t%s : \n%s\n\n", storedIndent, "Data", super.data.toString(indent + 1))
                + String.format("%s\t%s : [%s = %b]\n%s\n\n", storedIndent, "P-Best", "Data : P-Best", super.data == super.pBest, super.pBest.toString(indent + 1))
                + String.format("%s\t%s : \n%s\t{\n", storedIndent, "Velocities", storedIndent) + String.format("%s", Velocity.toString(this.velocity, indent + 2))
                + String.format("%s\t}\n\n", storedIndent) + String.format("%s\t%s : \n%s\n", storedIndent, "Velocity Properties", this.velocity_properties.toString(indent + 1))
                + String.format("%s}", storedIndent);
    }

    public @NotNull PlacementProperty getPlacementProperty()
    {
        return this.placement_properties;
    }

    public IntHList getLessonConflict(int i_cluster)
    {
        return this.lesson_conflicts[i_cluster];
    }

    public void setFitness(double fitness)
    {
        this.data.setFitness(fitness);
    }
}
