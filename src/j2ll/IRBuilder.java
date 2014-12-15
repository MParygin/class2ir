package j2ll;

import org.objectweb.asm.Label;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import java.util.Stack;

/**
 * IR Builder
 */
public class IRBuilder {

    private int tmp;

    private List<String> strings = new ArrayList<String>();

    @Deprecated
    public void add(String str) {
        strings.add(str);
    }

    public void comment(String str) {
        strings.add("; " + str);
    }

    public void in2out1(_Stack stack, String op, String type) {
        StackValue op2 = stack.pop();
        StackValue op1 = stack.pop();
        String res = stack.push(type);
        StringBuilder tmp = new StringBuilder();
        tmp.append(res);
        tmp.append(" = ");
        tmp.append(op);
        tmp.append(' ');
        tmp.append(type); // IR of op1 ???
        tmp.append(' ');
        tmp.append(op1);
        tmp.append(", ");
        tmp.append(op2);
        add(tmp.toString());
    }



    public void _new(_Stack stack, Resolver resolver, String name) {
        // state
        String struct = resolver.resolveStruct(name);
        String object = resolver.resolve(name);
        String res = stack.pushObjRef(object);
        String reg = allocReg();
        // out
        comment(resolver.resolveStruct(name));
        add(reg + " = call i8* @malloc(" + Internals.structSize(struct) + ")");
        add(res + " = bitcast i8* " + reg + " to " + object);
    }

    public void neg(_Stack stack, String type) {
        StackValue value = stack.pop();
        String res = stack.push(type);
        if (Internals.INT.equals(type) || Internals.LONG.equals(type)) {
            add(res + " = sub " + value.getIR() + " 0, " + value);
        } else {
            add(res + " = fsub " + value.getIR() + " 0.0, " + value);
        }
    }


    public void branch(_Stack stack, Stack<String> commands, Label label, int op) {
        if (commands.size() > 0) {
            System.err.println("Unknown prefix: " + commands.pop());
        }

        //  %cond = icmp eq i32 %a, %b
        // br i1 %cond, label %IfEqual, label %IfUnequal
        // IfEqual:
        StackValue op2 = stack.pop();
        StackValue op1 = stack.pop();
        add("%__tmpc" + tmp + " = icmp " + IR.ICMP[op] + " i32 " + op1 + ", " + op2);
        add("br i1 %__tmpc" + tmp + ", label %" + label + ", label %__tmpl" + tmp);
        add("__tmpl" + tmp + ":");
        tmp++;

    }

    public void branch0(_Stack stack, Stack<String> commands, Label label, int op) {
        if (commands.size() == 0) {
            //  %cond = icmp eq i32 %a, 0
            // br i1 %cond, label %IfEqual, label %IfUnequal
            // IfEqual:
            StackValue op1 = stack.pop();
            add("%__tmpc" + tmp + " = icmp " + IR.ICMP[op] + " i32 " + op1 + ", 0");
            add("br i1 %__tmpc" + tmp + ", label %" + label + ", label %__tmpl" + tmp);
            add("__tmpl" + tmp + ":");
            tmp++;
        } else {
            // POP prefix
            String cmd = commands.pop();
            if (Prefix.FCMPL.equals(cmd) || Prefix.FCMPG.equals(cmd) || Prefix.DCMPL.equals(cmd) || Prefix.DCMPG.equals(cmd)) {
                // double compare
                StackValue value2 = stack.pop();
                StackValue value1 = stack.pop();
                add("%__tmpc" + tmp + " = fcmp " + IR.FCMP[op] + " " + value1.fullName() + ", " + value2); // ordered compare
            } else if (Prefix.LCMP.equals(cmd)) {
                // long compare
                StackValue value2 = stack.pop();
                StackValue value1 = stack.pop();
                add("%__tmpc" + tmp + " = icmp " + IR.ICMP[op] + " " + value1.fullName() + ", " + value2);
            } else {
                System.err.println("Unknown prefix: " + cmd);
            }
            add("br i1 %__tmpc" + tmp + ", label %" + label + ", label %__tmpl" + tmp);
            add("__tmpl" + tmp + ":");
            tmp++;
        }
    }

    // todo split primitive and objects
    public void newArray(_Stack stack, Resolver resolver, String arrayType) {
        comment("new array " + arrayType);
        //arrayType = "[" + arrayType;
        //StackValue op = stack.pop();
        String ty = Util.javaSignature2irType(resolver, arrayType);
        String res = stack.pushObjRef(ty);
        String reg1 = allocReg();
        String reg2 = allocReg();
        String reg3 = allocReg();
        // size array in bytes
        int bytes = Internals.sizeOf(res);
        //todo dodo add(reg1 + " = mul " + op.fullName() + ", " + bytes);
        add(reg2 + " = add i32 " + reg1 + ", 4");
        add(reg3 + " = call i8* @malloc(i32 " + reg2 + ")");
        add(res + " = bitcast i8* " + reg3 + " to " + arrayType);
    }

    public void putfield(_Stack stack, Resolver resolver, String className, String name, String signature) {
        // state
        StackValue value = stack.pop();
        StackValue inst = stack.pop();
        String ty = Util.javaSignature2irType(resolver, signature);
        String reg = allocReg();
        // out
        comment("putfield " + className + " " + name + " " + signature + " ( " + inst.fullName() + " := " + value.fullName() + " )");
        getelementptr(reg, inst.fullName(), 0, Util.class2ptr(className, name));
        store(ty, value.toString(), reg);
    }

