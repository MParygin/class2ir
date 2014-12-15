package j2ll;

/**
 *
 */
public class StackValue {

    public static final int TYPE_IMM = 0;
    public static final int TYPE_REG = 1;
    public static final int TYPE_OBJREF = 2;

    private int type;
    private Object value;
    private String IR;

    public StackValue(int type, String IR) {
        this.type = type;
        this.IR = IR;
    }

    public StackValue(int type, Object value, String IR) {
        this.type = type;
        this.value = value;
        this.IR = IR;
    }

    public int getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public String getIR() {
        return IR;
    }

    public String toString() {
        if (this.type == TYPE_REG || this.type == TYPE_OBJREF) return "%stack" + value;
        if (value instanceof Float) {
            Float f = (Float) value;
            return "0x" + Integer.toHexString(Float.floatToRawIntBits(f)) + "00000000";
        }
        if (value instanceof Double) {
            Double f = (Double) value;
            return "0x" + Long.toHexString(Double.doubleToRawLongBits(f));
        }
        return value.toString(); // imm ??
    }

    public String fullName() { //todo
        return getIR() + " " + toString();
    }

}
