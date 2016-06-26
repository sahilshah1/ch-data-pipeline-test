package com.ch.parser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sahil on 6/25/16.
 */
public enum DataType {
    TEXT,
    BOOLEAN,
    INTEGER;

    private static final Map<String, DataType> NAME_TO_VALUE = new HashMap<>();

    static {
        Arrays.stream(DataType.values()).forEach(e -> NAME_TO_VALUE.put(e.toString(), e));
    }

    public static DataType fromString(final String name) {
        return NAME_TO_VALUE.get(name);
    }

}
