package model.method.pso.hdpso.component;

import model.method.pso.hdpso.core.VelocityCalculator;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.method.pso.hdpso.component> created by : 
 * Name         : syafiq
 * Date / Time  : 22 November 2016, 10:17 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings({"WeakerAccess", "unused"}) public class Setting
{
    public static final VelocityCalculator PURE_PSO;
    public static final VelocityCalculator PTVPSO;
    public static final VelocityCalculator PURE_PTVPSO;
    private static      Setting            ourInstance;

    static
    {
        /*
        * operator : desc
        * 0 = random val [0 ... 1]
        * 1 = (max {loc, glob, rand})
        * */
        PURE_PSO = new VelocityCalculator()
        {
            @Override public double calculateLoc(double... operator)
            {
                return operator[0] * operator[1];
            }

            @Override public double calculateGlob(double... operator)
            {
                return operator[0] * operator[1];
            }

            @Override public double calculateRand(double... operator)
            {
                return operator[0] * operator[1];
            }
        };
        /*
        * operator : desc
        * 0 = random val [0 ... 1]
        * 1 = (max {loc, glob, rand})
        * 2 = (min {loc, glob, rand})
        * 3 = current epoch
        * 4 = max epoch
        * */
        PTVPSO = new VelocityCalculator()
        {
            @Override public double calculateLoc(double... operator)
            {
                return operator[0] * (((operator[1] - operator[2]) * (operator[3] / operator[4])) + operator[2]);
            }

            @Override public double calculateGlob(double... operator)
            {
                return operator[0] * (operator[1] - ((operator[1] - operator[2]) * (operator[3] / operator[4])));
            }

            @Override public double calculateRand(double... operator)
            {
                return operator[0] * (operator[1] - ((operator[1] - operator[2]) * (operator[3] / operator[4])));
            }
        };
        /*
        * tv_weight = time variant weight
        * operator : desc
        * 1 = (max {loc, glob, rand})
        * 2 = (min {loc, glob, rand})
        * 3 = current epoch
        * 4 = max epoch
        * */
        PURE_PTVPSO = new VelocityCalculator()
        {
            @Override public double calculateLoc(double... operator)
            {
                return Setting.ourInstance.tv_weight * (((operator[1] - operator[2]) * (operator[3] / operator[4])) + operator[2]);
            }

            @Override public double calculateGlob(double... operator)
            {
                return Setting.ourInstance.tv_weight * (operator[1] - ((operator[1] - operator[2]) * (operator[3] / operator[4])));
            }

            @Override public double calculateRand(double... operator)
            {
                return Setting.ourInstance.tv_weight * (operator[1] - ((operator[1] - operator[2]) * (operator[3] / operator[4])));
            }
        };
    }

    private int max_particle;
    private int max_epoch;

    private double bLoc_min;
    private double bLoc_max;
    private double bGlob_min;
    private double bGlob_max;
    private double bRand_min;
    private double bRand_max;
    private double tv_weight;

    private int                total_core;
    private int                total_pool;
    private boolean            multi_process;
    private int                window_size;
    private VelocityCalculator calculator;

    private Setting()
    {
        this.max_epoch = 0;
        this.bLoc_min = 0.0;
        this.bLoc_max = 0.0;
        this.bGlob_min = 0.0;
        this.bGlob_max = 0.0;
        this.bRand_min = 0.0;
        this.bRand_max = 0.0;
        this.tv_weight = 1.0;

        this.max_particle = 0;
        this.setTotalCore(Runtime.getRuntime().availableProcessors());
        this.multi_process = false;
        this.window_size = Integer.MAX_VALUE;
        this.calculator = Setting.PURE_PSO;
    }

    public static synchronized Setting getInstance()
    {
        if(Setting.ourInstance == null)
        {
            Setting.ourInstance = new Setting();
        }
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

    public void setMultiProcess(boolean multi_process)
    {
        this.multi_process = multi_process;
    }

    public int getWindowSize()
    {
        return this.window_size;
    }

    public void setWindowSize(int window_size)
    {
        this.window_size = window_size;
    }

    public VelocityCalculator getCalculator()
    {
        return this.calculator;
    }

    public void setCalculator(VelocityCalculator calculator)
    {
        this.calculator = calculator;
    }

    public double getTimeVariantWeight()
    {
        return this.tv_weight;
    }

    public void setTimeVariantWeight(double tv_weight)
    {
        this.tv_weight = tv_weight;
    }
}
