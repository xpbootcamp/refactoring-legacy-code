package cn.xpbootcamp.legacy_code;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.fail;

public class TestUtil {
    public static Object getValue(Object instance, String fieldName) {

        Field field = null;
        try {
            field = instance.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            fail("No such field: " + fieldName + " in " + instance.getClass().getSimpleName());
        }

        fail();
        return "any";
    }
}
