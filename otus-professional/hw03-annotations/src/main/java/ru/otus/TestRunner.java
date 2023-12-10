package ru.otus;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;
import ru.otus.utils.ReflectionHelper;
import ru.otus.utils.TestStatistic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {
    public void runClassTests(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            Method[] methods = clazz.getDeclaredMethods();
            List<Method> beforeMethods = new ArrayList<>();
            List<Method> afterMethods = new ArrayList<>();
            List<Method> testMethods = new ArrayList<>();
            TestStatistic testStatistic = new TestStatistic();

            classifyMethods(methods, beforeMethods, afterMethods, testMethods);
            runMethods(clazz, beforeMethods, afterMethods, testMethods, testStatistic);
            testStatistic.printStatistic();
        } catch (ClassNotFoundException ex) {
            System.out.printf("Class: %s не найден", className);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            System.out.println("У class: %s отсутствует public конструктор без параметров");
        }
    }

    private void classifyMethods(Method[] methods, List<Method> beforeMethods, List<Method> afterMethods,
                                 List<Method> testMethods) {
        for (Method method : methods) {
            if (method.isAnnotationPresent(Before.class)) {
                beforeMethods.add(method);
            } else if (method.isAnnotationPresent(After.class)) {
                afterMethods.add(method);
            } else if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            }
        }
    }

    private void runMethods(Class<?> testClass, List<Method> beforeMethods, List<Method> afterMethods, List<Method> testMethods,
                            TestStatistic testStatistic) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        for (Method testMethod : testMethods) {
            Object testClassInstance = ReflectionHelper.createInstance(testClass);

            ReflectionHelper.runMethods(testClassInstance, beforeMethods);
            try {
                ReflectionHelper.runMethod(testClassInstance, testMethod);
                testStatistic.testPassed();
            } catch (Exception ex) {
                testStatistic.testFailed();
            } finally {
                try {
                    ReflectionHelper.runMethods(testClassInstance, afterMethods);
                } catch (Exception ex) {
                    System.out.println("Возникла ошибка при выполнении @After методов.");
                }
            }
        }
    }
}
