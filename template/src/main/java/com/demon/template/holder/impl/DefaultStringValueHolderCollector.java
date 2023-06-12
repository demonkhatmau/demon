package com.demon.template.holder.impl;

import com.demon.template.holder.IValueHolderCollector;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultStringValueHolderCollector implements IValueHolderCollector<String, String> {
    private static final Pattern DEFAULT_HOLDER_PATTERN =  Pattern.compile("\\{\\{\\S+}}");

    private Pattern holderPattern;

    public DefaultStringValueHolderCollector() {
        this.holderPattern = DEFAULT_HOLDER_PATTERN;
    }

    public DefaultStringValueHolderCollector(Pattern holderPattern) {
        this.holderPattern = holderPattern;
    }

    @Override
    public List<String> collect(String input) {
        if(StringUtils.isEmpty(input)) {
            return Collections.emptyList();
        }
        List<String> result = new ArrayList<>();
        Matcher matcher = holderPattern.matcher(input);
        while (matcher.find()) {
            String matched = matcher.group(0);
            System.out.println("matched: " + matched);
            result.add(matched);
        }
        return result;
    }
}
