package ServiceLayer.Loggers;

import DomainLayer.Market.Users.Message;

import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

public class ErrorLogger {
    private static final Logger LOG = getLogger("ErrorLogger");
    private static ErrorLogger instance = null;
    private static Object instanceLock = new Object();

    private ErrorLogger(){

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
