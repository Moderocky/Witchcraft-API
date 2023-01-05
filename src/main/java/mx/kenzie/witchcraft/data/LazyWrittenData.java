package mx.kenzie.witchcraft.data;

import mx.kenzie.fern.Fern;
import mx.kenzie.sloth.Lazy;
import mx.kenzie.witchcraft.WitchcraftAPI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class LazyWrittenData extends Lazy<Void> {
    
    protected transient File file;
    
    public void scheduleSave() {
        WitchcraftAPI.executor.submit(this::save);
    }
    
    public void save() {
        assert this.isReady();
        try {
            if (!file.exists()) {
                this.file.getParentFile().mkdirs();
                this.file.createNewFile();
            }
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
}