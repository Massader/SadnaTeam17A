package DomainLayer.Market;

import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.Client;
import DomainLayer.Market.Users.Roles.Role;
import DomainLayer.Market.Users.Roles.StorePermissions;
import DomainLayer.Market.Users.ShoppingBasket;
import DomainLayer.Market.Users.ShoppingCart;
import DomainLayer.Payment.PaymentController;
import DomainLayer.Supply.SupplyController;
import ServiceLayer.Response;

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



   public Response<Boolean> purchaseCart(UUID clientCredentials, ShoppingCart shoppingCart, double expectedPrice,
                                         String address, String city, String country, int zip, String cardNumber, String month, String year, String holder, String cvv, String id) {
       try {//check
           if (!userController.isNonRegisteredClient(clientCredentials) && !userController.isRegisteredUser(clientCredentials))
               return Response.getFailResponse("User does not exist.");
           Client client = userController.getClientOrUser(clientCredentials);
           if (shoppingCart.getShoppingBaskets().isEmpty()){
               return Response.getFailResponse("Shopping Cart is empty");}
           if(paymentController.handshake().isError())
               return Response.getFailResponse("The payment service is not available");
           if(supplyController.handshake().isError())
               return Response.getFailResponse("The supply service is not available");
           validateOrder(address, city, country, zip);
           validatePayment(cardNumber, month, year, holder, cvv, id);
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
                   return Response.getFailResponse("verify Cart Price fail now price is "+nowPrice+ ", your expected Price is "+expectedPrice);
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
                       return Response.getFailResponse(e.getMessage());
                   }
                   for (Map.Entry<UUID, Role> role : store.getRolesMap().entrySet()) {
                       if (role.getValue().getPermissions().contains(StorePermissions.STORE_OWNER))
                           notificationController.sendNotification(role.getKey(), "A purchase from "
                                   + store.getName() + " has been made.");
                   }
               }
               int transactionId= paymentController.pay(nowPrice, cardNumber, month, year, holder, cvv, id).getValue();
               if(transactionId==-1){
                   unPurchaseCart(client,shoppingCart);
                   return Response.getFailResponse("There was a problem with your payment");
               }
               //get user name
               String clientName="client";
               if(userController.isUser(client.getId()).getValue()){
                   clientName = userController.getUser(client.getId()).getValue().getUsername();}
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

    private boolean validatePayment(String cardNumber, String month, String year, String holder, String ccv, String id) throws Exception {
        String[] intProperties = {cardNumber, month, year, ccv, id};
        String[] propertiesName = {"Card number", "Month", "Year", "CVV", "ID Card"};

        for (int i = 0; i < intProperties.length; i++) {
            String property = intProperties[i];
            if (property == null || property.isEmpty()) {
                throw new Exception(propertiesName[i] + " can not be empty");
            }
            if(!property.matches("\\d+")){
                throw new Exception(property + " has to be numbers only");
            }
        }
        if(holder == null || holder.isEmpty()){
            return false;
        }
        if(ccv.length()!=3){
            throw new Exception("CVV needs to be 3 numbers");
        }
        return true;
    }

    private boolean validateOrder(String address, String city, String country, int zip) throws Exception {

        String[] properties = {address ,city,country};
        String[] propertiesName = {"Address" ,"City","Country"};

        for (int i = 0; i < properties.length; i++) {
            String property =properties[i];
            if (property == null || property.isEmpty()) {
                throw new Exception(propertiesName[i] + " can not be empty");
            }

        }
        if (zip <= 0) {
            throw new Exception("ZIP can not be empty");
        }

        return true;
    }

    public void unPurchaseCart(Client client, ShoppingCart shoppingCart) throws Exception {
        for (Map.Entry<UUID, ShoppingBasket> entry : shoppingCart.getShoppingBaskets().entrySet()) {
            UUID storeId = entry.getKey();
            ShoppingBasket basket = entry.getValue();
            Store store = storeController.getStore(storeId);
            store.unPurchaseBasket(client,basket);
        }
    }
}
