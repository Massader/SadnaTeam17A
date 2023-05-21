package APILayer.Requests;

import java.util.UUID;

public class CategoryPolicyTermRequest extends PolicyTermRequest {
    private String category;
    
    public CategoryPolicyTermRequest(UUID clientCredentials, UUID storeId, boolean atLeast, int quantity, String category) {
        super(clientCredentials, storeId, atLeast, quantity);
        this.category = category;
    }
    
    public String getCategory() {
        return category;
    }
}
