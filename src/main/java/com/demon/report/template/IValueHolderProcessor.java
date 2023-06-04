package com.demon.report.template;

import java.util.Map;

public interface IValueHolderProcessor<T> {
    T process(T data, Map<String, T> mapHoldersAndValues);
}