    public void putstatic(_Stack stack, Resolver resolver, String className, String name, String signature) {
        // state
        StackValue value = stack.pop();
        String ty = Util.javaSignature2irType(resolver, signature);
        String reg = allocReg();
        // out
        comment("putstatic " + className + " " + name + " " + signature + " ( " + signature + " := " + value.fullName() + " )");
        getelementptr(reg, ty, Util.static2str(className, name));
        store(ty, value.toString(), reg);
    }


    public void getfield(_Stack stack, Resolver resolver, String className, String name, String signature) {
        // state
        StackValue inst = stack.pop();
        String ty = Util.javaSignature2irType(resolver, signature);
        String result = stack.push(ty);
        String reg = allocReg();
        // out
        comment("getfield " + className + " " + name + " " + signature + " ( " + inst.fullName() + " )");
        getelementptr(reg, inst.fullName(), 0, Util.class2ptr(className, name));
        load(result, ty, reg);
    }

    public void getstatic(_Stack stack, Resolver resolver, String className, String name, String signature) {
        // state
        String ty = Util.javaSignature2irType(resolver, signature);
        String result = stack.push(ty);
        String reg = allocReg();
        // out
        comment("getstatic " + className + " " + name + " " + signature + " ( " + result + " := " + signature + " )");
        getelementptr(reg, ty, Util.static2str(className, name));
        load(result, ty, reg);
    }

    public void astore(_Stack stack, String type) {
        // state
        StackValue value = stack.pop();
        StackValue index = stack.pop();
        StackValue arrayRef = stack.pop();
        String reg = allocReg();
        // out
        comment("astore " + type);
        getelementptr(reg, arrayRef.fullName(), 0, 1, index.fullName()); // pointer to element of array
        store(type, value.toString(), reg);
    }

    public void aload(_Stack stack, String type) {
        // state
        StackValue index = stack.pop();
        StackValue arrayRef = stack.pop();
        String value = stack.push(type);
        String reg = allocReg();
        // out
        comment("aload " + type);
        getelementptr(reg, arrayRef.fullName(), 0, 1, index.fullName()); // pointer to element of array
        load(value, type, reg);
    }

    public void aaload(_Stack stack) {
        // state
        StackValue index = stack.pop();
        StackValue arrayRef = stack.pop();
        String ty = Internals.dearrayOf(arrayRef.getIR());
        String value = stack.push(ty);
        String reg = allocReg();
        // out
        comment("aload " + ty);
        getelementptr(reg, arrayRef.fullName(), 0, 1, index.fullName()); // pointer to element of array
        load(value, ty, reg);
    }

    public void fptosi(_Stack stack, String type) {
        operationto(stack, "fptosi", type);
    }

    public void sitofp(_Stack stack, String type) {
        operationto(stack, "sitofp", type);
    }


    public void operationto(_Stack stack, String op, String type) {
        StackValue f = stack.pop();
        String res = stack.push(type);
        add(res + " = " + op + " " + f.fullName() + " to " + type);
    }

    // =================================================================================================================

    private String allocReg() {
        String result = "%__tmp" + tmp;
        tmp++;
        return result;
    }

    // <result> = getelementptr <pty>* <ptrval>{, <ty> <idx>}*
    public void getelementptr(String result, String pty, String ptrval, Object ... idx) {
        StringBuilder tmp = new StringBuilder();
        tmp.append(result);
        tmp.append(" = getelementptr ");
        tmp.append(pty);
        tmp.append("* ");
        tmp.append(ptrval);
        for (Object id : idx) {
            if (id instanceof Integer) {
                tmp.append(", i32 ");
            } else if (id instanceof Long) {
                tmp.append(", i64 ");
            }
            tmp.append(id);
        }
        add(tmp.toString());
    }

    public void getelementptr(String result, String pointer, Object ... idx) {
        StringBuilder tmp = new StringBuilder();
        tmp.append(result);
        tmp.append(" = getelementptr ");
        tmp.append(pointer);
        for (Object id : idx) {
            tmp.append(", ");
            if (id instanceof Integer) {
                tmp.append("i32 ");
                tmp.append(id);
            } else if (id instanceof Long) {
                tmp.append("i64 ");
                tmp.append(id);
            } else {
                tmp.append(id);
            }
        }
        add(tmp.toString());
    }

    // <result> = load [volatile] <ty>* <pointer>
    public void load(String result, String ty, String pointer) {
        StringBuilder tmp = new StringBuilder();
        tmp.append(result);
        tmp.append(" = load ");
        tmp.append(ty);
        tmp.append("* ");
        tmp.append(pointer);
        add(tmp.toString());
    }

    // store [volatile] <ty> <value>, <ty>* <pointer>
    public void store(String ty, String value, String pointer) {
        StringBuilder tmp = new StringBuilder();
        tmp.append("store ");
        tmp.append(ty);
        tmp.append(" ");
        tmp.append(value);
        tmp.append(", ");
        tmp.append(ty);
        tmp.append("* ");
        tmp.append(pointer);
        add(tmp.toString());
    }


    public List<String> getStrings() {
        return strings;
    }
}
