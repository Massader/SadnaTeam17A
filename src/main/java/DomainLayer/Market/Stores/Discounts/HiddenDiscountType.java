package DomainLayer.Market.Stores.Discounts;

public class HiddenDiscountType extends DiscountType {
    public HiddenDiscountType(double discount) {
        super(discount);
    }

    @Override
    public double calculatePrice(double basePrice) {
        return basePrice;
    }
}
