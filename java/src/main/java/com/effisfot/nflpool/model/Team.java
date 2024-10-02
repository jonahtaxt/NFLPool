package com.effisfot.nflpool.model;

public abstract class Team {

    private String logo;
    private String poolName;

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public String getLogo() {
        return logo;
    }

    public String getPoolName() {
        return poolName;
    }

    protected void setLogo(String logo) {
        this.logo = logo;
    }

    protected void setPoolName(String poolName) {
        this.poolName = poolName;
    }

}
