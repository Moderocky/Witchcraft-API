package mx.kenzie.witchcraft.spell;

import java.util.Map;
import java.util.function.Function;

public interface SpellSupplier extends Function<Map<String, Object>, StandardSpell> {
    SpellSupplier DEFAULT = NoEffectSpell::new;
    
}
