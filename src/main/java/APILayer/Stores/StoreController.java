package APILayer.Stores;

import APILayer.Requests.*;
import DomainLayer.Market.Stores.PurchaseTypes.Bid;
import DomainLayer.Market.Users.Roles.OwnerPetition;
import DataAccessLayer.RepositoryFactory;
import ServiceLayer.Response;
import ServiceLayer.Service;
import ServiceLayer.ServiceObjects.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/stores")
public class StoreController {
    private final Service service;

    @Autowired
    public StoreController(Service service, RepositoryFactory repositoryFactory) {
        this.service = service;
        service.init(repositoryFactory);
    }

    //DB
    @PostMapping(path = "/create-store")
    public Response<ServiceStore> createStore(@RequestBody CreateStoreRequest request) {
        return service.createStore(request.getClientCredentials(), request.getName(), request.getDescription());
    }
    //DB
    @PutMapping(path = "/close-store")
    public Response<Boolean> closeStore(@RequestBody TargetRequest request) {
        return service.closeStore(request.getClientCredentials(), request.getTargetId());
    }

    //DB
    @PutMapping(path = "/reopen-store")
    public Response<Boolean> reopenStore(@RequestBody TargetRequest request) {
        return service.reopenStore(request.getClientCredentials(), request.getTargetId());
    }
    //DB
    @PutMapping(path = "/admin/shutdown-store")
    public Response<Boolean> shutdownStore(@RequestBody TargetRequest request) {
        return service.shutdownStore(request.getClientCredentials(), request.getTargetId());
    }
//DB
    @GetMapping(path = "/store-info/id={id}&storeId={storeId}")
    public Response<ServiceStore> getStoreInfo(@PathVariable(name = "id") UUID clientCredentials,
                                               @PathVariable(name = "storeId") UUID storeId) {
        return service.getStoreInformation(clientCredentials, storeId);
    }

//DB
    @GetMapping(path = "/item-info/storeId={storeId}&itemId={itemId}")
    public Response<ServiceItem> getItemInfo(@PathVariable(name = "storeId") UUID storeId,
                                             @PathVariable(name = "itemId") UUID itemId) {
        return service.getItemInformation(storeId, itemId);
    }

    //DB
    @PostMapping(path = "/post-item-review")
    public Response<UUID> postItemReview(@RequestBody ReviewRequest request) {
        return service.postItemReview(request.getClientCredentials(), request.getTargetId(), request.getBody(), request.getRating());
    }
    //DB
    @GetMapping(path = "/get-item-reviews/storeId={storeId}&itemId={itemId}")
    public Response<List<ServiceItemReview>> getReviews(@PathVariable(name = "storeId") UUID storeId,
                                                        @PathVariable(name = "itemId") UUID itemId) {
        return service.getItemReviews(storeId, itemId);
    }


    @GetMapping(path = "/item-is-reviewable-by-user/id={id}&storeId={storeId}&itemId={itemId}")
    public Response<Boolean> isReviewableByUser(@PathVariable(name = "id") UUID clientCredentials,
                                                @PathVariable(name = "storeId") UUID storeId,
                                                @PathVariable(name = "itemId") UUID itemId) {
        return service.isReviewableByUser(clientCredentials, storeId, itemId);
    }

    //DB
    @PutMapping(path = "/role/set-manager-permissions")
    public Response<Boolean> setManagerPermissions(@RequestBody SetManagerPermissionsRequest request) {
        return service.setManagerPermissions(request.getClientCredentials(), request.getManagerId(),
                request.getStoreId(), request.getPermissions());
    }


    @GetMapping(path = "/store-staff/id={id}&storeId={storeId}")
    public Response<List<ServiceUser>> getStoreStaff(@PathVariable(name = "id") UUID clientCredentials,
                                                     @PathVariable(name = "storeId") UUID storeId) {
        return service.getStoreStaff(clientCredentials, storeId);
    }

    //DB
    @PostMapping(path = "/role/appoint-manager")
    public Response<Boolean> appointStoreManager(@RequestBody RoleRequest request) {
        return service.appointStoreManager(request.getClientCredentials(), request.getAppointee(), request.getStoreId());
    }

    @PostMapping(path = "/role/appoint-owner")
    public Response<Boolean> appointStoreOwner(@RequestBody RoleRequest request) {
        return service.appointStoreOwner(request.getClientCredentials(), request.getAppointee(), request.getStoreId());
    }
    
