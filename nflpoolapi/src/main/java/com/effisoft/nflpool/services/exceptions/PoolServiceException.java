package com.effisoft.nflpool.services.exceptions;

public class PoolServiceException extends Exception {
    public PoolServiceException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
