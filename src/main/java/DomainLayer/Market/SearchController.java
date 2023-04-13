package DomainLayer.Market;

import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Security.SecurityController;
import ServiceLayer.Response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SearchController {

    private StoreController storeController;

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
}
