package ServiceLayer.Loggers;

import ServiceLayer.Service;

import java.nio.file.Path;
import java.util.logging.*;

import static java.util.logging.Logger.getLogger;

public class EventLogger {
    private static final Logger LOG = getLogger("EventLogger");
    private static EventLogger instance = null;
    private static Object instanceLock = new Object();
    private static FileHandler fileHandler;

    private EventLogger(){
        try {
            fileHandler = new FileHandler(System.getProperty("user.dir") + "/logs/EventLog.log");
            fileHandler.setFormatter(new SimpleFormatter() {
                private static final String format = "%1$tF %1$tT %4$s %3$s: %5$s%6$s%n";

                @Override
                public synchronized String format(LogRecord record) {
                    return String.format(format,
                            record.getMillis(),
                            record.getMillis(),
                            record.getSourceClassName(),
                            record.getLevel().getName(),
                            record.getMessage(),
                            (record.getThrown() != null) ? record.getThrown() : "");
                }
            });
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
