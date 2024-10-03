package com.effisoft.nflpool.services;

import com.couchbase.client.java.*;
import com.effisoft.nflpool.interfaces.DatabaseAccess;
import com.effisoft.nflpool.model.data.DatabaseDTO;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class CouchbaseAccess implements DatabaseAccess {

    static String connectionString = "couchbase://couchbase.cloudsolo.net";
    static String username = "";
    static String password = "";
    static String bucketName = "NFLPool-bucket";

    @Override
    public <T> void Upsert(DatabaseDTO<T> documentDTO) {
        Collection weekDataCollection = GetCollection(documentDTO.getCollectionName());
        weekDataCollection.upsert(documentDTO.getId(), documentDTO.getDocument());
    }

    private Collection GetCollection(String collectionName) {
        Cluster cluster = Cluster.connect(connectionString, username, password);

        Bucket bucket = cluster.bucket(bucketName);
        bucket.waitUntilReady(Duration.ofSeconds(60));
        Scope scope = bucket.scope("WeeklyResults-scope");
        return scope.collection("ParticipantWeekData");
    }
}
