package com.effisoft.nflpool.services;

import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.java.*;
import com.couchbase.client.java.kv.GetResult;
import com.effisoft.nflpool.configuration.CouchbaseConfiguration;
import com.effisoft.nflpool.interfaces.DatabaseAccess;
import com.effisoft.nflpool.model.data.DatabaseDTO;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@Service
public class CouchbaseAccess implements DatabaseAccess {

    private final String connectionString;
    private final String username;
    private final String password;
    private final String bucketName = "NFLPool-bucket";

    public CouchbaseAccess(CouchbaseConfiguration couchbaseConfiguration) {
        this.connectionString = couchbaseConfiguration.getConnectionString();
        this.username = couchbaseConfiguration.getUsername();
        this.password = couchbaseConfiguration.getPassword();
    }

    private Collection GetCollection(Cluster cluster, String collectionName) {
        Bucket bucket = cluster.bucket(bucketName);
        bucket.waitUntilReady(Duration.ofSeconds(10));
        Scope scope = bucket.scope("WeeklyResults-scope");
        return scope.collection("ParticipantWeekData");
    }

    @Override
    public <T> void upsert(DatabaseDTO<T> documentDTO) {
        Cluster cluster = null;
        try {
            cluster = Cluster.connect(connectionString, username, password);
            Collection weekDataCollection = GetCollection(cluster, documentDTO.getCollectionName());
            weekDataCollection.upsert(documentDTO.getId(), documentDTO.getDocument());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if(cluster != null) {
                cluster.disconnect();
            }
        }
    }

    public <T> CompletableFuture<T> asyncGetById(Class<T> classType, String repositoryName, String id) {
        CompletableFuture<T> result = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {
            Cluster cluster = null;
            try {
                cluster = Cluster.connect(connectionString, username, password);
                Collection weekDataCollection = GetCollection(cluster, repositoryName);
                GetResult getResult = weekDataCollection.get(id);
                result.complete(weekDataCollection.get(id).contentAs(classType));
                return null;
            } catch (DocumentNotFoundException e) {
                result.complete(null);
                return null;
            } catch (Exception e) {
                result.completeExceptionally(new RuntimeException(e));
                return null;
            } finally {
                if(cluster != null) {
                    cluster.disconnect();
                }
            }
        });
        return result;
    }
}
