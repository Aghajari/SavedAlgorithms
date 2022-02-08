
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
/**
 * Change value of a reference, 100% works if src be instance of String, Integer,
 * Character, Double, Float, Short, Byte, Long and Boolean,
 * Otherwise only copies fields (both private and public fields) from newValue to src
 *
 * @author AmirHossein Aghajari
 */
public class ChangeValueByReference {

    public static void main(String[] args) {
        String str = "Hello";
        System.out.println(str);
        changeValueByReference(str, "World");
        System.out.println(str);

        Integer num = 25;
        System.out.println(num);
        changeValueByReference(num, 50);
        System.out.println(num);
    }

    public static void changeValueByReference(final Object src, Object newValue) {
        try {
            if (!(src instanceof String || src instanceof Boolean || src instanceof Character || src instanceof Number)) {
                ArrayList<Field> fields = new ArrayList<>(
                        src.getClass().getFields().length + src.getClass().getDeclaredFields().length + 1);
                fields.addAll(Arrays.asList(src.getClass().getFields()));
                fields.addAll(Arrays.asList(src.getClass().getDeclaredFields()));
                Method method = Object.class.getDeclaredMethod("clone");
                method.setAccessible(true);
                for (Field f : fields) {
                    if ((f.getModifiers() | Modifier.STATIC) == f.getModifiers())
                        continue;
                    f.setAccessible(true);
                    try {
                        f.set(src, method.invoke(f.get(newValue)));
                    } catch (Exception ignore) {
                        f.set(src, f.get(newValue));
                    }
                }
                return;
            }

            Field field = src.getClass().getDeclaredField("value"), accessible;
            int modifiers = 0;

            try {
                accessible = field.getClass().getDeclaredField("modifiers");
                modifiers = accessible.getInt(field);
                accessible.setInt(field, Modifier.PUBLIC);
            } catch (Exception ignore) {
                accessible = null;
            }
            field.setAccessible(true);

            if (src.getClass().isAssignableFrom(newValue.getClass())) {
                field.set(src, field.get(newValue));
            } else {
                if (src instanceof String)
                    field.set(src, field.get(newValue = newValue.toString()));
                else if (src instanceof Character)
                    field.setChar(src, (char) ((Number) newValue).intValue());
                else if (src instanceof Boolean)
                    field.setBoolean(src, ((Number) newValue).intValue() != 0);
                else if (src instanceof Integer)
                    field.setInt(src, ((Number) newValue).intValue());
                else if (src instanceof Float)
                    field.setFloat(src, ((Number) newValue).floatValue());
                else if (src instanceof Double)
                    field.setDouble(src, ((Number) newValue).doubleValue());
                else if (src instanceof Long)
                    field.setLong(src, ((Number) newValue).longValue());
                else if (src instanceof Short)
                    field.setShort(src, ((Number) newValue).shortValue());
                else if (src instanceof Byte)
                    field.setByte(src, ((Number) newValue).byteValue());
            }
            if (accessible != null)
                accessible.setInt(field, modifiers);

            if (src instanceof String) {
                // Clear cache of hashCode
                field = src.getClass().getDeclaredField("hash");
                field.setAccessible(true);
                field.set(src, 0);
                field = src.getClass().getDeclaredField("hashIsZero");
                field.setAccessible(true);
                field.set(src, false);
                // Update string coder
                field = src.getClass().getDeclaredField("coder");
                field.setAccessible(true);
                field.set(src, field.get(newValue));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
