package com.effisoft.nflpool.services;

import com.couchbase.client.java.*;
import com.effisoft.nflpool.interfaces.DatabaseAccess;
import com.effisoft.nflpool.model.data.DatabaseDTO;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class CouchbaseAccess implements DatabaseAccess {

    private Cluster cluster;

    static String connectionString = "couchbase://couchbase.cloudsolo.net";
    static String username = "";
    static String password = "";
    static String bucketName = "NFLPool-bucket";

    public CouchbaseAccess() {
        this.cluster = Cluster.connect(connectionString, username, password);
    }

    private Collection GetCollection(String collectionName) {
        this.cluster = Cluster.connect(connectionString, username, password);
        Bucket bucket = this.cluster.bucket(bucketName);
        bucket.waitUntilReady(Duration.ofSeconds(10));
        Scope scope = bucket.scope("WeeklyResults-scope");
        return scope.collection("ParticipantWeekData");
    }

    @Override
    public <T> void Upsert(DatabaseDTO<T> documentDTO) {
        Collection weekDataCollection = GetCollection(documentDTO.getCollectionName());
        weekDataCollection.upsert(documentDTO.getId(), documentDTO.getDocument());
        this.cluster.close();
    }

    public <T> T GetById(Class<T> classType, String repositoryName, String id) {
        Collection weekDataCollection = GetCollection(repositoryName);
        return weekDataCollection.get(id).contentAs(classType);
    }
}
