package com.demon.report.template.impl;

import com.demon.report.template.IValueHolderTranformer;

public class DefaultStringValueHolderBuilder implements IValueHolderTranformer<String> {
    @Override
    public String transform(String source) {
        return new StringBuilder()
                .append("\\{\\{")
                .append(source)
                .append("}}").toString();
    }

}
