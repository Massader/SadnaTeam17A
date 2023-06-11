package ServiceLayer.Loggers;

import java.io.File;
import java.nio.file.Files;
import java.util.logging.*;

import static java.util.logging.Logger.getLogger;

public class ErrorLogger {
    private static Logger LOG;
    private static ErrorLogger instance = null;
    private static Object instanceLock = new Object();
    private static FileHandler fileHandler;

    private ErrorLogger(){
        try {
            boolean file = new File(System.getProperty("user.dir") + "/logs").mkdirs();
            LOG = getLogger("ErrorLog.log");
            fileHandler = new FileHandler(System.getProperty("user.dir") + "/logs/ErrorLog.log");
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
        String greenMsg = "\u001B[31m" + msg + "\u001B[0m";
        LOG.log(level, msg);
    }
}
