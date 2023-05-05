package APILayer.Requests;

import java.util.UUID;

public class SearchItemRequest extends Request {
    private String keyword;
    private String category;
    private double minPrice;
    private double maxPrice;
    private int itemRating;
    private int storeRating;

    public SearchItemRequest(UUID clientCredentials, String keyword, String category, double minPrice, double maxPrice, int itemRating, int storeRating) {
        super(clientCredentials);
        this.keyword = keyword;
        this.category = category;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.itemRating = itemRating;
        this.storeRating = storeRating;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public int getItemRating() {
        return itemRating;
    }

    public void setItemRating(int itemRating) {
        this.itemRating = itemRating;
    }

    public int getStoreRating() {
        return storeRating;
    }

    public void setStoreRating(int storeRating) {
        this.storeRating = storeRating;
    }
}
