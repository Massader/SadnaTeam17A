package APILayer.Stores;

import APILayer.Requests.CreateStoreRequest;
import ServiceLayer.Response;
import ServiceLayer.Service;
import ServiceLayer.ServiceObjects.ServiceStore;
import ServiceLayer.ServiceObjects.ServiceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/stores")
public class StoreController {
    private final Service service;

    @Autowired
    public StoreController(Service service) {
        this.service = service;
        service.init();
    }

    @PostMapping(path = "/createStore")
    public ServiceStore createStore(@RequestBody CreateStoreRequest req) {
        ServiceStore store = service.createStore(req.getClientCredentials(), req.getName(), req.getDescription());
        return store;
    }

}