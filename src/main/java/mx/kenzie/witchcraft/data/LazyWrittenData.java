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
    private static final Queue<LazyWrittenData<?>> SAVE_QUEUE = new ConcurrentLinkedQueue<>();
    private static volatile boolean closing;
    
    protected transient File file;
    
    @SuppressWarnings("unchecked")
    protected LazyWrittenData() {
        target = (Type) this;
    }
    
    public synchronized void scheduleSave() {
        if (SAVE_QUEUE.contains(this)) return;
        SAVE_QUEUE.add(this);
    }
    
    public static void setClosing(boolean closing) {
        LazyWrittenData.closing = closing;
    }
    
    public static Runnable processSaveTask() {
        return () -> {
            while (!closing || !SAVE_QUEUE.isEmpty()) {
                final LazyWrittenData<?> data = SAVE_QUEUE.poll();
                if (data == null) {
                    WitchcraftAPI.sleep(100);
                    continue;
                }
                data.save();
            }
        };
    }
    
    public void save() {
        assert this.isReady();
        try {
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
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
    
    public void scheduleLoad() {
        WitchcraftAPI.executor.submit(this::load);
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
}
