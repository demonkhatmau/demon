package com.demon.template.holder.impl;

import com.demon.template.holder.IValueHolderCollector;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringValueHolderFromCollectionCollector implements IValueHolderCollector<Collection<String>, String> {
    private static final Pattern DEFAULT_HOLDER_PATTERN =  Pattern.compile("\\{\\{\\S+}}");
    private Pattern holderPattern;

    public StringValueHolderFromCollectionCollector() {
        this.holderPattern = DEFAULT_HOLDER_PATTERN;
    }

    public StringValueHolderFromCollectionCollector(Pattern holderPattern) {
        this.holderPattern = holderPattern;
    }

    @Override
    public List<String> collect(Collection<String> input) {
        if(CollectionUtils.isEmpty(input)) {
            Collections.emptyList();
        }
        List<String> result = new ArrayList<>();
        input.stream().forEach(line -> {
            Matcher matcher = holderPattern.matcher(line);
            while (matcher.find()) {
                String matched = matcher.group(0);
                result.add(matched);
            }
        });
        return result;
    }
}
