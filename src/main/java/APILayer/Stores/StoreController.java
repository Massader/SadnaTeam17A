package APILayer.Stores;

import APILayer.Requests.*;
import DomainLayer.Market.Stores.Sale;
import ServiceLayer.Response;
import ServiceLayer.Service;
import ServiceLayer.ServiceObjects.ServiceItem;
import ServiceLayer.ServiceObjects.ServiceSale;
import ServiceLayer.ServiceObjects.ServiceStore;
import ServiceLayer.ServiceObjects.ServiceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/stores")
public class StoreController {
    private final Service service;

    @Autowired
    public StoreController(Service service) {
        this.service = service;
        service.init();
    }

    @PostMapping(path = "/create-store")
    public Response<ServiceStore> createStore(@RequestBody CreateStoreRequest request) {
        return service.createStore(request.getClientCredentials(), request.getName(), request.getDescription());
    }

    @PutMapping(path = "/close-store")
    public Response<Boolean> closeStore(@RequestBody TargetRequest request) {
        return service.closeStore(request.getClientCredentials(), request.getTargetId());
    }

    @PutMapping(path = "/reopen-store")
    public Response<Boolean> reopenStore(@RequestBody TargetRequest request) {
        return service.reopenStore(request.getClientCredentials(), request.getTargetId());
    }

    @PutMapping(path = "/admin/shutdown-store")
    public Response<Boolean> shutdownStore(@RequestBody TargetRequest request) {
        return service.shutdownStore(request.getClientCredentials(), request.getTargetId());
    }

    @GetMapping(path = "/store-info")
    public Response<ServiceStore> getStoreInfo(@RequestBody TargetRequest request) {
        return service.getStoreInformation(request.getTargetId());
    }

    @GetMapping(path = "/item-info")
    public Response<ServiceItem> getItemInfo(@RequestBody TargetItemRequest request) {
        return service.getItemInformation(request.getStoreId(), request.getItemId());
    }

    @PostMapping(path = "/post-review")
    public Response<UUID> postReview(@RequestBody ReviewRequest request) {
        return service.postReview(request.getClientCredentials(), request.getItemId(), request.getBody());
    }

    @PutMapping(path = "/role/set-manager-permissions")
    public Response<Boolean> setManagerPermissions(@RequestBody SetManagerPermissionsRequest request) {
        return service.setManagerPermissions(request.getClientCredentials(), request.getManagerId(),
                request.getStoreId(), request.getPermissions());
    }

    @GetMapping(path = "/store-staff")
    public Response<List<ServiceUser>> getStoreStaff(@RequestBody TargetRequest request) {
        return service.getStoreStaff(request.getClientCredentials(), request.getTargetId());
    }

    @PostMapping(path = "/role/appoint-manager")
    public Response<Boolean> appointStoreManager(@RequestBody RoleRequest request) {
        return service.appointStoreManager(request.getClientCredentials(), request.getAppointee(), request.getStoreId());
    }

    @PostMapping(path = "/role/appoint-owner")
    public Response<Boolean> appointStoreOwner(@RequestBody RoleRequest request) {
        return service.appointStoreOwner(request.getClientCredentials(), request.getAppointee(), request.getStoreId());
    }

    @DeleteMapping(path = "/role/remove")
    public Response<Boolean> removeStoreRole(@RequestBody RoleRequest request) {
        return service.removeStoreRole(request.getClientCredentials(), request.getAppointee(), request.getStoreId());
    }

    @PutMapping(path = "/item/quantity")
    public Response<Boolean> setItemQuantity(@RequestBody ItemRequest request) {
        return service.setItemQuantity(request.getClientCredentials(), request.getItem().getStoreId(),
                request.getItem().getId(), request.getItem().getQuantity());
    }

    @PutMapping(path = "/item/name")
    public Response<Boolean> setItemName(@RequestBody ItemRequest request) {
        return service.setItemName(request.getClientCredentials(), request.getItem().getStoreId(),
                request.getItem().getId(), request.getItem().getName());
    }

    @PutMapping(path = "/item/description")
    public Response<Boolean> setItemDescription(@RequestBody ItemRequest request) {
        return service.setItemDescription(request.getClientCredentials(), request.getItem().getStoreId(),
                request.getItem().getId(), request.getItem().getDescription());
    }

    @PutMapping(path = "/item/price")
    public Response<Boolean> setItemPrice(@RequestBody ItemRequest request) {
        return service.setItemPrice(request.getClientCredentials(), request.getItem().getStoreId(),
                request.getItem().getId(), request.getItem().getPrice());
    }

    @GetMapping(path = "/sale-history")
    public Response<List<ServiceSale>> getStoreSaleHistory(@RequestBody TargetRequest request) {
        return service.getStoreSaleHistory(request.getClientCredentials(), request.getTargetId());
    }
}