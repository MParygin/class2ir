package j2ll;

import org.objectweb.asm.*;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CV extends ClassVisitor {

    // out
    private PrintStream ps;
    private Statistics statistics;

    // state
    String className;
    String superName;
    private List<_Field> staticFields = new ArrayList<_Field>();
    private List<_Field> fields = new ArrayList<_Field>();

    private List<MV> methods = new ArrayList<MV>();
    // shared states
    Set<String> declares = new HashSet<String>();

    public CV(PrintStream ps, Statistics statistics) {
        super(Opcodes.ASM5);
        this.ps = ps;
        this.statistics = statistics;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.className = name;
        this.superName = superName;
    }

    public void visitSource(String source, String debug) {
//        this.ps.println("src " + source);
    }

    public void visitOuterClass(String owner, String name, String desc) {
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return null;
    }

    public void visitAttribute(Attribute attr) {
        this.ps.println(" attr " + attr);
    }

    public void visitInnerClass(String name, String outerName, String innerName, int access) {
    }

    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        if ((access & Opcodes.ACC_STATIC) != 0) {
            this.staticFields.add(new _Field(name, desc));
        } else {
            this.fields.add(new _Field(name, desc));
//            this.ps.println("  f  " + desc + " " + name + " " + signature + " " + value);
        }
        return null;
    }

    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MV mv = new MV(name, desc, this);
        this.methods.add(mv);
        return mv;
    }

    public void visitEnd() {
        this.ps.println("; CLASS: " + this.className + " extends " + this.superName);
        this.ps.println();
        this.ps.println("declare noalias i8* @malloc(i32)");
        this.ps.println("declare void @free(i8*)");
        this.ps.println();

        // declares
        for (String name : declares) {
            this.ps.println("declare " + name);
        }
        this.ps.println();

        // use classes
        this.ps.println("; first generation");
        Resolver next = new Resolver();
        for (String name : statistics.getResolver().getClasses()) {
            this.ps.println(Util.class2struct(next, name) + " ; use " + name);
        }
        this.ps.println("; second generation");
        Resolver next2 = new Resolver();
        for (String name : next.getClasses()) {
            this.ps.println(Util.class2struct(next2, name) + " ; use " + name);
        } // todo next2 ???


        // out fields
        this.ps.println("; globals");
        for (_Field field : staticFields) {
            String ir = Util.javaSignature2irType(statistics.getResolver(), field.javaSignature);
            this.ps.println(Util.static2str(this.className, field.name) + " = internal global " + ir + " 0");
        }
        this.ps.println();

        // out methods
        for (MV method : this.methods) {
            method.out(this.ps);
        }

    }
}