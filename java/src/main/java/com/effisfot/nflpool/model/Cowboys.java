package com.effisfot.nflpool.model;

public class Cowboys extends Team {
    public Cowboys() {
        this.setPoolName("DALLAS");
        this.setLogo("data:image/png;base64,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");
    }
}