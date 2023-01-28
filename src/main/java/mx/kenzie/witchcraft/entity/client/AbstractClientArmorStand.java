package mx.kenzie.witchcraft.entity.client;

import java.util.UUID;

public abstract class AbstractClientArmorStand extends AbstractClientEntity {

    public final float[] headPose = new float[3];
    public final float[] rightArmPose = new float[3];
    public final float[] leftArmPose = new float[3];
    public final float[] bodyPose = new float[3];
    public final float[] rightLegPose = new float[3];
    public final float[] leftLegPose = new float[3];
    public boolean marker;
    public boolean arms;
    public boolean small;
    public boolean basePlate;

    public AbstractClientArmorStand(UUID uuid) {
        this(uuid, getRandom());
    }

    public AbstractClientArmorStand(UUID uuid, int id) {
        super(uuid, id);
    }

    public AbstractClientArmorStand(int id) {
        this(UUID.randomUUID(), id);
    }

    public AbstractClientArmorStand() {
        this(UUID.randomUUID(), getRandom());
    }

}
