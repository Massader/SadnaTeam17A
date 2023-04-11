package DomainLayer.Supply;

import ServiceLayer.Response;

public class SupplyController {


    public Response<Boolean> validateOrder(/*args*/){
        return Response.getSuccessResponse(true);
    }

    public Response<Integer> sendOrder(/*args*/){
        return Response.getSuccessResponse(1);
    }
}