    @GetMapping(path = "/get-store-owner-appointments/id={id}&storeId={storeId}")
    public Response<Collection<OwnerPetition>> getStoreOwnerPetitions(@PathVariable(name = "id") UUID clientCredentials,
                                                                      @PathVariable(name = "storeId") UUID storeId) {
        return service.getStoreOwnerPetitions(clientCredentials, storeId);
    }
    
    @DeleteMapping(path = "/remove-store-owner-approval")
    public Response<Boolean> removeStoreOwnerApproval(@RequestBody RoleRequest request) {
        return service.removeOwnerPetitionApproval(request.getClientCredentials(), request.getStoreId(), request.getAppointee());
    }

    @DeleteMapping(path = "/role/remove")
    public Response<Boolean> removeStoreRole(@RequestBody RoleRequest request) {
        return service.removeStoreRole(request.getClientCredentials(), request.getAppointee(), request.getStoreId());
    }

    @PutMapping(path = "/item/quantity")
    public Response<Boolean> setItemQuantity(@RequestBody ItemRequest request) {
        return service.setItemQuantity(request.getClientCredentials(), request.getStoreId(),
                request.getId(), request.getQuantity());
    }

    @PutMapping(path = "/item/name")
    public Response<Boolean> setItemName(@RequestBody ItemRequest request) {
        return service.setItemName(request.getClientCredentials(), request.getStoreId(),
                request.getId(), request.getName());
    }

    @PutMapping(path = "/item/description")
    public Response<Boolean> setItemDescription(@RequestBody ItemRequest request) {
        return service.setItemDescription(request.getClientCredentials(), request.getStoreId(),
                request.getId(), request.getDescription());
    }

    @PutMapping(path = "/item/price")
    public Response<Boolean> setItemPrice(@RequestBody ItemRequest request) {
        return service.setItemPrice(request.getClientCredentials(), request.getStoreId(),
                request.getId(), request.getPrice());
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
                        "&maxPrice={maxPrice}&itemRating={itemRating}&storeRating={storeRating}" +
                        "&storeId={storeId}&number={number}&page={page}")
    public Response<List<ServiceItem>> searchItem(@PathVariable(name = "keyword", required = false) String keyword,
                                                  @PathVariable(name = "category", required = false) String category,
                                                  @PathVariable(name = "minPrice", required = false) Double minPrice,
                                                  @PathVariable(name = "maxPrice", required = false) Double maxPrice,
                                                  @PathVariable(name = "itemRating", required = false) Integer itemRating,
                                                  @PathVariable(name = "number", required = false) Integer number,
                                                  @PathVariable(name = "page", required = false) Integer page,
                                                  @PathVariable(name = "storeId", required = false) UUID storeId,
                                                  @PathVariable(name = "storeRating", required = false) Integer storeRating) {
        return service.searchItem(keyword, category, minPrice, maxPrice, itemRating, storeRating, number, page, storeId);
    }

    @GetMapping(path = "/search-item-num/keyword={keyword}&category={category}&minPrice={minPrice}" +
            "&maxPrice={maxPrice}&itemRating={itemRating}&storeRating={storeRating}" +
            "&storeId={storeId}&number={number}&page={page}")
    public Response<Integer> searchItemNum(@PathVariable(name = "keyword", required = false) String keyword,
                                                  @PathVariable(name = "category", required = false) String category,
                                                  @PathVariable(name = "minPrice", required = false) Double minPrice,
                                                  @PathVariable(name = "maxPrice", required = false) Double maxPrice,
                                                  @PathVariable(name = "itemRating", required = false) Integer itemRating,
                                                  @PathVariable(name = "number", required = false) Integer number,
                                                  @PathVariable(name = "page", required = false) Integer page,
                                                  @PathVariable(name = "storeId", required = false) UUID storeId,
                                                  @PathVariable(name = "storeRating", required = false) Integer storeRating) {
        return service.searchItemNum(keyword, category, minPrice, maxPrice, itemRating, storeRating, number, page, storeId);
    }

    @PostMapping(path = "/add-item-to-store")
    public Response<ServiceItem> addItemToStore(@RequestBody ItemRequest request){
        return service.addItemToStore(request.getClientCredentials(), request.getName(), request.getPrice(),
                request.getStoreId(), request.getQuantity(), request.getDescription());
    }
    
