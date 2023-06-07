package com.demon.object.manipulator.path.impl;

import com.demon.commons.utils.ReflectionUtils;
import com.demon.object.manipulator.path.IToObjectBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DefaultToObjectBuilder<O> implements IToObjectBuilder<O> {
    private Class<?> defaultCollectionClass = ArrayList.class;

    private int defaultArrayLength = 2;

    @Override
    public Optional<O> buildToObject(Class<O> clazzToBuild) throws IllegalAccessException, InstantiationException {
        if (null == clazzToBuild) {
            throw new IllegalArgumentException("ToClass can not be null");
        }

        if (Object.class.equals(clazzToBuild)) {
            return Optional.empty();
        }

        if (ReflectionUtils.isPrimitiveWrapperOrString(clazzToBuild)) {
            return Optional.of((O) ReflectionUtils.getDefaultPrimitiveOrWrapperOrString(clazzToBuild.getName()));
        }

        if (ReflectionUtils.isCollection(clazzToBuild)) {
            return Optional.of((O) defaultCollectionClass.newInstance());
        }

        if (ReflectionUtils.isArray(clazzToBuild))  {
            return Optional.of((O) Array.newInstance(clazzToBuild.getComponentType(), defaultArrayLength));
        }

        Optional<O> result = Optional.of(clazzToBuild.newInstance());
        List<Field> listFields = ReflectionUtils.getAllFields(clazzToBuild);
        listFields.stream().forEach(f -> {
            DefaultToObjectBuilder defaultToObjectBuilder = new DefaultToObjectBuilder();
            try {
                System.out.println(f.getName() + ", " + f.getType().getName() + ", " + f.getDeclaringClass().getName());
                Optional<Object> currentFieldValue = defaultToObjectBuilder.buildToObject(f.getType());
                if (currentFieldValue.isPresent()) {
                    System.out.println(ToStringBuilder.reflectionToString(currentFieldValue.get()));
                    f.set(result.get(), currentFieldValue.get());
                }
            } catch (IllegalAccessException | InstantiationException e) {
            }
        });
        return result;
    }
}