package com.example.travelapp.core.utils.callback;

import androidx.annotation.Nullable;

public interface IGlobalCallback<T> {
    void executeCallback(@Nullable T args);
}
