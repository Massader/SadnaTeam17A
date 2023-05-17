package APILayer.Alerts;

import DomainLayer.Market.Notification;
import ServiceLayer.Loggers.ErrorLogger;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
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
        SseEmitter emitter = new SseEmitter(-1L);
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
        emitters.get(clientCredentials).complete();
        emitters.remove(clientCredentials);
    }

    @GetMapping(path = "/get-notifier/id={id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public SseEmitter getNotifier(@PathVariable(name = "id") UUID clientCredentials) {
        return emitters.get(clientCredentials);
    }
}
