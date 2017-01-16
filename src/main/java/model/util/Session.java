package model.util;/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 16 January 2017, 8:30 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("SuspiciousMethodCalls")
public class Session {
    private static Session ourInstance = new Session();

    @NotNull
    private final Object2ObjectMap<String, Object> sessions;

    private Session() {
        sessions = new Object2ObjectLinkedOpenHashMap<>();
    }

    public static Session getInstance() {
        return ourInstance;
    }

    public Object put(String key, Object value) {
        return sessions.put(key, value);
    }

    public Object get(Object key) {
        return sessions.get(key);
    }

    public boolean containsKey(Object key) {
        return sessions.containsKey(key);
    }
}
