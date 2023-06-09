package DataAccessLayer.controllers;

import DataAccessLayer.ItemRepository;
import DataAccessLayer.RepositoryFactory;
import DataAccessLayer.StoreRepository;
import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class StoreDalController{
    RepositoryFactory repositoryFactory;
    StoreRepository storeRepository;
    ItemRepository itemRepository;
    private static StoreDalController singleton = null;


    private StoreDalController(RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
        this.storeRepository = repositoryFactory.storeRepository;
        this.itemRepository = repositoryFactory.itemRepository;
    }
    public static synchronized StoreDalController getInstance(RepositoryFactory repositoryFactory) {
        if (singleton == null) {
            singleton = new StoreDalController(repositoryFactory);
        }
        return singleton;
    }

//    public void init(RepositoryFactory repositoryFactory) {
//        this.repositoryFactory = repositoryFactory;
//        this.storeRepository = repositoryFactory.storeRepository;
//        this.itemRepository = repositoryFactory.itemRepository;
//    }

//store
    public UUID saveStore(Store store){
        storeRepository.save(store);
        return store.getStoreId();
    }

    public Store getStore(UUID uuid){
        Optional<Store> store= storeRepository.findById(uuid);
        if(!store.isPresent())
            return null;
        return store.get();
    }

    public List<Store> getAllStores(){
        return storeRepository.findAll();
    }
    public List<Store> getStoresPage(int number, int page){
        Pageable pageable = PageRequest.of(page, number);
        return storeRepository.findAllByClosedIsFalseAndShutdownIsFalse(pageable);
    }
    public boolean isStoreExists(UUID id ){
        return storeRepository.existsById(id);
    }

    public boolean isStoreExists(String name){
        return storeRepository.existsByName(name);
    }
public long storesCount(){
    return storeRepository.count();
}
    public long openStoresCount(){
        return storeRepository.countByClosedIsFalse();
    }

    //items
    public UUID saveItem(Item item){
        itemRepository.save(item);
        return item.getId();
    }

    public Item getItem(UUID uuid){
        Optional<Item> item= itemRepository.findById(uuid);
        if(!item.isPresent())
            return null;
        return item.get();
    }
    public void deleteItem(Item item ){
        itemRepository.delete(item);
    }

//    public List<Store> getItemPage(int number, int page){
//        Pageable pageable = PageRequest.of(page, number);
//        return storeRepository.findAllByClosedIsFalseAndShutdownIsFalse(pageable);
//    }
    public boolean isItemExists(UUID id ){
        return itemRepository.existsById(id);
    }

    public long itemsCount(){
        return itemRepository.count();
    }

//    public List<Item> getUnavailableItems(){
//
//    }


}