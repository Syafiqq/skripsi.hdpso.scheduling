package model.method.pso.hdpso.component;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.component> created by : 
 * Name         : syafiq
 * Date / Time  : 22 November 2016, 10:17 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings({"WeakerAccess", "unused"}) public class Setting
{
    private static Setting ourInstance = new Setting();

    private int max_particle = 0;
    private int max_epoch    = 0;

    private double bLoc_min  = 0.0;
    private double bLoc_max  = 0.0;
    private double bGlob_min = 0.0;
    private double bGlob_max = 0.0;
    private double bRand_min = 0.0;
    private double bRand_max = 0.0;

    private int     total_core;
    private int     total_pool;
    private boolean multi_process;

    private Setting()
    {
        this.total_core = Runtime.getRuntime().availableProcessors();
        this.multi_process = false;
    }

    public static Setting getInstance()
    {
        return Setting.ourInstance;
    }

    public int getTotalCore()
    {
        return this.total_core;
    }

    public void setTotalCore(int total_core)
    {
        final int host_core = Runtime.getRuntime().availableProcessors();
        this.total_core = total_core > host_core ? host_core : total_core;
        this.total_core = this.total_core < 1 ? 1 : this.total_core;
        this.total_pool = ((this.max_particle - this.total_core) < this.total_core ? this.total_core : this.max_particle);
    }

    public int getMaxParticle()
    {
        return this.max_particle;
    }

    public void setMaxParticle(int max_particle)
    {
        this.max_particle = max_particle;
        this.setTotalCore(this.total_core);
    }

    public int getMaxEpoch()
    {
        return this.max_epoch;
    }

    public void setMaxEpoch(int max_epoch)
    {
        this.max_epoch = max_epoch;
    }

    public double getbLocMin()
    {
        return this.bLoc_min;
    }

    public void setbLocMin(double bLoc_min)
    {
        this.bLoc_min = bLoc_min;
    }

    public double getbLocMax()
    {
        return this.bLoc_max;
    }

    public void setbLocMax(double bLoc_max)
    {
        this.bLoc_max = bLoc_max;
    }

    public double getbGlobMin()
    {
        return this.bGlob_min;
    }

    public void setbGlobMin(double bGlob_min)
    {
        this.bGlob_min = bGlob_min;
    }

    public double getbGlobMax()
    {
        return this.bGlob_max;
    }

    public void setbGlobMax(double bGlob_max)
    {
        this.bGlob_max = bGlob_max;
    }

    public double getbRandMin()
    {
        return this.bRand_min;
    }

    public void setbRandMin(double bRand_min)
    {
        this.bRand_min = bRand_min;
    }

    public double getbRandMax()
    {
        return this.bRand_max;
    }

    public void setbRandMax(double bRand_max)
    {
        this.bRand_max = bRand_max;
    }

    public int getTotalPool()
    {
        return this.total_pool;
    }

    public boolean getMultiProcess()
    {
        return this.multi_process;
    }

    public void setMultiProcess(boolean multiProcess)
    {
        this.multi_process = multiProcess;
    }
}
