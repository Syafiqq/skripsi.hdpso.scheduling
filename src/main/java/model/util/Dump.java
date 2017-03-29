package model.util;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 23 January 2017, 6:43 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import model.database.component.metadata.DBMSchool;

public class Dump {
    private static DBMSchool school;

    public static DBMSchool schoolMetadata()
    {
        System.err.println("Caution !");
        System.err.println("This is for Testing Only");
        System.err.println("I Warned You");
        //this.schoolMetadata = new DBTimetable(1, "Program Teknologi Informasi dan Ilmu Komputer Universitas Brawijaya", "PTIIK UB", "Jl. Veteran No.8, Ketawanggede, Kec. Lowokwaru, Kota Malang, Jawa Timur", "2013 - 2014", 0, 17, 5);
        Dump.school = new DBMSchool(1, "Program Teknologi Informasi dan Ilmu Komputer Universitas Brawijaya", "2013 - 2014", 0, 17, 5);
        //Dump.school = new DBMSchool(2, "A", "B", 1, 17, 5);
        return Dump.school;
    }
}
