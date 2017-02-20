package model.custom.javafx.scene.control;
/*
 * This <skripsi.hdpso.scheduling> project created by : 
 * Name         : Muhammad Syafiq
 * Date / Time  : 24 January 2017, 7:29 AM.
 * Email        : syafiq.rezpector@gmail.com
 * Github       : syafiqq
 */

import javafx.scene.control.Label;

@SuppressWarnings("unused")
public class CoordinatedLabel extends Label {

    private int row, column;

    public CoordinatedLabel(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public CoordinatedLabel(String text, int row, int column) {
        super(text);
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }
}
