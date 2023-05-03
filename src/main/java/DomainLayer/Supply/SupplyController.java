package DomainLayer.Supply;

import DomainLayer.Market.StoreController;
import DomainLayer.Market.Users.ShoppingCart;
import ServiceLayer.Response;

public class SupplyController {
    private SupplyProxy supplyProxy;
    private static SupplyController instance = null;
    private static final Object instanceLock = new Object();

    public static SupplyController getInstance() {
        synchronized(instanceLock) {
            if (instance == null)
                instance = new SupplyController();
        }
        return instance;
    }
    public Response<Boolean> validateOrder(/*args*/){
        return Response.getSuccessResponse(supplyProxy.validateOrder(" address"));
    }

    public Response<Integer> sendOrder(/*args*/){
        return Response.getSuccessResponse(supplyProxy.sendOrder());
    }

    public void resetController() {
        instance = new SupplyController();
        supplyProxy.setReal();
    }
}
