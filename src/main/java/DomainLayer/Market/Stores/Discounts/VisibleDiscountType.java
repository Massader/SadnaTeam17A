package DomainLayer.Market.Stores.Discounts;

public class VisibleDiscountType extends DiscountType {
    public VisibleDiscountType(double discount) {
        super(discount);
    }

    @Override
    public double calculatePrice(double basePrice) {
        return basePrice;
    }
}
