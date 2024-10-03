package com.effisoft.nflpool.model;

public class ExcelLimits {
    public int getLastColumn() {
        return lastColumn;
    }

    public int getLastRow() {
        return lastRow;
    }

    private int lastColumn;
    private int lastRow;

    public ExcelLimits(int lastColumn, int lastRow) {
        this.lastColumn = lastColumn;
        this.lastRow = lastRow;
    }
}
