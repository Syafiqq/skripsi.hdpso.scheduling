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

    public int max_particle = 0;
    public int max_epoch    = 0;

    public double bLoc_min  = 0.0;
    public double bLoc_max  = 0.0;
    public double bGlob_min = 0.0;
    public double bGlob_max = 0.0;
    public double bRand_min = 0.0;
    public double bRand_max = 0.0;

    public int total_core;

    private Setting()
    {
    }

    public static Setting getInstance()
    {
        return ourInstance;
    }
}
