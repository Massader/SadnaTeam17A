package APILayer.Alerts;

import DomainLayer.Market.Notification;
import ServiceLayer.Loggers.ErrorLogger;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

@Scope(value = "singleton")
public class AlertController {

    private final ConcurrentHashMap<UUID, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter createNotifier(UUID clientCredentials) {
        return emitters.put(clientCredentials, new SseEmitter());
    }

    public void sendNotification(UUID clientCredentials, Notification notification) {
        SseEmitter emitter = emitters.get(clientCredentials);
        if (emitter != null) {
            try {
                emitter.send(notification);
            } catch (Exception e) {
                ErrorLogger.getInstance().log(Level.WARNING, "Failed to send alert to " + clientCredentials);
            }
        }
    }

    public void closeEmitter(UUID clientCredentials) {
        emitters.remove(clientCredentials);
    }
}
