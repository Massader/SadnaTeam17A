package DomainLayer.Market.Stores.Discounts.condition;

import DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule.PurchaseTerm;

public class Discount {
    private Double discountPercentage;
    private PurchaseTerm purchaseTerm;


    public Discount(Double discountPercentage, PurchaseTerm purchaseTerm) {
        this.discountPercentage = discountPercentage;
        this.purchaseTerm = purchaseTerm;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public PurchaseTerm getPurchaseTerm() {
        return purchaseTerm;
    }
}
