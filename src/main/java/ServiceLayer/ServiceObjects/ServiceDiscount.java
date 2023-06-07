package ServiceLayer.ServiceObjects;

import DomainLayer.Market.Stores.Discounts.CalculateDiscount;

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
