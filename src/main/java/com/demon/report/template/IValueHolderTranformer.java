package com.demon.report.template;

public interface IValueHolderTranformer<T> {
    T transform(T source);
}
