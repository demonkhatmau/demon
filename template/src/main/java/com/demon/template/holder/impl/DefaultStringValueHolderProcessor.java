package com.demon.template.holder.impl;

import com.demon.template.holder.IValueHolderProcessor;
import com.demon.template.holder.IValueHolderTranformer;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultStringValueHolderProcessor implements IValueHolderProcessor<String> {
    private final static Pattern DEFAULT_HOLDER_PATTERN = Pattern.compile("\\{\\{\\S+}}");

    private Pattern holderPattern;
    private IValueHolderTranformer<String> holderBuilder;
    private IValueHolderTranformer<String> holderResolver;

    public DefaultStringValueHolderProcessor() {
        this.holderPattern = DEFAULT_HOLDER_PATTERN;
        this.holderBuilder = new DefaultStringValueHolderBuilder();
        this.holderResolver = new DefaultStringValueHolderResolver();
    }

    public DefaultStringValueHolderProcessor(Pattern holderPattern, IValueHolderTranformer<String> builder, IValueHolderTranformer<String> resolver) {
        this.holderPattern = holderPattern;
        this.holderBuilder = builder;
        this.holderResolver = resolver;
    }
    @Override
    public String process(String data, Map<String, String> mapHoldersAndValues) {
        Matcher matcher = holderPattern.matcher(data);
        String result = data;
        while (matcher.find()) {
            String currentHolder = matcher.group(0);
            if(StringUtils.isNotBlank(currentHolder)){
                String resolvedHolder = holderResolver.transform(currentHolder);
                if(mapHoldersAndValues.containsKey(currentHolder)) {
                    result = result.replaceAll(holderBuilder.transform(resolvedHolder), mapHoldersAndValues.get(currentHolder));
                }
            }
        }
        return result;
    }
}
