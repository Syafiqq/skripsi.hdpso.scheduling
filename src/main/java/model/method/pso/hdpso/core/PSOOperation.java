package model.method.pso.hdpso.core;

import model.dataset.component.DSScheduleShufflingProperty;
import model.method.pso.hdpso.component.Data;
import model.method.pso.hdpso.component.ParticleBuilder;
import model.method.pso.hdpso.component.Velocity;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.core> created by : 
 * Name         : syafiq
 * Date / Time  : 22 November 2016, 4:07 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings({"unused", "WeakerAccess"}) public abstract class PSOOperation<TData extends Data, TVelocity extends Velocity, TParticle extends ParticleBuilder<TData, TVelocity>, TProperties extends DSScheduleShufflingProperty> implements SwarmInitialization, StoppingCondition, FitnessCalculator<TParticle>
{
    protected TParticle[] particles;
    protected TData       gBest;
    protected int         cEpoch;

    public void run()
    {
        this.initialize();
        while(!this.isConditionSatisfied())
        {
            for(TParticle particle : this.particles)
            {
                particle.assignPBest();
            }
            this.assignGBest();
            for(TParticle particle : this.particles)
            {
                particle.calculateVelocity(this.gBest, this.cEpoch, Integer.MAX_VALUE);
                particle.updateData();
                this.calculate(particle);
            }
            this.updateStoppingCondition();
        }
    }

    protected abstract void assignGBest();
}
