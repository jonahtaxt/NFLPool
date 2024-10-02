package com.effisfot.nflpool.model;

public class Falcons extends Team {
    public Falcons() {
        this.setPoolName("ATLANTA");
        this.setLogo("data:image/png;base64,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");
    }
}
