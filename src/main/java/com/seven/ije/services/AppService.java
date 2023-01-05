package com.seven.ije.services;

import com.seven.ije.models.requests.AppRequest;

import java.util.Set;

public interface AppService<T extends Record, R extends AppRequest>{
    Set<? extends T> getAll();
    T get(Object idOrRequestBody);

    T create(R request);
    void delete(Object id);
    T update(R request);
}
