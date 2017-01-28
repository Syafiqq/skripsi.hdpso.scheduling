package model.database.component;

import model.database.component.metadata.DBMSchool;
import model.method.pso.hdpso.core.VelocityCalculator;
import org.apache.commons.lang3.builder.ToStringBuilder;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.component> created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 26 January 2017, 7:05 PM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
public class DBParameter
{
    private final int                id;
    private final DBMSchool          school;
    private       double             gLobMin;
    private       double             gLobMax;
    private       double             bLocMin;
    private       double             bLocMax;
    private       double             bRandMin;
    private       double             bRandMax;
    private       int                iteration;
    private       int                particle;
    private       int                processor;
    private       VelocityCalculator method;
    private       boolean            isMultiThread;

    public DBParameter(final int id, DBMSchool school, double gLobMin, double gLobMax, double bLocMin, double bLocMax, double bRandMin, double bRandMax, int iteration, int particle, int processor, VelocityCalculator method, boolean isMultiThread)
    {
        this.id = id;
        this.school = school;
        this.gLobMin = gLobMin;
        this.gLobMax = gLobMax;
        this.bLocMin = bLocMin;
        this.bLocMax = bLocMax;
        this.bRandMin = bRandMin;
        this.bRandMax = bRandMax;
        this.iteration = iteration;
        this.particle = particle;
        this.processor = processor;
        this.method = method;
        this.isMultiThread = isMultiThread;
    }

    public int getId()
    {
        return id;
    }

    public DBMSchool getSchool()
    {
        return this.school;
    }

    public double getgLobMin()
    {
        return this.gLobMin;
    }

    public void setgLobMin(double gLobMin)
    {
        this.gLobMin = gLobMin;
    }

    public double getgLobMax()
    {
        return this.gLobMax;
    }

    public void setgLobMax(double gLobMax)
    {
        this.gLobMax = gLobMax;
    }

    public double getbLocMin()
    {
        return this.bLocMin;
    }

    public void setbLocMin(double bLocMin)
    {
        this.bLocMin = bLocMin;
    }

    public double getbLocMax()
    {
        return this.bLocMax;
    }

    public void setbLocMax(double bLocMax)
    {
        this.bLocMax = bLocMax;
    }

    public double getbRandMin()
    {
        return this.bRandMin;
    }

    public void setbRandMin(double bRandMin)
    {
        this.bRandMin = bRandMin;
    }

    public double getbRandMax()
    {
        return this.bRandMax;
    }

    public void setbRandMax(double bRandMax)
    {
        this.bRandMax = bRandMax;
    }

    public int getIteration()
    {
        return this.iteration;
    }

    public void setIteration(int iteration)
    {
        this.iteration = iteration;
    }

    public int getParticle()
    {
        return this.particle;
    }

    public void setParticle(int particle)
    {
        this.particle = particle;
    }

    public int getProcessor()
    {
        return this.processor;
    }

    public void setProcessor(int processor)
    {
        this.processor = processor;
    }

    public VelocityCalculator getMethod()
    {
        return this.method;
    }

    public void setMethod(VelocityCalculator method)
    {
        this.method = method;
    }

    public boolean isMultiThread()
    {
        return this.isMultiThread;
    }

    public void setMultiThread(boolean multiThread)
    {
        this.isMultiThread = multiThread;
    }

    @Override public String toString()
    {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("school", school.getId())
                .append("gLobMin", gLobMin)
                .append("gLobMax", gLobMax)
                .append("bLocMin", bLocMin)
                .append("bLocMax", bLocMax)
                .append("bRandMin", bRandMin)
                .append("bRandMax", bRandMax)
                .append("iteration", iteration)
                .append("particle", particle)
                .append("processor", processor)
                .append("method", method.toString())
                .append("isMultiThread", isMultiThread)
                .toString();
    }
}
