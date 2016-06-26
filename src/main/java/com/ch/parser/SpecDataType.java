package com.ch.parser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sahil on 6/25/16.
 */
public enum SpecDataType {
    TEXT,
    BOOLEAN,
    INTEGER;

    private static final Map<String, SpecDataType> NAME_TO_VALUE = new HashMap<>();

    static {
        Arrays.stream(SpecDataType.values()).forEach(e -> NAME_TO_VALUE.put(e.toString(), e));
    }

    public static SpecDataType fromString(final String name) {
        return NAME_TO_VALUE.get(name);
    }

}
