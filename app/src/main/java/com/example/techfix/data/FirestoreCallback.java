package com.example.techfix.data;

import java.util.List;

public interface FirestoreCallback<T> {
    void onSuccess(List<T> result);
    void onFailure(Exception e);
}