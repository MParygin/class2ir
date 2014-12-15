package j2ll;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static j2ll.Internals.*;

public final class Util {

    public static String javaSignature2irType(Resolver resolver, String str) {
        if (str.startsWith("[")) {
            return Internals.arrayOf(javaSignature2irType(resolver, str.substring(1)));
        }

        if (str.equals("B")) {
            return BYTE;
        } else if (str.equals("S")) {
            return SHORT;
        } else if (str.equals("C")) {
            return CHAR;
        } else if (str.equals("I")) {
            return INT;
        } else if (str.equals("J")) {
            return LONG;
        } else if (str.equals("F")) {
            return FLOAT;
        } else if (str.equals("D")) {
            return DOUBLE;
        } else if (str.equals("V")) {
            return "void";
        } else if (str.equals("Z")) {
            return BOOLEAN;
        } else if (str.startsWith("L")) {
            str = str.substring(1, str.length() - 1);
            return resolver.resolve(str);
        }
        return null;
    }

    public static List<String> javaSignatures2irTypes(Resolver resolver, String str) {
        System.out.print("Parse ");
        System.out.println(str);

        List<String> result = new ArrayList<>();
        StringBuilder tmp = new StringBuilder();
        for (char c : str.toCharArray()) {
            tmp.append(c);
            if (c == 'S' || c == 'C' || c == 'I' || c == 'J' || c == 'F' || c == 'D' ||  c == ';') { // todo all java signs
                result.add(javaSignature2irType(resolver, tmp.toString()));
                tmp.setLength(0);
            }
        }
        if (tmp.length() > 0) result.add(javaSignature2irType(resolver, tmp.toString()));
        return result;
    }

    public static String enumArgs(List<String> types, String prefix) {
        StringBuilder tmp = new StringBuilder();
        int index = 0;
        for (int i = 0; i < types.size(); i++) {
            if (i != 0) tmp.append(", ");
            String type = types.get(i);
            tmp.append(type);
            tmp.append(" ");
            tmp.append(prefix);
            tmp.append(index++);
            if ("i64".equals(type) || "double".equals(type)) index++; // long & double have 2 slots
        }
        return tmp.toString();
    }

    public static String classMethodSignature2id(String className, String methodName, _JavaSignature signature) {
        StringBuilder result = new StringBuilder();
        String nm = methodName.replace('<', '_').replace('>', '_');
        //result.append('"');
        result.append(className.replace('/', '_'));
        result.append('_');
        result.append(nm);
        if (!signature.getJavaArgs().isEmpty()) {
            result.append('_');
            result.append(signature.getJavaArgs());
        }
        //result.append('"');
        return result.toString();
    }

    public static String class2struct(Resolver resolver, String className) {
        try {
            Class c = Class.forName(className.replace('/', '.'));
            StringJoiner joiner = new StringJoiner(", ", "{", "}");
            for (Field f : c.getDeclaredFields()) {
                Class fc = f.getType();
                joiner.add(Internals.java2ir(resolver, fc));
            }
            return "%" + className.replace('/', '_') + " = type " + joiner; //todo struct -> internals
        } catch (Exception e) {
            return className + " is unknown";
        }
    }

    public static int class2ptr(String className, String name) {
        try {
            Class c = Class.forName(className.replace('/', '.'));
            StringJoiner joiner = new StringJoiner(", ", "{", "}");
            int pos = 0;
            for (Field f : c.getDeclaredFields()) {
                if (name.equals(f.getName())) return pos;
                pos++;
            }
            return pos;
        } catch (Exception e) {
            return 0;
        }
    }

    public static String static2str(String className, String name) {
        String cn = className.replace('/', '_');
        String nm = name.replace('<', '_').replace('>', '_'); //todo
        return "@" + cn + "_" + nm + "";
    }

}
