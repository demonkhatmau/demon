package com.demon.template.holder.impl;

import com.demon.template.holder.IValueHolderTranformer;

public class DefaultStringValueHolderBuilder implements IValueHolderTranformer<String> {
    @Override
    public String transform(String source) {
        return new StringBuilder()
                .append("\\{\\{")
                .append(source)
                .append("}}").toString();
    }

}
