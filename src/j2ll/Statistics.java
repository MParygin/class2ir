package j2ll;

import java.util.HashMap;
import java.util.Map;

/**
 * Statistics
 */
public final class Statistics {

    private Resolver resolver = new Resolver();
    private Map<String, LocalVars> vars = new HashMap<String, LocalVars>();

    public Resolver getResolver() {
        return resolver;
    }

    public void put(String name, LocalVars lv) {
        this.vars.put(name, lv);
    }

    public LocalVars get(String name) {
        return this.vars.get(name);
    }
}
