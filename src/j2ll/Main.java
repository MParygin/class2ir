package j2ll;

import org.objectweb.asm.ClassReader;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

/**
 *
 */
public class Main {



    public static void main(String[] args) throws IOException {
        //String className = "org.objectweb.asm.ClassReader";
        //String className = "clojure.main";
        String className = "test.Test";
        //String className = "java.lang.Boolean";


        String out = "test.Test.ll";
        PrintStream ps = new PrintStream(new File("./tmp", out));

        Statistics statistics = new Statistics();
        StatisticsCollector sc = new StatisticsCollector(statistics);
        CV cv = new CV(ps, statistics);

        // read class
        ClassReader cr = new ClassReader(className);
        cr.accept(sc, 0);

        System.out.println(statistics.getResolver().getClasses());

        cr.accept(cv, 0);
        ps.flush();
    }

}
