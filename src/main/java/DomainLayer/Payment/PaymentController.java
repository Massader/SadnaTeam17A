package DomainLayer.Payment;

import ServiceLayer.Response;

public class PaymentController {

    public Response<Boolean> validatePaymentDetails(/*args*/){
        return Response.getSuccessResponse(true);
    }
    public Response<Integer> requestPayment(/*args*/){
        return Response.getSuccessResponse(1);
    }
}
