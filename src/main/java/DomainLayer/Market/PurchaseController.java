package DomainLayer.Market;

import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.Client;
import DomainLayer.Market.Users.Roles.Role;
import DomainLayer.Market.Users.Roles.StorePermissions;
import DomainLayer.Market.Users.ShoppingBasket;
import DomainLayer.Market.Users.ShoppingCart;
import DomainLayer.Payment.PaymentProxy;
import DomainLayer.Supply.SupplyProxy;
import ServiceLayer.Response;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PurchaseController {
    private static PurchaseController instance = null;
    private static final Object instanceLock = new Object();
    StoreController storeController;
    PaymentProxy paymentProxy;
    SupplyProxy supplyProxy;
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
//        paymentProxy.setReal();
//        supplyProxy.setReal();
    }


    public Response<Boolean> purchaseCart(Client client, ShoppingCart shoppingCart, double expectedPrice,
                                          String address, String credit) {
        try {//check
            if (shoppingCart.getShoppingBaskets().isEmpty()){
                return Response.getFailResponse("shopping cart is empty");}
            if(paymentProxy==null)
                return Response.getFailResponse("the payment service is not available");
            if(supplyProxy==null)
                return Response.getFailResponse("the supply service is not available");
            if(!supplyProxy.validateOrder(address))
                return Response.getFailResponse("the address is not available for supply");
            if (!credit.matches("[0-9]+"))  // check if the credit card number is all numbers
                return Response.getFailResponse("Credit card number must consist only of numbers");

            ConcurrentLinkedQueue<Item> missingItems = new ConcurrentLinkedQueue<>();
            synchronized (instanceLock) {
                //check items are Available
                for (Map.Entry<UUID, ShoppingBasket> entry : shoppingCart.getShoppingBaskets().entrySet()) {
                    UUID storeId = entry.getKey();
                    ShoppingBasket basket = entry.getValue();
                    Store store = storeController.getStore(storeId);
                    if(store==null){return Response.getFailResponse("The store is not exist "+storeId);}
                    if(store.isClosed()){return Response.getFailResponse("The store is close "+storeId);}
                    missingItems.addAll(store.getUnavailableItems(basket));
                    if (!missingItems.isEmpty()) {
                        return Response.getFailResponse("The following items are no longer available "
                                + Arrays.toString(missingItems.toArray()));
                    }
                }

                double nowPrice = storeController.verifyCartPrice(shoppingCart);
                if(expectedPrice!=nowPrice){
                    return Response.getFailResponse("Price for shopping cart has changed, it's " + nowPrice);
                }

                //purchase all Basket -> cart
                for (Map.Entry<UUID, ShoppingBasket> entry : shoppingCart.getShoppingBaskets().entrySet()) {
                    UUID storeId = entry.getKey();
                    ShoppingBasket basket = entry.getValue();
                    Store store = storeController.getStore(storeId);
                    store.purchaseBasket(client,basket);
                    for (Map.Entry<UUID, Role> role : store.getRolesMap().entrySet()) {
                        if (role.getValue().getPermissions().contains(StorePermissions.STORE_OWNER))
                        notificationController.sendNotification(role.getKey(), "A purchase from "
                                + store.getName() + " has been made.");
                    }
                }
                if(!paymentProxy.pay(nowPrice, credit)){
                    unPurchaseCart(client,shoppingCart);
                    return Response.getFailResponse("There was a problem with your payment");
                }
                if(supplyProxy.sendOrder() == null){
                    unPurchaseCart(client,shoppingCart);
                    paymentProxy.cancelPay(nowPrice, credit);
                    return Response.getFailResponse("Supply request failed");
                }

                client.clearCart();
                return Response.getSuccessResponse(true);
            }
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public void unPurchaseCart(Client client, ShoppingCart shoppingCart) throws Exception {
         for (Map.Entry<UUID, ShoppingBasket> entry : shoppingCart.getShoppingBaskets().entrySet()) {
            UUID storeId = entry.getKey();
            ShoppingBasket basket = entry.getValue();
            Store store = storeController.getStore(storeId);
            store.unPurchaseBasket(client,basket);
    }}}
