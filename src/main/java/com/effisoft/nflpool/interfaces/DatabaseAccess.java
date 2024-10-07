package com.effisoft.nflpool.interfaces;

import com.effisoft.nflpool.model.data.DatabaseDTO;

import java.util.concurrent.CompletableFuture;

public interface DatabaseAccess {
    <T> void upsert(DatabaseDTO<T> documentDTO);
    <T> CompletableFuture<T> asyncGetById(Class<T> classType, String repositoryName, String id);
}
