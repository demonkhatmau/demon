package com.demon.report.model.test;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class PrimaryModel {
    private long id;
    private String name;
    private InnerModel innerModel;
    private int[] arrInt;
    private Collection<String> listStr = new ArrayList<>();
    private Map<String, InnerModel> mapInnerModel = new ConcurrentHashMap<>();
    private Set<String> setString = new HashSet<>();

    private class InnerModel {
        private long innerId;
        private String innerName;
    }
}
