package DomainLayer.Market.Stores.Discounts;

import DomainLayer.Market.Stores.PurchaseRule.PurchaseTerm;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Discount {
    private UUID id;
    private CalculateDiscount optionCalculateDiscount;//basket/item/category
    private Double discountPercentage;
    private PurchaseTerm purchaseTerm;

    public Discount(CalculateDiscount optionCalculateDiscount, Double discountPercentage, PurchaseTerm purchaseTerm) throws Exception {
        if (optionCalculateDiscount == null)
            throw new Exception("the discount is null, please put valid discount");
        this.optionCalculateDiscount = optionCalculateDiscount;
        if (discountPercentage >= 1 || discountPercentage <= 0)
            throw new Exception("discount Percentage have to be between 0 to 1");
        this.discountPercentage = discountPercentage;
        this.purchaseTerm = purchaseTerm;
        this.id = UUID.randomUUID();
    }

    public CalculateDiscount getOptionCalculateDiscount() {
        return optionCalculateDiscount;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public PurchaseTerm getPurchaseTerm() {
        return purchaseTerm;
    }

    public Double calculateDiscount(ShoppingBasket shoppingBasket, Store store) {
        ConcurrentLinkedQueue<Double> discountOption = new ConcurrentLinkedQueue<>();
        Double discount = 0.0;
        if (purchaseTerm == null || purchaseTerm.purchaseRuleOccurs(shoppingBasket,store))
            discount = getOptionCalculateDiscount().calculateDiscount(shoppingBasket,store,discountPercentage);
        return discount;
    }
    
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public void setOptionCalculateDiscount(CalculateDiscount optionCalculateDiscount) {
        this.optionCalculateDiscount = optionCalculateDiscount;
    }
    
    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
    
    public void setPurchaseTerm(PurchaseTerm purchaseTerm) {
        this.purchaseTerm = purchaseTerm;
    }
}
