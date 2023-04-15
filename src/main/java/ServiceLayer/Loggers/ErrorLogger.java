package ServiceLayer.Loggers;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

public class ErrorLogger {
    private static final Logger LOG = getLogger(System.getProperty("user.dir") + "/ErrorLog.log");
    private static ErrorLogger instance = null;
    private static Object instanceLock = new Object();
    private static FileHandler fileHandler;

    private ErrorLogger(){
        try {
            fileHandler = new FileHandler("ErrorLog");
            LOG.addHandler(fileHandler);
        } catch (Exception ignored) {}
    }

    public static ErrorLogger getInstance() {
        synchronized (instanceLock) {
            if (instance == null) {
                instance = new ErrorLogger();
            }
        }
        return instance;
    }

    public void log(Level level, String msg){
        LOG.log(level, msg);
    }
}