    @PostMapping(path = "/send-complaint")
    public Response<UUID> sendComplaint(@RequestBody ComplaintRequest request) {
        return service.sendComplaint(request.getClientCredentials(), request.getPurchaseId(),
                request.getStoreId(), request.getItemId(), request.getBody());
    }

    @GetMapping(path = "/admin/get-complaints/id={id}")
    public Response<List<ServiceComplaint>> getComplaints(@PathVariable(name = "id") UUID id){
        return service.getComplaints(id);
    }
    
    @GetMapping(path = "/admin/get-assigned-complaints/id={id}")
    public Response<List<ServiceComplaint>> getAssignedComplaints(@PathVariable(name = "id") UUID id){
        return service.getAssignedComplaints(id);
    }

    @GetMapping(path = "/admin/get-complaint/id={id}&complaintId={complaintId}")
    public Response<ServiceComplaint> getComplaint(@PathVariable(name = "id") UUID id,
                                                   @PathVariable(name = "complaintId") UUID complaintId){
        return service.getComplaint(id,complaintId);
    }

    @PutMapping(path = "/admin/assign-admin-to-complaint")
    public Response<Boolean> assignAdminToComplaint(@RequestBody TargetRequest request){
        return service.assignAdminToComplaint(request.getClientCredentials(), request.getTargetId());
    }
    
    @PutMapping(path = "/admin/close-complaint")
    public Response<Boolean> closeComplaint(@RequestBody TargetRequest request) {
        return service.closeComplaint(request.getClientCredentials(), request.getTargetId());
    }
    
