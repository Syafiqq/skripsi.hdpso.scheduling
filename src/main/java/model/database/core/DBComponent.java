package model.database.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/*
 * This <skripsi.hdpso.scheduling> project in package <model.database.core> created by :
 * Name         : syafiq
 * Date / Time  : 23 October 2016, 9:49 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */
@SuppressWarnings(value = {"WeakerAccess", "unused"}) public abstract class DBComponent
{
    public Connection        connection;
    public PreparedStatement statement;
    public ResultSet         result_set;

    public DBComponent()
    {
        this.connection = null;
        this.statement = null;
        this.result_set = null;
    }

    public abstract void activateDatabase();

    public abstract void deActivateDatabase();
}
