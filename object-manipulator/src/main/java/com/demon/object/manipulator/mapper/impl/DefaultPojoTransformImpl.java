package com.demon.object.manipulator.mapper.impl;


import com.demon.commons.utils.ReflectionUtils;
import com.demon.object.manipulator.constant.MappingMode;
import com.demon.object.manipulator.mapper.IMapper;
import com.demon.object.manipulator.mapper.IMapperClassSetup;
import com.demon.object.manipulator.path.IToObjectBuilder;
import com.demon.object.manipulator.path.impl.DefaultToObjectBuilder;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;

public class DefaultPojoTransformImpl<I, O> implements IMapperClassSetup<I, O>, IMapper<I, O> {
    private Class<I> fromClass;

    private Class<O> toClass;

    private IToObjectBuilder<O> toObjectBuilder;

    private MappingMode mappingMode = MappingMode.DEFAULT_MODE;

    @Override
    public O mapTo(I input, Class<O> outputClass) throws IllegalAccessException, InstantiationException, IllegalArgumentException {
        if (null == input) {
            throw new IllegalArgumentException("input can not be null");
        }
        O result = toObjectBuilder.buildToObject(outputClass).get();
        Map<String, Field> mapOutputFields = ReflectionUtils.getMapFields(outputClass);
        Arrays.stream(input.getClass().getDeclaredFields()).forEach(f -> {
            if (!f.isAccessible()) {
                f.setAccessible(true);
            }
            if (mapOutputFields.containsKey(f.getName())) {
                try {
                    Object inputField = f.get(input);
                    mapOutputFields.get(f.getName()).set(result, inputField);
                } catch (IllegalAccessException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        return result;
    }

    @Override
    public void setUpMapper(Class<I> inputClass, Class<O> outputClass) {
        fromClass = inputClass;
        toClass = outputClass;
        toObjectBuilder = new DefaultToObjectBuilder<>();
    }

    @Override
    public MappingMode getMappingMode() {
        return this.mappingMode;
    }
}
