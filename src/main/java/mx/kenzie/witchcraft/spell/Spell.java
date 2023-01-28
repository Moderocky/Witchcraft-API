package mx.kenzie.witchcraft.spell;

import mx.kenzie.witchcraft.data.MagicClass;
import mx.kenzie.witchcraft.data.SpellType;
import mx.kenzie.witchcraft.data.recipe.Ingredient;
import org.bukkit.entity.LivingEntity;

public interface Spell {

    MagicClass getStyle();

    SpellType getType();

    String getId();

    String getName();

    String getPatternPicture();

    String getDescription();

    Ingredient[] getMaterials();

    String getCircumstances();

    String getRealm();

    int getPoints();

    int getEnergy();

    Pattern getPattern();

    void cast(LivingEntity caster, int range, float scale, double amplitude);

    SpellResult checkCast(LivingEntity caster, int range, float scale, double amplitude);

}

