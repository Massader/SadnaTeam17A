package DomainLayer.Payment;

import DomainLayer.Market.StoreController;
import ServiceLayer.Response;

public class PaymentController {
    private PaymentProxy paymentProxy;

    private static PaymentController instance = null;
    private static final Object instanceLock = new Object();

    public static PaymentController getInstance() {
        synchronized (instanceLock) {
            if (instance == null)
                instance = new PaymentController();

        }
        return instance;
    }

    public Response<Boolean> validatePaymentDetails(/*args*/) {
        return Response.getSuccessResponse(paymentProxy.validatePaymentDetails());
    }

    public Response<Integer> requestPayment(/*args*/) {
        return Response.getSuccessResponse(paymentProxy.requestPayment());
    }

    public void resetController() {
        instance = new PaymentController();
        paymentProxy.setReal();
    }

    public Response<Boolean> pay(double price, int credit) {
        return Response.getSuccessResponse(paymentProxy.pay(price, credit));
    }

    public Response<Boolean> cancelPay(double nowPrice, int credit) {
        return Response.getSuccessResponse(paymentProxy.cancelPay(nowPrice, credit));
    }
}
