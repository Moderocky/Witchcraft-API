package mx.kenzie.witchcraft.data;

import mx.kenzie.fern.Fern;
import mx.kenzie.sloth.Lazy;
import mx.kenzie.witchcraft.WitchcraftAPI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LazyWrittenData<Type> extends Lazy<Type> {
    public static final LazyWrittenData<Void> FLAG = new LazyWrittenData<>() {
        @Override
        public void save() {
        }

        @Override
        public void load() {
        }

        @Override
        public boolean exists() {
            return false;
        }
    };
    private static final Queue<LazyWrittenData<?>.Task> IO_QUEUE = new ConcurrentLinkedQueue<>();
    private transient final Task save = new SaveTask(), load = new LoadTask();
    protected transient File file;

    @SuppressWarnings("unchecked")
    protected LazyWrittenData() {
        target = (Type) this;
    }

    public static Runnable processSaveTask() {
        return () -> {
            while (!WitchcraftAPI.isClosing() || !IO_QUEUE.isEmpty()) {
                try {
                    final LazyWrittenData<?>.Task task = IO_QUEUE.poll();
                    if (task == null) {
                        if (WitchcraftAPI.isClosing()) break;
                        WitchcraftAPI.sleep(100);
                        continue;
                    }
                    task.run();
                } catch (Throwable ex) {
                    System.err.println("Error in IO process:");
                    ex.printStackTrace();
                }
            }
        };
    }

    public void save() {
        assert this.isReady();
        try {
            if (file.getParentFile() != null && !file.getParentFile().exists()) file.getParentFile().mkdirs();
            if (!file.exists()) this.file.createNewFile();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        try (final Fern fern = new Fern(null, new FileOutputStream(file))) {
            fern.write(this, "\t");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public synchronized void scheduleSave() {
        if (IO_QUEUE.contains(this.save)) return;
        IO_QUEUE.add(this.save);
    }

    public void scheduleLoad() {
        if (IO_QUEUE.contains(this.load)) return;
        IO_QUEUE.add(this.load);
    }

    public void load() {
        if (file.exists()) try (final Fern fern = new Fern(new FileInputStream(file))) {
            fern.toObject(this);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        this.finish();
    }

    public boolean exists() {
        return file != null && file.exists() && file.isFile();
    }

    public class SaveTask extends Task {
        @Override
        public void run() {
            save();
        }
    }

    public class LoadTask extends Task {
        @Override
        public void run() {
            load();
        }
    }

    public abstract class Task implements Runnable {

        public abstract void run();

        @Override
        public int hashCode() {
            return LazyWrittenData.this.hashCode() * System.identityHashCode(this);
        }

    }
}
