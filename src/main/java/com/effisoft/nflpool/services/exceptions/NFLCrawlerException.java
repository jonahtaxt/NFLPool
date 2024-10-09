package com.effisoft.nflpool.services.exceptions;

public class NFLCrawlerException extends Exception {
    public NFLCrawlerException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
