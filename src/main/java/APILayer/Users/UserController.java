package APILayer.Users;

import APILayer.Alerts.AlertController;
import APILayer.Requests.*;
import DataAccessLayer.ClientRepository;
import DataAccessLayer.RepositoryFactory;
import DomainLayer.Market.Notification;
import DomainLayer.Market.Stores.PurchaseTypes.Bid;
import DomainLayer.Market.Users.Roles.Role;
import ServiceLayer.Response;
import ServiceLayer.Service;
import ServiceLayer.ServiceObjects.ServiceMessage;
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
    
    private final AlertController alertController;
    private final Service service;
    private final RepositoryFactory repositoryFactory;
    
    @Autowired
    public UserController(Service service, AlertController alertController, RepositoryFactory repositoryFactory) {
        this.repositoryFactory =repositoryFactory;
        this.service = service;
        service.init(repositoryFactory);
        this.alertController = alertController;
    }
    //DB
    @PostMapping(path = "/register")
    public Response<Boolean> register(@RequestBody LoginRegisterRequest request) {
        return service.register(request.getUsername(), request.getPassword());
    }
    //DB
    @PostMapping(path = "/login")
    public Response<ServiceUser> login(@RequestBody LoginRegisterRequest request) {
        Response<ServiceUser> response = service.login(request.getClientCredentials(), request.getUsername(),
                request.getPassword(), alertController::sendNotification);
        if (!response.isError())
            alertController.createNotifier(response.getValue().getId());
        return response;
    }

//DB
    @PostMapping(path = "/create-client")
    public Response<UUID> createClient() {
        return service.createClient();
    }
    //DB
    @PostMapping(path = "/logout")
    public Response<UUID> logout(@RequestBody Request request) {
        Response<UUID> response = service.logout(request.getClientCredentials());
        if (!response.isError())
            alertController.closeEmitter(request.getClientCredentials());
        return response;
    }
    
    @DeleteMapping(path = "/close-client")
    public Response<Boolean> closeClient(@RequestBody Request request) {
        return service.closeClient(request.getClientCredentials());
    }
    
    @DeleteMapping(path = "/admin/delete-user")
    public Response<Boolean> deleteUser(@RequestBody TargetRequest request) {
        return service.deleteUser(request.getClientCredentials(), request.getTargetId());
    }
    
    @GetMapping(path = "/purchase-history/id={id}")
    public Response<List<ServicePurchase>> getPurchaseHistory(@PathVariable(name = "id") UUID clientCredentials) {
        return service.getPurchaseHistory(clientCredentials, clientCredentials);
    }
    
    @GetMapping(path = "/info/id={id}")
    public Response<ServiceUser> getUserInfo(@PathVariable(name = "id") UUID clientCredentials) {
        return service.getUserInfo(clientCredentials);
    }

    //DB
    @PostMapping(path = "/security/add-question")
    public Response<Boolean> addSecurityQuestion(@RequestBody SecurityQuestionRequest request) {
        return service.addSecurityQuestion(request.getClientCredentials(), request.getQuestion(), request.getAnswer());
    }


    @GetMapping(path = "/security/validate-question/id={id}&answer={answer}")
    public Response<Boolean> validateSecurityQuestion(@PathVariable(name = "id") UUID clientCredentials,
                                                      @PathVariable(name = "answer") String answer) {
        Response<ServiceUser> response = service.validateSecurityQuestion(clientCredentials, answer, alertController::sendNotification);
        if (!response.isError()) {
            alertController.createNotifier(response.getValue().getId());
            return Response.getSuccessResponse(true);
        }
        return Response.getFailResponse(response.getMessage());
    }

    //DB
    @GetMapping(path = "/security/get-question/id={id}")
    public Response<String> getSecurityQuestion(@PathVariable(name = "id") UUID clientCredentials) {
        return service.getSecurityQuestion(clientCredentials);
    }

    //DB
    @PostMapping(path = "/admin/register")
    public Response<Boolean> registerAdmin(@RequestBody LoginRegisterRequest request) {
        return service.registerAdmin(request.getClientCredentials(), request.getUsername(), request.getPassword());
    }

    //DB
    @PutMapping(path = "/security/change-password")
    public Response<Boolean> changePassword(@RequestBody ChangePasswordRequest request) {
        return service.changePassword(request.getClientCredentials(), request.getOldPassword(), request.getNewPassword());
    }

    //DB
    @GetMapping(path = "/get-cart/id={id}")
    public Response<List<ServiceShoppingBasket>> getCart(@PathVariable(name = "id") UUID clientCredentials) {
        return service.getCart(clientCredentials);
    }

    //DB
    @PostMapping(path = "/add-to-cart")
    public Response<Boolean> addItemToCart(@RequestBody CartItemRequest request) {
        return service.addItemToCart(request.getClientCredentials(), request.getItemId(), request.getQuantity(),
                request.getStoreId());
    }

    //DB
    @DeleteMapping(path = "/remove-from-cart")
    public Response<Boolean> removeItemFromCart(@RequestBody CartItemRequest request) {
        return service.removeItemFromCart(request.getClientCredentials(), request.getItemId(), request.getQuantity(),
                request.getStoreId());
    }
    
    @GetMapping(path = "/get-item-discount/id={id}&storeId={storeId}&itemId={itemId}")
    public Response<Double> getItemDiscount(@PathVariable(name = "id") UUID clientCredentials,
                                                      @PathVariable(name = "storeId") UUID storeId,
                                                      @PathVariable(name = "itemId") UUID itemId) {
        return service.getItemDiscount(clientCredentials, storeId, itemId);
    }
    
    @PostMapping(path = "/messages/send-message")
    public Response<UUID> sendMessage(@RequestBody MessageRequest request) {
        return service.sendMessage(request.getClientCredentials(), request.getSender(), request.getRecipient(), request.getBody());
    }
    
    @GetMapping(path = "/get-user-roles/id={id}")
    public Response<List<Role>> getUserRoles(@PathVariable(name = "id") UUID clientCredentials) {
        return service.getUserRoles(clientCredentials);
    }
    
    @GetMapping(path = "/get-notifications/id={id}")
    public Response<List<Notification>> getUserNotifications(@PathVariable(name = "id") UUID clientCredentials) {
        return service.getNotifications(clientCredentials, clientCredentials);
    }
    
    @GetMapping(path = "/get-cart-price/id={id}")
    public Response<Double> getCartPrice(@PathVariable(name = "id") UUID clientCredentials) {
        return service.getCartTotal(clientCredentials);
    }
    
    @GetMapping(path = "/search-user/username={username}")
    public Response<List<ServiceUser>> searchUser(@PathVariable(name = "username") String username) {
        return service.searchUser(username);
    }
    
    @GetMapping(path = "/messages/get-messages/id={id}")
    public Response<List<ServiceMessage>> getMessages(@PathVariable(name = "id") UUID clientCredentials) {
        return service.getMessages(clientCredentials, clientCredentials);
    }
    
    @GetMapping(path = "/get-user-by-username/username={username}")
    public Response<ServiceUser> getUserByUsername(@PathVariable(name = "username") String username) {
        return service.getUserByUsername(username);
    }
    
    @GetMapping(path = "/get-user-bids/id={id}")
    public Response<List<Bid>> getUserBids(@PathVariable(name = "id") UUID clientCredentials) {
        return service.getUserBids(clientCredentials);
    }
}
