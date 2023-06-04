package com.demon.report.template.impl;

import com.demon.report.template.IValueHolderTranformer;
import org.apache.commons.lang3.StringUtils;

public class DefaultStringValueHolderResolver implements IValueHolderTranformer<String> {
    @Override
    public String transform(String source) {
        String result = source.replace("{{", StringUtils.EMPTY);
        return  result.replace("}}" , StringUtils.EMPTY);
    }
}
