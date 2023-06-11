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

    public Response<Boolean> validatePaymentDetails(/*args*/) {//TODO: change
       // return Response.getSuccessResponse(paymentProxy.validatePaymentDetails());
        return  Response.getSuccessResponse(true);
    }

    public Response<Integer> requestPayment(/*args*/) {
        //return Response.getSuccessResponse(paymentProxy.requestPayment());
        return  Response.getSuccessResponse(1);
    }

    public void resetController() {
        instance = new PaymentController();
        paymentProxy.setReal();
    }

    public Response<Integer> pay(double price, String card_number, String month, String year, String holder, String ccv, String id) {
        int transactionId=paymentProxy.pay(card_number, month, year, holder, ccv, id);
        if(transactionId!=-1){
        return Response.getSuccessResponse(transactionId);}
        return Response.getFailResponse("transaction payment has failed.");

    }

    public Response<Boolean> cancelPay(int transactionId) {
        int  cancellation =paymentProxy.cancel_Pay(transactionId);
        if(cancellation==1){
            return Response.getSuccessResponse(true);}
        return Response.getFailResponse("cancel Payment has failed.");
    }

    public  Response<Boolean> handshake(){
        String message =paymentProxy.handshake();
        if(message.equals("OK")){
            return Response.getSuccessResponse(true);}
        return Response.getFailResponse("handshake has failed.");

    }
}
