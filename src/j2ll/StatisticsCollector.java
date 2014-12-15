package j2ll;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Statistics Collector
 */
public final class StatisticsCollector extends ClassVisitor {

    private Statistics statistics;

    public StatisticsCollector(Statistics statistics) {
        super(Opcodes.ASM5);

        this.statistics = statistics;
    }

    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        // collect vars
        LocalVars lv = new LocalVars();
        this.statistics.put(name + desc, lv);
        return new MethodStatisticsCollector(Opcodes.ASM5, lv, this.statistics.getResolver());
    }


}
