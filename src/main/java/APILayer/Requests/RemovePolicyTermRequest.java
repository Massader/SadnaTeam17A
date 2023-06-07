package APILayer.Requests;

import java.util.UUID;

public class RemovePolicyTermRequest extends Request {
    
    private UUID storeId;
    private UUID termId;
    private String categoryName;
    
    public RemovePolicyTermRequest(UUID clientCredentials, UUID storeId, UUID termId, String categoryName) {
        super(clientCredentials);
        this.storeId = storeId;
        this.termId = termId;
        this.categoryName = categoryName;
    }
    
    public UUID getStoreId() {
        return storeId;
    }
    
    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }
    
    public UUID getTermId() {
        return termId;
    }
    
    public void setTermId(UUID termId) {
        this.termId = termId;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
