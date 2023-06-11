package DomainLayer.Market.Stores.Discounts.condition;

import DomainLayer.Market.Stores.PurchaseRule.PurchaseTerm;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Discount {
    private  CalculateDiscount OptioncalculateDiscount;//basket/item/category
    private Double discountPercentage;
    private PurchaseTerm purchaseTerm;


    public Discount(CalculateDiscount OptioncalculateDiscount, Double discountPercentage, PurchaseTerm purchaseTerm) throws Exception {
        if(OptioncalculateDiscount ==null){throw new Exception("the discount is null, please put valid discount");}
        this.OptioncalculateDiscount = OptioncalculateDiscount;
        if(discountPercentage>=1||discountPercentage<=0) throw new Exception("discount Percentage have to be between 0 to 1");
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
        if(purchaseTerm==null||purchaseTerm.purchaseRuleOccurs(shoppingBasket,store)){
        discount =getOptioncalculateDiscount().CalculateDiscount(shoppingBasket,store,discountPercentage);}
        return  discount;
    }
}
