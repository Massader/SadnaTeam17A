package DomainLayer.Market.Stores.Discounts.condition;

import DomainLayer.Market.Stores.PurchaseRule.PurchaseTerm;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Discount {
    private  CalculateDiscount OptioncalculateDiscount;//basket/item/category
    private Double discountPercentage;
    private PurchaseTerm purchaseTerm;


    public Discount(CalculateDiscount OptioncalculateDiscount, Double discountPercentage, PurchaseTerm purchaseTerm) {
        this.OptioncalculateDiscount = OptioncalculateDiscount;
        this.discountPercentage = discountPercentage;
        this.purchaseTerm = purchaseTerm;
    }


    public CalculateDiscount getOptioncalculateDiscount() {
        return OptioncalculateDiscount;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public PurchaseTerm getPurchaseTerm() {
        return purchaseTerm;
    }

    public Double CalculateDiscount(ShoppingBasket shoppingBasket, Store store) {
        ConcurrentLinkedQueue<Double> DiscountOption= new ConcurrentLinkedQueue<>();
        Double discount=0.0;
        if(purchaseTerm.purchaseRuleOccurs(shoppingBasket,store)){
        discount =getOptioncalculateDiscount().CalculateDiscount(shoppingBasket,store,discountPercentage);}
        return  discount;
    }
}
