package model.util.pattern.observer;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 16 January 2017, 8:53 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import model.database.component.DBSchool;
import org.jetbrains.annotations.Nullable;

import java.util.Observable;
import java.util.Observer;

public class ObservableDBSchool extends Observable {
    @Nullable
    private DBSchool school;

    public ObservableDBSchool(@Nullable DBSchool school) {
        this.setSchool(school);
    }

    @Nullable
    public DBSchool getSchool() {
        return school;
    }

    public void setSchool(@Nullable DBSchool school) {
        this.school = school;
        this.update();
    }

    public void update() {
        this.setChanged();
        this.notifyObservers();
    }

    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
        o.update(this, null);
    }
}
