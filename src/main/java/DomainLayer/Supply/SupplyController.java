package DomainLayer.Supply;

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
    
    public void init() {
        supplyProxy = new SupplyProxy();
        supplyProxy.setReal();
    }
    
    public Response<Boolean> validateOrder(/*args*/){
        //return Response.getSuccessResponse(supplyProxy.validateOrder(" address"));//TODO: change
        return  Response.getSuccessResponse(true);
    }

    public Response<Integer> sendOrder(/*args*/){
        //return Response.getSuccessResponse(supplyProxy.supply());//TODO: change
        return Response.getSuccessResponse(1);
    }

    public Response<Integer> supply(String name , String address, String city, String country, int zip){
        int transactionId=supplyProxy.supply(name, address, city, country, zip);
        if(transactionId!=-1){
            return Response.getSuccessResponse(transactionId);}
        return Response.getFailResponse("transaction supply has failed.");

    }

    public  Response<Boolean> handshake(){
        String message =supplyProxy.handshake();
        if(message.equals("OK")){
            return Response.getSuccessResponse(true);}
        return Response.getFailResponse("handshake has failed.");

    }
    public void resetController() {
        instance = new SupplyController();
        supplyProxy.setReal();
    }
}
