package com.effisoft.nflpool.interfaces;

import com.effisoft.nflpool.model.Participant;

import java.util.List;

public interface FileReader {
    public List<Participant> readParticipants(int year, int week);
}
