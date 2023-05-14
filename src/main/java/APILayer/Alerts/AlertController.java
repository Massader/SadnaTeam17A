package APILayer.Alerts;

import DomainLayer.Market.Notification;
import ServiceLayer.Loggers.ErrorLogger;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

@Component
@Scope(value = "singleton")
@RestController
@RequestMapping(path = "/api/v1/alerts")
public class AlertController {

    private final ConcurrentHashMap<UUID, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter createNotifier(UUID clientCredentials) {
        SseEmitter emitter = new SseEmitter();
        emitters.put(clientCredentials, emitter);
        return emitter;
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

    @GetMapping(path = "/get-notifier/id={id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter getNotifier(@PathVariable(name = "id") UUID clientCredentials) {
        return emitters.get(clientCredentials);
    }
}
