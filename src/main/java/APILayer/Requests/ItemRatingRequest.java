package APILayer.Requests;

import java.util.UUID;

public class ItemRatingRequest extends Request {
    private int rating;
    private UUID itemId;
    private UUID storeId;
//    1.
//    {
//        ClientCredentials = "1234421",
//        rating = 2,
//        itemId = "432522",
//        storeId = "756345234234",
//        terms = "1 2 3 4 5"
//    }
//    2.
//    addTErms
//    {1}
//    {2}
//    {3}
//
//    3.
//    {
//        ClientCredentials = "1234421",
//                rating = 2,
//                itemId = "432522",
//                storeId = "756345234234",
//                terms = { or {{and {1, 2}}, xor{{}, 4, 5}}
//    }
//        (1 and 2) or (3 xor 4 xor 5)


    public ItemRatingRequest(UUID clientCredentials, int rating, UUID itemId, UUID storeId) {
        super(clientCredentials);
        this.rating = rating;
        this.itemId = itemId;
        this.storeId = storeId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public UUID getItemId() {
        return itemId;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    public UUID getStoreId() {
        return storeId;
    }

    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }
}
