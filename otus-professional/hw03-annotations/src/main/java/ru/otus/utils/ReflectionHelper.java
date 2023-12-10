package ru.otus.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ReflectionHelper {
    private ReflectionHelper() {
    }

    public static Object createInstance(Class<?> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<?> constructor = clazz.getConstructor();
        return constructor.newInstance();
    }

    public static void runMethod(Object object, Method method) throws InvocationTargetException, IllegalAccessException {
        method.setAccessible(true);
        method.invoke(object);
    }

    public static void runMethods(Object object, List<Method> methods) throws InvocationTargetException, IllegalAccessException {
        for (Method method : methods) {
            runMethod(object, method);
        }
    }
}
