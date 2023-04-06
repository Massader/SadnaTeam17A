package DomainLayer.Market;

import DomainLayer.Market.Stores.Store;
import ServiceLayer.Response;

import java.util.HashMap;
import java.util.UUID;

public class StoreController {
    private HashMap<UUID, Store> storeMap = new HashMap<>();

    private void checkExistStore(UUID storeId){
        if(!this.storeMap.containsKey(storeId))
            throw new IllegalArgumentException("no Store with id :" + storeId );}



    public Boolean closeStore(UUID clientCredentials ,UUID storeId ) {
        checkExistStore(storeId);
            return storeMap.get(storeId).closeStore();

        }

    public Boolean reopenStore(UUID clientCredentials ,UUID storeId ) {
        checkExistStore(storeId);
        return storeMap.get(storeId).reopenStore();

    }

    public Boolean shutdownStore(UUID clientCredentials , UUID storeId ) {
    checkExistStore(storeId);
    return storeMap.get(storeId).ShutDown();

    }





    }

