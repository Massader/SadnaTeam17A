package ServiceLayer.ServiceObjects;

import DomainLayer.Market.Stores.Category;
import DomainLayer.Market.Stores.Discounts.CalculateDiscount;
import DomainLayer.Market.Stores.Discounts.CategoryCalculateDiscount;
import DomainLayer.Market.Stores.Discounts.Discount;
import DomainLayer.Market.Stores.Discounts.ItemCalculateDiscount;

import java.util.UUID;

public class ServiceDiscount {
    private UUID id;
    private String type;
    private String itemIdOrCategoryOrNull;
    private double discountPercentage;
    private ServicePurchaseTerm purchaseTerm;
    
    public ServiceDiscount(UUID id, String type, String itemIdOrCategoryOrNull, double discountPercentage, ServicePurchaseTerm purchaseTerm) {
        this.id = id;
        this.type = type;
        this.itemIdOrCategoryOrNull = itemIdOrCategoryOrNull;
        this.discountPercentage = discountPercentage;
        this.purchaseTerm = purchaseTerm;
    }
    
    public ServiceDiscount(Discount discount) {
        this.id = discount.getId();
        this.discountPercentage = discount.getDiscountPercentage() * 100;
        if (discount.getPurchaseTerm() != null)
            this.purchaseTerm = new ServicePurchaseTerm(discount.getPurchaseTerm());
        CalculateDiscount calc = discount.getOptionCalculateDiscount();
        if (calc instanceof ItemCalculateDiscount) {
            this.type = "ITEM";
            this.itemIdOrCategoryOrNull = ((ItemCalculateDiscount) calc).getItemId().toString();
        }
        else if (calc instanceof CategoryCalculateDiscount) {
            this.type = "CATEGORY";
            this.itemIdOrCategoryOrNull = ((CategoryCalculateDiscount) calc).getCategory().getCategoryName();
        }
        else {
            this.type = "BASKET";
            this.itemIdOrCategoryOrNull = null;
        }
    }
    
    public ServiceDiscount() {
    }
    
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getItemIdOrCategoryOrNull() {
        return itemIdOrCategoryOrNull;
    }
    
    public void setItemIdOrCategoryOrNull(String itemIdOrCategoryOrNull) {
        this.itemIdOrCategoryOrNull = itemIdOrCategoryOrNull;
    }
    
    public double getDiscountPercentage() {
        return discountPercentage;
    }
    
    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
    
    public ServicePurchaseTerm getPurchaseTerm() {
        return purchaseTerm;
    }
    
    public void setPurchaseTerm(ServicePurchaseTerm purchaseTerm) {
        this.purchaseTerm = purchaseTerm;
    }
}
