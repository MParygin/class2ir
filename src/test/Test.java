package test;

public class Test extends TestParent {

    boolean bl;
    byte bt;
    short sh;
    int in;
    long ln;
    float ft;
    double db;

    static long sl = 1111;
    static Test singleton = new Test();

    public Test() {
        this.bl = true;
        this.bt = 127;
        this.sh = 100;
        this.in = 142;
        this.ln = 42L;
        this.ft = 123.33f;
        this.db = 0.53;

        linux.glibc.put(this.in);
    }

    @Override
    public int length() {
        return super.length();
    }

    public static void main() {
        long a = System.currentTimeMillis();

        Test test = new Test();
        test.in = 9;
        linux.glibc.put(test.in);

/*
        double v[] = new double[10000];
        int ii[] = new int[123];
        for (int i = 0; i < 10000; i++) v[i] = i * 1.2d;

        switch (test.in) {
            case 7:
            case 9:
            case 10: linux.glibc.put(10);
                break;
            case 11: linux.glibc.put(7);
                break;
            //case 16:
            case 18: linux.glibc.put(5);
                break;
            default:
                linux.glibc.put(5888);
        }
*/


        linux.glibc.put(System.currentTimeMillis() - a);

        linux.glibc.put(sl);
//        linux.glibc.put(ii[6]);
        linux.glibc.put(singleton.ln);


        //double d[][] = new double[101][201];
    }
}
