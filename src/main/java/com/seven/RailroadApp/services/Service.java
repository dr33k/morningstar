package com.seven.RailroadApp.services;

import java.util.Set;

public abstract class Service {
    abstract Set<? extends Record> getAll();
    abstract Record get(Object id);
    Record create(Record recordObject){return null;}
    Boolean delete(Object id){return false;}
    abstract Record update(Record recordObject);
}
