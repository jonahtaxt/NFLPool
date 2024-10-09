package com.effisoft.nflpool.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CouchbaseConfiguration {
    public String getConnectionString() {
        return connectionString;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Value("${couchbase.connectionstring}")
    private String connectionString;

    @Value(("${couchbase.username}"))
    private String username;

    @Value("${couchbase.password}")
    private String password;
}
