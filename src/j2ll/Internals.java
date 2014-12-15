package j2ll;

/**
 * Internal Functions
 */
public class Internals {

    public static final String BOOLEAN = "i32";
    public static final String BYTE = "i32"; //todo
    public static final String CHAR = "i32"; // todo
    public static final String SHORT = "i32"; // todo
    public static final String INT = "i32";
    public static final String LONG = "i64";
    public static final String FLOAT = "float";
    public static final String DOUBLE = "double";

    public static int sizeOf(String type) {
        if (BYTE.equals(type)) return 1;
        if (CHAR.equals(type)) return 2;
        if (SHORT.equals(type)) return 2;
        if (INT.equals(type)) return 4;
        if (LONG.equals(type)) return 8;
        if (FLOAT.equals(type)) return 4;
        if (DOUBLE.equals(type)) return 8;
        return 1;
    }

    public static String newJVMArray(int type) {
        switch (type) {
            case 4: return structireArrayFloat();
            case 5: return structireArrayChar();
            case 6: return structireArrayFloat();
            case 7: return structireArrayDouble();
            case 8: return structireArrayByte();
            case 9: return structireArrayShort();
            case 10: return structireArrayInt();
            case 11: return structireArrayLong();
        }
        return null;
    }

    public static String arrayOf(String type) {
        return "{i32, [0 x " + type + "]}*";
    }

    public static String dearrayOf(String type) {
        try {
            return type.substring(11, type.length() - 3);
        } catch (Exception e) {
            e.printStackTrace();
            return type;
        }
    }

    @Deprecated
    public static String structireArrayByte() {
        return "{i32, [0 x i8]}*";
    }

    @Deprecated
    public static String structireArrayShort() {
        return "{i32, [0 x i16]}*";
    }

    @Deprecated
    public static String structireArrayChar() {
        return "{i32, [0 x i16]}*";
    }

    @Deprecated
    public static String structireArrayInt() {
        return "{i32, [0 x i32]}*";
    }

    @Deprecated
    public static String structireArrayLong() {
        return "{i32, [0 x i64]}*";
    }

    @Deprecated
    public static String structireArrayFloat() {
        return "{i32, [0 x float]}*";
    }

    @Deprecated
    public static String structireArrayDouble() {
        return "{i32, [0 x double]}*";
    }

    public static String structSize(String struct) {
        return "i32 ptrtoint (" + struct + "* getelementptr (" + struct + "* null, i32 1) to i32)";
    }

    public static String java2ir(Resolver resolver, Class c) {
        if ("boolean".equals(c.getName())) return BOOLEAN;
        if ("byte".equals(c.getName())) return BYTE;
        if ("short".equals(c.getName())) return SHORT;
        if ("int".equals(c.getName())) return INT;
        if ("long".equals(c.getName())) return LONG;
        if ("char".equals(c.getName())) return CHAR;
        if ("float".equals(c.getName())) return FLOAT;
        if ("double".equals(c.getName())) return DOUBLE;
        return resolver.resolve(c.getName().replace('.', '/'));
    }

}
