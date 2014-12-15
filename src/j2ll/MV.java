package j2ll;

import org.objectweb.asm.*;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static j2ll.Internals.*;

/**
 */
public class MV extends MethodVisitor {

    // parent
    private CV cv;

    // state
    String methodName;
    String javaSignature;

    // arguments
    List<String> _argTypes;
    // res
    String _resType;

    // local vars
    LocalVars vars;
    // buffer
    IRBuilder out = new IRBuilder();
    // stack
    _Stack stack = new _Stack();
    Stack<String> commands = new Stack<>();
    // labels
    List<Label> labels = new ArrayList<>();
    List<String> usedLabels = new ArrayList<>();


    int max_local;
    int max_stack;
    int tmp;

    public MV(String methodName, String javaSignature, CV cv) {
        super(Opcodes.ASM5);
        this.methodName = methodName;
        this.javaSignature = javaSignature;
        this.vars = cv.getStatistics().get(methodName + javaSignature);
        this.cv = cv;

        // signature
        _JavaSignature s = new _JavaSignature(cv.getStatistics().getResolver(), this.javaSignature);
        _argTypes = s.getArgs();
        _resType = s.getResult();
        // constructor`s implicit parameter
        if ("<init>".equals(methodName)) {
            _argTypes.add(0, cv.getStatistics().getResolver().resolve(this.cv.className));
        }
    }

    public MV(int i, MethodVisitor methodVisitor) {
        super(i, methodVisitor);
    }

