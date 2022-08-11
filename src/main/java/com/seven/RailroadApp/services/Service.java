package com.seven.RailroadApp.services;

import com.seven.RailroadApp.models.records.Recordable;

import java.util.Set;

public interface Service {
    Set<Object> getAll();
    Recordable get(Object id);
    Boolean create(Recordable recordObject);
    Recordable delete(Object id);
    Recordable update(Recordable recordObject);
}
