package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.entity.CustomEntityType;

import java.util.Map;

public class CallToArmsSpell extends BladesOfGlorySpell {
    
    public CallToArmsSpell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    protected CustomEntityType getEntityType() {
        return CustomEntityType.DEAD_SOLDIER_SUMMON;
    }
    
}
