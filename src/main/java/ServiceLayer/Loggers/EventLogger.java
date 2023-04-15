package ServiceLayer.Loggers;

import ServiceLayer.Service;

import java.nio.file.Path;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

public class EventLogger {
    private static final Logger LOG = getLogger("EventLogger");
    private static EventLogger instance = null;
    private static Object instanceLock = new Object();
    private static FileHandler fileHandler;

    private EventLogger(){
        try {
            fileHandler = new FileHandler(System.getProperty("user.dir") + "/EventLog.log");
            LOG.addHandler(fileHandler);
        } catch (Exception ignored) {}
    }
    public static EventLogger getInstance() {
        synchronized (instanceLock) {
            if (instance == null) {
                instance = new EventLogger();
            }
        }
        return instance;
    }

    public void log(Level level, String msg){
        LOG.log(level, msg);
    }
}
