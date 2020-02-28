package com.cityfruit.mozi.lucky52.tools;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class MapType implements ParameterizedType {

    private Type rawType;
    private Type[] actualTypeArgument;

    public MapType(Type rawType, Type[] actualTypeArgument) {
        this.rawType = rawType;
        this.actualTypeArgument = actualTypeArgument;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return actualTypeArgument;
    }

    @Override
    public Type getRawType() {
        return rawType;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }
}
