package DomainLayer.Supply;

import DomainLayer.Market.StoreController;
import ServiceLayer.Response;

public class SupplyController {
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
        return Response.getSuccessResponse(true);
    }

    public Response<Integer> sendOrder(/*args*/){
        return Response.getSuccessResponse(1);
    }
}
