package DomainLayer.Market.Stores.Discounts;

public class VisibleDiscount extends Discount {
    public VisibleDiscount(double discount) {
        super(discount);
    }

    @Override
    public double calculatePrice(double basePrice) {
        return basePrice;
    }
}
