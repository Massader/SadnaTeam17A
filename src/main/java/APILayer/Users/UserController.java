package APILayer.Users;

import APILayer.Requests.LoginRegisterRequest;
import ServiceLayer.Service;
import ServiceLayer.ServiceObjects.ServiceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/users")
public class UserController {

    private final Service service;

    @Autowired
    public UserController(Service service) {
        this.service = service;
        service.init();
    }

    @PostMapping(path = "/register")
    public boolean register(@RequestBody LoginRegisterRequest request) {
        return service.register(request.getUsername(), request.getPassword());
    }

    @PostMapping(path = "/login")
    public ServiceUser login(@RequestBody LoginRegisterRequest request) {
        return service.login(request.getClientCredentials(), request.getUsername(), request.getPassword());
    }

    @PostMapping(path = "/createClient")
    public UUID createClient() {
        return service.createClient();
    }

    @PostMapping(path = "/logout")
    public UUID logout() {
        return service.logout();
    }
}
