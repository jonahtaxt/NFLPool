package com.effisoft.nflpool.model;

public class Lions extends Team {
    public Lions() {
        this.setPoolName("DETROIT");
        this.setLogo("data:image/png;base64,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");
    }
}