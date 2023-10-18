package com.rjgconfeccoes.api.client;

public interface OnDataFetchedListener<T> {
    void onDataFetched(T data);

    void onCancelled(Exception ex);
}
