package DomainLayer.Payment;

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

    public void init() {
        paymentProxy = new PaymentProxy();
        paymentProxy.setReal();
    }

    public Response<Boolean> validatePaymentDetails(/*args*/) {
        return Response.getSuccessResponse(true);
    }

    public Response<Integer> requestPayment(/*args*/) {
        //return Response.getSuccessResponse(paymentProxy.requestPayment());
        return Response.getSuccessResponse(1);
    }

    public void resetController() {
        instance = new PaymentController();
        paymentProxy.setReal();
    }

    public Response<Integer> pay(double price, String card_number, String month, String year, String holder, String ccv, String id) {
        try {
            int transactionId = paymentProxy.pay(card_number, month, year, holder, ccv, id);
            if (transactionId != -1) {
                return Response.getSuccessResponse(transactionId);
            }
        } catch (Exception e) {
            return Response.getFailResponse("Transaction payment has failed.");
        }

        return Response.getFailResponse("Transaction payment has failed.");
    }

    public Response<Boolean> cancelPay(int transactionId) {
        try {
            int cancellation = paymentProxy.cancel_Pay(transactionId);
            if (cancellation == 1) {
                return Response.getSuccessResponse(true);
            }
        } catch (Exception e) {
            return Response.getFailResponse("cancel Payment has failed.");
        }

        return Response.getFailResponse("cancel Paymenא has failed.");
    }

    public  Response<Boolean> handshake(){
        try {
        String message =paymentProxy.handshake();
        if(message.equals("OK")){
            return Response.getSuccessResponse(true);}
        } catch (Exception e) {
        return Response.getFailResponse("handshake has failed.");

    }   return Response.getFailResponse("handshake has failed.");}
}
