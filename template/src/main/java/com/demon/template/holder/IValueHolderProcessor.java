package com.demon.template.holder;

import java.util.Map;

public interface IValueHolderProcessor<T> {
    T process(T data, Map<String, T> mapHoldersAndValues);
}
