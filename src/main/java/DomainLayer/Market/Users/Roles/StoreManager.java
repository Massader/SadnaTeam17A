package DomainLayer.Market.Users.Roles;


//import java.util.UUID;
//import java.util.concurrent.ConcurrentLinkedQueue;

//public class StoreManager extends Role {
    //private UUID storeId;
   // private  ConcurrentLinkedQueue<StorePermissions> myRoles;
 //   private ConcurrentLinkedQueue<UUID> appointmentOFStoreManager;


    //public ConcurrentLinkedQueue<UUID> getAppointmentOFStoreManager() {
      //  return appointmentOFStoreManager;
    //}

    //public StoreManager(){
        //super();
        //myRoles.add(StorePermissions.STORE_COMMUNICATION);
        //myRoles.add(StorePermissions.STORE_SALE_HISTORY);
      //  this.appointmentOFStoreManager = new ConcurrentLinkedQueue<>();
    //}

    //public boolean addAppointmentOFStoreManager(UUID newStoreManager ) {
        //if(checkAppointedStoreManager(newStoreManager))return  false;//already exist
       // appointmentOFStoreManager.add(newStoreManager);
      //  return  true;
    //}
    //public Boolean removeAppointmentOFStoreManager(UUID newStoreManager ) {
     //   if(checkAppointedStoreManager(newStoreManager)){
   //     appointmentOFStoreManager.remove(newStoreManager);
        //return  true;}
      //  return false;// not exist
    //}

    //public Boolean checkAppointedStoreManager(UUID oldStoreManager ){
    //    if(appointmentOFStoreManager.contains(oldStoreManager))return  true;
  //      return  false;
//    }


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StoreManager extends Role{
    private List<StorePermissions> permissionsList;

    public StoreManager(UUID storeId) {
        super(storeId);
        permissionsList = new ArrayList<StorePermissions>();
        permissionsList.add(StorePermissions.STORE_MANAGEMENT_INFORMATION);
        permissionsList.add(StorePermissions.STORE_COMMUNICATION);
        permissionsList.add(StorePermissions.STORE_SALE_HISTORY);
    }

    @Override
    public List<StorePermissions> getPermissions() {
        return permissionsList;
    }

    @Override
    public void addPermission(StorePermissions permission) {
        if(!permissionsList.contains(permission))
            permissionsList.add(permission);
    }

}
