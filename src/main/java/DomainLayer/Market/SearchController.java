package DomainLayer.Market;

import DomainLayer.Market.Stores.Item;
import ServiceLayer.Response;

import java.util.LinkedList;
import java.util.List;

public class SearchController {

    public Response<List<Item>> searchItem(String keyword, String category, double minPrice, double maxPrice, int itemRating, int storeRating){
        return Response.getSuccessResponse(new LinkedList<Item>());
    }
}
