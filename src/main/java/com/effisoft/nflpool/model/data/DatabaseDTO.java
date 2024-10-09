package com.effisoft.nflpool.model.data;

public class DatabaseDTO<T> {
    public String getId() {
        return id;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public T getDocument() {
        return document;
    }

    public void setDocument(T document) {
        this.document = document;
    }

    private final String id;
    private final String collectionName;
    private T document;

    public DatabaseDTO(T document, String collectionName, String id) {
        this.id = id;
        this.collectionName = collectionName;
        this.document = document;
    }
}
