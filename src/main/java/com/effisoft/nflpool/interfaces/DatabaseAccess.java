package com.effisoft.nflpool.interfaces;

import com.effisoft.nflpool.model.data.DatabaseDTO;

import java.util.List;

public interface DatabaseAccess {
    <T> void Upsert(DatabaseDTO<T> documentDTO);
    public <T> T GetById(Class<T> classType, String repositoryName, String id);
}
