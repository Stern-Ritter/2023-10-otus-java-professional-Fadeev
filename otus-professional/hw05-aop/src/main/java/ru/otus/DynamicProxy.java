package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotations.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DynamicProxy {
    private final static Logger logger = LoggerFactory.getLogger(DynamicProxy.class);

    public static <T, I> I createInstance(T instance, Class<I> interfaceClass) {
        InvocationHandler handler = new LogInvocationHandler(instance);
        return interfaceClass.cast(Proxy.newProxyInstance(
                DynamicProxy.class.getClassLoader(),
                new Class<?>[]{interfaceClass},
                handler));
    }

    static class LogInvocationHandler implements InvocationHandler {
        private final static String METHOD_EXECUTION_LOGGING_TEMPLATE = "Executed method: {}, params: {}";
        private final Set<String> annotatedMethodsSignature;
        private final Map<String, Object> cache;
        private final Object innerObject;

        LogInvocationHandler(Object innerObject) {
            this.innerObject = innerObject;
            this.annotatedMethodsSignature = new HashSet<>();
            this.cache = new HashMap<>();

            Method[] methods = innerObject.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Log.class)) {
                    annotatedMethodsSignature.add(getMethodSignature(method));
                }
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodSignature = getMethodSignature(method);
            if (annotatedMethodsSignature.contains(methodSignature)) {
                logMethodExecution(method, args);
            }

            if (cache.containsKey(methodSignature)) {
                logger.info("Get result for method: {} from cache", methodSignature);
                return cache.get(methodSignature);
            } else {
                Object result = method.invoke(innerObject, args);
                cache.put(methodSignature, result);
                return result;
            }
        }

        private String getMethodSignature(Method method) {
            String methodName = method.getName();
            String parameters = Arrays.stream(method.getParameters())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
            return String.format("%s (%s)", methodName, parameters);
        }

        private void logMethodExecution(Method method, Object[] args) {
            String methodName = method.getName();
            String parameters = args == null ? "without params"
                    : Arrays.stream(args).map(Object::toString).collect(Collectors.joining(", "));
            logger.info(METHOD_EXECUTION_LOGGING_TEMPLATE, methodName, parameters);
        }
    }
}