    @PutMapping(path = "/admin/reopen-complaint")
    public Response<Boolean> reopenComplaint(@RequestBody TargetRequest request) {
        return service.reopenComplaint(request.getClientCredentials(), request.getTargetId());
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
    public Response<Long> numOfStores(){
        return service.numOfStores();
    }

    @GetMapping(path = "/num-of-items/storeId={storeId}")
    public Response<Long> numOfItems(@PathVariable(name = "storeId", required = false) UUID storeId){
        return service.numOfItems(storeId);
    }

    @PostMapping(path = "/rate-item")
    public Response<Boolean> rateItem(@RequestBody ItemRatingRequest request) {
        return service.addItemRating(request.getClientCredentials(), request.getItemId(), request.getStoreId(),
                request.getRating());
    }
    
    @GetMapping(path = "/get-store-managers/id={id}&storeId={storeId}")
    public Response<List<ServiceUser>> getStoreManagers(@PathVariable(name = "id") UUID clientCredentials,
                                                        @PathVariable(name = "storeId") UUID storeId) {
        return service.getStoreManagers(clientCredentials, storeId);
    }

    @PostMapping(path = "/messages/send-message")
    public Response<UUID> sendMessage(@RequestBody MessageRequest request){
        return service.sendMessage(request.getClientCredentials(), request.getSender(), request.getRecipient(), request.getBody());
    }

    @GetMapping(path = "/messages/get-messages/id={id}&storeId={storeId}")
    public Response<List<ServiceMessage>> getMessages(@PathVariable(name = "id") UUID clientCredentials,
                                                      @PathVariable(name = "storeId") UUID storeId) {
        return service.getMessages(clientCredentials, storeId);
    }
    
    @PostMapping(path = "/add-item-policy-term")
    public Response<Boolean> addItemPolicyTerm(@RequestBody ItemPolicyTermRequest request) {
        return service.addItemPolicyTerm(request.getClientCredentials(), request.getStoreId(),
                request.getItemId(), request.getQuantity(), request.getAtLeast());
    }
    
    @PostMapping(path = "/add-category-policy-term")
    public Response<Boolean> addCategoryPolicyTerm(@RequestBody CategoryPolicyTermRequest request) {
        return service.addCategoryPolicyTerm(request.getClientCredentials(), request.getStoreId(),
                request.getCategory(), request.getQuantity(), request.getAtLeast());
    }
    
    @PostMapping(path = "/add-basket-policy-term")
    public Response<Boolean> addBasketPolicyTerm(@RequestBody BasketPolicyTermRequest request) {
        return service.addBasketPolicyTerm(request.getClientCredentials(), request.getStoreId(),
                request.getQuantity(), request.getAtLeast());
    }
    
    @PostMapping(path = "/add-conditional-policy-term")
    public Response<Boolean> addConditionalPolicyTerm(@RequestBody ConditionalPolicyTermRequest request) {
        return service.addConditionalPurchaseTerm(request.getClientCredentials(), request.getStoreId(), request.getTerm());
    }
    
    @PostMapping(path = "/add-composite-policy-term")
    public Response<Boolean> addCompositePolicyTerm(@RequestBody CompositePolicyTermRequest request) {
        return service.addCompositePolicyTerm(request.getClientCredentials(), request.getStoreId(), request.getTerm());
    }
    
    @GetMapping(path = "/get-store-purchase-terms/id={id}&storeId={storeId}")
    public Response<List<Object>> getStorePurchaseTerms(@PathVariable(name = "id") UUID clientCredentials,
                                                                      @PathVariable(name = "storeId") UUID storeId) {
        return service.getStorePurchaseTerms(clientCredentials, storeId);
    }
    
    @DeleteMapping(path = "/remove-policy-term")
    public Response<Boolean> removePolicyTerm(@RequestBody RemovePolicyTermRequest request) {
        if (request.getItemId() != null)
            return service.removePolicyTerm(request.getClientCredentials(), request.getStoreId(), request.getItemId());
        else if (request.getCategoryName() != null)
            return service.removePolicyTerm(request.getClientCredentials(), request.getStoreId(), request.getCategoryName());
        else
            return service.removePolicyTerm(request.getClientCredentials(), request.getStoreId());
    }
    
    @PostMapping(path = "/post-store-review")
    public Response<UUID> postStoreReview(@RequestBody ReviewRequest request) {
        return service.addStoreReview(request.getClientCredentials(), request.getTargetId(), request.getBody(),
                request.getRating());
    }
    
    @GetMapping(path = "/store-is-reviewable-by-user/id={id}&storeId={storeId}")
    public Response<Boolean> isReviewableByUser(@PathVariable(name = "id") UUID clientCredentials,
                                                @PathVariable(name = "storeId") UUID storeId) {
        return service.isReviewableByUser(clientCredentials, storeId);
    }
    
    @GetMapping(path = "/get-store-reviews/storeId={storeId}")
    public Response<List<ServiceStoreReview>> getStoreReviews(@PathVariable(name = "storeId") UUID storeId) {
        return service.getStoreReviews(storeId);
    }
    
    @PostMapping(path = "/add-bid-to-item")
    public Response<Boolean> addBidToItem(@RequestBody AddBidRequest request) {
        return service.addBidToItem(request.getClientCredentials(), request.getStoreId(),
                request.getItemId(), request.getBidPrice(), request.getQuantity());
    }
    
    @PostMapping(path = "/accept-item-bid")
    public Response<Boolean> acceptItemBid(@RequestBody AcceptBidRequest request) {
        return service.acceptItemBid(request.getClientCredentials(), request.getStoreId(), request.getItemId(),
                request.getBidderId(), request.getBidPrice());
    }
    
    @GetMapping(path = "/get-item-bids/id={id}&storeId={storeId}&itemId={itemId}")
    public Response<List<Bid>> getItemBids(@PathVariable(name = "id") UUID clientCredentials,
                                           @PathVariable(name = "storeId") UUID storeId,
                                           @PathVariable(name = "itemId") UUID itemId) {
        return service.getItemBids(clientCredentials, storeId, itemId);
    }
    
    @PostMapping(path = "/add-discount")
    public Response<Boolean> addDiscount(@RequestBody DiscountRequest request) {
        return service.addDiscount(request.getClientCredentials(), request.getStoreId(), request.getDiscount());
    }
    
    @GetMapping(path = "/get-discounts/id={id}&storeId={storeId}")
    public Response<List<ServiceDiscount>> getStoreDiscounts(@PathVariable(name = "id") UUID clientCredentials,
                                                             @PathVariable(name = "storeId") UUID storeId) {
        return service.getStoreDiscounts(clientCredentials, storeId);
    }
    
    @DeleteMapping(path = "/remove-discount")
    public Response<Boolean> removeDiscount(@RequestBody DiscountRequest request) {
        return service.removeDiscount(request.getClientCredentials(), request.getStoreId(), request.getDiscount());
    }
}