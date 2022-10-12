package com.seven.railroadapp.services;

import java.util.Set;

public interface Service{
    Set<? extends Record> getAll();
    Record get(Object id);
    Record create(Record recordObject);
    Boolean delete(Object id);
    Record update(Record recordObject);
}
