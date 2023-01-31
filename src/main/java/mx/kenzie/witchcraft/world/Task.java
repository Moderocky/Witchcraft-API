package mx.kenzie.witchcraft.world;

public interface Task {
    boolean tick();

    void finish();

    boolean isFinished();

    int getTotalStages();

    int getStage();

    int remaining();
}
