package DomainLayer.Market;

import DomainLayer.Market.Stores.Store;
import ServiceLayer.Response;

import java.util.HashMap;
import java.util.UUID;

public class StoreController {
    private HashMap<UUID, Store> storeMap = new HashMap<>();

    private Boolean checkExistStore(UUID storeId){
        return this.storeMap.containsKey(storeId);
    }


    public Boolean closeStore(UUID clientCredentials ,UUID storeId ) {
        if(!checkExistStore(storeId)){
            throw new IllegalArgumentException("no Store with id :" + storeId );}
            return storeMap.get(storeId).closeStore();

        }

    public Boolean reopenStore(UUID clientCredentials ,UUID storeId ) {
        if(!checkExistStore(storeId)){
            throw new IllegalArgumentException("no Store with id :" + storeId );}
        return storeMap.get(storeId).reopenStore();

    }

    }

