package DomainLayer.Market;

import DataAccessLayer.RepositoryFactory;
import DataAccessLayer.controllers.StoreDalController;
import DataAccessLayer.controllers.UserDalController;
import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.User;
import DomainLayer.Security.SecurityController;
import DomainLayer.Supply.SupplyController;
import ServiceLayer.Response;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class SearchController {

    private StoreController storeController;

    private static SearchController instance = null;
    private static final Object instanceLock = new Object();
    private RepositoryFactory repositoryFactory;
    private UserDalController userDalController;
    private StoreDalController storeDalController;

    public static SearchController getInstance() {
        synchronized(instanceLock) {
            if (instance == null)
                instance = new SearchController();
        }
        return instance;
    }

    private SearchController() {
    }
    public void init(RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
        userDalController = UserDalController.getInstance(repositoryFactory);
        storeController = StoreController.getInstance();
        storeDalController = StoreDalController.getInstance(repositoryFactory);

    }

    public Response<List<Item>> searchItem(String keyword, String category, double minPrice, double maxPrice, int itemRating, int storeRating){
        try{
            List<Store> stores = storeController.getStores();
            List<Store> filteredStores = stores.stream().filter(store -> store.getRating() >= storeRating).toList();
            List<Item> items = new ArrayList<>();
            for (Store store : filteredStores) {
                items.addAll(store.getItems().stream().filter(item ->
                        item.containsCategory(category) &
                                priceRange(item, minPrice, maxPrice) &
                                item.getRating() >= itemRating &
                                item.getName().toLowerCase().contains(keyword.toLowerCase())).toList());
            }
            return Response.getSuccessResponse(items);
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }
    private boolean priceRange(Item item, double min, double max) {
        return item.getPrice() >= min && item.getPrice() <= max;
    }

    public void resetController() {
        instance = new SearchController();
    }

    public Response<List<Item>> searchItem(String keyword, String category, Double minPrice, Double maxPrice,
                                           Integer itemRating, Integer storeRating, Integer number, Integer page, UUID storeId) {
        List<Item> items = new ArrayList<>();
        if(storeId != null){
            Store store = storeController.getStore(storeId);
            if (store == null)
                return Response.getFailResponse("Store does not exist");
            if (store.isClosed())
                return Response.getFailResponse("Store is not open");
            items = repositoryFactory.itemRepository.findAllByStore(storeId);
        }
        else {
            for (Store store : storeController.getStores()) {
                if (!store.isClosed())
                    items = repositoryFactory.itemRepository.findAll();
            }
        }
        if (keyword != null && !keyword.isEmpty()){
            items = items.stream().filter(item -> item.getName().toLowerCase().contains(keyword.toLowerCase())).toList();
        }
        if(category != null && !category.isEmpty()){
            items = items.stream().filter(item -> item.containsCategory(category)).toList();
        }
        if(minPrice != null){
            items = items.stream().filter(item -> item.getPrice() >= minPrice).toList();
        }
        if(maxPrice != null){
            items = items.stream().filter(item -> item.getPrice() <= maxPrice).toList();
        }
        if(itemRating != null){
            items = items.stream().filter(item -> item.getRating() >= itemRating).toList();
        }
        if(storeRating != null){
            items = items.stream().filter(item -> storeController.getStore(item.getStoreId()).getRating() >= storeRating).toList();
        }
        if (number != null && page != null){
            int start = (page - 1) * number;
            int end = Math.min(start + number, items.size());
            items = new ArrayList<>(items.subList(start, end));
        }

        return Response.getSuccessResponse(items);
    }

//    public Response<List<User>> searchUser(String username) {
//        try {
//            UserController userController = UserController.getInstance();
//            ConcurrentHashMap<String, UUID> usernames = userController.getUsernames();
//            List<User> users = new ArrayList<>();
//            for (Map.Entry<String, UUID> entry : usernames.entrySet()) {
//                if (entry.getKey().toLowerCase().contains(username.toLowerCase()))
//                    users.add(userController.getUserById(entry.getValue()));
//            }
//            return Response.getSuccessResponse(users);
//        } catch (Exception e) {
//            return Response.getFailResponse(e.getMessage());
//        }
//
//    }

    public Response<List<User>> searchUser(String keyrord) {
        try {
            return Response.getSuccessResponse(userDalController.serachUser(keyrord));
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }

}
