package com.effisoft.nflpool.interfaces;

import com.effisoft.nflpool.model.data.DatabaseDTO;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DatabaseAccess {
    <T> void Upsert(DatabaseDTO<T> documentDTO);
    <T> CompletableFuture<T> GetById(Class<T> classType, String repositoryName, String id);
}
