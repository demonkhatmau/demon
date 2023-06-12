package com.demon.template.holder;

import java.util.List;

public interface IValueHolderCollector <I,O> {
    List<O> collect(I input);
}
