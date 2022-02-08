/**
 * Change value of a reference (must be an instance of String, Integer,
 * Character, Double, Float, Short, Byte, Long and Boolean)
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
            java.lang.reflect.Field field = src.getClass().getDeclaredField("value"),
                    accessible;
            int modifiers = 0;

            try {
                accessible = field.getClass().getDeclaredField("modifiers");
                modifiers = accessible.getInt(field);
                accessible.setInt(field, java.lang.reflect.Modifier.PUBLIC);
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
