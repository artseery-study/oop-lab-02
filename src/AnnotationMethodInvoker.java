import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class AnnotationMethodInvoker {
    private AnnotationMethodInvoker() {
    }

    public static void invokeAnnotatedMethods(Object target) {
        for (Method method : target.getClass().getDeclaredMethods()) {
            int access = method.getModifiers();
            if (!Modifier.isPrivate(access) && !Modifier.isProtected(access)) {
                continue;
            }

            Repeat repeat = method.getAnnotation(Repeat.class);
            if (repeat == null) {
                continue;
            }

            int count = repeat.value();
            if (count < 0) {
                throw new IllegalArgumentException("Количество вызовов не может быть отрицательным: "
                        + method.getName());
            }
            if (count == 0) {
                continue;
            }

            try {
                Class<?>[] types = method.getParameterTypes();
                Object[] arguments = new Object[types.length];
                for (int i = 0; i < types.length; i++) {
                    try {
                        arguments[i] = createArgument(types[i]);
                    } catch (NoSuchMethodException e) {
                        Constructor<?>[] constructors = types[i].getDeclaredConstructors();
                        if (constructors.length == 0) {
                            throw new IllegalArgumentException("Нет конструкторов у типа " + types[i].getName());
                        }
                        Constructor<?> constructor = constructors[0];
                        for (Constructor<?> candidate : constructors) {
                            if (candidate.getParameterCount() < constructor.getParameterCount()) {
                                constructor = candidate;
                            }
                        }

                        Class<?>[] constructorTypes = constructor.getParameterTypes();
                        Object[] constructorArguments = new Object[constructorTypes.length];
                        for (int j = 0; j < constructorTypes.length; j++) {
                            constructorArguments[j] = createArgument(constructorTypes[j]);
                        }
                        constructor.setAccessible(true);
                        arguments[i] = constructor.newInstance(constructorArguments);
                    }
                }

                method.setAccessible(true);
                System.out.println("\nМетод " + method.getName() + ", повторений: " + count);
                for (int i = 0; i < count; i++) {
                    method.invoke(target, arguments);
                }
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException(
                        "Для вложенного аргумента нужен конструктор без параметров или правило в createArgument", e);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Не удалось подготовить аргументы или вызвать метод " + method.getName(), e);
            }
        }
    }

    private static Object createArgument(Class<?> type) throws ReflectiveOperationException {
        if (type == String.class) {
            return "водопой";
        }
        if (type == boolean.class || type == Boolean.class) {
            return false;
        }
        if (type == byte.class || type == Byte.class) {
            return (byte) 8;
        }
        if (type == short.class || type == Short.class) {
            return (short) 12;
        }
        if (type == int.class || type == Integer.class) {
            return 6;
        }
        if (type == long.class || type == Long.class) {
            return 100L;
        }
        if (type == float.class || type == Float.class) {
            return 3.5F;
        }
        if (type == double.class || type == Double.class) {
            return 15.0;
        }
        if (type == char.class || type == Character.class) {
            return 'С';
        }
        if (type.isEnum()) {
            Object[] constants = type.getEnumConstants();
            if (constants.length == 0) {
                throw new IllegalArgumentException("Перечисление без значений: " + type.getName());
            }
            return constants[0];
        }
        if (type.isArray()) {
            return Array.newInstance(type.getComponentType(), 0);
        }
        if (type == List.class) {
            return new ArrayList<>();
        }
        if (type == Set.class) {
            return new HashSet<>();
        }
        if (type == Map.class) {
            return new HashMap<>();
        }
        if (type.isInterface() || Modifier.isAbstract(type.getModifiers())) {
            throw new IllegalArgumentException("Нельзя создать объект интерфейса или абстрактного класса: "
                    + type.getName());
        }

        Constructor<?> constructor = type.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }
}
