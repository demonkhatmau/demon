package com.demon.template.holder.impl;

import com.demon.template.holder.IValueHolderTranformer;
import org.apache.commons.lang3.StringUtils;

public class DefaultStringValueHolderResolver implements IValueHolderTranformer<String> {
    @Override
    public String transform(String source) {
        String result = source.replace("{{", StringUtils.EMPTY);
        return  result.replace("}}" , StringUtils.EMPTY);
    }
}
