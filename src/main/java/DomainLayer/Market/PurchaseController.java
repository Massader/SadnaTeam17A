package DomainLayer.Market;

import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.Client;
import DomainLayer.Market.Users.Roles.Role;
import DomainLayer.Market.Users.Roles.StorePermissions;
import DomainLayer.Market.Users.ShoppingBasket;
import DomainLayer.Market.Users.ShoppingCart;
import DomainLayer.Payment.PaymentController;
import DomainLayer.Payment.PaymentProxy;
import DomainLayer.Supply.SupplyController;
import DomainLayer.Supply.SupplyProxy;
import ServiceLayer.Response;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PurchaseController {
    private static PurchaseController instance = null;
    private static final Object instanceLock = new Object();
    StoreController storeController;
    PaymentController paymentController;
    UserController userController;

    SupplyController supplyController;
    NotificationController notificationController;

    private PurchaseController() { }

    public static PurchaseController getInstance() {
        synchronized(instanceLock) {
            if (instance == null)
                instance = new PurchaseController();
        }
        return instance;
    }
    public void init() {
        storeController = StoreController.getInstance();
        notificationController = NotificationController.getInstance();
        paymentController =  PaymentController.getInstance();
        supplyController = SupplyController.getInstance();
        userController = UserController.getInstance();
    }



   public Response<Boolean> purchaseCart(Client client, ShoppingCart shoppingCart, double expectedPrice,
                                         String address, String city, String country, int zip,String card_number, String month, String year, String holder, String ccv, String id) {
       try {//check
           if (shoppingCart.getShoppingBaskets().isEmpty()){
               return Response.getFailResponse("shopping cart is empty");}
           if(!paymentController.handshake().getValue())
               return Response.getFailResponse("the payment service is not available");
           if(supplyController.handshake().getValue())
               return Response.getFailResponse("the supply service is not available");
           if(validateOrder( address, city, country, zip))
               return Response.getFailResponse("the address is not available for supply");
           if (!card_number.matches("\\d+")){  // check if the credit card number is all numbers
               return Response.getFailResponse("Credit card number must consist only of numbers");}

           ConcurrentLinkedQueue<Item> missingItems = new ConcurrentLinkedQueue<>();
           synchronized (instanceLock) {
               StringBuilder missingItemList = new StringBuilder();
               //check items are Available
               for (Map.Entry<UUID, ShoppingBasket> entry : shoppingCart.getShoppingBaskets().entrySet()) {
                   UUID storeId = entry.getKey();
                   ShoppingBasket basket = entry.getValue();
                   Store store = storeController.getStore(storeId);
                   if(store==null){
                       return Response.getFailResponse("The store is not exist "+storeId);
                   }
                   if(store.isClosed()){
                       return Response.getFailResponse("The store has been closed and the item is no longer available.");
                   }
                   ConcurrentLinkedQueue<Item> storeMissingItems = store.getUnavailableItems(basket);
                   for (Item item : storeMissingItems) {
                       missingItemList.append("\nItem Name: ").append(item.getName()).append(" - Store Name: ").append(store.getName());
                   }
                   missingItems.addAll(storeMissingItems);
               }
               if (!missingItems.isEmpty()) {
                   return Response.getFailResponse("The following items are no longer available:" + missingItemList);
               }

               double nowPrice = 0;
               try {
                   nowPrice = storeController.verifyCartPrice(shoppingCart);
               } catch (Exception e) {
                   throw new RuntimeException(e);
               }
               if(expectedPrice!=nowPrice){
                   return Response.getFailResponse("Price for shopping cart has changed, it's " + nowPrice);
               }

               //purchase all Basket -> cart
               for (Map.Entry<UUID, ShoppingBasket> entry : shoppingCart.getShoppingBaskets().entrySet()) {
                   UUID storeId = entry.getKey();
                   ShoppingBasket basket = entry.getValue();
                   Store store = storeController.getStore(storeId);
                   try {
                       store.purchaseBasket(client,basket);
                   } catch (Exception e) {
                       throw new Exception(e);
                   }
                   for (Map.Entry<UUID, Role> role : store.getRolesMap().entrySet()) {
                       if (role.getValue().getPermissions().contains(StorePermissions.STORE_OWNER))
                           notificationController.sendNotification(role.getKey(), "A purchase from "
                                   + store.getName() + " has been made.");
                   }
               }
               int transactionId= paymentController.pay(nowPrice, card_number, month, year, holder, ccv, id).getValue();
               if(transactionId==-1){
                   unPurchaseCart(client,shoppingCart);
                   return Response.getFailResponse("There was a problem with your payment");
               }
               //get user name
               String clientName="client";
               if(userController.isUser(client.getId()).getValue()){
                   clientName= userController.getUser(client.getId()).getValue().getUsername();}
               if(supplyController.supply(clientName, address, city, country, zip) == null){
                   unPurchaseCart(client,shoppingCart);
                   if(!paymentController.cancelPay(transactionId).getValue()){
                       return Response.getFailResponse("There was a problem with cancel payment");
                   }
                   return Response.getFailResponse("Supply request failed");
               }

               client.clearCart();
               return Response.getSuccessResponse(true);
           }
       } catch (Exception exception) {
           return Response.getFailResponse(exception.getMessage());
       }
   }

    private boolean validateOrder(String address, String city, String country, int zip) {

        String[] properties = {address ,city,country};

        for (String property : properties) {
            if (property == null || property.isEmpty()) {
                return false;
            }
        }
        if (zip <= 0) {
            return false; // Invalid zip code
        }
        return true;
    }

    public void unPurchaseCart(Client client, ShoppingCart shoppingCart) throws Exception {
         for (Map.Entry<UUID, ShoppingBasket> entry : shoppingCart.getShoppingBaskets().entrySet()) {
            UUID storeId = entry.getKey();
            ShoppingBasket basket = entry.getValue();
            Store store = storeController.getStore(storeId);
            store.unPurchaseBasket(client,basket);
    }}}
