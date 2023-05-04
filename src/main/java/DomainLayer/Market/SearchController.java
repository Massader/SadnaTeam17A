package DomainLayer.Market;

import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Store;
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

    public static SearchController getInstance() {
        synchronized(instanceLock) {
            if (instance == null)
                instance = new SearchController();
        }
        return instance;
    }

    private SearchController() {
        storeController = StoreController.getInstance();
    }

    public Response<List<Item>> searchItem(String keyword, String category, double minPrice, double maxPrice, int itemRating, int storeRating){
        try{
            Collection<Store> stores = storeController.getStores();
            List<Store> filteredStores = stores.stream().filter(store -> store.getRating() >= storeRating).toList();
            List<Item> items = new ArrayList<Item>();
            for (Store store : filteredStores) {
                items.addAll(store.getItems().values().stream().filter(item ->
                        item.containsCategory(category) &
                                priceRange(item, minPrice, maxPrice) &
                                item.getRating() >= itemRating &
                                item.getName().contains(keyword)).toList());
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

    public Response<List<Item>> searchItem(String keyword, String category, double minPrice, double maxPrice, int itemRating, int storeRating, int number, int page, UUID storeId) {
        List<Item> items = new LinkedList<>();
        for (Store store : storeController.getStores()){
            for (Item item : store.getItems().values()){
                items.add(item);
            }
        }
        if (keyword!=null){
            items = items.stream().filter(item -> item.getName().contains(keyword)).toList();
        }

        if(category!=null){
            items = items.stream().filter(item -> item.containsCategory(category)).toList();
        }
        if(minPrice!=	0.0d){
            items = items.stream().filter(item -> item.getPrice() >= minPrice).toList();
        }
        if(maxPrice!=	0.0d){
            items = items.stream().filter(item -> item.getPrice() >= minPrice).toList();
        }
        if(itemRating != 0){
            items = items.stream().filter(item -> item.getRating()>=itemRating).toList();
        }
        if(storeRating != 0){
            items = items.stream().filter(item -> item.getRating()>=storeRating).toList();
        }
        if(storeId!= null){
            items = items.stream().filter(item -> item.getStoreId()==storeId).toList();
        }
        if (number != 0 & page != 0){
            List<Item> output = new LinkedList<>();
            int start = (page - 1) * number;
            int end = Math.min(start + number, items.size());
            output.addAll(items.subList(start, end));
            items = output;
        }

        return Response.getSuccessResponse(items);
    }
}
