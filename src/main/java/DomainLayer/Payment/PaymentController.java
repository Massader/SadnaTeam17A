package DomainLayer.Payment;

import DomainLayer.Market.StoreController;
import ServiceLayer.Response;

public class PaymentController {

    private static PaymentController instance = null;
    private static final Object instanceLock = new Object();

    public static PaymentController getInstance() {
        synchronized(instanceLock) {
            if (instance == null)
                instance = new PaymentController();
        }
        return instance;
    }

    public Response<Boolean> validatePaymentDetails(/*args*/){
        return Response.getSuccessResponse(true);
    }
    public Response<Integer> requestPayment(/*args*/){
        return Response.getSuccessResponse(1);
    }

    public void resetController() {
        instance = new PaymentController();
    }
}
