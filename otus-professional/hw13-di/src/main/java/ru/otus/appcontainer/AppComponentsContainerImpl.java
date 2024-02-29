package ru.otus.appcontainer;

import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import static org.reflections.scanners.Scanners.TypesAnnotated;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {
    private static final String CONFIG_MULTIPLE_CLASS_COMPONENTS = "The configuration contains multiple components with class/interface: %s";
    private static final String CONFIG_DOES_NOT_CONTAIN_COMPONENT = "The configuration does not contain any components with class/interface: %s";
    private static final String CONFIG_ALREADY_CONTAINS_COMPONENT_WITH_NAME = "The configuration already contains component with name: %s";
    private static final String CONFIG_CLASS_DOES_NOT_CONTAIN_CONSTRUCTOR_WITHOUT_PARAMETERS = "The configuration does not contain constuctor without parameters";
    private static final String CONFIG_FAILED_TO_INVOKE_METHOD = "Failed to invoke the configuration method: %s";


    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) {
        processConfig(initialConfigClasses);
    }

    public AppComponentsContainerImpl(String packageName) {
        Class<?>[] configClasses = getConfigClasses(packageName);
        processConfig(configClasses);
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        Object component = appComponents.stream()
                .filter((c) -> {
                    boolean classMatch = c.getClass().equals(componentClass);
                    Class<?>[] interfaces = c.getClass().getInterfaces();
                    boolean implementsInterface = Arrays.stream(interfaces).anyMatch((i) -> i.isAssignableFrom(componentClass));
                    return classMatch || implementsInterface;
                })
                .reduce((c, v) -> {
                    throw new RuntimeException(String.format(CONFIG_MULTIPLE_CLASS_COMPONENTS, componentClass.getName()));
                })
                .orElseThrow(() -> new RuntimeException(String.format(CONFIG_DOES_NOT_CONTAIN_COMPONENT, componentClass.getName())));
        return (C) component;
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        Object component = appComponentsByName.get(componentName);
        if (component == null) {
            throw new RuntimeException(String.format(CONFIG_DOES_NOT_CONTAIN_COMPONENT, componentName));
        }
        return (C) component;
    }

    private Class<?>[] getConfigClasses(String packageName) {
        FilterBuilder inputsFilter = new FilterBuilder();
        inputsFilter.includePackage(packageName);
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .forPackage(packageName)
                        .addScanners(Scanners.TypesAnnotated)
                        .filterInputsBy(inputsFilter));
        Set<Class<?>> configClasses = reflections.get(TypesAnnotated.with(AppComponentsContainerConfig.class).asClass());
        return configClasses.toArray(new Class<?>[0]);
    }

    private void processConfigClass(Class<?> configClass) {
        Method[] methods = configClass.getDeclaredMethods();
        List<Method> annotatedMethods = getAnnotatedMethods(methods);
        Object configObject = createConfigObject(configClass);

        for (Method method : annotatedMethods) {
            String componentName = method.getAnnotation(AppComponent.class).name();
            checkComponentExists(componentName);

            Class<?>[] parameterTypes = method.getParameterTypes();
            List<Object> parameterComponents = getMethodParameterComponents(parameterTypes);

            Object component = createComponent(configObject, method, parameterComponents);
            appComponents.add(component);
            appComponentsByName.put(componentName, component);
        }
    }

    private void processConfig(Class<?>[] configClasses) {
        Arrays.stream(configClasses)
                .map((c) -> {
                    checkConfigClass(c);
                    return c;
                })
                .sorted((f, s) -> {
                    AppComponentsContainerConfig firstAppComponentsConfig = f.getAnnotation(AppComponentsContainerConfig.class);
                    AppComponentsContainerConfig secondAppComponentsConfig = s.getAnnotation(AppComponentsContainerConfig.class);
                    return Integer.compare(firstAppComponentsConfig.order(), secondAppComponentsConfig.order());
                })
                .forEach(this::processConfigClass);
    }

    private List<Object> getMethodParameterComponents(Class<?>[] parameterTypes) {
        List<Object> parameters = new ArrayList<>();
        for (Class<?> parameterType : parameterTypes) {
            Object parameterComponent = getAppComponent(parameterType);
            parameters.add(parameterComponent);
        }
        return parameters;
    }

    private void checkComponentExists(String componentName) {
        if (appComponentsByName.containsKey(componentName)) {
            throw new RuntimeException(String.format(CONFIG_ALREADY_CONTAINS_COMPONENT_WITH_NAME, componentName));
        }
    }

    private Object createConfigObject(Class<?> configClass) {
        try {
            Constructor<?> configClassConstructor = configClass.getConstructor();
            return configClassConstructor.newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(CONFIG_CLASS_DOES_NOT_CONTAIN_CONSTRUCTOR_WITHOUT_PARAMETERS);
        }
    }

    private Object createComponent(Object configObject, Method method, List<Object> parameters) {
        try {
            return method.invoke(configObject, parameters.toArray());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(String.format(CONFIG_FAILED_TO_INVOKE_METHOD, method.getName()));
        }
    }

    private List<Method> getAnnotatedMethods(Method[] methods) {
        return Arrays.stream(methods)
                .filter((m) -> m.isAnnotationPresent(AppComponent.class))
                .sorted((f, s) -> {
                    AppComponent firstMethodAppComponent = f.getAnnotation(AppComponent.class);
                    AppComponent secondMethodAppComponent = s.getAnnotation(AppComponent.class);
                    return Integer.compare(firstMethodAppComponent.order(), secondMethodAppComponent.order());
                })
                .toList();
    }


    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }
}
