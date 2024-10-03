package com.effisoft.nflpool.interfaces;

import com.effisoft.nflpool.model.data.DatabaseDTO;

public interface DatabaseAccess {
    public <T> void Upsert(DatabaseDTO<T> documentDTO);
}
