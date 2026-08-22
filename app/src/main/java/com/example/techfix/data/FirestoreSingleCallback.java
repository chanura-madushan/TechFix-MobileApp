package com.example.techfix.data;

public interface FirestoreSingleCallback<T> {
    void onSuccess(T result);
    void onFailure(Exception e);
}