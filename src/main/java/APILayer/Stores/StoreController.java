package APILayer.Stores;

import APILayer.Requests.*;
import DomainLayer.Market.Stores.Store;
import ServiceLayer.Response;
import ServiceLayer.Service;
import ServiceLayer.ServiceObjects.*;
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

    @GetMapping(path = "/get-stores-page/number={number}&page={page}")
    public Response<List<Store>> getStoresPage(@PathVariable(name = "number") int number, @PathVariable(name = "page") int page) {
        return service.getStoresPage(number, page);
    }

    @GetMapping(path = "searchItem")
    public Response<List<ServiceItem>> searchItem(@RequestBody searchItemRequest request) {
        return service.searchItem(request.getKeyword(), request.getCategory(), request.getMinPrice(),
                request.getMaxPrice(), request.getItemRating(), request.getStoreRating());
    }
    @PutMapping(path = "searchItem")
    public Response<ServiceItem> addItemToStore(@RequestBody ItemRequest request){
        ServiceItem item = request.getItem();
        return service.addItemToStore(request.getClientCredentials(), item.getName(), item.getPrice(),
                item.getStoreId(), item.getQuantity(), item.getDescription());
    }

    @GetMapping(path = "/getComplaints")
    public Response<List<ServiceComplaint>> getComplaints(@RequestBody Request request){
        return service.getComplaints(request.getClientCredentials());
    }

    @GetMapping(path = "/getComplaint")
    public Response<ServiceComplaint> getComplaint(@RequestBody TargetRequest request){
        return service.getComplaint(request.getClientCredentials(), request.getTargetId());
    }

    @PutMapping(path = "/assignAdminToComplaint")
    public Response<Boolean> assignAdminToComplaint(@RequestBody TargetRequest request){
        return service.assignAdminToComplaint(request.getClientCredentials(), request.getTargetId());
    }

    @PostMapping(path = "/addItemCategory")
    public Response<Boolean> addItemCategory(@RequestBody CategoryRequest request){
        return service.addItemCategory(request.getClientCredentials(),
                request.getStoreId(), request.getItemId(), request.getCategory());
    }
    @DeleteMapping(path = "/removeItemFromStore")
    public Response<Boolean> removeItemFromStore(@RequestBody TargetItemRequest request){
        return service.removeItemFromStore(request.getClientCredentials(),
                request.getStoreId(), request.getItemId());
    }

    @PutMapping(path = "/purchaseCart")
    public Response<Boolean> purchaseCart(@RequestBody PurchaseCartRequest request){
        return service.purchaseCart(request.getClientCredentials(),
                request.getExpectedPrice(), request.getAddress(), request.getCredit());
    }

}