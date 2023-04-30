package APILayer.Users;

import APILayer.Requests.SecurityQuestionRequest;
import APILayer.Requests.TargetUserRequest;
import APILayer.Requests.LoginRegisterRequest;
import APILayer.Requests.Request;
import ServiceLayer.Response;
import ServiceLayer.Service;
import ServiceLayer.ServiceObjects.ServicePurchase;
import ServiceLayer.ServiceObjects.ServiceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public Response<Boolean> register(@RequestBody LoginRegisterRequest request) {
        return service.register(request.getUsername(), request.getPassword());
    }

    @PostMapping(path = "/login")
    public Response<ServiceUser> login(@RequestBody LoginRegisterRequest request) {
        return service.login(request.getClientCredentials(), request.getUsername(), request.getPassword());
    }

    @PostMapping(path = "/create-client")
    public Response<UUID> createClient() {
        return service.createClient();
    }

    @PostMapping(path = "/logout")
    public Response<UUID> logout(@RequestBody Request request) {
        return service.logout(request.getClientCredentials());
    }

    @PostMapping(path = "/close-client")
    public Response<Boolean> closeClient(@RequestBody Request request) {
        return service.closeClient(request.getClientCredentials());
    }

    @PostMapping(path = "/admin/delete-user")
    public Response<Boolean> deleteUser(@RequestBody TargetUserRequest request) {
        return service.deleteUser(request.getClientCredentials(), request.getTargetUser());
    }

    @PostMapping(path = "/purchase-history")
    public Response<List<ServicePurchase>> getPurchaseHistory(@RequestBody TargetUserRequest request) {
        return service.getPurchaseHistory(request.getClientCredentials(), request.getTargetUser());
    }

    @GetMapping(path = "/info")
    public Response<ServiceUser> getUserInfo(@RequestBody Request request) {
        return service.getUserInfo(request.getClientCredentials());
    }

    @PostMapping(path = "/security/add-question")
    public Response<Boolean> addSecurityQuestion(@RequestBody SecurityQuestionRequest request) {
        return service.addSecurityQuestion(request.getClientCredentials(), request.getQuestion(), request.getAnswer());
    }

    @GetMapping(path = "/security/validate-question")
    public Response<Boolean> validateSecurityQuestion(@RequestBody SecurityQuestionRequest request) {
        return service.validateSecurityQuestion(request.getClientCredentials(), request.getAnswer());
    }

    @GetMapping(path = "/security/get-question")
    public Response<String> getSecurityQuestion(@RequestBody Request request) {
        return service.getSecurityQuestion(request.getClientCredentials());
    }

    @PostMapping(path = "/admin/register")
    public Response<Boolean> registerAdmin(@RequestBody LoginRegisterRequest request) {
        return service.registerAdmin(request.getClientCredentials(), request.getUsername(), request.getPassword());
    }

}
