package DomainLayer.Market.Users.Roles;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class StoreOwner extends Role{
    private UUID storeId;
    private  ConcurrentLinkedQueue<StorePermissions> myRoles;
    private ConcurrentLinkedQueue<UUID> appointmentOFStoreOwner;


    public ConcurrentLinkedQueue<UUID> getAppointmentOFStoreOwner() {
        return appointmentOFStoreOwner;
    }

    public StoreOwner(){
        super();
        this.storeId= UUID.randomUUID();
        this.myRoles.add(StorePermissions.STORE_COMMUNICATION);//
        this.myRoles.add(StorePermissions.STORE_SALE_HISTORY);//
        this.myRoles.add(StorePermissions.STORE_STOCK_MANAGEMENT);//
        this.myRoles.add(StorePermissions.STORE_ITEM_MANAGEMENT);
        //this.myRoles.add(StorePermissions.STORE_POLICY_MANAGEMENT); just founder
        this.myRoles.add(StorePermissions.STORE_MANAGEMENT_INFORMATION);//
        this.myRoles.add(StorePermissions.STORE_DISCOUNT_MANAGEMENT);//


    }


    public boolean addAppointmentOFStoreOwner(UUID newStoreOwner ) {
        if(checkAppointedStoreOwner(newStoreOwner))return  false;//already exist
        appointmentOFStoreOwner.add(newStoreOwner);
        return  true;
    }
    public Boolean removeAppointmentOFStoreOwner(UUID newStoreOwner ) {
        if(checkAppointedStoreOwner(newStoreOwner)){
            appointmentOFStoreOwner.remove(newStoreOwner);
            return  true;}
        return false;// not exist
    }

    public Boolean checkAppointedStoreOwner(UUID oldStoreOwner ){
        if(appointmentOFStoreOwner.contains(oldStoreOwner))return  true;
        return  false;
    }

}


