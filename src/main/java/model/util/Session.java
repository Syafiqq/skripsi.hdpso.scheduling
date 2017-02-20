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
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings({"SuspiciousMethodCalls", "unused"})
public class Session extends Observable{
    private static Session ourInstance = new Session();

    @NotNull
    private final Object2ObjectMap<String, Object> sessions;

    private Session() {
        sessions = new Object2ObjectLinkedOpenHashMap<>();
    }

    public static Session getInstance() {
        return ourInstance;
    }

    /** Associates the specified value with the specified key in this function (optional operation).
     *
     * @param key the key.
     * @param value the value.
     * @return the old value, or <code>null</code> if no value was present for the given key.
     * @see Map#put(Object, Object)
     */
    @Nullable public Object put(String key, Object value) {
        @Nullable final Object obj = this.sessions.put(key, value);
        this.setChanged();
        this.notifyObservers(key);
        return obj;
    }

    /** Removes this key and the associated value from this function if it is present (optional operation).
     *
     * @param key the key.
     * @return the old value, or <code>null</code> if no value was present for the given key.
     * @see Map#remove(Object)
     */
    @Nullable public Object remove(Object key) {
        @Nullable final Object obj = this.sessions.remove(key);
        this.setChanged();
        this.notifyObservers(key);
        return obj;
    }

    public Object get(Object key) {
        return this.sessions.get(key);
    }

    public boolean containsKey(Object key) {
        return this.sessions.containsKey(key);
    }

    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
        o.update(this, null);
    }
}
