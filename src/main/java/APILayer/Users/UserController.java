package APILayer.Users;

import ServiceLayer.Service;
import ServiceLayer.ServiceObjects.ServiceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/hello")
public class UserController {

    private final Service service;

    @Autowired
    public UserController(Service service) {
        this.service = service;
        service.init();
    }

    @GetMapping(path = "/getUser/id={id}")
    public ServiceUser getUser(@PathVariable(name = "id") UUID id) {
        //UUID uuid = UUID.fromString(id);
        ServiceUser user = service.getUserInfo(id);
        if (user == null) return new ServiceUser(UUID.fromString("3c4cefa-bde3-40c8-9254-29e0df6c387f"), "person");
        return user;
    }

    @PostMapping(path = "/addUser")
    public ServiceUser register(@RequestBody ServiceUser user) {
        return user;
    }

    @GetMapping(path = "/hello")
    public String hello() {
        return "Hello world";
    }
}
