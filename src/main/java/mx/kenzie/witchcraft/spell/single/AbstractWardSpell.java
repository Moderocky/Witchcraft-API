package mx.kenzie.witchcraft.spell.single;

import mx.kenzie.witchcraft.Minecraft;
import mx.kenzie.witchcraft.Protection;
import mx.kenzie.witchcraft.WitchcraftAPI;
import mx.kenzie.witchcraft.entity.CustomEntityType;
import mx.kenzie.witchcraft.entity.WardCube;
import mx.kenzie.witchcraft.spell.StandardSpell;
import mx.kenzie.witchcraft.spell.effect.ParticleCreator;
import mx.kenzie.witchcraft.spell.effect.VectorShape;
import mx.kenzie.witchcraft.ward.SimpleWardInstance;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

abstract class AbstractWardSpell extends StandardSpell {
    protected transient Block target;

    public AbstractWardSpell(Map<String, Object> map) {
        super(map);
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        if (Minecraft.getInstance().nearbyEntities(caster, CustomEntityType.WARD_CUBE_TOTEM) > 2) return false;
        final List<Block> blocks = AbstractSummonSpell.getValidSpawnSpaces(caster.getLocation(), 3);
        if (blocks.isEmpty()) return false;
        this.target = blocks.get(0);
        return true;
    }

    protected WardCube summonWard(LivingEntity caster, int lifetime) {
        final Location spawn = target.getLocation().add(0.5, 3, 0.5);
        final WardCube ward = CustomEntityType.WARD_CUBE_TOTEM.summon(caster, spawn);
        assert ward != null;
        Protection.getInstance().registerWard(new SimpleWardInstance(caster, ward, lifetime));
        return ward;
    }

    protected void drawCircle(ParticleCreator creator, Location centre) {
        final int particles = 80;
        final VectorShape circle = creator.createCircle(new Vector(0, -1, 0), 10, particles);
        for (Vector vector : circle) {
            final Location point = centre.clone().add(vector);
            creator.getBuilder().location(point).spawn();
            WitchcraftAPI.sleep(4000 / particles);
        }
    }

    protected List<LivingEntity> getAffected(LivingEntity caster, LivingEntity totem, boolean includeAllies) {
        final Location centre = totem.getLocation();
        final List<LivingEntity> list = new ArrayList<>(centre.getNearbyLivingEntities(10, 5));
        final Minecraft minecraft = Minecraft.getInstance();
        list.removeIf(found -> {
            if (totem == found) return true;
            if (!minecraft.isValidToDamage(found)) return true;
            return !includeAllies && minecraft.isAlly(found, caster);
        });
        return list;
    }
}
