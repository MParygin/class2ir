package j2ll;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Block of Local Vars (for 1 method)
 */
public final class LocalVars {

    Map<Integer, _LocalVar> vars = new HashMap<Integer, _LocalVar>();

    public void put(int slot, _LocalVar localVar) {
        vars.put(slot, localVar);
    }

    public _LocalVar get(int slot) {
        return vars.get(slot);
    }

    public Set<Integer> keySet() {
        return vars.keySet();
    }
}
