package model.method.pso.hdpso.component;

import java.util.Iterator;
import model.util.list.HList;
import org.apache.commons.math3.util.FastMath;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.component> created by :
 * Name         : syafiq
 * Date / Time  : 15 November 2016, 11:00 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings({"unused", "WeakerAccess"}) public class Velocity extends HList<Transposition>
{
    /**
     * Constructor of Velocity
     *
     * @param expected_size = Size Maximum Of Velocity
     */
    public Velocity(int expected_size)
    {
        super(new Transposition[expected_size]);

        for(int c = -1; ++c < expected_size; )
        {
            super.list[c] = new Transposition();
        }
    }

    /**
     * Get distance between two positions
     *
     * @param velocity       : velocity container
     * @param destination    : destination position of particle
     * @param source         : source position of particle (read-only)
     * @param source_mimic   : source position of particle (simulation change)
     * @param temp_container : temporary container for processing
     */
    public static void calculateDistance(final Velocity velocity, final Position destination, final Position source, final Position source_mimic, final Position temp_container)
    {
        velocity.reset();
        System.arraycopy(source.getPosition(), 0, source_mimic.getPosition(), 0, source.getPositionSize());
        Velocity.calculateDistance(velocity, destination.getPosition(), source_mimic.getPosition(), temp_container.getPosition());
    }

    /**
     * Clone value of velocity
     *
     * @param source      : source of velocity
     * @param destination : destination of velocity
     */
    public static void cloneVelocity(final Velocity source, final Velocity destination)
    {
        destination.reset();
        for(@NotNull final Iterator<Transposition> iterator = source.iterator(); iterator.hasNext(); )
        {
            destination.set(iterator.next());
        }
    }

    /**
     * Reversing order of transposition of velocity
     *
     * @param velocity = velocity to be reversed
     */
    public static void reverse(final Velocity velocity)
    {
        Transposition         temp = new Transposition();
        final Transposition[] list = velocity.list;
        for(int front = -1, front_limit = velocity.size() / 2, end = velocity.size(); ++front < front_limit; )
        {
            temp.set(list[front]);
            list[front].set(list[--end]);
            list[end].set(temp);
        }
    }

    /**
     * Add velocity speed from another velocity
     *
     * @param destination = velocity to be added
     * @param source      = source of velocity
     */
    public static void addition(final Velocity destination, final Velocity source)
    {
        final Transposition[] vel_destination  = destination.list;
        final Transposition[] vel_source       = source.list;
        final int             size_destination = destination.size();
        final int             size_source      = source.size();

        /*
        * @param vel_destination = ABC_______
        * @param vel_source      = ACDEF_____
        */
        if(size_destination <= size_source)
        {
            /*
            * @param vel_destination = ABC_______
            * @param vel_source      = ACDEF_____
            *
            * Rearrange destination first;
            * @param vel_destination = a_b_c_____
            * */
            for(int counter_destination = size_destination * 2, counter_lookup = size_destination, separation_length = size_destination - 1; --separation_length >= 0; )
            {
                vel_destination[counter_destination -= 2].set(vel_destination[--counter_lookup]);
            }

            /*
            * Fill blank then
            * @param vel_destination = AaBcCd____
            * */
            for(int counter_destination = -1, counter_lookup = -1, insertion_length = size_destination; --insertion_length >= 0; )
            {
                vel_destination[counter_destination += 2].set(vel_source[++counter_lookup]);
            }

            /*
            * Put Remaining source
            * @param vel_destination = AABCDgh__
            * */
            for(int counter_destination = size_destination * 2 - 1, counter_lookup = size_destination - 1, remaining_length = size_source - size_destination; --remaining_length >= 0; )
            {
                vel_destination[++counter_destination].set(vel_source[++counter_lookup]);
            }
        }

        /*
        * @param vel_source      = ACDEF_____
        * @param vel_destination = ADC_______
        */
        else
        {
            /*
            * @param vel_source      = ACDEF_____
            * @param vel_destination = ADC_______
            *
            * Rearrange destination last first;
            * @param vel_destination = ACD___ef__
            * */
            for(int counter_destination = size_source + size_destination, counter_lookup = size_destination, remaining_length = size_destination - size_source; --remaining_length >= 0; )
            {
                vel_destination[--counter_destination].set(vel_destination[--counter_lookup]);
            }

            /*
            * Rearrange remaining destination
            * @param vel_destination = a_c_d_EF__
            * */
            for(int counter_destination = size_source * 2, counter_lookup = size_source, separation_length = size_source - 1; --separation_length >= 0; )
            {
                vel_destination[counter_destination -= 2].set(vel_destination[--counter_lookup]);
            }

            /*
            * Last, fill the blank
            * @param vel_destination = AaCdDcEF__
            * */
            for(int counter_destination = -1, counter_lookup = -1, insertion_length = size_source; --insertion_length >= 0; )
            {
                vel_destination[counter_destination += 2].set(vel_source[++counter_lookup]);
            }
        }

        destination.moveTo(size_destination + size_source);
    }

    /**
     * Multiplication of velocity given coefficient
     *
     * @param coefficient    = coefficient to be multiplicate
     * @param velocity       = velocity
     * @param temp_container = container of velocity if needed for processing
     */
    public static void multiplication(double coefficient, final Velocity velocity, final Velocity temp_container)
    {
        if(!velocity.isEmpty())
        {
            if(coefficient == 0.0)
            {
                velocity.reset();
            }
            else if(FastMath.abs(coefficient) <= 1.0)
            {
                if(coefficient < 0.0)
                {
                    Velocity.reverse(velocity);
                }
                velocity.backward((int) FastMath.floor(velocity.size() * (1.0 - FastMath.abs(coefficient))));
            }
            else
            {
                if(coefficient < 0.0)
                {
                    Velocity.reverse(velocity);
                    coefficient = FastMath.abs(coefficient);
                }
                Velocity.cloneVelocity(velocity, temp_container);

                int natural_coefficient = (int) FastMath.floor(coefficient);
                coefficient -= natural_coefficient;
                if(natural_coefficient % 2 == 0)
                {
                    velocity.reset();
                }

                //Keep that line below
                Velocity.multiplication(coefficient, temp_container, null);
                Velocity.addition(velocity, temp_container);
            }
        }
    }

    /**
     * Get distance between two positions
     *
     * @param velocity       : velocity container
     * @param destinations   : destination position of particle
     * @param mimic          : source position of particle (simulation change)
     * @param temp_container : temporary container for processing
     */
    private static void calculateDistance(final Velocity velocity, final int[] destinations, final int[] mimic, final int[] temp_container)
    {
        for(int i = -1, is = mimic.length; ++i < is; )
        {
            int value = mimic[i];
            temp_container[value] = i;
        }

        for(int counter_destination = -1, destination_size = destinations.length; ++counter_destination < destination_size; )
        {
            final int destination = destinations[counter_destination];

            /*
            * If position is not in correct order
            * */
            if(counter_destination != temp_container[destination])
            {
                velocity.set(counter_destination, temp_container[destination]);

                /*
                 * Swap Mimic
                 * */
                mimic[temp_container[destination]] = mimic[counter_destination];

                /*
                 * Swap Temp Container
                 * */
                temp_container[mimic[counter_destination]] = temp_container[destination];
            }
        }
    }

    @NotNull public static String toString(final Velocity[] velocities, int indent)
    {
        String storedIndent = "";
        for(int i = indent; --i >= 0; )
        {
            storedIndent += '\t';
        }

        StringBuilder sb = new StringBuilder();
        for(int i = -1, is = velocities.length; ++i < is; )
        {
            sb.append(String.format("%s%14s : %s\n", storedIndent, String.format("%s [%d]", "Velocity", i), velocities[i]));
        }
        return sb.toString();
    }

    @Override public void set(@NotNull Transposition data)
    {
        this.set(data.getSource(), data.getDestination());
    }

    /**
     * Insert Transposition to Velocity
     *
     * @param source      : Source of index
     * @param destination : Destination of index
     */
    public void set(int source, int destination)
    {
        super.list[++super.counter].set(source, destination);
    }

    /**
     * Check whether velocity is empty or not
     *
     * @return velocity empty state
     */
    public boolean isEmpty()
    {
        return super.counter == -1;
    }

    public String toString()
    {
        if(this.isEmpty())
        {
            return "[\u2205]";
        }
        else
        {
            StringBuilder sb = new StringBuilder();
            sb.append('[');
            sb.append(super.list[0]);
            for(int i = 0, is = super.counter + 1; ++i < is; )
            {
                sb.append(", ");
                sb.append(super.list[i]);
            }
            sb.append(']');
            return sb.toString();
        }
    }
}