    @Override
    public void visitParameter(String s, int i) {
        System.out.println("visitParameter " + s + " " + i);
    }

    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        System.out.println("visitAnnotationDefault");
        return null;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String s, boolean b) {
        return super.visitAnnotation(s, b);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int i, TypePath typePath, String s, boolean b) {
        return super.visitTypeAnnotation(i, typePath, s, b);
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int i, String s, boolean b) {
        return super.visitParameterAnnotation(i, s, b);
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        System.out.println("visitAttribute " + attribute);
    }

    @Override
    public void visitCode() {
        System.out.println("visitCode");
    }

    @Override
    public void visitFrame(int i, int i1, Object[] objects, int i2, Object[] objects1) {
        out.add("; TODO FRame: " + i + " " + i1 + " " + i2);
    }

    @Override
    public void visitInsn(int opcode) {
        int value = 0;
        switch (opcode) {
            case Opcodes.NOP: // 0
                break;
            // =============================================== Constants ==
            case Opcodes.ACONST_NULL: // 1
                out.add("const null"); //todo
                break;
            case Opcodes.ICONST_M1: // 2
            case Opcodes.ICONST_0: // 3
            case Opcodes.ICONST_1: // 4
            case Opcodes.ICONST_2: // 5
            case Opcodes.ICONST_3: // 6
            case Opcodes.ICONST_4: // 7
            case Opcodes.ICONST_5: // 8
                value = opcode - Opcodes.ICONST_0;
                stack.pushImm(value, INT);
                out.add("; push " + value);
                break;
            case Opcodes.LCONST_0: // 9
            case Opcodes.LCONST_1: // 10
                value = opcode - Opcodes.LCONST_0;
                stack.pushImm((long) value, LONG);
                out.add("; push " + value);
                break;
            case Opcodes.FCONST_0: // 11
            case Opcodes.FCONST_1: // 12
            case Opcodes.FCONST_2: // 13
                value = opcode - Opcodes.FCONST_0;
                stack.pushImm((float)value, FLOAT);
                out.add("; push " + value);
                break;
            case Opcodes.DCONST_0: // 14
            case Opcodes.DCONST_1: // 15
                value = opcode - Opcodes.DCONST_0;
                stack.pushImm((double)value, DOUBLE);
                out.add("; push " + value);
                break;
            // =============================================== Array Load ==
            case Opcodes.IALOAD: // 46
                out.aload(stack, INT);
                break;
            case Opcodes.LALOAD: // 47
                out.aload(stack, LONG);
                break;
            case Opcodes.FALOAD: // 48
                out.aload(stack, FLOAT);
                break;
            case Opcodes.DALOAD: // 49
                out.aload(stack, DOUBLE);
                break;
            case Opcodes.AALOAD: // 50
                out.aaload(stack);
                break;
            case Opcodes.BALOAD: // 51
                out.add("baload"); //todo pop
                break;
            case Opcodes.CALOAD: // 52
                out.add("caload"); //todo pop
                break;
            case Opcodes.SALOAD: // 53
                out.add("saload"); //todo pop
                break;
            // =============================================== Array Store ==
            case Opcodes.IASTORE: // 79
                out.astore(stack, INT);
                break;
            case Opcodes.LASTORE: // 80
                out.astore(stack, LONG);
                break;
            case Opcodes.FASTORE: // 81
                out.astore(stack, FLOAT);
                break;
            case Opcodes.DASTORE: // 82
                out.astore(stack, DOUBLE);
                break;
            case Opcodes.AASTORE: // 83
                out.astore(stack, "s32"); // todo array
                break;
            case Opcodes.BASTORE: // 84
                out.astore(stack, BYTE); // todo array
                break;
            case Opcodes.CASTORE: // 85
                out.astore(stack, CHAR); // todo array
                break;
            case Opcodes.SASTORE: // 86
                out.astore(stack, SHORT); // todo array
                break;
            // =============================================== Array Store ==
            case Opcodes.POP: // 87 todo
                stack.pop();
                out.add("; pop");
                break;
            case Opcodes.POP2: // 88 todo
                stack.pop();
                stack.pop();
                out.add("; pop2");
                break;
            case Opcodes.DUP: // 89 todo
                {
                    StackValue _op = stack.pop();
                    stack.push(_op);
                    stack.push(_op);
                    out.add("; dup");
                }
                break;
            case Opcodes.DUP_X1: // 90 todo
                {
                    StackValue _op1 = stack.pop();
                    StackValue _op2 = stack.pop();
                    stack.push(_op1);
                    stack.push(_op2);
                    stack.push(_op1);
                    out.add("; dup x1");
                }
                break;
            case Opcodes.DUP_X2: // 91 todo
                {
                    StackValue _op1 = stack.pop();
                    StackValue _op2 = stack.pop();
                    StackValue _op3 = stack.pop();
                    stack.push(_op1);
                    stack.push(_op3);
                    stack.push(_op2);
                    stack.push(_op1);
                    out.add("; dup x2");
                }
                break;
            case Opcodes.DUP2: // 92 todo
                {
                    StackValue op = stack.pop();
                    stack.push(op);
                    stack.push(op);
                }
                break;
            case Opcodes.DUP2_X1: // 93 todo
                {
                    StackValue _op1 = stack.pop();
                    StackValue _op2 = stack.pop();
                    StackValue _op3 = stack.pop();
                    stack.push(_op2);
                    stack.push(_op1);
                    stack.push(_op3);
                    stack.push(_op2);
                    stack.push(_op1);
                }
                break;
            case Opcodes.DUP2_X2: // 94 todo
                {
                    StackValue _op1 = stack.pop();
                    StackValue _op2 = stack.pop();
                    StackValue _op3 = stack.pop();
                    StackValue _op4 = stack.pop();
                    stack.push(_op2);
                    stack.push(_op1);
                    stack.push(_op4);
                    stack.push(_op3);
                    stack.push(_op2);
                    stack.push(_op1);
                }
                break;
            case Opcodes.SWAP: // 95 (Swap only first class values)
                {
                    StackValue _op1 = stack.pop();
                    StackValue _op2 = stack.pop();
                    stack.push(_op1);
                    stack.push(_op2);
                }
                break;
            // =============================================== ADD ==
            case Opcodes.IADD: // 96
                out.in2out1(stack, "add", INT);
                break;
            case Opcodes.LADD: // 97
                out.in2out1(stack, "add", LONG);
                break;
            case Opcodes.FADD: // 98
                out.in2out1(stack, "fadd", FLOAT);
                break;
            case Opcodes.DADD: // 99
                out.in2out1(stack, "fadd", DOUBLE);
                break;
            // =============================================== SUB ==
            case Opcodes.ISUB: // 100
                out.in2out1(stack, "sub", INT);
                break;
            case Opcodes.LSUB: // 101
                out.in2out1(stack, "sub", LONG);
                break;
            case Opcodes.FSUB: // 102
                out.in2out1(stack, "fsub", FLOAT);
                break;
            case Opcodes.DSUB: // 103
                out.in2out1(stack, "fsub", DOUBLE);
                break;
            // =============================================== MUL ==
            case Opcodes.IMUL: // 104
                out.in2out1(stack, "mul", INT);
                break;
            case Opcodes.LMUL: // 105
                out.in2out1(stack, "mul", LONG);
                break;
            case Opcodes.FMUL: // 106
                out.in2out1(stack, "fmul", FLOAT);
                break;
            case Opcodes.DMUL: // 107
                out.in2out1(stack, "fmul", DOUBLE);
                break;
            // =============================================== DIV ==
            case Opcodes.IDIV: // 108
                out.in2out1(stack, "sdiv", INT);
                break;
            case Opcodes.LDIV: // 109
                out.in2out1(stack, "sdiv", LONG);
                break;
            case Opcodes.FDIV: // 110
                out.in2out1(stack, "fdiv", FLOAT);
                break;
            case Opcodes.DDIV: // 111
                out.in2out1(stack, "fdiv", DOUBLE);
                break;
            // =============================================== REM ==
            case Opcodes.IREM: // 112
                out.in2out1(stack, "srem", INT);
                break;
            case Opcodes.LREM: // 113
                out.in2out1(stack, "srem", LONG);
                break;
            case Opcodes.FREM: // 114
                out.in2out1(stack, "frem", FLOAT);
                break;
            case Opcodes.DREM: // 115
                out.in2out1(stack, "frem", DOUBLE);
                break;
            // =============================================== NEG ==
            case Opcodes.INEG: // 116
                out.neg(stack, INT);
                break;
            case Opcodes.LNEG: // 117
                out.neg(stack, LONG);
                break;
            case Opcodes.FNEG: // 118
                out.neg(stack, FLOAT);
                break;
            case Opcodes.DNEG: // 119
                out.neg(stack, DOUBLE);
                break;
            // =============================================== SH* ==
            case Opcodes.ISHL: // 120
                out.in2out1(stack, "shl", INT);
                break;
            case Opcodes.LSHL: // 121
                out.operationto(stack, "sext", LONG); // extend stack to long (!)
                out.in2out1(stack, "shl", LONG);
                break;
            case Opcodes.ISHR: // 122
                out.in2out1(stack, "ashr", INT);
                break;
            case Opcodes.LSHR: // 123
                out.operationto(stack, "sext", LONG); // extend stack to long (!)
                out.in2out1(stack, "ashr", LONG);
                break;
            case Opcodes.IUSHR: // 124
                out.in2out1(stack, "lshr", INT);
                break;
            case Opcodes.LUSHR: // 125
                out.operationto(stack, "sext", LONG); // extend stack to long (!)
                out.in2out1(stack, "lshr", LONG);
                break;
            // =============================================== AND ==
            case Opcodes.IAND: // 126
                out.in2out1(stack, "and", INT);
                break;
            case Opcodes.LAND: // 127
                out.in2out1(stack, "and", LONG);
                break;
            // =============================================== OR ==
            case Opcodes.IOR: // 128
                out.in2out1(stack, "or", INT);
                break;
            case Opcodes.LOR: // 129
                out.in2out1(stack, "or", LONG);
                break;
            // =============================================== XOR ==
            case Opcodes.IXOR: // 130
                out.in2out1(stack, "xor", INT);
                break;
            case Opcodes.LXOR: // 131
                out.in2out1(stack, "xor", LONG);
                break;
            // =============================================== converts ==
            case Opcodes.I2L: // 133
                out.operationto(stack, "sext", LONG);
                break;
            case Opcodes.I2F: // 134
                out.sitofp(stack, FLOAT);
                break;
            case Opcodes.I2D: // 135
                out.sitofp(stack, DOUBLE);
                break;
            case Opcodes.L2I: // 136
                out.operationto(stack, "trunc", INT);
                break;
            case Opcodes.L2F: // 137
                out.sitofp(stack, FLOAT);
                break;
            case Opcodes.L2D: // 138
                out.sitofp(stack, DOUBLE);
                break;
            case Opcodes.F2I: // 139
                out.fptosi(stack, INT);
                break;
            case Opcodes.F2L: // 140
                out.fptosi(stack, LONG);
                break;
            case Opcodes.F2D: // 141
                out.operationto(stack, "fpext", DOUBLE);
                break;
            case Opcodes.D2I: // 142
                out.fptosi(stack, INT);
                break;
            case Opcodes.D2L: // 143
                out.fptosi(stack, LONG);
                break;
            case Opcodes.D2F: // 144
                out.operationto(stack, "fptrunc", FLOAT);
                break;
            case Opcodes.I2B: // 145
                out.operationto(stack, "strunc", BYTE); //todo ?
                break;
            case Opcodes.I2C: // 146
                out.operationto(stack, "utrunc", CHAR); //todo ? strunc ?
                break;
            case Opcodes.I2S: // 147
                out.operationto(stack, "utrunc", SHORT); //todo ? strunc ?
                break;
            // =============================================== Long compares (use with IF* command) ==
            case Opcodes.LCMP: // 148
                commands.push(Prefix.LCMP);
                break;
            // =============================================== Float compares (use with IF* command) ==
            case Opcodes.FCMPL: // 149
                commands.push(Prefix.FCMPL);
                break;
            case Opcodes.FCMPG: // 150
                commands.push(Prefix.FCMPG);
                break;
            // =============================================== Double compares (use with IF* command) ==
            case Opcodes.DCMPL: // 151
                commands.push(Prefix.DCMPL);
                break;
            case Opcodes.DCMPG: // 152
                commands.push(Prefix.DCMPG);
                break;
            // =============================================== returns ==
            case Opcodes.IRETURN: // 172
            case Opcodes.LRETURN: // 173
            case Opcodes.FRETURN: // 174
            case Opcodes.DRETURN: // 175
            case Opcodes.ARETURN: // 176
                out.add("ret " + stack.pop().fullName());
                break;
            case Opcodes.RETURN: // 177
                out.add("ret void");
                break;
            // =============================================== misc ==
            case Opcodes.ARRAYLENGTH: // 190
                out.add("arraylength"); // todo
                break;
            case Opcodes.ATHROW: // 191
                out.add("athrow"); // todo
                break;
            case Opcodes.MONITORENTER: // 194
                out.add("monitorenter");
                break;
            case Opcodes.MONITOREXIT: // 195
                out.add("monitorexit");
                break;
            default:
                System.out.println("IN " + opcode);
        }
    }

    @Override
    public void visitIntInsn(int opcode, int value) {
        switch (opcode) {
            case Opcodes.BIPUSH: // 16
                stack.pushImm(value, INT);
                out.add("; push " + value);
                break;
            case Opcodes.SIPUSH: // 17
                stack.pushImm(value, INT);
                out.add("; push " + value);
                break;
            case Opcodes.NEWARRAY: // 188
                out.newArray(stack, this.cv.getStatistics().getResolver(), Internals.newJVMArray(value));
                break;
            default:
                System.out.println("visitIntInsn " + opcode + " " + value);
        }
    }

    @Override
    public void visitVarInsn(int opcode, int slot) {
        switch (opcode) {
            // =============================================== Load ==
            case Opcodes.ILOAD: // 21
            case Opcodes.LLOAD: // 22
            case Opcodes.FLOAD: // 23
            case Opcodes.DLOAD: // 24
            case Opcodes.ALOAD: // 25
                {
                    _LocalVar lv = this.vars.get(slot);
                    if (lv != null) {
                        String type = Util.javaSignature2irType(this.cv.getStatistics().getResolver(), lv.signature);
                        String s = stack.push(type);
                        out.add(s + " = load " + type + "* %" + lv.name);
                    } else {
                        String s = stack.push("i32");
                        out.add(s + " = load " + "i32" + "* %" + slot);  //todo
                    }
                }
                break;
            // =============================================== Store (Store stack into local variable) ==
            case Opcodes.ISTORE: // 54
            case Opcodes.LSTORE: // 55
            case Opcodes.FSTORE: // 56
            case Opcodes.DSTORE: // 57
            case Opcodes.ASTORE: // 58
                {
                    _LocalVar lv = this.vars.get(slot);
                    if (lv != null) {
                        String type = Util.javaSignature2irType(this.cv.getStatistics().getResolver(), lv.signature);
                        StackValue value = stack.pop();
                        out.comment("*store "  + value.getIR());
                        out.add("store " + value.fullName() + ", " + type + "* %" + lv.name);
                    } else {
                        out.add("store " + stack.pop().fullName() + ", " + "i32" + "* %" + slot); // todo
                    }
                }
                break;
            default:
                System.out.println("visitVarInsn " + opcode + " " + slot);
        }
    }

    @Override
    public void visitTypeInsn(int opcode, String s) {
        switch (opcode) {
            case Opcodes.NEW: // 187
                out._new(stack, this.cv.getStatistics().getResolver(), s);
                break;
            case Opcodes.ANEWARRAY: // 189
                out.newArray(stack, this.cv.getStatistics().getResolver(), s);
                break;
            case Opcodes.CHECKCAST: // 192
                out.add("checkcast " + s);
                break;
            case Opcodes.INSTANCEOF: // 193
                out.add("instanceof " + s);
                break;
            default:
                System.out.println("visitTypeInsn " + opcode + " " + s);
        }
    }

    @Override
    public void visitFieldInsn(int opcode, String className, String name, String signature) {
        switch (opcode) {
            case Opcodes.GETSTATIC: // 178
                out.getstatic(stack, this.cv.getStatistics().getResolver(), className, name, signature);
                break;
            case Opcodes.PUTSTATIC: // 179
                out.putstatic(stack, this.cv.getStatistics().getResolver(), className, name, signature);
                break;
            case Opcodes.GETFIELD: // 180
                out.getfield(stack, this.cv.getStatistics().getResolver(), className, name, signature);
                break;
            case Opcodes.PUTFIELD: // 181
                out.putfield(stack, this.cv.getStatistics().getResolver(), className, name, signature);
                break;
            default:
                System.out.println("visitFieldInsn " + opcode + " " + className);
        }
    }

    @Override
    public void visitMethodInsn(int opcode, String s, String s1, String s2) {
        System.out.println("visitMethodInsn " + opcode + " " + s);
    }

    @Override
    public void visitMethodInsn(int opcode, String className, String methodName, String signature, boolean b) {
        switch (opcode) {
            case Opcodes.INVOKEVIRTUAL: // 182
                {
                    _JavaSignature s = new _JavaSignature(this.cv.getStatistics().getResolver(), signature);
                    String call = s.getSignatureCall(className, methodName, this.stack, null);
                    if ("void".equals(s.getResult())) {
                        out.add("call virt  " + call);
                    } else {
                        String op = stack.push(s.getResult());
                        out.add(op + " = call virt " + call);
                    }
                    // declare
                    if (!this.cv.className.equals(className)) {
                        this.cv.declares.add(s.getSignatureDeclare(className, methodName));
                    }
                }
                break;
            case Opcodes.INVOKESPECIAL: // 183
                {
                    _JavaSignature s = new _JavaSignature(this.cv.getStatistics().getResolver(), signature);
                    StackValue th = stack.pop();
                    String call = s.getSignatureCall(className, methodName, this.stack, th.fullName());
                    if ("void".equals(s.getResult())) {
                        out.add("call " + call + " ; special call");
                    } else {
                        String op = stack.push(s.getResult());
                        out.add(op + " = call " + call + " ; special call");
                    }
                    // declare
                    if (!this.cv.className.equals(className)) {
                        this.cv.declares.add(s.getSignatureDeclare(className, methodName));
                    }
                }
                break;
            case Opcodes.INVOKESTATIC: // 184
                {
                    _JavaSignature s = new _JavaSignature(this.cv.getStatistics().getResolver(), signature);
                    String call = s.getSignatureCall(className, methodName, this.stack, null);
                    if ("void".equals(s.getResult())) {
                        out.add("call " + call);
                    } else {
                        String op = stack.push(s.getResult());
                        out.add(op + " = call " + call);
                    }
                    // declare
                    if (!this.cv.className.equals(className)) {
                        this.cv.declares.add(s.getSignatureDeclare(className, methodName));
                    }
                }
                break;
            case Opcodes.INVOKEINTERFACE: // 185
                {
                    _JavaSignature s = new _JavaSignature(this.cv.getStatistics().getResolver(), signature);
                    String call = s.getSignatureCall(className, methodName, this.stack, null);
                    if ("void".equals(s.getResult())) {
                        out.add("call int " + call);
                    } else {
                        String op = stack.push(s.getResult());
                        out.add(op + " = call int " + call);
                    }
                    // declare
                    if (!this.cv.className.equals(className)) {
                        this.cv.declares.add(s.getSignatureDeclare(className, methodName));
                    }
                }
                break;
            case Opcodes.INVOKEDYNAMIC: // 186
                {
                    _JavaSignature s = new _JavaSignature(this.cv.getStatistics().getResolver(), signature);
                    String call = s.getSignatureCall(className, methodName, this.stack, null);
                    if ("void".equals(s.getResult())) {
                        out.add("call dyn " + call);
                    } else {
                        String op = stack.push(s.getResult());
                        out.add(op + " = call syn " + call);
                    }
                    // declare
                    if (!this.cv.className.equals(className)) {
                        this.cv.declares.add(s.getSignatureDeclare(className, methodName));
                    }
                }
                break;
            default:
                System.out.println("visitMethodInsn " + opcode);
        }
    }

    @Override
    public void visitInvokeDynamicInsn(String s, String s1, Handle handle, Object... objects) {
        System.out.println("visitInvokeDynamicInsn " + s + " " + s1);
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        switch (opcode) {
            case Opcodes.IFEQ: // 153
            case Opcodes.IFNE: // 154
            case Opcodes.IFLT: // 155
            case Opcodes.IFGE: // 156
            case Opcodes.IFGT: // 157
            case Opcodes.IFLE: // 158
                usedLabels.add(label.toString());
                out.branch0(stack, commands, label, opcode - Opcodes.IFEQ);
                break;
            case Opcodes.IF_ICMPEQ: // 159
            case Opcodes.IF_ICMPNE: // 160
            case Opcodes.IF_ICMPLT: // 161
            case Opcodes.IF_ICMPGE: // 162
            case Opcodes.IF_ICMPGT: // 163
            case Opcodes.IF_ICMPLE: // 164
                usedLabels.add(label.toString());
                out.branch(stack, commands, label, opcode - Opcodes.IF_ICMPEQ);
                break;
            case Opcodes.IF_ACMPEQ: // 165
            case Opcodes.IF_ACMPNE: // 166
                // todo commands prefix
                usedLabels.add(label.toString());
                out.add("acmp*"); // todo
                break;
            case Opcodes.GOTO: // 167
                usedLabels.add(label.toString());
                out.add("br label %" + label);
                break;
            case Opcodes.JSR: // 168
                out.add("jsr*"); // todo
                break;
            case Opcodes.RET: // 169
                out.add("ret*"); // todo
                break;
            case Opcodes.IFNULL: // 198
                out.add("ifnull*"); //todo
                break;
            case Opcodes.IFNONNULL: // 199
                out.add("ifnotnull*"); //todo
                break;
            default:
                out.add("visitJumpInsn " + opcode + " " + label.toString());
        }
    }

    @Override
    public void visitLabel(Label label) {
        labels.add(label);
        out.add("label:" + label.toString());
    }

    @Override
    public void visitLdcInsn(Object o) {
        if (o instanceof String) {
            // const
            stack.pushImm(10, "i32"); // todo
            out.add("VLDC " + o);
        } else if (o instanceof Integer) {
            Integer value = (Integer) o;
            stack.pushImm(value, INT);
            out.add("; push " + value);
        } else if (o instanceof Long) {
            Long value = (Long) o;
            stack.pushImm(value, LONG);
            out.add("; push " + value);
        } else if (o instanceof Float) {
            Float value = (Float) o;
            stack.pushImm(value, FLOAT);
            out.add("; push " + value);
        } else if (o instanceof Double) {
            Double value = (Double) o;
            stack.pushImm(value, DOUBLE);
            out.add("; push " + value);
        } else {
            out.add("; todo add const " + o);
        }
    }

    @Override
    public void visitIincInsn(int slot, int value) {
        out.add("inc:" + slot + ":" + value); // 132
    }

    @Override
    public void visitTableSwitchInsn(int from, int to, Label label, Label... labels) {
        usedLabels.add(label.toString());
        StackValue sv = stack.pop();
        out.add("switch " + sv.fullName() + ", label %" + label + " [");
        for(Label l : labels) {
            usedLabels.add(l.toString());
            out.add("    i32 " + from + ", label %" + l);
            from++;
        }
        out.add("]");
    }

    @Override
    public void visitLookupSwitchInsn(Label label, int[] values, Label[] labels) {
        usedLabels.add(label.toString());
        StackValue sv = stack.pop();
        out.add("switch " + sv.fullName() + ", label %" + label + " [");
        for (int i = 0; i < values.length; i++) {
            usedLabels.add(labels[i].toString());
            out.add("    i32 " + values[i] + ", label %" + labels[i]);
        }
        out.add("]");
    }

    @Override
    public void visitMultiANewArrayInsn(String s, int dims) {
        if (dims == 2) {
            StackValue size2 = stack.pop();
            StackValue size1 = stack.pop();

            out.comment("Multi Dimension Array: " + s + " " + dims);
            out.comment(size1.fullName());
            out.comment(size2.fullName());
            //todo
        } else {
            out.add("visitMultiANewArrayInsn " + s + " " + dims);
        }
    }

    @Override
    public AnnotationVisitor visitInsnAnnotation(int i, TypePath typePath, String s, boolean b) {
        System.out.println("visitInsnAnnotation");
        return null;
    }

    @Override
    public void visitTryCatchBlock(Label label, Label label1, Label label2, String s) {
        System.out.println("visitTryCatchBlock " + label + " " + label1 + " " + s);
    }

    @Override
    public AnnotationVisitor visitTryCatchAnnotation(int i, TypePath typePath, String s, boolean b) {
        System.out.println("visitTryCatchAnnotation");
        return null;
    }

    @Override
    public void visitLocalVariable(String name, String sign, String s2, Label label, Label label1, int slot) {
        //System.out.println("VLV + " + name + " / " + sign + " " + s2 + " " + slot);
/*
        vars.put(slot, new _LocalVar(name, sign));
        Util.javaSignature2irType(this.cv.getStatistics().getResolver(), sign);
*/
    }

    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(int i, TypePath typePath, Label[] labels, Label[] labels1, int[] ints, String s, boolean b) {
        return super.visitLocalVariableAnnotation(i, typePath, labels, labels1, ints, s, b);
    }

    @Override
    public void visitLineNumber(int i, Label label) {
        //System.out.println("LN " + i + " " + label);
        //super.visitLineNumber(i, label);
    }

    public void visitMaxs(int stack, int local) {
        // Maximums
        this.max_stack = stack;
        this.max_local = local;
    }

    @Override
    public void visitEnd() {
    }


    public void out(PrintStream ps) {
        // 0) info
        _JavaSignature ss = new _JavaSignature(this.cv.getStatistics().getResolver(), this.javaSignature);

        ps.print("; locals: ");
        ps.println(max_local);
        ps.print("; stack: ");
        ps.println(max_stack);
        ps.print("; args: ");
        ps.println(this._argTypes.size());
        ps.println("define " + this._resType +  " @" + Util.classMethodSignature2id(this.cv.className, this.methodName, ss) + "(" + Util.enumArgs(this._argTypes, "%s") + ") {");


        // 1) local vars & args
        int cntSlot = 0;
        for (Integer i : vars.keySet()) {
            _LocalVar lv = vars.get(i);
            cntSlot++;
            // local var
            ps.print("    ");
            ps.println("%" + lv.name + " = alloca " + Util.javaSignature2irType(this.cv.getStatistics().getResolver(), lv.signature) +  "\t\t; slot " + i + " = " + lv.signature);
            // init from arg (!)
            if (this._argTypes.size() < cntSlot) continue;
            String argType = this._argTypes.get(cntSlot-1);
            ps.println("    store "+argType+" %s" + i + ", "+argType+"* %" + lv.name); //todo
        }
        // 2) text
        for (String str : out.getStrings()) {
            int p = str.indexOf("slot-pointer2");  //todo
            if (p != -1) {
                for (Integer slot : vars.keySet()) {
                    _LocalVar lv = vars.get(slot);
                    String s = "slot-pointer:" + slot;
                    String r = Util.javaSignature2irType(this.cv.getStatistics().getResolver(), lv.signature) + "* %" + lv.name; // todo
                    str = str.replace(s, r);
                }
            }

            p = str.indexOf("slot-type2");
            if (p != -1) {
                for (Integer slot : vars.keySet()) {
                    _LocalVar lv = vars.get(slot);
                    String s = "slot-type:" + slot;
                    String r = Util.javaSignature2irType(this.cv.getStatistics().getResolver(), lv.signature);
                    if (r == null) {
                        System.out.println("CF TYPE " + lv.signature);
                    } else {
                        str = str.replace(s, r);
                    }
                }
            }

            // label
            p = str.indexOf("label:");
            if (p != -1) {
                str = str.substring(6);
                if (usedLabels.contains(str)) {
                    ps.print("br label %");
                    ps.println(str);
                    str = str + ":";
                } else {
                    continue;
                }
            }
            // inc
            p = str.indexOf("inc:");
            if (p != -1) {
                str = str.substring(4);
                p = str.indexOf(":");
                int slot = Integer.parseInt(str.substring(0, p));
                int value = Integer.parseInt(str.substring(p + 1));
                _LocalVar var = this.vars.get(slot);
                if (var != null) {
                    //%x = load i32* %x.ptr        ; загрузили значение типа i32 по указателю %x.ptr
                    //%tmp = add i32 %x, 5         ; прибавили 5
                    //store i32 %tmp, i32* %x.ptr
                    ps.println("    %__tmpv" + tmp + " = load i32* %" + var.name); tmp++;
                    ps.println("    %__tmpv" + tmp + " = add i32 %__tmpv" + (tmp - 1) + ", " + value); tmp++;
                    ps.println("    store i32 %__tmpv" + (tmp - 1) + ", i32* %" + var.name);
                    continue;
                }
            }
            ps.print("    ");
            ps.println(str);
        }
        // 3) end
        ps.println("}");
        ps.println("");
    }
}
