package DomainLayer.Market.Users.Roles;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Role {
    private UUID storeId;
    private ConcurrentLinkedQueue<StorePermissions> myRoles;

    public Role(){
        this.storeId= UUID.randomUUID();
        this.myRoles= new ConcurrentLinkedQueue<>();}

    public Boolean addNewRole(StorePermissions newRole) {
        if(myRoles.contains(newRole)){return  false;}//already have this role
        this.myRoles.add(newRole);
        return  true;
    }

    public Boolean removeRole(StorePermissions role) {
        if(myRoles.contains(role)){
            this.myRoles.remove(role);
            return  true;}//already have this role
        return  false;// not exist role
    }

}
