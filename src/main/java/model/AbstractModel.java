package model;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 21 January 2017, 6:15 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SuppressWarnings("WeakerAccess")
public abstract class AbstractModel {
    @SuppressWarnings("NullableProblems")
    @NotNull
    public Connection connection;
    @NotNull
    private final String path;

    public AbstractModel(@NotNull String dbPath) throws SQLException {
        this.path = dbPath;
        this.reconnect();
    }

    public AbstractModel(@NotNull final AbstractModel model) {
        this.path = model.path;
        this.connection = model.connection;
    }

    public void reconnect() throws SQLException {
        this.connection = DriverManager.getConnection(this.path);
    }

    public void close() throws SQLException {
        this.connection.close();
    }

    public boolean isClosed() throws SQLException {
        return this.connection.isClosed();
    }
}
