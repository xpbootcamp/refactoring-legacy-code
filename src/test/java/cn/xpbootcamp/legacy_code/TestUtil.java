package cn.xpbootcamp.legacy_code;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.fail;

public class TestUtil {
    static Object getValue(Object instance, String fieldName) {

        Field field = getField(instance.getClass(), fieldName);
        return getValue(instance, field);
    }

    static Object getValue(Object instance, Field field) {
        try {
            field.setAccessible(true);
            return field.get(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        fail();
        return "any";
    }

    static void setValue(Object instance, String fieldName, Object value) {

        Class<?> theClass = instance.getClass();
        Field field = getField(theClass, fieldName);
        try {
            field.setAccessible(true);
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    static Field getField(Class<?> theClass, String fieldName) {
        Field field = null;
        try {
            field = theClass.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            fail("No such field: " + fieldName + " in " + theClass.getSimpleName());
        }
        return field;
    }
}
