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

    @GetMapping(path = "/store-info/storeId={storeId}")
    public Response<ServiceStore> getStoreInfo(@PathVariable(name = "storeId") UUID storeId) {
        return service.getStoreInformation(storeId);
    }

    @GetMapping(path = "/item-info/storeId={storeId}&itemId={itemId}")
    public Response<ServiceItem> getItemInfo(@PathVariable(name = "storeId") UUID storeId,
                                                @PathVariable(name = "itemId") UUID itemId) {
        return service.getItemInformation(storeId, itemId);
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

    @GetMapping(path = "/store-staff/id={id}&storeId={storeId}")
    public Response<List<ServiceUser>> getStoreStaff(@PathVariable(name = "id") UUID id,
                                                     @PathVariable(name = "storeId") UUID storeId) {
        return service.getStoreStaff(id, storeId);
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

    @GetMapping(path = "/sale-history/id={id}&storeId={storeId}")
    public Response<List<ServiceSale>> getStoreSaleHistory(@PathVariable(name = "id") UUID id,
                                                           @PathVariable(name = "storeId") UUID storeId) {
        return service.getStoreSaleHistory(id, storeId);
    }

    @GetMapping(path = "/get-stores-page/number={number}&page={page}")
    public Response<List<ServiceStore>> getStoresPage(@PathVariable(name = "number") int number, @PathVariable(name = "page") int page) {
        return service.getStoresPage(number, page);
    }


    @GetMapping(path = "/search-item/keyword={keyword}&category={category}&minPrice={minPrice}" +
                        "&maxPrice={maxPrice}&itemRating={itemRating}&storeRating={storeRating}")
    public Response<List<ServiceItem>> searchItem(@PathVariable(name = "keyword") String keyword,
                                                  @PathVariable(name = "category") String category,
                                                  @PathVariable(name = "minPrice") double minPrice,
                                                  @PathVariable(name = "maxPrice") double maxPrice,
                                                  @PathVariable(name = "itemRating") int itemRating,
                                                  @PathVariable(name = "storeRating") int storeRating) {
        return service.searchItem(keyword, category, minPrice, maxPrice, itemRating, storeRating);
    }

    @PutMapping(path = "/add-item-to-store")
    public Response<ServiceItem> addItemToStore(@RequestBody ItemRequest request){
        ServiceItem item = request.getItem();
        return service.addItemToStore(request.getClientCredentials(), item.getName(), item.getPrice(),
                item.getStoreId(), item.getQuantity(), item.getDescription());
    }


    @GetMapping(path = "/get-complaints/id={id}")
    public Response<List<ServiceComplaint>> getComplaints(@PathVariable(name = "id") UUID id){
        return service.getComplaints(id);
    }

    @GetMapping(path = "/get-complaint/id={id}&complaintId={complaintId}")
    public Response<ServiceComplaint> getComplaint(@PathVariable(name = "id") UUID id,
                                                   @PathVariable(name = "complaintId") UUID complaintId){
        return service.getComplaint(id,complaintId);
    }

    @PutMapping(path = "/assign-admin-to-complaint")
    public Response<Boolean> assignAdminToComplaint(@RequestBody TargetRequest request){
        return service.assignAdminToComplaint(request.getClientCredentials(), request.getTargetId());
    }

    @PostMapping(path = "/add-item-category")
    public Response<Boolean> addItemCategory(@RequestBody CategoryRequest request){
        return service.addItemCategory(request.getClientCredentials(),
                request.getStoreId(), request.getItemId(), request.getCategory());
    }
    @DeleteMapping(path = "/remove-item-from-store")
    public Response<Boolean> removeItemFromStore(@RequestBody TargetItemRequest request){
        return service.removeItemFromStore(request.getClientCredentials(),
                request.getStoreId(), request.getItemId());
    }

    @PutMapping(path = "/purchase-cart")
    public Response<Boolean> purchaseCart(@RequestBody PurchaseCartRequest request){
        return service.purchaseCart(request.getClientCredentials(),
                request.getExpectedPrice(), request.getAddress(), request.getCredit());
    }

    @GetMapping(path = "/get-items-page/number={number}&page={page}&storeId={storeId}")
    public Response<List<ServiceItem>> getItemsPage(@PathVariable(name = "number") int number,
                                                    @PathVariable(name = "page") int page,
                                                    @PathVariable(name = "storeId", required = false) UUID storeId) {
        return service.getItemsPage(number, page, storeId);
    }

    @GetMapping(path = "/num-of-stores")
    public Response<Integer> numOfStores(){
        return service.numOfStores();
    }

    @GetMapping(path = "/num-of-items/storeId")
    public Response<Integer> numOfItems(@PathVariable(name = "storeId", required = false) UUID storeId){
        return service.numOfItems(storeId);
    }
}