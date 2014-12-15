package j2ll;

/**
 * Stack
 */
public class _Stack {

    int ord = 0;
    private final int SIZE = 1024;

    int[] names = new int[SIZE];
    StackValue[] imm2 = new StackValue[SIZE];
    int pos = 0;

    _Stack() {

    }

    public String push(String type) {
        int v = ord;
        this.names[pos] = ord;
        this.imm2[pos] = new StackValue(StackValue.TYPE_REG, v, type);
        ord++;
        pos++;
        return "%stack" + v;
    }

    public String pushObjRef(String type) {
        return push(new StackValue(StackValue.TYPE_OBJREF, ord, type));
    }

    public String pushImm(Object value, String type) {
        return push(new StackValue(StackValue.TYPE_IMM, value, type));
    }

    public String push(StackValue value) {
        int v = ord;
        this.names[pos] = ord;
        this.imm2[pos] = value;
        ord++;
        pos++;
        return "%stack" + v;
    }

    public StackValue pop() {
        if (pos == 0) return new StackValue(StackValue.TYPE_IMM, -1, "i32");
        pos--;
        return this.imm2[pos];
    }

}
