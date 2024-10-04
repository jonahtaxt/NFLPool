package com.effisoft.nflpool.interfaces;

import com.effisoft.nflpool.model.ParticipantWeekData;

import java.io.InputStream;

public interface FileReader {
    ParticipantWeekData readParticipants(InputStream fileStream);
}
