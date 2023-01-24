package mx.kenzie.witchcraft.data;

import java.io.File;

public class Settings extends LazyWrittenData<Settings> {
    
    public String server_name = "Witchcraft", flavour = "unknown";
    
    protected Settings() {
        this(new File("data/settings.fern"));
    }
    
    protected Settings(File file) {
        this.file = file;
        this.scheduleLoad();
    }
    
}
