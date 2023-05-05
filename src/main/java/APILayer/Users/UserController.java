package APILayer.Users;

import APILayer.Requests.*;
import DomainLayer.Market.Users.Roles.Role;
import ServiceLayer.Response;
import ServiceLayer.Service;
import ServiceLayer.ServiceObjects.ServiceComplaint;
import ServiceLayer.ServiceObjects.ServicePurchase;
import ServiceLayer.ServiceObjects.ServiceShoppingBasket;
import ServiceLayer.ServiceObjects.ServiceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
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

    @DeleteMapping(path = "/close-client")
    public Response<Boolean> closeClient(@RequestBody Request request) {
        return service.closeClient(request.getClientCredentials());
    }

    @DeleteMapping(path = "/admin/delete-user")
    public Response<Boolean> deleteUser(@RequestBody TargetRequest request) {
        return service.deleteUser(request.getClientCredentials(), request.getTargetId());
    }

    @PostMapping(path = "/purchase-history")
    public Response<List<ServicePurchase>> getPurchaseHistory(@RequestBody TargetRequest request) {
        return service.getPurchaseHistory(request.getClientCredentials(), request.getTargetId());
    }

    @GetMapping(path = "/info/id={id}")
    public Response<ServiceUser> getUserInfo(@PathVariable(name = "id") UUID clientCredentials) {
        return service.getUserInfo(clientCredentials);
    }

    @PostMapping(path = "/security/add-question")
    public Response<Boolean> addSecurityQuestion(@RequestBody SecurityQuestionRequest request) {
        return service.addSecurityQuestion(request.getClientCredentials(), request.getQuestion(), request.getAnswer());
    }

    @GetMapping(path = "/security/validate-question/id={id}&answer={answer}")
    public Response<Boolean> validateSecurityQuestion(@PathVariable(name = "id") UUID clientCredentials,
                                                      @PathVariable(name = "answer") String answer) {
        return service.validateSecurityQuestion(clientCredentials, answer);
    }

    @GetMapping(path = "/security/get-question/id={id}")
    public Response<String> getSecurityQuestion(@PathVariable(name = "id") UUID clientCredentials) {
        return service.getSecurityQuestion(clientCredentials);
    }

    @PostMapping(path = "/admin/register")
    public Response<Boolean> registerAdmin(@RequestBody LoginRegisterRequest request) {
        return service.registerAdmin(request.getClientCredentials(), request.getUsername(), request.getPassword());
    }

    @PutMapping(path = "/security/change-password")
    public Response<Boolean> changePassword(@RequestBody ChangePasswordRequest request) {
        return service.changePassword(request.getClientCredentials(), request.getOldPassword(), request.getNewPassword());
    }

    @GetMapping(path = "/get-cart/id={id}")
    public Response<List<ServiceShoppingBasket>> getCart(@PathVariable(name = "id") UUID clientCredentials) {
        return service.getCart(clientCredentials);
    }

    @PostMapping(path = "/add-to-cart")
    public Response<Boolean> addItemToCart(@RequestBody CartItemRequest request) {
        return service.addItemToCart(request.getClientCredentials(), request.getItemId(), request.getQuantity(),
                request.getStoreId());
    }

    @DeleteMapping(path = "/remove-from-cart")
    public Response<Boolean> removeItemFromCart(@RequestBody CartItemRequest request) {
        return service.removeItemFromCart(request.getClientCredentials(), request.getItemId(), request.getQuantity(),
                request.getStoreId());
    }

    @PostMapping(path = "/send-message")
    public Response<UUID> sendMessage(@RequestBody MessageRequest request){
        return service.sendMessage(request.getClientCredentials(), request.getSender(), request.getRecipient(), request.getBody());
    }

    @GetMapping(path = "/get-user-roles/id={id}")
    public Response<List<Role>> getUserRoles(@PathVariable(name = "id") UUID clientCredentials) {
        return service.getUserRoles(clientCredentials);
    }


}