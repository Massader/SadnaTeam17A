package APILayer.Requests;

import java.util.UUID;

public class AddPolicyRequest extends Request{
    private UUID storeId;
    private int rule;
    private Boolean atLeast;
    private int quantity;
    private UUID itemId;
    private String category;

    public AddPolicyRequest(UUID clientCredentials, UUID storeId, int rule, Boolean atLeast, int quantity, UUID itemId, String category) {
        super(clientCredentials);
        this.storeId = storeId;
        this.rule = rule;
        this.atLeast = atLeast;
        this.quantity = quantity;
        this.itemId = itemId;
        this.category = category;
    }

    public UUID getStoreId() {
        return storeId;
    }

    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }

    public int getRule() {
        return rule;
    }

    public void setRule(int rule) {
        this.rule = rule;
    }

    public Boolean getatLeast() {
        return atLeast;
    }

    public void setatLeast(Boolean atLeast) {
        this.atLeast = atLeast;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public UUID getItemId() {
        return itemId;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
