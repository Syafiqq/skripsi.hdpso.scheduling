package model.method.pso.hdpso.component;

import java.util.Iterator;
import model.util.list.HList;
import org.jetbrains.annotations.NotNull;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.component> created by : 
 * Name         : syafiq
 * Date / Time  : 15 November 2016, 11:00 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings({"unused", "WeakerAccess"}) public class Velocity extends HList<ActivableTransposition>
{
    /**
     * Constructor of Velocity
     *
     * @param expected_size = Size Maximum Of Velocity
     */
    public Velocity(int expected_size)
    {
        super(new ActivableTransposition[expected_size]);

        for(int c = -1; ++c < expected_size; )
        {
            super.list[c] = new ActivableTransposition();
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
        Velocity.getDistance(velocity, destination.getPosition(), source_mimic.getPosition(), temp_container.getPosition());
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
        for(@NotNull final Iterator<ActivableTransposition> iterator = source.iterator(); iterator.hasNext(); )
        {
            destination.set(iterator.next());
        }
    }

    /**
     * Reversing order of transposition of velocity
     *
     * @param velocity = velocity to be reversed
     */
    public static void reverseVelocity(final Velocity velocity)
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
    @SuppressWarnings("Duplicates") public static void additionVelocity(final Velocity destination, final Velocity source)
    {
        final ActivableTransposition[] vel_destination  = destination.list;
        final ActivableTransposition[] vel_source       = source.list;
        final int                      size_destination = destination.size();
        final int                      size_source      = source.size();
        boolean                        need_arrange     = false;

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
            * @param vel_destination = A_B_C_____
            * */
            for(int counter_destination = size_destination * 2, counter_lookup = size_destination, separation_length = size_destination - 1; --separation_length >= 0; )
            {
                vel_destination[counter_destination -= 2].set(vel_destination[--counter_lookup]);
            }

            for(int counter_destination = -1, counter_lookup = -1, insertion_length = size_destination; --insertion_length >= 0; )
            {
                try
                {
                    /*
                    * @param vel_destination = A_B_C_____
                    * @param vel_source      = ACDEF_____
                    *
                    * If same in current
                    * @param vel_destination = AaB_C_____
                    *                          o
                    * @param vel_destination = __B_C_____
                    *                           o
                    * */
                    if(vel_source[++counter_lookup].equalsTransposition(vel_destination[++counter_destination]))
                    {
                        vel_destination[counter_destination].setActive(false);
                        vel_destination[++counter_destination].setActive(false);
                        need_arrange = true;
                    }

                    /*
                    * @param vel_destination = __B_C_____
                    * @param vel_source      = _CDEF_____
                    *
                    * If same in front of current
                    * @param vel_destination = __BcC_____
                    *                            o
                    * @param vel_destination = __B_______
                    *                             o
                    */
                    else if(vel_source[counter_lookup].equalsTransposition(vel_destination[counter_destination + 2]))
                    {
                        vel_destination[++counter_destination].setActive(false);
                        vel_destination[counter_destination + 1].setActive(false);
                        need_arrange = true;
                    }

                    /*
                    * @param vel_destination = __B_______
                    * @param vel_source      = __DEF_____
                    *
                    * If Not Same
                    * @param vel_destination = __B__d____
                    *                               o
                    */
                    else
                    {
                        vel_destination[++counter_destination].set(vel_source[counter_lookup]);
                    }
                }
                catch(ArrayIndexOutOfBoundsException ignored)
                {
                    vel_destination[++counter_destination].set(vel_source[counter_lookup]);
                }
            }

            /*
            * @param vel_destination = ABC_______
            * @param vel_source      = DEFGH_____
            *
            * Put Remaining source
            * @param vel_destination = ABCDEFgh__
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
            * @param vel_destination = ACD___EF__
            * */
            for(int counter_destination = size_source + size_destination, counter_lookup = size_destination, remaining_length = size_destination - size_source; --remaining_length >= 0; )
            {
                vel_destination[--counter_destination].set(vel_destination[--counter_lookup]);
            }

            /*
            * Rearrange remaining destination
            * @param vel_destination = A_C_D_EF__
            * */
            for(int counter_destination = size_source * 2, counter_lookup = size_source, separation_length = size_source - 1; --separation_length >= 0; )
            {
                vel_destination[counter_destination -= 2].set(vel_destination[--counter_lookup]);
            }

            for(int counter_destination = -1, counter_lookup = -1, insertion_length = size_source; --insertion_length >= 0; )
            {
                /*
                * @param vel_destination = A_C_D_EF__
                * @param vel_destination = ADC_______
                *
                * If same in current
                * @param vel_destination = AaC_D_EF__
                *                          o
                * @param vel_destination = __C_D_EF__
                *                           o
                * */
                if(vel_source[++counter_lookup].equalsTransposition(vel_destination[++counter_destination]))
                {
                    vel_destination[counter_destination].setActive(false);
                    vel_destination[++counter_destination].setActive(false);
                    need_arrange = true;
                }

                /*
                * @param vel_destination = __C_D_EF__
                * @param vel_destination = ADC_______
                *
                * If same in current
                * @param vel_destination = __CdD_EF__
                *                            o
                * @param vel_destination = __C___EF__
                *                             o
                * */
                else if(vel_source[counter_lookup].equalsTransposition(vel_destination[counter_destination + 2]))
                {
                    vel_destination[++counter_destination].setActive(false);
                    vel_destination[counter_destination + 1].setActive(false);
                    need_arrange = true;
                }

                /*
                * @param vel_destination = __C_D_EF__
                * @param vel_destination = ADC_______
                *
                * If same in current
                * @param vel_destination = __C___EF__
                *                              o
                * @param vel_destination = _____CEF__
                *                               o
                * */
                else
                {
                    vel_destination[++counter_destination].set(vel_source[counter_lookup]);
                }
            }
        }
        if(need_arrange)
        {
            int counter_repair = -1;
            for(int repair_lookup = -1, velocity_size = size_destination + size_source; ++repair_lookup < velocity_size; )
            {
                if((vel_destination[repair_lookup].isActive()) && (repair_lookup != ++counter_repair))
                {
                    vel_destination[counter_repair].set(vel_destination[repair_lookup]);
                }
            }
            destination.moveTo(counter_repair + 1);
        }
        else
        {
            destination.moveTo(size_destination + size_source);
        }
    }

    /**
     * Multiplication of velocity given coefficient
     *
     * @param coefficient    = coefficient to be multiplicate
     * @param velocity       = velocity
     * @param temp_container = container of velocity if needed for processing
     */
    public static void multiplicationVelocity(double coefficient, final Velocity velocity, final Velocity temp_container)
    {
        if(!velocity.isEmpty())
        {
            if(coefficient == 0.0)
            {
                velocity.reset();
            }
            else if(Math.abs(coefficient) <= 1.0)
            {
                if(coefficient < 0.0)
                {
                    reverseVelocity(velocity);
                }
                velocity.backward((int) Math.floor(velocity.size() * (1.0 - Math.abs(coefficient))));
            }
            else
            {
                if(coefficient < 0.0)
                {
                    reverseVelocity(velocity);
                    coefficient = Math.abs(coefficient);
                }
                cloneVelocity(velocity, temp_container);
                int natural_coefficient = (int) Math.floor(coefficient);
                coefficient -= natural_coefficient;
                for(int i = 1; i < natural_coefficient; ++i)
                {
                    Velocity.additionVelocity(velocity, temp_container);
                }
                //Keep that line below
                Velocity.multiplicationVelocity(coefficient, temp_container, null);
                Velocity.additionVelocity(velocity, temp_container);
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
    private static void getDistance(final Velocity velocity, final int[] destinations, final int[] mimic, final int[] temp_container)
    {
        for(int i = -1, is = mimic.length; ++i < is; )
        {
            int value = mimic[i];
            if(value != 0)
            {
                temp_container[value] = i;
            }
        }

        for(int counter_destination = -1, destination_size = destinations.length; ++counter_destination < destination_size; )
        {
            final int destination = destinations[counter_destination];
            if(destination != 0)
            {
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
    }

    public static String toString(final Velocity[] velocities, int indent)
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

    /**
     * Insert Transposition with checking index first
     *
     * @param transposition : transposition to be inserted
     */
    public void checkAndSet(final Transposition transposition)
    {
        if((super.counter != -1) && (super.list[super.counter].equalsTransposition(transposition)))
        {
            super.backward(1);
        }
        else
        {
            super.list[++super.counter].set(transposition);
        }
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
